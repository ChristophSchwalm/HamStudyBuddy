package com.mlm4u.hamstudybuddy.ui

import android.content.res.ColorStateList
import com.mlm4u.hamstudybuddy.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mlm4u.hamstudybuddy.data.model.GameStatus
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


        vb.btNewAnswers.setOnClickListener {
            sharedViewModel.setGameStatus(GameStatus.NULL)
        }
        vb.btWrongAnswers.setOnClickListener {
            sharedViewModel.setGameStatus(GameStatus.WRONG)
        }
        vb.btRightAnswers.setOnClickListener {
            sharedViewModel.setGameStatus(GameStatus.RIGHT)
        }

        sharedViewModel.gameStatus.observe(viewLifecycleOwner) { status ->
            Log.d("CSChecker", "GameStatus: $status")
            when (status) {
                GameStatus.NULL -> gameQuestionsNew()
                GameStatus.WRONG -> gameQuestionsWrongAnswers()
                GameStatus.RIGHT -> gameQuestionsRightAnswers()
                else -> {}
            }
        }

        sharedViewModel.gameQuestions.observe(viewLifecycleOwner) { gameQuestions ->
            if (gameQuestions.isNotEmpty()) {
                resetView()
                val qSize = gameQuestions?.size
                sharedViewModel.setGameQuestion(gameQuestions.random())
                vb.tvQuestionSize.text = "Du spielst mit ${qSize} Fragen"
                val answers = listOf(
                    sharedViewModel.gameQuestion.value?.answerA,
                    sharedViewModel.gameQuestion.value?.answerB,
                    sharedViewModel.gameQuestion.value?.answerC,
                    sharedViewModel.gameQuestion.value?.answerD
                )
                val shuffledAnswers = answers.shuffled()

                vb.tvGameQuestionNumber.text =
                    "Frage Nr.: ${sharedViewModel.gameQuestion.value?.number}"
                vb.tvGameQuestion.text = sharedViewModel.gameQuestion.value?.question
                vb.tvGameAnswerA.text = shuffledAnswers[0]
                vb.tvGameAnswerB.text = shuffledAnswers[1]
                vb.tvGameAnswerC.text = shuffledAnswers[2]
                vb.tvGameAnswerD.text = shuffledAnswers[3]

                vb.cvGameAnswerA.visibility = View.VISIBLE
                vb.cvGameAnswerB.visibility = View.VISIBLE
                vb.cvGameAnswerC.visibility = View.VISIBLE
                vb.cvGameAnswerD.visibility = View.VISIBLE

            } else {
                vb.tvGameQuestion.text = "Es gibt keine Fragen!"
                vb.tvQuestionSize.text = ""
                vb.cvGameAnswerA.visibility = View.GONE
                vb.cvGameAnswerB.visibility = View.GONE
                vb.cvGameAnswerC.visibility = View.GONE
                vb.cvGameAnswerD.visibility = View.GONE
                resetView()
            }

        }

        vb.cvGameAnswerA.setOnClickListener {
            checkAnswer(vb.tvGameAnswerA.text.toString())
            vb.cvGameAnswerB.isClickable = false
            vb.cvGameAnswerB.alpha = 0.5f
            vb.cvGameAnswerC.isClickable = false
            vb.cvGameAnswerC.alpha = 0.5f
            vb.cvGameAnswerD.isClickable = false
            vb.cvGameAnswerD.alpha = 0.5f
        }
        vb.cvGameAnswerB.setOnClickListener {
            checkAnswer(vb.tvGameAnswerB.text.toString())
            vb.cvGameAnswerA.isClickable = false
            vb.cvGameAnswerA.alpha = 0.5f
            vb.cvGameAnswerC.isClickable = false
            vb.cvGameAnswerC.alpha = 0.5f
            vb.cvGameAnswerD.isClickable = false
            vb.cvGameAnswerD.alpha = 0.5f
        }
        vb.cvGameAnswerC.setOnClickListener {
            checkAnswer(vb.tvGameAnswerC.text.toString())
            vb.cvGameAnswerA.isClickable = false
            vb.cvGameAnswerA.alpha = 0.5f
            vb.cvGameAnswerB.isClickable = false
            vb.cvGameAnswerB.alpha = 0.5f
            vb.cvGameAnswerD.isClickable = false
            vb.cvGameAnswerD.alpha = 0.5f
        }
        vb.cvGameAnswerD.setOnClickListener {
            checkAnswer(vb.tvGameAnswerD.text.toString())
            vb.cvGameAnswerA.isClickable = false
            vb.cvGameAnswerA.alpha = 0.5f
            vb.cvGameAnswerB.isClickable = false
            vb.cvGameAnswerB.alpha = 0.5f
            vb.cvGameAnswerC.isClickable = false
            vb.cvGameAnswerC.alpha = 0.5f
        }
    }

    private fun resetView() {
        vb.tvGameAnswerA.text = ""
        vb.tvGameAnswerB.text = ""
        vb.tvGameAnswerC.text = ""
        vb.tvGameAnswerD.text = ""
        vb.cvGameAnswerA.isClickable = true
        vb.cvGameAnswerB.isClickable = true
        vb.cvGameAnswerC.isClickable = true
        vb.cvGameAnswerD.isClickable = true
        vb.cvGameAnswerA.alpha = 1f
        vb.cvGameAnswerB.alpha = 1f
        vb.cvGameAnswerC.alpha = 1f
        vb.cvGameAnswerD.alpha = 1f


        vb.cvGameAnswerA.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.cvAnswer
            )
        )
        vb.cvGameAnswerB.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.cvAnswer
            )
        )
        vb.cvGameAnswerC.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.cvAnswer
            )
        )
        vb.cvGameAnswerD.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.cvAnswer
            )
        )
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

        } else {
            Timer().schedule(timerTask {
                sharedViewModel.addWrongFlag()
            }, 2000)
        }

    }

    private fun gameQuestionsNew() {
        sharedViewModel.gameQuestionsNew()
        vb.btNewAnswers.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gameActive))
        vb.btWrongAnswers.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gameInactive))
        vb.btRightAnswers.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gameInactive))
        vb.btNewAnswers.setTextColor(ContextCompat.getColor(requireContext(), R.color.gameBtText))
        vb.btWrongAnswers.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        vb.btRightAnswers.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    private fun gameQuestionsWrongAnswers() {
        sharedViewModel.gameQuestionsWrongAnswers()
        vb.btWrongAnswers.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gameActive))
        vb.btNewAnswers.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gameInactive))
        vb.btRightAnswers.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gameInactive))
        vb.btWrongAnswers.setTextColor(ContextCompat.getColor(requireContext(), R.color.gameBtText))
        vb.btNewAnswers.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        vb.btRightAnswers.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    private fun gameQuestionsRightAnswers() {
        sharedViewModel.gameQuestionsRightAnswers()
        vb.btRightAnswers.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gameActive))
        vb.btNewAnswers.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gameInactive))
        vb.btWrongAnswers.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gameInactive))
        vb.btRightAnswers.setTextColor(ContextCompat.getColor(requireContext(), R.color.gameBtText))
        vb.btNewAnswers.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        vb.btWrongAnswers.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

}