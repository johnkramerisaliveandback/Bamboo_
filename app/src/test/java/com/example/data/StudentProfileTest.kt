package com.example.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StudentProfileTest {

    @Test
    fun testUserProfileDefaults() {
        val profile = UserProfile()
        assertEquals("", profile.studentName)
        assertEquals("CS (AI)", profile.branch)
        assertEquals("1st Year", profile.year)
        assertEquals(TimetableSection.SECTION_1, profile.section)
        assertFalse(profile.hasCompletedOnboarding)
    }

    @Test
    fun testUserProfileCustomValues() {
        val profile = UserProfile(
            studentName = "Rahul",
            branch = "CSE AI",
            year = "2nd Year",
            section = TimetableSection.SECTION_2,
            hasCompletedOnboarding = true
        )
        assertEquals("Rahul", profile.studentName)
        assertEquals("CSE AI", profile.branch)
        assertEquals("2nd Year", profile.year)
        assertEquals(TimetableSection.SECTION_2, profile.section)
        assertTrue(profile.hasCompletedOnboarding)
    }
}
