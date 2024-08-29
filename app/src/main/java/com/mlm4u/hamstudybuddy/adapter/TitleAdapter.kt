package com.mlm4u.hamstudybuddy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.TitleViewBinding

class TitleAdapter(
    private val dataset: List<Questions>,
    private val viewModel: SharedViewModel
) : RecyclerView.Adapter<TitleAdapter.TitleViewHolder>(){

    inner class TitleViewHolder(val binding: TitleViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val binding = TitleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TitleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val title = dataset[position]

        holder.binding.tvTitle.text = title.titleQuestion
    }

}