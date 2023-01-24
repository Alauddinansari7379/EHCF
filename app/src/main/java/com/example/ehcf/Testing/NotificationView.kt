package com.example.ehcf.Testing

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.databinding.ActivityNotificationViewBinding


class NotificationView : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationViewBinding
    val textView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getting the notification message
        //getting the notification message
        val message = intent.getStringExtra("message")
        binding.textView.text = message
    }
}