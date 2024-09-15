package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mlm4u.hamstudybuddy.MainActivity
import com.mlm4u.hamstudybuddy.R
import com.mlm4u.hamstudybuddy.data.viewModel.AuthenticationViewModel
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var vb: FragmentHomeBinding
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentHomeBinding.inflate(layoutInflater)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bottomNavigationView =
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE

        authenticationViewModel.currentUser.observe(viewLifecycleOwner) {
            if (it == null) {
                Log.d("DEBUG", "currentUser==0: ${sharedViewModel.currentUser.value.toString()}")
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            } else {
                Log.d("DEBUG", "currentUser!=0: ${sharedViewModel.currentUser.value.toString()}")
                sharedViewModel.userClass.observe(viewLifecycleOwner) {
                    Log.d("DEBUG", "Vor der If-Abfrage: userClass: $it")
                    if (sharedViewModel.userClass.value == "0") {
                        Log.d("DEBUG", "Nach der If-Abfrage: userClass: ${sharedViewModel.userClass.value.toString()}")
                        findNavController().navigate(R.id.onboardingFragment)
                    }
                }
            }
        }

        vb.btLogout.setOnClickListener {
            authenticationViewModel.logout()
        }
    }
}