package com.example.ehcf.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ehcf.*
import com.example.ehcf.DateForConsultaion.DateForConsultation
import com.example.ehcf.Specialities.activity.Specialities
import com.example.ehcf.databinding.FragmentBooking3Binding

class BookingFragment : Fragment() {
    private lateinit var binding: FragmentBooking3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooking3Binding.inflate(inflater)

        binding.btnConsultDoctor.setOnClickListener {
            startActivity(Intent(requireContext(), Specialities::class.java))
        }
        binding.btnBookAppointment.setOnClickListener {
            startActivity(Intent(requireContext(), Specialities::class.java))
        }
        binding.btnHomeVisit.setOnClickListener {
            startActivity(Intent(requireContext(), Specialities::class.java))
        }

        return (binding.root)

    }
}