package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.mlm4u.hamstudybuddy.R
import com.mlm4u.hamstudybuddy.adapter.QuestionAdapter
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment() {

    private lateinit var vb: FragmentQuestionBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: QuestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentQuestionBinding.inflate(layoutInflater)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb.rvQuestions.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Füge den SnapHelper hinzu, aber nur einmal
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(vb.rvQuestions)

        // Beobachte die LiveData
        sharedViewModel.questionsByTitle.observe(viewLifecycleOwner) { questionsByTitle ->
            adapter = QuestionAdapter(questionsByTitle, sharedViewModel)
            vb.rvQuestions.adapter = adapter
        }
    }
}