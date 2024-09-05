package com.mlm4u.hamstudybuddy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_questions")
class GameQuestions(
    @PrimaryKey
    val number: String,

    val classQuestion: String,
    val titleQuestion: String,
    val question: String,
    val answerA: String,
    val answerB: String,
    val answerC: String,
    val answerD: String,
    val pictureQuestion: String? = null,
    val pictureA: String? = null,
    val pictureB: String? = null,
    val pictureC: String? = null,
    val pictureD: String? = null,

    var gameCorrectAnswer: Boolean = false


)