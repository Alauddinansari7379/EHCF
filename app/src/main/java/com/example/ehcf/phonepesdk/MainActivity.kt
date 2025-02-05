package com.example.ehcf.phonepesdk

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ehcf.R
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.PhonePeInitException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.Charset
import java.security.MessageDigest


class MainActivity : AppCompatActivity() {
    var apiEndPoint = "/pg/v1/pay"
    val salt = "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399" // salt key
    val MERCHANT_ID = "PGTESTPAYUAT"  // Merhcant id
    val MERCHANT_TID = "txnId"
    val BASE_URL = "https://api-preprod.phonepe.com/"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phonepay)

        PhonePe.init(this)

        try {
            val upiApps = PhonePe.getUpiApps()
            Log.e("phonepe",upiApps.toString())
        } catch (exception: PhonePeInitException) {
            exception.printStackTrace();
        }

        val data = JSONObject()
        data.put("merchantTransactionId", MERCHANT_TID)//String. Mandatory

        data.put("merchantId" , MERCHANT_ID) //String. Mandatory

         data.put("amount", 200 )//Long. Mandatory

         data.put("mobileNumber", "7908834635") //String. Optional
         data.put("callbackUrl", "https://webhook.site/6658f3da-60a4-440f-a743-27e3dcdb91a8") //String. Mandatory

        val paymentInstrument = JSONObject()
        paymentInstrument.put("type", "PAY_PAGE")
      //  paymentInstrument.put("targetApp", "com.phonepe.simulator")

         data.put("paymentInstrument", paymentInstrument )//OBJECT. Mandatory


        val deviceContext = JSONObject()
        deviceContext.put("deviceOS", "ANDROID")
         data.put("deviceContext", deviceContext)


//        val base64Body = android.util.Base64(Gson().toJson(data))

        val payloadBase64 = android.util.Base64.encodeToString(
            data.toString().toByteArray(Charset.defaultCharset()), android.util.Base64.NO_WRAP
        )

        val checksum = sha256(payloadBase64 + apiEndPoint + salt) + "###1";

        Log.d("phonepe", "onCreate: $payloadBase64")
        Log.d("phonepe", "onCreate: $checksum")

        val b2BPGRequest = B2BPGRequestBuilder()
            .setData(payloadBase64)
            .setChecksum(checksum)
            .setUrl(apiEndPoint)
            .build()


        val button = findViewById<Button>(R.id.buttonPAy)
        button.setOnClickListener {
            //For SDK call below function
            Log.d("phonepe", "payloadBase64: $payloadBase64")
            Log.d("phonepe", "checksum: $checksum")

            try {
                PhonePe.getImplicitIntent(this, b2BPGRequest, "")
                    ?.let { startActivityForResult(it, 1) };
            } catch (e: PhonePeInitException) {
            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {

            Log.d("phonepe", "data: $data")
            Log.d("phonepe", "data!!.data: ${data!!.data}")

            checkStatus()

            /*This callback indicates only about completion of UI flow.
            Inform your server to make the transaction
            status call to get the status. Update your app with the
            success/failure status.*/
        }
    }

    private fun checkStatus() {
        val xVerify = sha256("/pg/v1/status/$MERCHANT_ID/${MERCHANT_TID}${salt}") + "###1"

        Log.d("phonepe", "xverify : $xVerify")

        val headers = mapOf(
            "Content-Type" to "application/json",
            "X-VERIFY" to xVerify,
            "X-MERCHANT-ID" to MERCHANT_ID,
        )

        lifecycleScope.launch(Dispatchers.IO) {

            val res = ApiUtilities.getApiInterface().checkStatus(MERCHANT_ID, MERCHANT_TID, headers)

            withContext(Dispatchers.Main) {

                Log.d("phonepe", "APIResponse${res.body()}")

                if (res.body() != null && res.body()!!.success) {
                    Log.d("phonepe", "onCreate: success")
                    Toast.makeText(this@MainActivity, res.body()!!.message, Toast.LENGTH_SHORT).show()

                }
            }
        }


    }


    private fun sha256(input: String): String {
        val bytes = input.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}