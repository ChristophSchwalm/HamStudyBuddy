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
import com.mlm4u.hamstudybuddy.data.database.GameQuestions
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.database.QuestionsDatabase.Companion.getDatabase
import com.mlm4u.hamstudybuddy.data.model.Root
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


    private val _version = MutableLiveData(0.0)
    val version: LiveData<Double>
        get() = _version

    private val _userClass = MutableLiveData<String>("0")
    val userClass: LiveData<String>
        get() = _userClass

    private val _selectedTitle = MutableLiveData<String>()
    val selectedTitle: LiveData<String>
        get() = _selectedTitle

    private var _gameQuestion = MutableLiveData<GameQuestions>()
    val gameQuestion: LiveData<GameQuestions>
        get() = _gameQuestion


    init {
        getVersionApi()
        getUserSettings()
    }


    val allTitle: LiveData<List<Questions>> = userClass.switchMap { it ->
        repository.getAllTitle(it)
    }

    val questionsByTitle: LiveData<List<Questions>> = selectedTitle.switchMap { it ->
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

    //Closure <- anschauen !!!
    fun countQuestions(onCompletion: (Int) -> (Unit)) {
        viewModelScope.launch {
            val result = repository.countQuestions(_userClass.value.toString())
            onCompletion(result)
        }
    }

    fun deleteNumber(number: String) {
        viewModelScope.launch {
            repository.deleteNumber(number)
        }
    }


//**************************************************************************************************
//GameQuestions

    fun insertGameQuestion(question: Questions){
        viewModelScope.launch {
            val gameQuestions = GameQuestions(
                number = question.number,
                classQuestion = question.classQuestion,
                titleQuestion = question.titleQuestion,
                question = question.question,
                answerA = question.answerA,
                answerB = question.answerB,
                answerC = question.answerC,
                answerD = question.answerD,
                pictureQuestion = question.pictureQuestion,
                pictureA = question.pictureA,
                pictureB = question.pictureB,
                pictureC = question.pictureC,
                pictureD = question.pictureD,
            )
            repository.insertGameQuestion(gameQuestions)
        }
    }

    //Closure <- anschauen !!!
    fun countGameQuestions(onCompletion: (Int) -> (Unit)) {
        viewModelScope.launch {
            val result = repository.countGameQuestions(_userClass.value.toString())
            onCompletion(result)
        }
    }

    fun setGameQuestion(questions: GameQuestions) {
        viewModelScope.launch {
            _gameQuestion.value = questions
        }
    }

    fun addCorrectFlag() {
        _gameQuestion.value?.gameCorrectAnswer = true
        viewModelScope.launch {
            _gameQuestion.value?.let { repository.updateGameQuestion(it) }
        }
    }

    fun allGameQuestions(): LiveData<List<GameQuestions>> {
        val result = MutableLiveData<List<GameQuestions>>()
        viewModelScope.launch {
            repository.allGameQuestions(userClass.value ?: "default").observeForever { gameQuestions ->
                if (gameQuestions != null) {
                    result.postValue(gameQuestions)
                    Log.d("ViewModel", "GameQuestions gefunden: ${gameQuestions.size}")
                    Log.d("ViewModel", "GameQuestions gefunden userClass: ${userClass.value.toString()}")
                } else {
                    // Fehlerbehandlung: z.B. leere Liste setzen oder Fehlermeldung anzeigen
                    result.postValue(emptyList())
                    Log.e("ViewModel", "Keine GameQuestions gefunden")
                }
            }
        }
        return result
    }

//**************************************************************************************************
//Firebase

    fun getUserSettings() {
        viewModelScope.launch {
            val userSettings = firebaseRepository.getUserSettings()
            if (userSettings != null) {
                _userClass.value = userSettings["UserClass"] as? String
                userSettings["Name"] as? String
                // Rufe allGameQuestions erst auf, nachdem userClass geladen wurde
                allGameQuestions()
                Log.d("ViewModel", "UserSettings gefunden: ${userSettings["UserClass"]}")
            } else {
                // Handle den Fall, dass keine User Settings gefunden wurden
            }
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
            Log.d("ViewModel", "Version: ${_version.value}")
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




