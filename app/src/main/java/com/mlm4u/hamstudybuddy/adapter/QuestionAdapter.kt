package com.mlm4u.hamstudybuddy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mlm4u.hamstudybuddy.R
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.QuestionViewBinding

class QuestionAdapter(
    private val dataset: List<Questions>,
    private val viewModel: SharedViewModel
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>(){

    inner class QuestionViewHolder(val binding: QuestionViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = QuestionViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return QuestionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item = dataset[position]

        holder.binding.tvQuestion.text = item.question

        val answers = listOf(item.answerA, item.answerB, item.answerC, item.answerD)
        val shuffledAnswers = answers.shuffled()

        holder.binding.tvAnswerA.text = shuffledAnswers[0]
        holder.binding.tvAnswerB.text = shuffledAnswers[1]
        holder.binding.tvAnswerC.text = shuffledAnswers[2]
        holder.binding.tvAnswerD.text = shuffledAnswers[3]

        holder.binding.cvAnswerA.setOnClickListener{ checkAnswer(holder.binding.tvAnswerA.text.toString(), item, holder) }
        holder.binding.cvAnswerB.setOnClickListener{ checkAnswer(holder.binding.tvAnswerB.text.toString(), item, holder) }
        holder.binding.cvAnswerC.setOnClickListener{ checkAnswer(holder.binding.tvAnswerC.text.toString(), item, holder) }
        holder.binding.cvAnswerD.setOnClickListener{ checkAnswer(holder.binding.tvAnswerD.text.toString(), item, holder) }

/*        holder.binding.cvReadyForGame.setOnClickListener{
            viewModel.setReady4Game(item.number)
        }*/

    }

    private fun checkAnswer(answer: String, item: Questions, holder: QuestionViewHolder) {
        var isCorrectAnswer = false
        val cardBackgroundColor = if (answer == item.answerA) {
            ContextCompat.getColor(holder.itemView.context, R.color.green)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.red)
        }

        when (answer) {
            holder.binding.tvAnswerA.text.toString() -> {
                holder.binding.cvAnswerA.setCardBackgroundColor(cardBackgroundColor)
                if (cardBackgroundColor == ContextCompat.getColor(holder.itemView.context, R.color.green)) {
                    isCorrectAnswer = true
                }
            }
            holder.binding.tvAnswerB.text.toString() -> {
                holder.binding.cvAnswerB.setCardBackgroundColor(cardBackgroundColor)
                if (cardBackgroundColor == ContextCompat.getColor(holder.itemView.context, R.color.green)) {
                    isCorrectAnswer = true
                }
            }
            holder.binding.tvAnswerC.text.toString() -> {
                holder.binding.cvAnswerC.setCardBackgroundColor(cardBackgroundColor)
                if (cardBackgroundColor == ContextCompat.getColor(holder.itemView.context, R.color.green)) {
                    isCorrectAnswer = true
                }
            }
            holder.binding.tvAnswerD.text.toString() -> {
                holder.binding.cvAnswerD.setCardBackgroundColor(cardBackgroundColor)
                if (cardBackgroundColor == ContextCompat.getColor(holder.itemView.context, R.color.green)) {
                    isCorrectAnswer = true
                }
            }
        }

        holder.binding.cvReadyForGame.visibility = if (isCorrectAnswer) View.VISIBLE else View.GONE
    }
}