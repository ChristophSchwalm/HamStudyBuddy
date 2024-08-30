package com.mlm4u.hamstudybuddy.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.database.QuestionsDatabase

class Repository (
    private val database: QuestionsDatabase
) {

    //val titleList: LiveData<List<Questions>> = database.QuestionsDao.getAllTitle("1")

    suspend fun insertQuestions(questions: List<Questions>) {
        database.QuestionsDao.insertAll(questions)
    }

    fun getAllTitle(userClass: String): LiveData<List<Questions>> {
        return database.QuestionsDao.getAllTitle(userClass)
    }

    fun getQuestionsByTitle(userClass: String, title: String): LiveData<List<Questions>> {
        return database.QuestionsDao.getQuestionsByTitle(userClass, title)
    }

    fun titleList(userClass: String) : LiveData<List<Questions>> {
        return database.QuestionsDao.getAllTitle(userClass)
    }
}