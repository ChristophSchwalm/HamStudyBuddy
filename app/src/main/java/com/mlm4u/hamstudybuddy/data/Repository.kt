package com.mlm4u.hamstudybuddy.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.database.QuestionsDatabase

class Repository (
    private val database: QuestionsDatabase
) {

    val titleList: LiveData<List<Questions>> = database.QuestionsDao.getAllTitle("1")

    suspend fun insertQuestions(questions: List<Questions>) {
        database.QuestionsDao.insertAll(questions)
    }

    fun getAllTitle(classQuestions: String) {
        database.QuestionsDao.getAllTitle(classQuestions)
    }

    fun getQuestionsByTitle(classQuestions: String, selectedTitle: String) {
        database.QuestionsDao.getQuestionsByTitle(classQuestions, selectedTitle)
    }

    fun titleList(userClass: String) : LiveData<List<Questions>> {
        return database.QuestionsDao.getAllTitle(userClass)
    }
}