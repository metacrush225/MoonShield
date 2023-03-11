package com.example.moveshield.Fragments

import com.example.moveshield.Activities.ContactActivity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.moveshield.Lockscreen.LockScreenWidgetService
import com.example.moveshield.Services.MySensorService
import com.example.moveshield.R
import java.io.File

class FragmentPrincipal : Fragment() {

    private lateinit var myCheckBoxSmsPermission: CheckBox
    private lateinit var myCheckBoxLocationPermission: CheckBox
    private lateinit var myCheckBoxContactsPermission: CheckBox
    private lateinit var mySwitch: Switch
    private lateinit var myButton: Button




    private lateinit var listView: ListView
    private var isSmsOk = false
    private var isLocationOk = false
    private var isContactOk = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_principal, container, false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // In your Activity or Fragment class:
        //val myFont = Typeface.createFromFile(File("/font/dsdigib.ttf"))
        val typeface = ResourcesCompat.getFont(view.context, R.font.dsdigib)
        //textView.typeface = typeface

        myCheckBoxSmsPermission = view.findViewById(R.id.checkBoxSms)
        myCheckBoxLocationPermission = view.findViewById(R.id.checkBoxLocation)
        myCheckBoxContactsPermission = view.findViewById(R.id.checkBoxContacts)

        myCheckBoxSmsPermission.typeface = typeface
        myCheckBoxLocationPermission.typeface = typeface
        myCheckBoxContactsPermission.typeface = typeface

        myCheckBoxSmsPermission.isClickable = false
        myCheckBoxLocationPermission.isClickable = false
        myCheckBoxContactsPermission.isClickable = false


        myButton = view.findViewById(R.id.button_list_contacts)
        myButton.setOnClickListener {
            Log.d("MoveShielder", "Détection du click ! Affichage des contacts imminent !")
            val intent = Intent(context, ContactActivity::class.java)
            context?.startActivity(intent)
        }

        mySwitch = view.findViewById(R.id.switch1)
        mySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.light_blue_200))
                mySwitch.thumbTintList = colorStateList

                Log.d("MoveShield", "Activation du service")
                val intent = Intent(context, MySensorService::class.java)
                context?.startService(intent)
                val intent2 = Intent(context, LockScreenWidgetService::class.java)
                context?.startService(intent2)


            } else {
                val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.beautiful_red))
                mySwitch.thumbTintList = colorStateList
                Log.d("MoveShield", "Désactivation du service")
                val intent = Intent(context, MySensorService::class.java)
                context?.stopService(intent)
            }
        }




    }


    override fun onResume() {
        super.onResume()
    }


    fun enableSwitchForegroundService() {
        if (isSmsOk && isLocationOk && isContactOk) {
            mySwitch.isClickable = true
        } else {
            mySwitch.isClickable = false
        }
    }

    fun setBooleanSmsPermission(b: Boolean) {
        isSmsOk = b
        myCheckBoxSmsPermission.isChecked = true
    }

    fun setBooleanLocationPermission(b: Boolean) {
        isLocationOk = b
        myCheckBoxLocationPermission.isChecked = true
    }

    fun setBooleanContactPermission(b: Boolean) {
        isContactOk = b
        myCheckBoxContactsPermission.isChecked = true
    }

    fun setChkbTextSmsPermissionText(text: String, color: Int) {
        //myCheckBoxSmsPermission.typeface = myFont
        myCheckBoxSmsPermission.text = text
        myCheckBoxSmsPermission.setTextColor(color)
        myCheckBoxSmsPermission.isChecked = isSmsOk
    }

    fun setChkbTextLocationPermissionText(text: String, color: Int) {
        myCheckBoxLocationPermission.text = text
        //myCheckBoxLocationPermission.typeface = myFont
        myCheckBoxLocationPermission.setTextColor(color)
        myCheckBoxLocationPermission.isChecked = isLocationOk

    }
    fun setChkbTextContactsPermissionText(text: String, color: Int) {
        myCheckBoxContactsPermission.text = text
        //myCheckBoxContactsPermission.typeface = myFont
        myCheckBoxContactsPermission.setTextColor(color)
        myCheckBoxContactsPermission.isChecked = isContactOk

    }

}