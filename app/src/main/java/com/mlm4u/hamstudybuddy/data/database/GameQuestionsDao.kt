package com.mlm4u.hamstudybuddy.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameQuestionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameQuestion(gameQuestions: GameQuestions)

    @Query("SELECT COUNT(*) FROM game_questions")
    suspend fun countGameQuestions(): Int

    @Query("SELECT * FROM game_questions")
    fun getAllGameQuestions(): LiveData<List<GameQuestions>>
}