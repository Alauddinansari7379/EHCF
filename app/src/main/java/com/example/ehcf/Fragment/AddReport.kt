package com.example.ehcf.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ehcf.Fragment.test.ImageUpload
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentViewReportBinding


class AddReport : Fragment() {
    private lateinit var binding: FragmentViewReportBinding

    private var selectedImageUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewReportBinding.bind(view)


        binding.btnSelectImage.setOnClickListener {
        startActivity(Intent(requireContext(),ImageUpload::class.java))
        }


    }

}