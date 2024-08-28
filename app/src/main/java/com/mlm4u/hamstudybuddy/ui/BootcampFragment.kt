package com.mlm4u.hamstudybuddy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mlm4u.hamstudybuddy.R
import com.mlm4u.hamstudybuddy.databinding.FragmentBootcampBinding

class BootcampFragment : Fragment() {

    private lateinit var vb: FragmentBootcampBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentBootcampBinding.inflate(layoutInflater)
        return vb.root
    }

}