package com.mlm4u.hamstudybuddy.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.mlm4u.hamstudybuddy.data.database.GameQuestions
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.database.QuestionsDatabase

class Repository(
    private val database: QuestionsDatabase
) {

    suspend fun insertQuestions(questions: List<Questions>) {
        database.questionsDao.insertAll(questions)
    }

    fun getAllTitle(userClass: String): LiveData<List<Questions>> {
        return database.questionsDao.getAllTitle(userClass)
    }

    fun getQuestionsByTitle(userClass: String, title: String): LiveData<List<Questions>> {
        return database.questionsDao.getQuestionsByTitle(userClass, title)
    }

    fun titleList(userClass: String): LiveData<List<Questions>> {
        return database.questionsDao.getAllTitle(userClass)
    }

    suspend fun setReady4Game(number: String) {
        database.questionsDao.setReady4Game(number)
    }

    suspend fun countQuestions(classQuestion: String): Int {
        return database.questionsDao.countQuestions(classQuestion)
    }

    suspend fun deleteNumber(number: String) {
        database.questionsDao.deleteNumber(number)
    }

//**************************************************************************************************
//GameQuestionDao

    suspend fun insertGameQuestion(gameQuestions: GameQuestions) {
        database.gameQuestionsDao.insertGameQuestion(gameQuestions)
    }

    suspend fun countGameQuestions(classQuestion: String): Int {
        return database.gameQuestionsDao.countGameQuestions(classQuestion)
    }

    suspend fun countRightAnswers(classQuestion: String): LiveData<Int> {
        return database.gameQuestionsDao.countRightAnswers(classQuestion)
    }

    suspend fun countWrongAnswers(classQuestion: String): LiveData<Int> {
        return database.gameQuestionsDao.countWrongAnswers(classQuestion)
    }

    suspend fun allGameQuestions(userClass: String): LiveData<List<GameQuestions>> {
        return database.gameQuestionsDao.allGameQuestions(userClass)
    }

    suspend fun gameQuestionsNew(userClass: String): LiveData<List<GameQuestions>> {
        Log.d("CSChecker", "gameQuestionsNew() called in Repository")
        return database.gameQuestionsDao.gameQuestionsNew(userClass)
    }

    suspend fun gameQuestionsWrongAnswers(userClass: String): LiveData<List<GameQuestions>> {
        return database.gameQuestionsDao.gameQuestionsWrongAnswers(userClass)
    }

    suspend fun gameQuestionsRightAnswers(userClass: String): LiveData<List<GameQuestions>> {
        return database.gameQuestionsDao.gameQuestionsRightAnswers(userClass)
    }

    suspend fun resetGame() {
        database.gameQuestionsDao.resetGame()
    }


    suspend fun updateGameQuestion(gameQuestions: GameQuestions) {
        database.gameQuestionsDao.updateGameQuestion(gameQuestions)
    }
}