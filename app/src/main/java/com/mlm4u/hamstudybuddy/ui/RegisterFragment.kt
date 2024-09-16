package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mlm4u.hamstudybuddy.R
import com.mlm4u.hamstudybuddy.data.viewModel.AuthenticationViewModel
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var vb: FragmentRegisterBinding
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentRegisterBinding.inflate(layoutInflater)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb.btRegister.setOnClickListener {
            val email = vb.teEmail.text.toString()
            val password = vb.tePassword.text.toString()
            val passwordRepeated = vb.tePassword2.text.toString()
            if (password == passwordRepeated) {
                authenticationViewModel.register(email, password)
                findNavController().navigate(R.id.action_registerFragment_to_onboardingFragment)
            } else {
                Toast.makeText(context, "Passwörter stimmen nicht überein", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        vb.btBack.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}