package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.model.Root
import com.mlm4u.hamstudybuddy.data.remote.QuestionApi
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private lateinit var vb: FragmentSettingsBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentSettingsBinding.inflate(layoutInflater)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Starten einer Coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            val userSettings = sharedViewModel.getUserSettings()
            vb.teName.setText(userSettings)
        }

        sharedViewModel.userClass.observe(viewLifecycleOwner){
            when(it){
                "1" -> vb.rbClassN.isChecked = true
                "2" -> vb.rbClassE.isChecked = true
                "3" -> vb.rbClassA.isChecked = true
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            val version = sharedViewModel.getVersion()
            vb.tvVersionNumber.setText(version.version.toString())
        }


        vb.rgKlassen.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId){
                vb.rbClassN.id -> {sharedViewModel.changeUserClass("1")}
                vb.rbClassE.id -> {sharedViewModel.changeUserClass("2")}
                vb.rbClassA.id -> {sharedViewModel.changeUserClass("3")}
            }
        }

        vb.btSave.setOnClickListener {
            sharedViewModel.saveUserSettings(vb.teName.text.toString())
        }

        vb.btUpdate.setOnClickListener{
            api()
        }

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