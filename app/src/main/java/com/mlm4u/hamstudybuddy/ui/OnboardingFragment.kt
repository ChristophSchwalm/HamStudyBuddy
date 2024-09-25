package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mlm4u.hamstudybuddy.MainActivity
import com.mlm4u.hamstudybuddy.R
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private lateinit var vb: FragmentOnboardingBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentOnboardingBinding.inflate(layoutInflater)
        return (vb.root)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.loading.observe(viewLifecycleOwner) {
            vb.linearProgressIndicatorOnboarding.visibility = if (it) View.VISIBLE else View.GONE
        }

        val bottomNavigationView =
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.INVISIBLE

        vb.rgOnboardingKlassen.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                vb.rbOnboardingClassN.id -> {
                    sharedViewModel.changeUserClass("1")
                }

                vb.rbOnboardingClassE.id -> {
                    sharedViewModel.changeUserClass("2")
                }

                vb.rbOnboardingClassA.id -> {
                    sharedViewModel.changeUserClass("3")
                }
            }
        }

        vb.btOnboardingSave.setOnClickListener {
            if (vb.rgOnboardingKlassen.checkedRadioButtonId != -1) {
                sharedViewModel.saveUserSettings(vb.etName.text.toString())
                sharedViewModel.getQuestionsApi()
                findNavController().navigate(R.id.action_onboardingFragment_to_homeFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Bitte wähle eine Klasse aus",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}