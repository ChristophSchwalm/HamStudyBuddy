package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.model.Root
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
            vb.teName.setText(userSettings?.get("Name") as? String)
        }

        sharedViewModel.userClass.observe(viewLifecycleOwner){
            when(it){
                "1" -> vb.rbClassN.isChecked = true
                "2" -> vb.rbClassE.isChecked = true
                "3" -> vb.rbClassA.isChecked = true
            }
        }
        lifecycleScope.launch{
            val version = sharedViewModel.getVersionApi()
            vb.tvVersionNumber.setText("Version: " + version.version.toString())
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
            sharedViewModel.getQuestionsApi()
        }

    }

}