package com.example.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject_attendance")
data class SubjectAttendanceEntity(
    @PrimaryKey val courseCode: String,
    val title: String,
    val attendedClasses: Int = 0,
    val totalClasses: Int = 0
)

@Entity(tableName = "assignments")
data class AssignmentEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subject: String,
    val description: String,
    val assignedDate: String,
    val dueDate: String,
    val dueTime: String,
    val priority: String,
    val status: String,
    val reminderTiming: String
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val date: String,
    val subjectCode: String?,
    val tagsJson: String,
    val attachmentsJson: String
)
