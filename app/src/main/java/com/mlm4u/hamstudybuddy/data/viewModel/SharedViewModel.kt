package com.mlm4u.hamstudybuddy.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mlm4u.hamstudybuddy.data.FirebaseRepository
import com.mlm4u.hamstudybuddy.data.Repository
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.database.QuestionsDatabase.Companion.getDatabase

class SharedViewModel(
    application: Application
) :  AndroidViewModel(application) {


    private val database = getDatabase(application)
    private val repository = Repository(database)
    private val firebaseRepository = FirebaseRepository()

    val currentUser = firebaseRepository.currentUser

    suspend fun insertQuestions(questions: List<Questions>){
        repository.insertQuestions(questions)
    }







}