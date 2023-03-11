package com.example.moveshield.Helpers

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import android.util.Log

class SmsHelper {



    fun sendSMSMessage(ctx: Context, phoneNumber: String, message: String) {




        // on the below line we are creating a try and catch block
        try {

            // on below line we are initializing sms manager.
            //as after android 10 the getDefault function no longer works
            //so we have to check that if our android version is greater
            //than or equal toandroid version 6.0 i.e SDK 23
            val smsManager:SmsManager
            if (Build.VERSION.SDK_INT>=23) {
                //if SDK is greater that or equal to 23 then
                //this is how we will initialize the SmsManager
                smsManager = ctx.getSystemService(SmsManager::class.java)
            }
            else{
                //if user's SDK is less than 23 then
                //SmsManager will be initialized like this
                smsManager = SmsManager.getDefault()
            }

            // on below line we are sending text message.
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            // on below line we are displaying a toast message for message send,
            Log.d("MOONSHIELD SMS OK", "SMS envoyé")

        } catch (e: Exception) {

            // on catch block we are displaying toast message for error.
            Log.e("MOONSHIELD SMS ERROR", e.message.toString())
        }
    }

}