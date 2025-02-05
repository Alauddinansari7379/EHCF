package com.example.ehcf.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Specialities.activity.Specialities
import com.example.ehcf.databinding.FragmentBooking3Binding
import com.example.ehcf.sharedpreferences.SessionManager
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class BookingFragment : Fragment() {
    private lateinit var binding: FragmentBooking3Binding
    private lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooking3Binding.inflate(inflater)
        sessionManager = SessionManager(requireContext())

        binding.btnConsultDoctor.setOnClickListener {
            val bookingType = "1"
            sessionManager.bookingType = "1"
            val intent = Intent(context as Activity, Specialities::class.java)
                .putExtra("bookingType", bookingType)
            (context as Activity).startActivity(intent)

//            val intent = Intent(context as Activity, SpecialitiesConsultDoctor::class.java)
//                .putExtra("bookingType",bookingType)
//            (context as Activity).startActivity(intent)
        }
        binding.btnBookAppointment.setOnClickListener {
            sessionManager.bookingType = "2"
            val bookingType = "2"
            val intent = Intent(context as Activity, Specialities::class.java)
                .putExtra("bookingType", bookingType)
            (context as Activity).startActivity(intent)
        }
        binding.btnHomeVisit.setOnClickListener {
            sessionManager.bookingType = "3"
            val bookingType = "3"
            val intent = Intent(context as Activity, Specialities::class.java)
                .putExtra("bookingType", bookingType)
            (context as Activity).startActivity(intent)
        }

        return (binding.root)

    }

    override fun onStart() {
        super.onStart()
        if (isOnline(requireContext())) {
            //  myToast(requireActivity(), "Connected")
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }
//        CheckInternet().check { connected ->
//            if (connected) {
//             //    myToast(requireActivity(),"Connected")
//            }
//            else {
//                val changeReceiver = NetworkChangeReceiver(context)
//                changeReceiver.build()
//                //  myToast(requireActivity(),"Check Internet")
//            }
//        }
    }

}