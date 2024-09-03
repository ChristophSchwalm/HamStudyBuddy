package com.mlm4u.hamstudybuddy.data.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.mlm4u.hamstudybuddy.data.FirebaseRepository
import com.mlm4u.hamstudybuddy.data.Repository
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.database.QuestionsDatabase.Companion.getDatabase
import com.mlm4u.hamstudybuddy.data.model.Root
import com.mlm4u.hamstudybuddy.data.model.VersionResponse
import com.mlm4u.hamstudybuddy.data.remote.QuestionApi
import kotlinx.coroutines.launch

class SharedViewModel(
    application: Application
) :  AndroidViewModel(application) {


    private val database = getDatabase(application)
    private val repository = Repository(database)
    private val firebaseRepository = FirebaseRepository()
    private val api = QuestionApi

    val currentUser = firebaseRepository.currentUser

    private val _version = MutableLiveData<Double>(0.0)
    val version: LiveData<Double>
        get() = _version

    private val _userClass = MutableLiveData<String>("")
    val userClass: LiveData<String>
        get() = _userClass

    private val _selectedTitle = MutableLiveData<String>()
    val selectedTitle: LiveData<String>
        get() = _selectedTitle

    init {
        getVersionApi()
    }

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

    fun setReady4Game(number: String){
        viewModelScope.launch {
            repository.setReady4Game(number)
        }
    }


//**************************************************************************************************
//Firebase

    suspend fun getUserSettings(): Map<String, Any>? {
        val userSettings = firebaseRepository.getUserSettings()
        if (userSettings != null) {
            _userClass.value = userSettings["UserClass"] as? String
            userSettings["Name"] as? String
            // Verwende die Werte userClass und name
            return userSettings
        } else {
            // Handle den Fall, dass keine User Settings gefunden wurden
            return null
        }

    }

    fun saveUserSettings(name: String) {
        firebaseRepository.saveUserSettings(name, userClass.value.toString())
    }


//**************************************************************************************************
//Api


    fun getVersionApi(){
        viewModelScope.launch {
            _version.value = api.retrofitService.getVersionApi().version // Wert zurückgeben
        }
    }

    fun getQuestionsApi() {
        viewModelScope.launch {
            try {
                val data: Root = api.retrofitService.getQuestionsApi()
                data.let {
                    // Alle Questions aus den JSON-Daten extrahieren
                    val allQuestions = it.getAllQuestions()
                    // Die Questions in UserQuestions umwandeln
                    val gameQuestions: List<Questions> = allQuestions.map { question ->
                        question.toUserQuestion()
                    }
                    // Die Fragen in die Datenbank einfügen
                    repository.insertQuestions(gameQuestions)
                }
            } catch (e: Exception) {
                // Fehlerbehandlung, z. B. Anzeige einer Fehlermeldung
                Log.e("MainActivity", "Fehler Api -> Room: ${e.message}")
            }
        }
    }
}




