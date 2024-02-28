package com.example.ehcf.Pharmacy.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentPastBinding
import com.example.ehcf.sharedpreferences.SessionManager


class FragmentPast : Fragment() {
    lateinit var binding: FragmentPastBinding
    lateinit var sessionManager: SessionManager
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_past, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPastBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        with(binding){
         }
    }




}