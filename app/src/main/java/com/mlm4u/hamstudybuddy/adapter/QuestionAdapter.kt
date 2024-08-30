package com.mlm4u.hamstudybuddy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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


    }
}