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
import okhttp3.internal.threadName

class SharedViewModel(
    application: Application
) :  AndroidViewModel(application) {


    private val database = getDatabase(application)
    private val repository = Repository(database)
    private val firebaseRepository = FirebaseRepository()

    val currentUser = firebaseRepository.currentUser

    private val _userClass = MutableLiveData<String>("")
    val userClass: LiveData<String>
        get() = _userClass

    private val _selectedTitle = MutableLiveData<String>()
    val selectedTitle: LiveData<String>
        get() = _selectedTitle

    val allTitle: LiveData<List<Questions>> = userClass.switchMap { it ->
        repository.getAllTitle(it)
    }

    val questionsByTitle: LiveData<List<Questions>> = selectedTitle.switchMap { it ->
        repository.getQuestionsByTitle(userClass.value.toString(), selectedTitle.value.toString())
    }

    fun getQuestionsByTitle() {
        repository.getQuestionsByTitle(userClass.value.toString(), selectedTitle.value.toString())
    }

    suspend fun insertQuestions(questions: List<Questions>) {
        repository.insertQuestions(questions)
    }

    fun changeSelectedTitle(newQuestionTitle: String) {
        viewModelScope.launch {
            _selectedTitle.value = newQuestionTitle
        }
    }

    fun changeUserClass(newUserClass: String) {
        viewModelScope.launch {
            _userClass.value = newUserClass
        }
    }


//**************************************************************************************************
//Firebase

    suspend fun getUserSettings(): String? {
        val userSettings = firebaseRepository.getUserSettings()
        if (userSettings != null) {
            _userClass.value = userSettings["UserClass"] as? String
            val name = userSettings["Name"] as? String
            // Verwende die Werte userClass und name
            return name
        } else {
            // Handle den Fall, dass keine User Settings gefunden wurden
            return null
        }

    }

    fun saveUserSettings(name: String) {
        firebaseRepository.saveUserSettings(name, userClass.value.toString())
    }
}




