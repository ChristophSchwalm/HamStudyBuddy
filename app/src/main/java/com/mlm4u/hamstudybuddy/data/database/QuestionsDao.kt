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

    @Query("SELECT * FROM table_questions WHERE classQuestion = :classQuestion AND ready4Game = 0 GROUP BY titleQuestion ORDER BY number")
    fun getAllTitle(classQuestion: String): LiveData<List<Questions>>

    @Query("SELECT * FROM table_questions")
    fun getAllQuestions(): LiveData<List<Questions>>

    @Query("UPDATE table_questions SET ready4Game = 1 WHERE number = :number")
    suspend fun setReady4Game(number: String)

    @Query("SELECT * FROM table_questions WHERE classQuestion = :classQuestion AND titleQuestion = :selectedTitle AND ready4Game = 0")
    fun getQuestionsByTitle(classQuestion: String, selectedTitle: String): LiveData<List<Questions>>

    @Query("SELECT COUNT(*) FROM table_questions")
    fun countAllQuestions(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM table_questions WHERE classQuestion = :classQuestion")
    fun countQuestionsClass(classQuestion: String): LiveData<Int>

    @Query("DELETE FROM table_questions WHERE number = :number")
    suspend fun deleteNumber(number: String)
}