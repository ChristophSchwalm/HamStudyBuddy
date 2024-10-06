package com.mlm4u.hamstudybuddy.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.mlm4u.hamstudybuddy.data.FirebaseRepository
import kotlinx.coroutines.launch

class AuthenticationViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    val currentUser = firebaseRepository.currentUser

    fun login(email: String, password: String) {
        viewModelScope.launch {
            firebaseRepository.loginUser(email, password)
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            firebaseRepository.registerNewUser(email, password)
        }
    }

    fun logout() {
        firebaseRepository.logoutUser()
    }


    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            firebaseRepository.signInWithGoogle(account)
        }
    }

}