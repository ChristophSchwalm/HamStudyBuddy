package com.mlm4u.hamstudybuddy.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mlm4u.hamstudybuddy.R
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.PoolViewBinding

class PoolAdapter(
    private val dataset: List<Questions>,
    private val sharedViewModel: SharedViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<PoolAdapter.PoolViewHolder>(){

    inner class PoolViewHolder(val binding: PoolViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoolViewHolder {
        val binding = PoolViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PoolViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: PoolViewHolder, position: Int) {
        val title = dataset[position]

        holder.binding.tvTitle.text = title.titleQuestion

        //Wechselt auf QuestionFragment nachdem der Observer sein OK gibt
        holder.binding.cvTitle.setOnClickListener {
            sharedViewModel.changeSelectedTitle(title.titleQuestion)
            sharedViewModel.questionsByTitle.observe(lifecycleOwner) { questionsByTitle ->
                if (questionsByTitle.isNotEmpty()) {
                    holder.itemView.findNavController().navigate(R.id.questionFragment)
                }
            }
        }
    }
}


