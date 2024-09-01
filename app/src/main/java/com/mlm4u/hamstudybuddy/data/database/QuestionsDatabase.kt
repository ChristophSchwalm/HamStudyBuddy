package com.mlm4u.hamstudybuddy.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Questions::class], version = 1)
abstract class QuestionsDatabase : RoomDatabase() {
    abstract val questionsDao : QuestionsDao

    companion object {private lateinit var database: QuestionsDatabase

        fun getDatabase(context: Context) : QuestionsDatabase {
            synchronized(this) {
                if (!this::database.isInitialized) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        QuestionsDatabase::class.java,
                        "questions_database"
                    ).build()
                }
                return database
            }
        }
    }
}