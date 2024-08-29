package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
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
import com.mlm4u.hamstudybuddy.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var vb: FragmentLoginBinding
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentLoginBinding.inflate(layoutInflater)
        return vb.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.INVISIBLE

        vb.teEmailLogin.setOnFocusChangeListener { v, hasFocus ->
            vb.textInputLayout.hint = if(hasFocus) "" else "E-Mail"
        }
        vb.tePasswordLogin.setOnFocusChangeListener { v, hasFocus ->
            vb.textInputLayout2.hint = if (hasFocus) "" else "Password"
        }

        authenticationViewModel.logout()

        authenticationViewModel.currentUser.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        vb.btToRegister.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        vb.btLogin.setOnClickListener{
            val email = vb.teEmailLogin.text.toString()
            val password = vb.tePasswordLogin.text.toString()
            authenticationViewModel.login(email, password)
        }

    }
}