package com.mlm4u.hamstudybuddy.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _titleList = repository.titleList
    val titleList: LiveData<List<Questions>>
        get() = _titleList

    private val _userClass = MutableLiveData<String>("0")
    val userClass: LiveData<String>
        get() = _userClass

    private val _selectedTitle = MutableLiveData<String>()
    val selectedTitle: LiveData<String>
        get() = _selectedTitle

    suspend fun insertQuestions(questions: List<Questions>){
        repository.insertQuestions(questions)
    }

    suspend fun getQuestionsByTitle(){
        repository.getQuestionsByTitle(userClass.value.toString(), selectedTitle.value.toString())
    }







}