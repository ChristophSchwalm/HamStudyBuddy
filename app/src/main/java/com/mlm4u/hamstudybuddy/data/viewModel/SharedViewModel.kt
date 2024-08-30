package com.mlm4u.hamstudybuddy.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.mlm4u.hamstudybuddy.data.FirebaseRepository
import com.mlm4u.hamstudybuddy.data.Repository
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.database.QuestionsDatabase.Companion.getDatabase
import kotlinx.coroutines.launch

class SharedViewModel(
    application: Application
) :  AndroidViewModel(application) {


    private val database = getDatabase(application)
    private val repository = Repository(database)
    private val firebaseRepository = FirebaseRepository()

    val currentUser = firebaseRepository.currentUser



    private val _userClass = MutableLiveData<String>("1")
    val userClass: LiveData<String>
        get() = _userClass

    private val _selectedTitle = MutableLiveData<String>()
    val selectedTitle: LiveData<String>
        get() = _selectedTitle

    val allTitle: LiveData<List<Questions>> = userClass.switchMap { userClass ->
        repository.getAllTitle(userClass)
    }

    val questionsByTitle: LiveData<List<Questions>> = selectedTitle.switchMap { title ->
        userClass.switchMap { userClass ->
            repository.getQuestionsByTitle(userClass, title)
        }
    }


    fun getAllTitle() {
        //_allTitle.value = repository.getAllTitle("1")
    }

    fun getQuestionsByTitle(){
        repository.getQuestionsByTitle(userClass.value.toString(), selectedTitle.value.toString())
    }

    suspend fun insertQuestions(questions: List<Questions>){
        repository.insertQuestions(questions)
    }



    fun changeSelectedTitle(newQuestionTitle: String){
        viewModelScope.launch {
            _selectedTitle.value = newQuestionTitle
        }
    }








}