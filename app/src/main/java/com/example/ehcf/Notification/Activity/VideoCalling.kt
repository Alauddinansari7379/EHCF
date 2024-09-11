package com.example.ehcf.Notification.Activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.databinding.ActivityVideoCallingBinding
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.MalformedURLException
import java.net.URL


class VideoCalling : AppCompatActivity() {
    private val context: Context = this@VideoCalling

    private lateinit var binding: ActivityVideoCallingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnNotifiction.setOnClickListener {

        }

        binding.btnJoin.setOnClickListener {

            if (binding.edtEnterRoomID.text.isEmpty()) {
                Toast.makeText(this, "Enter Room Id", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                        .setServerURL(URL("https://meet.jit.si"))
                        .setRoom(binding.edtEnterRoomID.text.toString())
                        .setAudioMuted(false)
                        .setVideoMuted(false)
                        .build()
                    JitsiMeetActivity.launch(this, options)
                } catch (e: MalformedURLException) {
                    e.printStackTrace();
                }
            }


            binding.btnCreateMeeting.setOnClickListener {
                try {
                    val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                        .setServerURL(URL("https://meet.jit.si"))
                        .setRoom("test123456")
                        .setAudioMuted(false)
                        .setVideoMuted(false)
                        .build()
                    JitsiMeetActivity.launch(this, options)
                } catch (e: MalformedURLException) {
                    e.printStackTrace();
                }
            }


        }
    }

}
