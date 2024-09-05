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
                val questionsSize = gameQuestions.size

                vb.tvQuestionSize.text = "Du spielst mit $questionsSize Fragen"
                val randomQuestion = gameQuestions.random()
                vb.tvGameQuestion.text = randomQuestion.question
                vb.tvGameAnswerA.text = randomQuestion.answerA
                vb.tvGameAnswerB.text = randomQuestion.answerB
                vb.tvGameAnswerC.text = randomQuestion.answerC
                vb.tvGameAnswerD.text = randomQuestion.answerD
            }
        }


    }
}