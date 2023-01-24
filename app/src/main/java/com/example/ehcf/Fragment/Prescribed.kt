package com.example.ehcf.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ehcf.Prescription.MainActivity
import com.example.ehcf.databinding.FragmentViewReportBinding

class Prescribed : Fragment() {
    private lateinit var binding: FragmentViewReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewReportBinding.inflate(inflater)

        binding.btnAddReport.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        return (binding.root)
    }

}