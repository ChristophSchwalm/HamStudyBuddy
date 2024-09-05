package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var vb: FragmentGameBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentGameBinding.inflate(layoutInflater)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.allGameQuestions.observe(viewLifecycleOwner) { gameQuestions ->
            if (gameQuestions.isNotEmpty()) {
                val questions = gameQuestions[0]
                vb.tvGameQuestion.text = questions.question
                vb.tvGameAswerA.text = questions.answerA
                vb.tvGameAnswerB.text = questions.answerB
                vb.tvGameAnswerC.text = questions.answerC
                vb.tvGameAswerD.text = questions.answerD
            }
        }

    }
}