package com.mlm4u.hamstudybuddy.data

import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.database.QuestionsDatabase

class Repository (
    private val database: QuestionsDatabase
) {

    //val questionsList: LiveData<List<Questions>> = database.QuestionsDao.getAllQuestions()


    suspend fun insertQuestions(questions: List<Questions>) {
        database.QuestionsDao.insertAll(questions)
    }


}