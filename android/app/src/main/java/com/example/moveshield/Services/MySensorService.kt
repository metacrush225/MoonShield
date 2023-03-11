package com.example.moveshield.Services

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.*
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.moveshield.Helpers.SmsHelper
import com.example.moveshield.Lockscreen.LockScreenWidgetService
import java.util.*

class MySensorService : Service(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var lastUpdate: Long = 0
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f

    private var timer: Timer? = null

    private var lat: Double = 200.0
    private var lon: Double = 200.0
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private lateinit var geocoder: Geocoder



    override fun onCreate() {
        Log.v("MOONSHIELD Service", "onCreate called !")

        super.onCreate()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)



        Log.v("MOONSHIELD Service WIDGETSERVICE OK >> ", "ALL RIGHT")

    }


    fun getAddressFromGeoCoder(latitude: Double, longitude: Double) {
        geocoder = Geocoder(this, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            geocoder.getFromLocation(latitude,longitude,1,object : Geocoder.GeocodeListener{
                override fun onGeocode(addresses: MutableList<Address>) {


                    val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    val city = addresses[0].locality
                    val state = addresses[0].adminArea
                    val country = addresses[0].countryName
                    val postalCode = addresses[0].postalCode
                    val knownName = addresses[0].featureName // Only if available else return NU

                    //val streetName = address.thoroughfare
                    var streetName = address.toString()

                    Log.d("MOONSHIELD Service TESTGEO", streetName)
                    sendTheSms(lat, lon, streetName)

                    //Toast.makeText(applicationContext, "streetName = " + streetName, Toast.LENGTH_SHORT)
                }

            })
        } else {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)

            if (addressList!!.isNotEmpty()) {
                val address = addressList[0]
                var streetName = address.thoroughfare
                // Use the streetName value here
                Log.d("MOONSHIELD Service TESTGEO streetName : ", streetName)

            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
        //locationManager.removeUpdates(locationListener)

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }




//Ici on met startForeground avec 1 en parametre pour la priorité.
//Le return START_STICKY permet au service de tourner, meme s'il n'y a plus d'activité.
//Enfin, le timer permet de réveiller le tout toutes les 10 secondes
override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Log.d("MOONSHIELD Service startCommand", "onStartCommand called !")

    val intent = Intent(this, LockScreenWidgetService::class.java)
    this.startService(intent)
    Log.v("MOONSHIELD Service WIDGETSERVICE OK >> ", "ALL RIGHT")



    locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lat = location.latitude
            lon = location.longitude
            Log.v("MOONSHIELD Service TESTGEO latlon", "latitude = $lat and longitude = $lon")
            // do something with the latitude and longitude values
        }
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    try {
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000,
            0.0f,
            locationListener
        )
    } catch(e: SecurityException) {
        Log.e("MOONSHIELD Service Security Exception !!!", "Error = ${e.message}")
    }

    timer = Timer()
    timer?.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("moveshield_id", "MoveShield Channel", NotificationManager.IMPORTANCE_DEFAULT)
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }


            // Create a notification
            val builder = NotificationCompat.Builder(this@MySensorService, "moveshield_id")
                .setSmallIcon(R.drawable.ic_dialog_alert)
                .setContentTitle("MoonShield Service")
                .setContentText("MoonShield Service is running")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSilent(true) // Disable the sound
            // Start the service in the foreground with the notification
            startForeground(1, builder.build())
        }
    }, 0, 10000)
    return START_STICKY
}



    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastUpdate > 100) {
                val diffTime = currentTime - lastUpdate
                lastUpdate = currentTime

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                Log.d("MOONSHIELD Service sensor : ", "x = $x, y = $y, z = $z")



                val speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000

                if (speed > 1600) {
                    val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val vibratorManager =
                            getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                        vibratorManager.defaultVibrator
                    } else {
                        @Suppress("DEPRECATION")
                        getSystemService(VIBRATOR_SERVICE) as Vibrator
                    }

                    val pattern = longArrayOf(0, 500, 1000, 500, 1000)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        //vib.vibrate(pattern, -1)
                        vib.vibrate(VibrationEffect.createOneShot(1200,10));
                    } else {
                        @Suppress("DEPRECATION")
                        vib.vibrate(pattern, -1);

                    }


                    getAddressFromGeoCoder(lat, lon)

                    Log.d("MOONSHIELD Service", "Shake detected")
                    Log.d("MOONSHIELD Service", "Latitude = $lat")
                    Log.d("MOONSHIELD Service", "Longitude = $lon")
                }

                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //Quand la précision du Sensor est modifié, agir ici.
    }



    fun sendTheSms(latitude: Double, longitude: Double, streetName: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // Permission has already been granted, do something with the SMS
            val the_sms_sender = SmsHelper()

            val phoneNumber = "0769447324"
            val message = "MoonShield >>> Au secours, ma position est la suivante : $streetName [latitude : $latitude, longitude : $longitude]"
            the_sms_sender.sendSMSMessage(this, phoneNumber, message)

            try {



            } catch(e: Exception) {
                Log.e("MOONSHIELD Service WIDGETSERVICE ERROR >> ", e.message.toString())
            }


        } else {
            //val permissions_SMS = arrayOf(Manifest.permission.SEND_SMS)
            //ActivityCompat.requestPermissions(this, permissions_SMS, PERMISSION_SEND_SMS);
        }
    }
}







