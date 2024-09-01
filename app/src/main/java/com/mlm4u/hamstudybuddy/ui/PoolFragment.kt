package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mlm4u.hamstudybuddy.adapter.PoolAdapter
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.FragmentPoolBinding

class PoolFragment : Fragment() {

    private lateinit var vb: FragmentPoolBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: PoolAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentPoolBinding.inflate(layoutInflater)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb.rvPool.layoutManager = LinearLayoutManager(requireContext())


        sharedViewModel.allTitle.observe(viewLifecycleOwner) { allTitle ->
            adapter = PoolAdapter(allTitle, sharedViewModel)
            vb.rvPool.adapter = adapter
        }

    }
}