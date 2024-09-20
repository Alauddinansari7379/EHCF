package com.example.ehcf.SplashScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivitySplashScreenBinding
import com.example.ehcf.login.activity.SignIn
import com.example.ehcf.sharedpreferences.SessionManager
import com.google.auth.oauth2.GoogleCredentials
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Arrays


class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@SplashScreen)

        Handler().postDelayed({
//            Log.e("",NotificationValue)
//            Log.e("",sessionManager.isLogin.toString())
//            if (sessionManager.isLogin && NotificationValue =="1"){
//              //  NotificationValue =""
//                startActivity(Intent(this@SplashScreen, Appointments::class.java))
//            } else{
            startActivity(Intent(this, SignIn::class.java))

            // }
            finish()
        }, 2000)
    }

}



