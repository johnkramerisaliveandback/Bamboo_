package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.core.attendance.AttendanceEngine
import com.example.core.attendance.AttendanceStanding
import com.example.ui.state.DayStatus

data class OverallAttendanceStats(
    val totalPresent: Int,
    val totalAbsent: Int,
    val totalClasses: Int,
    val percentage: Float,
    val statusStanding: String
)

class AttendanceRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("bamboo_attendance_db", Context.MODE_PRIVATE)

    private fun getKey(date: String, courseCode: String): String = "${date}_${courseCode}"
    private fun getManualAttendedKey(courseCode: String): String = "manual_attended_$courseCode"
    private fun getManualTotalKey(courseCode: String): String = "manual_total_$courseCode"

    fun markAttendance(date: String, courseCode: String, status: DayStatus) {
        if (status == DayStatus.HOLIDAY || status == DayStatus.NONE) {
            prefs.edit().remove(getKey(date, courseCode)).apply()
        } else {
            prefs.edit().putString(getKey(date, courseCode), status.name).apply()
        }
    }

    fun getAttendanceForClass(date: String, courseCode: String): DayStatus {
        val valStr = prefs.getString(getKey(date, courseCode), null) ?: return DayStatus.NONE
        return try {
            DayStatus.valueOf(valStr)
        } catch (e: Exception) {
            DayStatus.NONE
        }
    }

    fun getAttendanceForDate(date: String, scheduledClasses: List<TimetableEntry>): Map<String, DayStatus> {
        val result = mutableMapOf<String, DayStatus>()
        for (entry in scheduledClasses) {
            val status = getAttendanceForClass(date, entry.subjectCode)
            result[entry.subjectCode] = status
        }
        return result
    }

    fun markAllPresent(date: String, scheduledClasses: List<TimetableEntry>) {
        val editor = prefs.edit()
        for (entry in scheduledClasses) {
            if (entry.subjectCode != "XXXX" && entry.subjectCode.isNotBlank()) {
                val currentStatus = getAttendanceForClass(date, entry.subjectCode)
                // Do not override cancelled classes automatically unless user changes it explicitly
                if (currentStatus != DayStatus.CANCELLED) {
                    editor.putString(getKey(date, entry.subjectCode), DayStatus.PRESENT.name)
                }
            }
        }
        editor.apply()
    }

    fun markAllAbsent(date: String, scheduledClasses: List<TimetableEntry>) {
        val editor = prefs.edit()
        for (entry in scheduledClasses) {
            if (entry.subjectCode != "XXXX" && entry.subjectCode.isNotBlank()) {
                val currentStatus = getAttendanceForClass(date, entry.subjectCode)
                if (currentStatus != DayStatus.CANCELLED) {
                    editor.putString(getKey(date, entry.subjectCode), DayStatus.ABSENT.name)
                }
            }
        }
        editor.apply()
    }

    fun resetAttendanceForDate(date: String, scheduledClasses: List<TimetableEntry>) {
        val editor = prefs.edit()
        for (entry in scheduledClasses) {
            editor.remove(getKey(date, entry.subjectCode))
        }
        editor.apply()
    }

    fun resetAllAttendance() {
        prefs.edit().clear().apply()
    }

    fun addManualSubjectAttendance(courseCode: String, attendedDelta: Int, totalDelta: Int) {
        val curAttended = prefs.getInt(getManualAttendedKey(courseCode), 0)
        val curTotal = prefs.getInt(getManualTotalKey(courseCode), 0)
        
        val newAttended = (curAttended + attendedDelta).coerceAtLeast(0)
        val newTotal = (curTotal + totalDelta).coerceAtLeast(newAttended)

        prefs.edit()
            .putInt(getManualAttendedKey(courseCode), newAttended)
            .putInt(getManualTotalKey(courseCode), newTotal)
            .apply()
    }

    fun getAllRecords(): List<AttendanceRecord> {
        val list = mutableListOf<AttendanceRecord>()
        val allEntries = prefs.all
        for ((key, value) in allEntries) {
            if (value is String && key.contains("_") && !key.startsWith("manual_")) {
                val parts = key.split("_")
                if (parts.size >= 2) {
                    val date = parts[0]
                    val code = parts.subList(1, parts.size).joinToString("_")
                    val status = try {
                        DayStatus.valueOf(value)
                    } catch (e: Exception) {
                        DayStatus.NONE
                    }
                    if (status == DayStatus.PRESENT || status == DayStatus.ABSENT || status == DayStatus.CANCELLED) {
                        list.add(AttendanceRecord(date, code, status))
                    }
                }
            }
        }
        return list
    }

    fun getSubjectAttendanceList(branch: String, section: TimetableSection? = TimetableSection.SECTION_1): List<SubjectAttendance> {
        val subjectCodes = mutableSetOf<String>()

        val branchTimetable = AcademicData.TIMETABLE[branch] ?: emptyMap()
        for ((_, entries) in branchTimetable) {
            val filtered = AcademicData.getFilteredTimetable(entries, section)
            for (entry in filtered) {
                if (entry.subjectCode.isNotBlank() && entry.subjectCode != "XXXX") {
                    subjectCodes.add(entry.subjectCode)
                }
            }
        }

        val records = getAllRecords()
        val logCounts = mutableMapOf<String, Pair<Int, Int>>() // courseCode -> Pair(Present, Absent)

        for (r in records) {
            val cur = logCounts.getOrDefault(r.courseCode, Pair(0, 0))
            if (r.status == DayStatus.PRESENT) {
                logCounts[r.courseCode] = Pair(cur.first + 1, cur.second)
            } else if (r.status == DayStatus.ABSENT) {
                logCounts[r.courseCode] = Pair(cur.first, cur.second + 1)
            }
            // CANCELLED status is intentionally excluded from total marked denominator!
        }

        val result = mutableListOf<SubjectAttendance>()
        for (code in subjectCodes.sorted()) {
            val title = AcademicData.resolveCourseTitle(code)
            val logs = logCounts.getOrDefault(code, Pair(0, 0))
            val manualAttended = prefs.getInt(getManualAttendedKey(code), 0)
            val manualTotal = prefs.getInt(getManualTotalKey(code), 0)

            val attended = logs.first + manualAttended
            val total = logs.first + logs.second + manualTotal

            result.add(
                SubjectAttendance(
                    id = "subj_$code",
                    subjectCode = code,
                    subjectName = title,
                    attendedClasses = attended,
                    totalClasses = total
                )
            )
        }

        return result
    }

    fun calculateOverallStats(subjectAttendanceList: List<SubjectAttendance>): OverallAttendanceStats {
        var totalPresent = 0
        var totalClasses = 0
        for (subj in subjectAttendanceList) {
            totalPresent += subj.attendedClasses
            totalClasses += subj.totalClasses
        }

        val totalAbsent = (totalClasses - totalPresent).coerceAtLeast(0)

        if (totalClasses == 0) {
            return OverallAttendanceStats(
                totalPresent = 0,
                totalAbsent = 0,
                totalClasses = 0,
                percentage = 0f,
                statusStanding = "NO DATA"
            )
        }

        val percentage = AttendanceEngine.calculatePercentage(totalPresent, totalClasses)
        val standing = when (AttendanceEngine.getStanding(percentage, totalClasses)) {
            AttendanceStanding.GOOD_STANDING -> "GOOD STANDING"
            AttendanceStanding.WARNING -> "WARNING"
            AttendanceStanding.CRITICAL -> "LOW ATTENDANCE"
            AttendanceStanding.NO_DATA -> "NO DATA"
        }

        return OverallAttendanceStats(
            totalPresent = totalPresent,
            totalAbsent = totalAbsent,
            totalClasses = totalClasses,
            percentage = percentage,
            statusStanding = standing
        )
    }

    fun calculateCurrentStreak(): Int {
        val records = getAllRecords().sortedByDescending { it.date }
        if (records.isEmpty()) return 0

        var streak = 0
        for (r in records) {
            if (r.status == DayStatus.PRESENT) {
                streak++
            } else if (r.status == DayStatus.ABSENT) {
                break
            }
        }
        return streak
    }
}
