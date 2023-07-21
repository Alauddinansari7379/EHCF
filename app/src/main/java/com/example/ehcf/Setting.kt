package com.example.ehcf

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Address.Activity.AddressList
import com.example.ehcf.FAQ.FAQCategories
import com.example.ehcf.FamailyMember.activity.AddNewFamily
import com.example.ehcf.FamailyMember.activity.FamilyList
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.databinding.ActivitySettingBinding
import com.example.ehcf.login.activity.SignIn
import com.example.ehcf.sharedpreferences.SessionManager
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver

class Setting : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private val context: Context = this@Setting
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
//        binding.tvAddFamilyMember.setOnClickListener {
//            startActivity(Intent(this, AddNewFamily::class.java))
//
//        }
//        binding.tvFamilyMemberList.setOnClickListener {
//            startActivity(Intent(this, FamilyList::class.java))
//        }

        binding.tvAddressBook.setOnClickListener {
            startActivity(Intent(this, AddressList::class.java))
        }
        binding.tvPrivacyPolicies.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicies::class.java))
        }
        binding.tvFaqCategories.setOnClickListener {
            startActivity(Intent(this, FAQCategories::class.java))
        }
        binding.tvLogOut.setOnClickListener {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure want to logout?")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()
                        sessionManager.logout()
                        val intent = Intent(applicationContext, SignIn::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                    }
                    .setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }
                    .show()
                   }
    }
    override fun onStart() {
        super.onStart()
        if (isOnline(this)) {
            //  myToast(requireActivity(), "Connected")
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }
    }

}