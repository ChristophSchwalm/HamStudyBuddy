package com.mlm4u.hamstudybuddy.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<Questions>)

    @Query("SELECT * FROM table_questions")
    fun getAllQuestions(): LiveData<List<Questions>>

}