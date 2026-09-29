package com.example.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SyllabusTest {

    @Test
    fun testFirstYearSemester1SyllabusExists() {
        val sem1Courses = AcademicData.SYLLABUS.filter { it.semester == 1 }
        assertFalse("Semester 1 syllabus should not be empty", sem1Courses.isEmpty())
        assertTrue(sem1Courses.any { it.courseCode == "IAS101" })
        val physics = sem1Courses.first { it.courseCode == "IAS101" }
        assertFalse("Physics units should not be empty", physics.units.isEmpty())
    }

    @Test
    fun testFirstYearSemester2SyllabusExists() {
        val sem2Courses = AcademicData.SYLLABUS.filter { it.semester == 2 }
        assertFalse("Semester 2 syllabus should not be empty", sem2Courses.isEmpty())
        assertTrue(sem2Courses.any { it.courseCode == "IAS201" || it.title.contains("Physics") })
    }
}
