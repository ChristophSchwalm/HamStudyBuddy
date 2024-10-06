package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mlm4u.hamstudybuddy.data.viewModel.AuthenticationViewModel
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var vb: FragmentSettingsBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentSettingsBinding.inflate(layoutInflater)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.loading.observe(viewLifecycleOwner) {
            vb.linearProgressIndicatorSettings.visibility = if (it) View.VISIBLE else View.GONE
        }

        sharedViewModel.userSettings.observe(viewLifecycleOwner) {
            Log.d("CSChecker", "Im Observer: $it")
            vb.teName.setText(it["Name"] as? String)
        }

        sharedViewModel.userClass.observe(viewLifecycleOwner) {
            when (it) {
                "1" -> vb.rbClassN.isChecked = true
                "2" -> vb.rbClassE.isChecked = true
                "3" -> vb.rbClassA.isChecked = true
            }
            //countQuestions()
        }

        sharedViewModel.version.observe(viewLifecycleOwner) {
            vb.tvVersionNumber.text = "HamStudyBuddy Version $it"
        }

        vb.rgKlassen.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                vb.rbClassN.id -> {
                    sharedViewModel.changeUserClass("1")
                }

                vb.rbClassE.id -> {
                    sharedViewModel.changeUserClass("2")
                }

                vb.rbClassA.id -> {
                    sharedViewModel.changeUserClass("3")
                }
            }
        }

        vb.btSave.setOnClickListener {
            sharedViewModel.saveUserSettings(vb.teName.text.toString())
        }

        vb.btUpdate.setOnClickListener {
            sharedViewModel.getQuestionsApi()
        }

        vb.btGameReset.setOnClickListener {
            sharedViewModel.resetGame()
        }

        vb.btnDeleteAccount.setOnClickListener {

        }

    }

}