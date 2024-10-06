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

    fun countQuestionsClass(classQuestion: String): LiveData<Int> {
        return database.questionsDao.countQuestionsClass(classQuestion)
    }

    suspend fun deleteNumber(number: String) {
        database.questionsDao.deleteNumber(number)
    }

//**************************************************************************************************
//GameQuestionDao

    suspend fun insertGameQuestion(gameQuestions: GameQuestions) {
        database.gameQuestionsDao.insertGameQuestion(gameQuestions)
    }

    fun countAllQuestions(): LiveData<Int> {
        return database.questionsDao.countAllQuestions()
    }

    fun countQuestionsGame(classQuestion: String): LiveData<Int> {
        return database.gameQuestionsDao.countGameQuestionsClass(classQuestion)
    }

    fun countNewQuestions(classQuestion: String): LiveData<Int> {
        return database.gameQuestionsDao.countNewQuestions(classQuestion)
    }

    fun countRightAnswers(classQuestion: String): LiveData<Int> {
        return database.gameQuestionsDao.countRightAnswers(classQuestion)
    }

    fun countWrongAnswers(classQuestion: String): LiveData<Int> {
        return database.gameQuestionsDao.countWrongAnswers(classQuestion)
    }

    fun allGameQuestions(userClass: String): LiveData<List<GameQuestions>> {
        return database.gameQuestionsDao.allGameQuestions(userClass)
    }

    fun gameQuestionsNew(userClass: String): LiveData<List<GameQuestions>> {
        Log.d("CSChecker", "gameQuestionsNew() called in Repository")
        return database.gameQuestionsDao.gameQuestionsNew(userClass)
    }

    fun gameQuestionsWrongAnswers(userClass: String): LiveData<List<GameQuestions>> {
        return database.gameQuestionsDao.gameQuestionsWrongAnswers(userClass)
    }

    fun gameQuestionsRightAnswers(userClass: String): LiveData<List<GameQuestions>> {
        return database.gameQuestionsDao.gameQuestionsRightAnswers(userClass)
    }

    suspend fun resetGame() {
        database.gameQuestionsDao.resetGame()
    }


    suspend fun updateGameQuestion(gameQuestions: GameQuestions) {
        database.gameQuestionsDao.updateGameQuestion(gameQuestions)
    }
}