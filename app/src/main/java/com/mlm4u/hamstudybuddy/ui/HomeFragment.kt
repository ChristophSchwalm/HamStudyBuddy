package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
        Log.d("CSChecker", "Home Fragment: onViewCreated")
        sharedViewModel.loading.observe(viewLifecycleOwner) {
            vb.linearProgressIndicatorHome.visibility = if (it) View.VISIBLE else View.GONE
        }

        val bottomNavigationView =
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE

        authenticationViewModel.currentUser.observe(viewLifecycleOwner) { curentUser ->
            Log.d("CSChecker", "Home Fragment: currentUser vor IF: ${curentUser?.email}")
            if (curentUser == null) {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            } else {
                sharedViewModel.loading.observe(viewLifecycleOwner) {
                    Log.d("CSChecker", "Home Fragment: loading: $it")
                    if (!it) {
                        Log.d("CSChecker", "Ich bin in Loading FALSE")
                        if (sharedViewModel.userClass.value.isNullOrEmpty()) {
                            Log.d(
                                "CSChecker",
                                "Home Fragment: userClass: ${sharedViewModel.userClass.value}"
                            )
                            findNavController().navigate(R.id.onboardingFragment)
                        }
                    }
                }
            }
        }


        vb.btLogout.setOnClickListener {
            authenticationViewModel.logout()
        }
    }
}




