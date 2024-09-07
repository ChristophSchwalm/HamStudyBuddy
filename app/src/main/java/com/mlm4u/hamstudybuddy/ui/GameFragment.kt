package com.mlm4u.hamstudybuddy.ui

import android.annotation.SuppressLint
import com.mlm4u.hamstudybuddy.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.FragmentGameBinding
import java.util.Timer
import kotlin.concurrent.timerTask

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

        if (sharedViewModel.gameQuestion.value == null) {
            vb.tvGameQuestion.text = "Es gibt noch keine Fragen im Game!"
            vb.tvQuestionSize.text = ""
        }

        sharedViewModel.allGameQuestions().observe(viewLifecycleOwner) { gameQuestions ->
            if (gameQuestions.isNotEmpty()) {
                resetView()
                val qSize = gameQuestions.size
                sharedViewModel.setGameQuestion(gameQuestions.random())
                vb.tvQuestionSize.text = "Du spielst mit ${qSize} Fragen"
                val answers = listOf(
                    sharedViewModel.gameQuestion.value?.answerA,
                    sharedViewModel.gameQuestion.value?.answerB,
                    sharedViewModel.gameQuestion.value?.answerC,
                    sharedViewModel.gameQuestion.value?.answerD
                )
                val shuffledAnswers = answers.shuffled()

                vb.tvGameQuestion.text = sharedViewModel.gameQuestion.value?.question
                vb.tvGameAnswerA.text = shuffledAnswers[0]
                vb.tvGameAnswerB.text = shuffledAnswers[1]
                vb.tvGameAnswerC.text = shuffledAnswers[2]
                vb.tvGameAnswerD.text = shuffledAnswers[3]

            }
        }

        vb.cvGameAnswerA.setOnClickListener {
            checkAnswer(vb.tvGameAnswerA.text.toString())
        }
        vb.cvGameAnswerB.setOnClickListener {
            checkAnswer(vb.tvGameAnswerB.text.toString())
        }
        vb.cvGameAnswerC.setOnClickListener {
            checkAnswer(vb.tvGameAnswerC.text.toString())
        }
        vb.cvGameAnswerD.setOnClickListener {
            checkAnswer(vb.tvGameAnswerD.text.toString())
        }

    }

    private fun resetView() {
        vb.cvGameAnswerA.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        vb.cvGameAnswerB.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        vb.cvGameAnswerC.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        vb.cvGameAnswerD.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    fun checkAnswer(answer: String) {
        val correctAnswer = sharedViewModel.gameQuestion.value?.answerA
        val isCorrectAnswer = answer == correctAnswer

        val cardBackgroundColor = if (isCorrectAnswer) {
            ContextCompat.getColor(requireContext(), R.color.green)
        } else {
            ContextCompat.getColor(requireContext(), R.color.red)
        }

        when (answer) {
            vb.tvGameAnswerA.text.toString() -> {
                vb.cvGameAnswerA.setCardBackgroundColor(
                    cardBackgroundColor
                )

            }
            vb.tvGameAnswerB.text.toString() -> {
                vb.cvGameAnswerB.setCardBackgroundColor(
                    cardBackgroundColor
                )

            }
            vb.tvGameAnswerC.text.toString() -> {
                vb.cvGameAnswerC.setCardBackgroundColor(
                    cardBackgroundColor
                )

            }
            vb.tvGameAnswerD.text.toString() -> {
                vb.cvGameAnswerD.setCardBackgroundColor(
                    cardBackgroundColor
                )

            }
        }

        if (isCorrectAnswer) {
            Timer().schedule(timerTask {
                sharedViewModel.addCorrectFlag()
            }, 2000)

        }

    }
}