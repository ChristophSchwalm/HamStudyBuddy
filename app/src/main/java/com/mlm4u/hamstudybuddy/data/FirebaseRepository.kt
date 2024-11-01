package com.mlm4u.hamstudybuddy.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val TAG = "FirebaseRepository"

class FirebaseRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    suspend fun loginUser(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            _currentUser.value = firebaseAuth.currentUser
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun registerNewUser(email: String, password: String) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            loginUser(email, password)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logoutUser() {
        firebaseAuth.signOut()
        _currentUser.value = firebaseAuth.currentUser
    }


    suspend fun signInWithGoogle(account: GoogleSignInAccount) {
        val idToken = account.idToken
        if (idToken != null) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            try {
                firebaseAuth.signInWithCredential(credential).await()
                _currentUser.value = firebaseAuth.currentUser
                Log.d(TAG, "Google sign-in successful")
            } catch (e: Exception) {
                Log.e(TAG, "Google sign-in failed", e)
            }
        } else {
            Log.e(TAG, "Google sign-in failed: ID Token is null")
        }
    }

    fun saveUserSettings(name: String, userClass: String){
        firebaseAuth.currentUser?.let { user ->
            val documentReference = firebaseFirestore
                .collection("USER_SETTINGS")
                .document(user.uid)
            documentReference.set(
                hashMapOf(
                    "UserClass" to userClass,
                    "Name" to name,
                )
            )
        }
    }


    suspend fun getUserSettings(): Map<String, Any>? {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return null
            val documentSnapshot = firebaseFirestore
                .collection("USER_SETTINGS")
                .document(userId)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                documentSnapshot.data
            } else {
                null
            }
        } catch (e: Exception) {
            // Fehlerbehandlung, z.B. Loggen des Fehlers
            Log.e("DEBUG", "Fehler beim Abrufen der Einstellungen", e)
            null
        }
    }


}