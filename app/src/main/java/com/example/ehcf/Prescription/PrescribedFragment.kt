package com.example.ehcf.Prescription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentCodeBinding


class PrescribedFragment : Fragment() {
    private lateinit var binding:FragmentCodeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?) =
        
        inflater.inflate(R.layout.fragment_code, container, false)!!
}
