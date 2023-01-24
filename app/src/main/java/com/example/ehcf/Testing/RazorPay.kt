package com.example.ehcf.Testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ehcf.databinding.ActivityRazorPayBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class RazorPay : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityRazorPayBinding
    var amt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRazorPayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnPayment.setOnClickListener {
            if (binding.edtEnterAmount.text.isEmpty()) {
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show()
            } else {
                amt = binding.edtEnterAmount.text.toString().toInt()
                startPayment()
            }
        }

    }

    private fun startPayment() {
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", "Alauddin Ansari")
            options.put("description", "Payment Description")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", amt * 100)//pass amount in currency subunits
            val prefill = JSONObject()
            prefill.put("email", "alauddinansari7379@gmail.com.com")
            prefill.put("contact", "7379452259")
            options.put("prefill", prefill)
            co.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successful: ", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Field ", Toast.LENGTH_LONG).show()
    }

}