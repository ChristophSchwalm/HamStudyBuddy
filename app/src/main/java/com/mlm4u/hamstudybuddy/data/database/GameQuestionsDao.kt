package com.mlm4u.hamstudybuddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface GameQuestionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameQuestion(gameQuestions: GameQuestions)
}