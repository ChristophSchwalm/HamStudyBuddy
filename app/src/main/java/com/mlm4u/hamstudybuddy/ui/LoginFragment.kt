package com.mlm4u.hamstudybuddy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mlm4u.hamstudybuddy.MainActivity
import com.mlm4u.hamstudybuddy.R
import com.mlm4u.hamstudybuddy.R.string
import com.mlm4u.hamstudybuddy.data.viewModel.AuthenticationViewModel
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var vb: FragmentLoginBinding
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentLoginBinding.inflate(layoutInflater)
        return vb.root
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // GoogleSignInOptions so konfigurieren, dass das ID-Token angefordert wird
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(string.default_web_client_id))  // Hole das ID-Token für Firebase
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //BottomNav auf Invisible da nicht gebraucht
        val bottomNavigationView =
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.INVISIBLE

        vb.teEmailLogin.setOnFocusChangeListener { v, hasFocus ->
            vb.textInputLayout.hint = if (hasFocus) "" else "E-Mail"
        }
        vb.tePasswordLogin.setOnFocusChangeListener { v, hasFocus ->
            vb.textInputLayout2.hint = if (hasFocus) "" else "Password"
        }

        vb.btGoogleSignIn.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                // Starte den Sign-In-Prozess erneut
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }

        authenticationViewModel.logout()

        authenticationViewModel.currentUser.observe(viewLifecycleOwner) {
            it?.let {
                sharedViewModel.getUserSettings()
                Log.d("CSChecker", "User ist eingeloggt")
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        vb.btToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        vb.btLogin.setOnClickListener {
            val email = vb.teEmailLogin.text.toString()
            val password = vb.tePasswordLogin.text.toString()
            authenticationViewModel.login(email, password)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    // Übergibt das GoogleSignInAccount-Objekt
                    authenticationViewModel.signInWithGoogle(it)
                }
            } catch (e: ApiException) {
                // Verbesserte Fehlerbehandlung mit spezifischerer Ausgabe
                when (e.statusCode) {
                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> {
                        Log.w("LoginFragment", "Anmeldung abgebrochen.")
                    }

                    GoogleSignInStatusCodes.SIGN_IN_FAILED -> {
                        Log.e("LoginFragment", "Anmeldung fehlgeschlagen: ${e.message}")
                    }

                    GoogleSignInStatusCodes.NETWORK_ERROR -> {
                        Log.e("LoginFragment", "Netzwerkfehler: Überprüfe die Internetverbindung.")
                    }

                    else -> {
                        Log.e(
                            "LoginFragment",
                            "Unbekannter Fehler bei der Anmeldung: Code=${e.statusCode}, Nachricht=${e.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                // Allgemeine Fehlerbehandlung
                Log.e("LoginFragment", "Ein unerwarteter Fehler ist aufgetreten: ${e.message}")
            }
        }
    }
}