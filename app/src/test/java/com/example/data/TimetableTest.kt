package com.example.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TimetableTest {

    @Test
    fun testFilteredTimetableForSection1() {
        val entries = listOf(
            TimetableEntry("I", "09:10 - 10:00", "IAS101", "Physics", "Lecture", "OPS", "Prof. Singh", "L-101", "", "MONDAY", TimetableSection.COMMON),
            TimetableEntry("III", "10:50 - 11:40", "JAS151", "Phy Lab Sec 1", "Lab", "OPS", "Prof. Singh", "Lab 1", "", "MONDAY", TimetableSection.SECTION_1),
            TimetableEntry("III", "10:50 - 11:40", "JEE151", "EE Lab Sec 2", "Lab", "SyS", "Dr. Singh", "Lab 2", "", "MONDAY", TimetableSection.SECTION_2)
        )

        val filteredSec1 = AcademicData.getFilteredTimetable(entries, TimetableSection.SECTION_1)
        assertEquals(2, filteredSec1.size)
        assertTrue(filteredSec1.any { it.subjectCode == "IAS101" })
        assertTrue(filteredSec1.any { it.subjectCode == "JAS151" })
        assertFalse(filteredSec1.any { it.subjectCode == "JEE151" })
    }

    @Test
    fun testFilteredTimetableForSection2() {
        val entries = listOf(
            TimetableEntry("I", "09:10 - 10:00", "IAS101", "Physics", "Lecture", "OPS", "Prof. Singh", "L-101", "", "MONDAY", TimetableSection.COMMON),
            TimetableEntry("III", "10:50 - 11:40", "JAS151", "Phy Lab Sec 1", "Lab", "OPS", "Prof. Singh", "Lab 1", "", "MONDAY", TimetableSection.SECTION_1),
            TimetableEntry("III", "10:50 - 11:40", "JEE151", "EE Lab Sec 2", "Lab", "SyS", "Dr. Singh", "Lab 2", "", "MONDAY", TimetableSection.SECTION_2)
        )

        val filteredSec2 = AcademicData.getFilteredTimetable(entries, TimetableSection.SECTION_2)
        assertEquals(2, filteredSec2.size)
        assertTrue(filteredSec2.any { it.subjectCode == "IAS101" })
        assertTrue(filteredSec2.any { it.subjectCode == "JEE151" })
        assertFalse(filteredSec2.any { it.subjectCode == "JAS151" })
    }

    @Test
    fun testTimetableBranchExists() {
        val branchTimetable = AcademicData.TIMETABLE["CS (AI)"]
        assertNotNull(branchTimetable)
        assertTrue(branchTimetable!!.containsKey("MONDAY"))
    }
}
