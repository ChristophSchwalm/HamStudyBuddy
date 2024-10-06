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
import com.mlm4u.hamstudybuddy.data.model.GameStatus
import com.mlm4u.hamstudybuddy.data.model.Root
import com.mlm4u.hamstudybuddy.data.remote.QuestionApi
import kotlinx.coroutines.launch

class SharedViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = Repository(database)
    private val firebaseRepository = FirebaseRepository()
    private val api = QuestionApi

    val currentUser = firebaseRepository.currentUser


    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _userSettings = MutableLiveData<Map<String, Any>>()
    val userSettings: LiveData<Map<String, Any>>
        get() = _userSettings

    private val _version = MutableLiveData(0.0)
    val version: LiveData<Double>
        get() = _version

    private val _userClass = MutableLiveData("")
    val userClass: LiveData<String>
        get() = _userClass

    private val _selectedTitle = MutableLiveData<String>()
    val selectedTitle: LiveData<String>
        get() = _selectedTitle

    private val _gameStatus = MutableLiveData(GameStatus.NULL)
    val gameStatus: LiveData<GameStatus>
        get() = _gameStatus

    private var _gameQuestion = MutableLiveData<GameQuestions>()
    val gameQuestion: LiveData<GameQuestions>
        get() = _gameQuestion

    private val _gameQuestions = MutableLiveData<List<GameQuestions>>()
    val gameQuestions: MutableLiveData<List<GameQuestions>>
        get() = _gameQuestions


    val countAllQuestions: LiveData<Int> = userClass.switchMap { userClassValue ->
        repository.countAllQuestions()
    }

    val countQuestionsClass: LiveData<Int> = userClass.switchMap { userClassValue ->
        repository.countQuestionsClass(userClassValue)
    }

    val countQuestionsGame: LiveData<Int> = userClass.switchMap { userClassValue ->
        repository.countQuestionsGame(userClassValue) // Assuming you have this function in your repository
    }

    val countRightAnswers: LiveData<Int> = userClass.switchMap { userClassValue ->
        repository.countRightAnswers(userClassValue)
    }

    val countWrongAnswers: LiveData<Int> = userClass.switchMap { userClassValue ->
        repository.countWrongAnswers(userClassValue)
    }

    val countNewQuestions: LiveData<Int> = userClass.switchMap { userClassValue ->
        repository.countNewQuestions(userClassValue)
    }

    init {
        Log.d("CSChecker", "SharedViewModel init")
        getUserSettings()
        getVersionApi()
    }

    val allTitle: LiveData<List<Questions>> = userClass.switchMap { it ->
        it?.let { repository.getAllTitle(it) }
    }

    val questionsByTitle: LiveData<List<Questions>> = selectedTitle.switchMap { it ->
        repository.getQuestionsByTitle(userClass.value.toString(), selectedTitle.value.toString())
    }

    suspend fun insertQuestions(questions: List<Questions>) {
        repository.insertQuestions(questions)
    }

    fun changeSelectedTitle(newQuestionTitle: String) {
        _selectedTitle.value = newQuestionTitle
    }

    fun changeUserClass(newUserClass: String) {
        viewModelScope.launch {
            _userClass.value = newUserClass
        }
    }

//    //Closure <- anschauen !!!
//    fun countQuestions(onCompletion: (Int) -> (Unit)) {
//        viewModelScope.launch {
//            val result = repository.countQuestions(_userClass.value.toString())
//            onCompletion(result)
//        }
//    }

//    fun countAllQuestions(onCompletion: (Int) -> (Unit)) {
//        viewModelScope.launch {
//            val result = repository.countAllQuestions()
//            onCompletion(result)
//        }
//    }

    fun deleteNumber(number: String) {
        viewModelScope.launch {
            repository.deleteNumber(number)
        }
    }


//**************************************************************************************************
//GameQuestions

    fun insertGameQuestion(question: Questions) {
        viewModelScope.launch {
            _loading.value = true
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
            _loading.value = false
        }
    }

//    //Closure <- anschauen !!!
//    fun countGameQuestionsClass(onCompletion: (Int) -> (Unit)) {
//        viewModelScope.launch {
//            val result = repository.countGameQuestionsClass(_userClass.value.toString())
//            onCompletion(result)
//        }
//    }

    fun countNewQuestions(onCompletion: (Int) -> (Unit)) {
        viewModelScope.launch {
            val result = repository.countNewQuestions(_userClass.value.toString())
            result.observeForever {
                onCompletion(it)
            }
        }
    }

    fun countRightAnswers(onCompletion: (Int) -> (Unit)) {
        viewModelScope.launch {
            val result = repository.countRightAnswers(_userClass.value.toString())
            result.observeForever {
                onCompletion(it)
            }
        }
    }

    fun countWrongAnswers(onCompletion: (Int) -> (Unit)) {
        viewModelScope.launch {
            val result = repository.countWrongAnswers(_userClass.value.toString())
            result.observeForever {
                onCompletion(it)
            }
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

    fun addWrongFlag() {
        _gameQuestion.value?.gameCorrectAnswer = false
        viewModelScope.launch {
            _gameQuestion.value?.let { repository.updateGameQuestion(it) }
        }
    }

    fun allGameQuestions() {
        val result = MutableLiveData<List<GameQuestions>?>()
        viewModelScope.launch {
            repository.allGameQuestions(userClass.value ?: "")
                .observeForever { gameQuestions ->
                    if (gameQuestions != null) {
                        result.postValue(gameQuestions)
                    } else {
                        // Fehlerbehandlung: z.B. leere Liste setzen oder Fehlermeldung anzeigen
                        result.postValue(emptyList())
                    }
                }
        }
        result.observeForever {
            _gameQuestions.value = it
        }
    }

    fun gameQuestionsNew() {
        val result = MutableLiveData<List<GameQuestions>?>()
        viewModelScope.launch {
            repository.gameQuestionsNew(userClass.value ?: "")
                .observeForever { gameQuestionsNew ->
                    if (gameQuestionsNew != null) {
                        result.postValue(gameQuestionsNew)
                    } else {
                        // Fehlerbehandlung: z.B. leere Liste setzen oder Fehlermeldung anzeigen
                        result.postValue(emptyList())
                    }
                }
        }
        Log.d("CSChecker", "gameQuestionsNew: ${result.value}")
        result.observeForever {
            _gameQuestions.value = it
        }
    }

    fun gameQuestionsWrongAnswers() {
        val result = MutableLiveData<List<GameQuestions>?>()
        viewModelScope.launch {
            repository.gameQuestionsWrongAnswers(userClass.value ?: "")
                .observeForever { gameQuestionsWrongAnswers ->
                    if (gameQuestionsWrongAnswers != null) {
                        result.postValue(gameQuestionsWrongAnswers)
                    } else {
                        // Fehlerbehandlung: z.B. leere Liste setzen oder Fehlermeldung anzeigen
                        result.postValue(emptyList())
                    }
                }
        }
        result.observeForever {
            _gameQuestions.value = it
        }
    }

    fun gameQuestionsRightAnswers() {
        val result = MutableLiveData<List<GameQuestions>?>()
        viewModelScope.launch {
            repository.gameQuestionsRightAnswers(userClass.value ?: "")
                .observeForever { gameQuestionsRightAnswers ->
                    if (gameQuestionsRightAnswers != null) {
                        result.postValue(gameQuestionsRightAnswers)
                    } else {
                        // Fehlerbehandlung: z.B. leere Liste setzen oder Fehlermeldung anzeigen
                        result.postValue(emptyList())
                    }
                }
        }
        result.observeForever {
            _gameQuestions.value = it
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            repository.resetGame()
        }
    }

    fun setGameStatus(status: GameStatus) {
        _gameStatus.value = status
    }


//**************************************************************************************************
//Firebase

    fun getUserSettings() {
        Log.d("CSChecker", "getUserSettings() called")
        viewModelScope.launch {
            _loading.value = true
            Log.d("CSChecker", "Loading auf true im Api Call")
            Log.d("CSChecker", "${_loading.value}")
            try {
                val userSettings = firebaseRepository.getUserSettings()
                Log.d("CSChecker", "getUserSettings() finished")
                Log.d("CSChecker", "ViewModel userSettings: $userSettings")
                if (userSettings != null) {
                    _userSettings.value = userSettings
                    _userClass.value = userSettings["UserClass"] as? String
                    // Rufe allGameQuestions erst auf, nachdem userClass geladen wurde
                    allGameQuestions()
                } else {
                    Log.w("CSChecker", "No User Settings found")
                }
            } catch (e: Exception) {
                Log.e("CSChecker", "Error fetching user settings: ${e.message}")
            }
            _loading.value = false
        }

    }

    fun saveUserSettings(name: String) {
        firebaseRepository.saveUserSettings(name, userClass.value.toString())
        getUserSettings()
    }


//**************************************************************************************************
//Api

    fun getVersionApi() {
        viewModelScope.launch {
            try {
                _version.value = api.retrofitService.getVersionApi().version
                Log.d("CSChecker", "Version: ${_version.value}")
            } catch (e: Exception) {
                Log.e("CSChecker", "Error fetching version: ${e.message}")
            }
        }
    }

    fun getQuestionsApi() {
        viewModelScope.launch {
            _loading.value = true
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
                Log.e("CSChecker", "Fehler Api -> Room: ${e.message}")
            }
            _loading.value = false
        }
    }


}





