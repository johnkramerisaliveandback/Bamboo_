package com.example.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SubjectAttendanceEntity::class,
        AssignmentEntity::class,
        NoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AcademicDatabase : RoomDatabase() {

    abstract fun attendanceDao(): AttendanceDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AcademicDatabase? = null

        fun getDatabase(context: Context): AcademicDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AcademicDatabase::class.java,
                    "bamboo_academic_os.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
