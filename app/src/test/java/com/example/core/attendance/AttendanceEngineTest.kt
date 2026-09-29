package com.example.core.attendance

import org.junit.Assert.assertEquals
import org.junit.Test

class AttendanceEngineTest {

    @Test
    fun testZeroClasses() {
        val result = AttendanceEngine.calculate(0, 0)
        assertEquals(0f, result.percentage, 0.01f)
        assertEquals(AttendanceStanding.NO_DATA, result.standing)
        assertEquals(0, result.classesCanSkip)
    }

    @Test
    fun testSingleClassAttended() {
        val result = AttendanceEngine.calculate(1, 1)
        assertEquals(100f, result.percentage, 0.01f)
        assertEquals(AttendanceStanding.GOOD_STANDING, result.standing)
        assertEquals(0, result.classesNeededForTarget)
    }

    @Test
    fun testSingleClassMissed() {
        val result = AttendanceEngine.calculate(0, 1)
        assertEquals(0f, result.percentage, 0.01f)
        assertEquals(AttendanceStanding.CRITICAL, result.standing)
        assertEquals(3, result.classesNeededForTarget) // 0/1 -> need 3 more (3/4 = 75%)
    }

    @Test
    fun testEightOfTenClasses() {
        val result = AttendanceEngine.calculate(8, 10)
        assertEquals(80f, result.percentage, 0.01f)
        assertEquals(AttendanceStanding.GOOD_STANDING, result.standing)
        assertEquals(0, result.classesNeededForTarget)
        // 8/10 = 80%. If miss 1 -> 8/11 = 72.7% < 75%. So can skip 0.
        assertEquals(0, result.classesCanSkip)
    }

    @Test
    fun testNineOfElevenClasses() {
        val result = AttendanceEngine.calculate(9, 11)
        assertEquals(81.8181f, result.percentage, 0.05f)
        assertEquals(AttendanceStanding.GOOD_STANDING, result.standing)
        assertEquals(0, result.classesNeededForTarget)
        assertEquals(1, result.classesCanSkip) // 9/12 = 75% >= 75%
    }

    @Test
    fun testTenOfTenClasses() {
        val result = AttendanceEngine.calculate(10, 10)
        assertEquals(100f, result.percentage, 0.01f)
        assertEquals(AttendanceStanding.GOOD_STANDING, result.standing)
        assertEquals(3, result.classesCanSkip) // 10/13 = 76.9% >= 75%
    }

    @Test
    fun testLowAttendanceTargetCalculation() {
        // 5 out of 10 = 50%
        val needed = AttendanceEngine.calculateClassesNeeded(5, 10, 75f)
        assertEquals(10, needed) // (5+10)/(10+10) = 15/20 = 75%
    }
}
