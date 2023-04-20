package com.example.moveshield

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.moveshield.Fragments.FragmentContacts
import com.example.moveshield.Fragments.FragmentPrincipal
import com.facebook.react.ReactInstanceManager
import com.facebook.react.shell.MainReactPackage
import com.google.android.gms.location.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [MainActivity.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainActivity : FragmentActivity() {
    class PermissionRequestCodes {
        companion object {
            // Add more permission requests here
            const val PERMISSION_SEND_SMS = 123
            const val PERMISSION_REQUEST_LOCATION = 1
            const val PERMISSION_REQUEST_READ_CONTACTS = 100
            const val OVERLAY_PERMISSION_REQ_CODE = 1  // Choose any value
        }
    }


    var fragmentPrincipal = FragmentPrincipal()
    var fragmentContact = FragmentContacts()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("INIT", "Initiating Main shit")


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "OVERLAY PERMISSION CANNOT DRAWOVERLAYS", Toast.LENGTH_LONG)

                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package: $packageName"))
                startActivityForResult(intent, MainActivity.PermissionRequestCodes.OVERLAY_PERMISSION_REQ_CODE);
            }
        }*/



        fragmentPrincipal = FragmentPrincipal()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_principal, fragmentPrincipal)
            .commit()

        fragmentContact = FragmentContacts()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_contact, fragmentContact) //On ajoute au fragment_container le fragment contact
            .commit()
    }



    override fun onResume() {
        super.onResume()

        requestSmsPermission()
        requestLocationPermission()
        requestContactPermission(this)

        //requestOverlayPermission()
        //fragmentPrincipal.enableSwitchForegroundService()
    }


    private fun requestContactPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), PermissionRequestCodes.PERMISSION_REQUEST_READ_CONTACTS)
            fragmentPrincipal.setChkbTextContactsPermissionText("Contacts : Permission non accordée", Color.RED)
            fragmentPrincipal.setBooleanContactPermission(false)
            //recreate()

        } else {
            fragmentPrincipal.setChkbTextContactsPermissionText("Contacts : Permission OK", Color.GREEN)
            fragmentPrincipal.setBooleanContactPermission(true)


        }
    }

    private fun requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // Permission has already been granted, do something with the SMS
            fragmentPrincipal.setChkbTextSmsPermissionText("SMS : Permission OK", Color.GREEN)
            fragmentPrincipal.setBooleanSmsPermission(true)
            //recreate()


        } else {
            // Permission is not yet granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), PermissionRequestCodes.PERMISSION_SEND_SMS)
            fragmentPrincipal.setChkbTextSmsPermissionText("SMS : Permission non accordée", Color.RED)
            fragmentPrincipal.setBooleanSmsPermission(false)

        }
    }

    private fun requestLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if not granted
            fragmentPrincipal.setBooleanLocationPermission(false)
            fragmentPrincipal.setChkbTextLocationPermissionText("Location : Permission non accordée", Color.RED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PermissionRequestCodes.PERMISSION_REQUEST_LOCATION)
            //return
        }
        fragmentPrincipal.setBooleanLocationPermission(true)
        fragmentPrincipal.setChkbTextLocationPermissionText("Location : Permission OK", Color.GREEN)
        //recreate()

    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !Settings.canDrawOverlays(this)) {
            // Request permission to draw overlays

           val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            //stopSelf()
            return
        }
    }





    /*private fun registerContact(contact: Contact?) {
        if (contact != null) {
            val db = SQLiteOpenHelper(context, "mydb", null, 1).writableDatabase
            val values = ContentValues().apply {
                put("name", contact.name)
                put("phone", contact.phoneNumber)
            }
            db.insert("contacts", null, values)
            db.close()
            Toast.makeText(this, "Registered ${contact.name}", Toast.LENGTH_SHORT).show()
        }
    }*/








    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionRequestCodes.PERMISSION_SEND_SMS -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                        // Show an explanation to the user
                        // You can use AlertDialog to display a message to the user explaining why you need the permission
                    } else {
                        // Request the permission
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), PermissionRequestCodes.PERMISSION_SEND_SMS)
                    }
                }
            }

            // Handle more permission requests here
        }
    }


}