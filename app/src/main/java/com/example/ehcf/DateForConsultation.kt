package com.example.ehcf

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.hardware.camera2.params.RggbChannelVector.RED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf.databinding.ActivityDateForConsultationBinding
import com.giphy.sdk.core.models.enums.MediaType

class DateForConsultation : AppCompatActivity() {
    private lateinit var binding: ActivityDateForConsultationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var selectTime = ""
        binding = ActivityDateForConsultationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnConfirm.setOnClickListener {
            startActivity(Intent(this, FindYourDoctor::class.java))
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.card0955.setOnClickListener {
            selectTime = "0955"
            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
        binding.card1025.setOnClickListener {
            selectTime = "1025"
            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
        binding.card1045.setOnClickListener {
            selectTime = "1045"
            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
        binding.card1125.setOnClickListener {
            selectTime = "1125"
            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
        binding.card0100.setOnClickListener {
            selectTime = "0100"
            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
        binding.card0130.setOnClickListener {
            selectTime = "0130"
            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
        binding.card0200.setOnClickListener {
            selectTime = "0200"

            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
        binding.card0230.setOnClickListener {
            selectTime = "0230"

            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
        binding.card0500.setOnClickListener {
            selectTime = "0500"
            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
        binding.card0520.setOnClickListener {
            selectTime = "0520"

            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
        binding.card0625.setOnClickListener {
            selectTime = "0625"
            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))

        }
        binding.card0555.setOnClickListener {
            selectTime = "0555"
            binding.card0955.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1025.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1045.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card1125.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0100.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0130.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0200.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0230.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0500.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0520.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.card0555.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#9F367A"))
            binding.card0625.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

        }
    }
}