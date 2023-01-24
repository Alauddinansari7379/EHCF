package com.example.ehcf.sharedpreferences

import android.content.Context
import android.preference.PreferenceManager

class SessionManager(context: Context?) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {

        private const val IS_LOGIN = "islogin"
        private const val BLOOD_GROUP="blood_group"
        private const val CREATED_AT="created_at"
        private const val CUSTOMER_NAME="customer_name"
        private const val EMAIL="email"
        private const val FCM_TOKEN="fcm_token"
        private const val GENDER="gender"
        private const val ID="id"
        private const val LAST_ACTIVE_ADDRESS="last_active_address"
        private const val NO_OF_RATINGS="no_of_ratings"
        private const val OVERALL_RATING="overall_ratings"
        private const val PASSWORD="password"
        private const val PHONE_NUMBER="phone_number"
        private const val PHONE_WITH_CODE="phone_with_code"
        private const val PRE_EXISTING_DESEASE="pre_existing_desease"
        private const val PROFILE_PICTURE="profile_picture"
        private const val STATUS="status"
        private const val UPDATED_AT="updated_at"
        private const val WALLET="wallet"
    }

    var isLogin: Boolean
        get() = prefs.getBoolean(IS_LOGIN, false)
        set(isLogin) {
            prefs.edit().putBoolean(IS_LOGIN, isLogin).apply()
        }

    fun logout() {
        prefs.edit().clear().apply()
    }

    var fcmToken: String?
        get() = prefs.getString(FCM_TOKEN, "")
        set(fcmToken) {
            prefs.edit().putString(FCM_TOKEN, fcmToken).apply()
        }
    var password: String?
        get() = prefs.getString(PASSWORD, "")
        set(password) {
            prefs.edit().putString(PASSWORD, password).apply()
        }
    var  customerName: String?
        get() = prefs.getString(CUSTOMER_NAME, "")
        set(customerName) {
            prefs.edit().putString(CUSTOMER_NAME, customerName).apply()
        }
    var  email: String?
        get() = prefs.getString(EMAIL, "")
        set(email) {
            prefs.edit().putString(EMAIL, email).apply()
        }

    var  phoneNumber: String?
        get() = prefs.getString(PHONE_NUMBER, "")
        set(phoneNumber) {
            prefs.edit().putString(PHONE_NUMBER, phoneNumber).apply()
        }
    var  phoneWithCode: String?
        get() = prefs.getString(PHONE_WITH_CODE, "")
        set(phoneWithCode) {
            prefs.edit().putString(PHONE_WITH_CODE, phoneWithCode).apply()
        }

    var id: Int
        get() = prefs.getInt(ID, -1)
        set(id) {
                prefs.edit().putInt(ID, id).apply()
        }

    var gender: String?
        get() = prefs.getString(GENDER, "")
        set(gender) {
            prefs.edit().putString(GENDER, gender).apply()
        }


}