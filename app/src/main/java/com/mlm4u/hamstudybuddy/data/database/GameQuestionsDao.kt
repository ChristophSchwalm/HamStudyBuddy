package com.mlm4u.hamstudybuddy.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GameQuestionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameQuestion(gameQuestions: GameQuestions)

    @Query("SELECT COUNT(*) FROM game_questions WHERE classQuestion = :classQuestion")
    suspend fun countGameQuestions(classQuestion: String): Int

    @Query("SELECT * FROM game_questions WHERE classQuestion = :classQuestion AND gameCorrectAnswer = 0")
    fun allGameQuestions(classQuestion: String): LiveData<List<GameQuestions>>

    @Update
    suspend fun updateGameQuestion(gameQuestions: GameQuestions)
}