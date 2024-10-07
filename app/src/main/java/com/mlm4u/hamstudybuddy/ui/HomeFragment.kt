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
                            findNavController().navigate(R.id.startFragment)
                        }
                    }
                }
            }
        }

        sharedViewModel.userSettings.observe(viewLifecycleOwner) {
            vb.tvUserName.text = "Hallo, " + it["Name"] as? String + " ! \n" +
                    "Willkommen bei HAM Study Buddy!"
        }

        sharedViewModel.countQuestionsClass.observe(viewLifecycleOwner) { count ->
            vb.tvQuestionClass.text = count.toString()
        }

        sharedViewModel.countQuestionsGame.observe(viewLifecycleOwner) { count ->
            vb.tvCountGame.text = count.toString()
            sharedViewModel.countAllQuestions.observe(viewLifecycleOwner) { allCount ->
                vb.tvAllQuestionDB.text = (count + allCount).toString()
            }
        }

        sharedViewModel.countRightAnswers.observe(viewLifecycleOwner) { count ->
            vb.tvCountGameRight.text = count.toString()
            val countQuestionsGameValue = sharedViewModel.countQuestionsGame.value
            if (countQuestionsGameValue != null && countQuestionsGameValue != 0) {
                vb.pbRightAnswer.progress =
                    (count * 100 / countQuestionsGameValue).coerceAtMost(100)
                vb.tvRightProzent.text = "${vb.pbRightAnswer.progress}%"
            } else {
                vb.pbRightAnswer.progress = 0
                vb.tvRightProzent.text = "0%"
            }
        }

        sharedViewModel.countWrongAnswers.observe(viewLifecycleOwner) { count ->
            vb.tvCountGameWrong.text = count.toString()
            val countQuestionsGameValue = sharedViewModel.countQuestionsGame.value
            if (countQuestionsGameValue != null && countQuestionsGameValue != 0) {
                vb.pbWrongAnswer.progress =
                    (count * 100 / countQuestionsGameValue).coerceAtMost(100)
                vb.tvWrongProzent.text = "${vb.pbWrongAnswer.progress}%"
            } else {
                vb.pbWrongAnswer.progress = 0
                vb.tvWrongProzent.text = "0%"
            }
        }

        sharedViewModel.countNewQuestions.observe(viewLifecycleOwner) { count ->
            vb.tvCountGameNew.text = count.toString()
            val countQuestionsGameValue = sharedViewModel.countQuestionsGame.value
            if (countQuestionsGameValue != null && countQuestionsGameValue != 0) {
                vb.pbNewInGame.progress = (count * 100 / countQuestionsGameValue).coerceAtMost(100)
                vb.tvNewProzent.text = "${vb.pbNewInGame.progress}%"
            } else {
                vb.pbNewInGame.progress = 0
                vb.tvNewProzent.text = "0%"
            }
        }




        vb.btLogout.setOnClickListener {
            authenticationViewModel.logout()
        }
    }

}




