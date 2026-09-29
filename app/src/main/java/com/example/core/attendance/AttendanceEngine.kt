package com.example.core.attendance

import kotlin.math.ceil

enum class AttendanceStanding {
    GOOD_STANDING,
    WARNING,
    CRITICAL,
    NO_DATA
}

data class AttendanceCalculation(
    val attendedClasses: Int,
    val totalClasses: Int,
    val percentage: Float,
    val standing: AttendanceStanding,
    val classesNeededForTarget: Int,
    val classesCanSkip: Int
)

object AttendanceEngine {

    /**
     * Calculates the attendance percentage safely handling division by zero.
     */
    fun calculatePercentage(attended: Int, total: Int): Float {
        if (total <= 0) return 0f
        val safeAttended = attended.coerceIn(0, total)
        return (safeAttended.toFloat() / total.toFloat()) * 100f
    }

    /**
     * Calculates how many consecutive classes the student must attend to reach the target percentage.
     * Formula: (attended + x) / (total + x) >= target / 100
     * => x >= (target * total - 100 * attended) / (100 - target)
     */
    fun calculateClassesNeeded(attended: Int, total: Int, targetPercentage: Float = 75f): Int {
        if (targetPercentage >= 100f) return Int.MAX_VALUE
        if (total <= 0) return ceil((targetPercentage * 1f) / (100f - targetPercentage)).toInt()

        val currentPct = calculatePercentage(attended, total)
        if (currentPct >= targetPercentage) return 0

        val safeAttended = attended.coerceIn(0, total)
        val targetRatio = targetPercentage / 100f
        val neededDouble = (targetRatio * total - safeAttended) / (1f - targetRatio)
        return ceil(neededDouble).toInt().coerceAtLeast(0)
    }

    /**
     * Calculates how many classes the student can miss before falling below the target percentage.
     * Formula: attended / (total + x) >= target / 100
     * => x <= (100 * attended - target * total) / target
     */
    fun calculateClassesCanSkip(attended: Int, total: Int, targetPercentage: Float = 75f): Int {
        if (total <= 0 || targetPercentage <= 0f) return 0
        val currentPct = calculatePercentage(attended, total)
        if (currentPct < targetPercentage) return 0

        val safeAttended = attended.coerceIn(0, total)
        val targetRatio = targetPercentage / 100f
        val skippableDouble = (safeAttended - targetRatio * total) / targetRatio
        return skippableDouble.toInt().coerceAtLeast(0)
    }

    /**
     * Determines attendance standing status.
     */
    fun getStanding(percentage: Float, totalClasses: Int): AttendanceStanding {
        if (totalClasses <= 0) return AttendanceStanding.NO_DATA
        return when {
            percentage >= 75f -> AttendanceStanding.GOOD_STANDING
            percentage >= 65f -> AttendanceStanding.WARNING
            else -> AttendanceStanding.CRITICAL
        }
    }

    /**
     * Performs full calculation for a subject.
     */
    fun calculate(attended: Int, total: Int, targetPercentage: Float = 75f): AttendanceCalculation {
        val pct = calculatePercentage(attended, total)
        val standing = getStanding(pct, total)
        val needed = calculateClassesNeeded(attended, total, targetPercentage)
        val skippable = calculateClassesCanSkip(attended, total, targetPercentage)

        return AttendanceCalculation(
            attendedClasses = attended.coerceAtLeast(0),
            totalClasses = total.coerceAtLeast(0),
            percentage = pct,
            standing = standing,
            classesNeededForTarget = needed,
            classesCanSkip = skippable
        )
    }
}
