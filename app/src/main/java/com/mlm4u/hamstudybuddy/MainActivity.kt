package com.mlm4u.hamstudybuddy

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.model.Root
import com.mlm4u.hamstudybuddy.data.remote.QuestionApi
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var vb: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        vb.bottomNavigationView.itemIconTintList = null

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        vb.bottomNavigationView.setupWithNavController(navController)


        vb.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bootcampFragment -> {
                   /* val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.bootcampFragment, true)
                        .build()*/
                    navController.navigate(R.id.bootcampFragment, null)
                    true
                }

                else -> {
                    navController.navigate(item.itemId)
                true
                }
            }
        }

        Log.d("MainActivity", "${sharedViewModel.currentUser}")
        api()
    }

        private fun api () {
            Log.d("MainActivity", "onCreate called")
            lifecycleScope.launch {
                Log.d("MainActivity", "lifecycleScope.launch called")
                try {
                    val questionRemoteApi = QuestionApi.retrofitService
                    Log.d("MainActivity", "QuestionRemoteApi created")
                    val data: Root = questionRemoteApi.getQuestions()

                    Log.d("MainActivity", "Data: $data")
                    data.let {
                        // Alle Questions aus den JSON-Daten extrahieren

                        val allQuestions = it.getAllQuestions()
                        Log.d("MainActivity", "Anzahl der Fragen: ${allQuestions.size}")
                        // Die Questions in UserQuestions umwandeln
                        val gameQuestions: List<Questions> = allQuestions.map { question ->
                            question.toUserQuestion()
                        }

                        // Die Fragen in die Datenbank einfügen
                        Log.d("MainActivity", "Vor Insert:")
                        sharedViewModel.insertQuestions(gameQuestions)
                        Log.d("MainActivity", "Nach Insert:")
                    }
                } catch (e: Exception) {
                    // Fehlerbehandlung, z. B. Anzeige einer Fehlermeldung
                    Log.e("MainActivity", "Fehler Api -> Room: ${e.message}")
                }
            }

            lifecycleScope.launch {
                try {
                    val version = QuestionApi.retrofitService.getVersion()
                    Log.d("MainActivity", "Version: ${version.version}")
                } catch (e: Exception) {
                    Log.e("MainActivity", "VersiongCheck Error: ${e.message}")
                }
            }
        }

}