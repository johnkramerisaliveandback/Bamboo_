package com.example.ui.state

import com.example.data.AcademicData
import com.example.data.AcademicStatus
import com.example.data.AttendanceRepository
import com.example.data.ManualStatus
import com.example.data.ParentAcademicStatus
import com.example.data.StatusSource
import com.example.data.TimetableEntry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AcademicStatusManager(private val attendanceRepo: AttendanceRepository) {

    fun calculateCurrentStatus(branch: String, studentSection: com.example.data.TimetableSection? = null, manualStatus: ManualStatus? = null): ParentAcademicStatus {
        val calendar = Calendar.getInstance()
        val now = System.currentTimeMillis()
        
        // 1. Priority 1: Manual status
        if (manualStatus != null) {
            val isActive = manualStatus.expiresAt == null || now < manualStatus.expiresAt
            if (isActive) {
                return ParentAcademicStatus(
                    status = manualStatus.type,
                    source = StatusSource.MANUAL,
                    activity = manualStatus.activity,
                    expiresAt = manualStatus.expiresAt
                )
            }
        }

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val todayDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

        if (AcademicData.isHoliday(todayDateStr) || dayOfWeek == Calendar.SUNDAY) {
            return ParentAcademicStatus(status = AcademicStatus.NO_CLASSES_TODAY)
        }

        val dayName = when (dayOfWeek) {
            Calendar.MONDAY -> "MONDAY"
            Calendar.TUESDAY -> "TUESDAY"
            Calendar.WEDNESDAY -> "WEDNESDAY"
            Calendar.THURSDAY -> "THURSDAY"
            Calendar.FRIDAY -> "FRIDAY"
            Calendar.SATURDAY -> "SATURDAY"
            else -> "MONDAY"
        }

        val todayEntries = AcademicData.getFilteredTimetable(
            AcademicData.TIMETABLE[branch]?.get(dayName) ?: emptyList(),
            studentSection
        )
        if (todayEntries.isEmpty()) {
            return ParentAcademicStatus(status = AcademicStatus.NO_CLASSES_TODAY)
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val currentMinutes = hour * 60 + minute

        var currentClass: TimetableEntry? = null
        var nextClass: TimetableEntry? = null
        var minutesLeftToNext: Int? = null
        var lastEndMinutes = 0

        for (entry in todayEntries) {
            val period = AcademicData.PERIODS.find { it.periodName == entry.period } ?: continue
            val startMins = period.startHour * 60 + period.startMinute
            val endMins = period.endHour * 60 + period.endMinute
            
            if (endMins > lastEndMinutes) lastEndMinutes = endMins

            if (currentMinutes in startMins..endMins) {
                currentClass = entry
            } else if (startMins > currentMinutes && nextClass == null) {
                nextClass = entry
                minutesLeftToNext = startMins - currentMinutes
            }
        }

        if (currentClass != null) {
            // Check if missed
            val attendance = attendanceRepo.getAttendanceForClass(todayDateStr, currentClass.subjectCode)
            if (attendance == DayStatus.ABSENT) {
                return ParentAcademicStatus(
                    status = AcademicStatus.CLASS_MISSED,
                    source = StatusSource.AUTO,
                    currentSubject = currentClass.subjectTitle,
                    room = currentClass.room,
                    timeRange = currentClass.time
                )
            }
            return ParentAcademicStatus(
                status = AcademicStatus.IN_CLASS,
                source = StatusSource.AUTO,
                currentSubject = currentClass.subjectTitle,
                room = currentClass.room,
                timeRange = currentClass.time
            )
        }

        if (currentMinutes > lastEndMinutes) {
            return ParentAcademicStatus(status = AcademicStatus.DAY_COMPLETE, source = StatusSource.AUTO)
        }

        if (nextClass != null) {
            val status = if (minutesLeftToNext != null && minutesLeftToNext <= 15) {
                AcademicStatus.BETWEEN_CLASSES
            } else {
                AcademicStatus.FREE
            }
            
            return ParentAcademicStatus(
                status = status,
                source = StatusSource.AUTO,
                nextSubject = nextClass.subjectTitle,
                nextInMinutes = minutesLeftToNext,
                nextTime = nextClass.time.split("-").first().trim()
            )
        }

        return ParentAcademicStatus(status = AcademicStatus.FREE, source = StatusSource.AUTO)
    }
}
