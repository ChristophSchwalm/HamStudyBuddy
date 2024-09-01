package com.mlm4u.hamstudybuddy.data

import androidx.lifecycle.LiveData
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.database.QuestionsDatabase

class Repository (
    private val database: QuestionsDatabase
) {

    //val titleList: LiveData<List<Questions>> = database.QuestionsDao.getAllTitle("1")

    suspend fun insertQuestions(questions: List<Questions>) {
        database.questionsDao.insertAll(questions)
    }

    fun getAllTitle(userClass: String): LiveData<List<Questions>> {
        return database.questionsDao.getAllTitle(userClass)
    }

    fun getQuestionsByTitle(userClass: String, title: String): LiveData<List<Questions>> {
        return database.questionsDao.getQuestionsByTitle(userClass, title)
    }

    fun titleList(userClass: String) : LiveData<List<Questions>> {
        return database.questionsDao.getAllTitle(userClass)
    }

    suspend fun setReady4Game(number: String) {
        database.questionsDao.setReady4Game(number)
    }
}