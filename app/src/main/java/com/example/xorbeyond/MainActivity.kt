package com.example.xorbeyond

import android.app.*
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.xorbeyond.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object {
        //change only this for different servers
        val baseURL = "http://192.168.137.81:5002/"
        var profileUpdateList:HashMap<String, String> = HashMap<String, String>()
    }



    private lateinit var binding: ActivityMainBinding

    //declare channel and request
    private val CHANNEL_ID = "channel_id"
    private val notifId = 101
    private val client = OkHttpClient()
    private var request = Request.Builder().url(baseURL + "intruder_get")
        .get().build()
    private var intruder_info = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(applicationInfo.name)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //notification channel should be created before used
        createNotificationChannel()

        //send request to server in thread

        GlobalScope.launch {
            while(true) {
                delay(2000)
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        //println(response.body?.string())
                        var str_response = response.body!!.string()

                        var json_contact = JSONObject(str_response)
                        var resp = json_contact.get("intruder").toString()
                        if(resp.equals("1"))
                            intruder_info = 1

                        else if(resp.equals("2"))
                            intruder_info = 2


                    }


                    override fun onFailure(call: Call, e: IOException) {
                        println(e.message.toString())
                    }
                })
                println(intruder_info)
                //if there is intrusion send notification
                if(intruder_info == 2) {
                    sendNotification_intruder()
                    //40 second delay after notification
                    //to not spam notification for same alert
                    delay(10000)
                }

                else if(intruder_info == 1) {
                    sendNotification_anomalous()
                    delay(10000)
                }
            }
        }

    }

    //notification channel declared
    private fun createNotificationChannel() {
        val name = "Notification Title"
        val descriptionText = "Example Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel:NotificationChannel = NotificationChannel(CHANNEL_ID,
            name, importance).apply {
            description = descriptionText
        }

        val notificationManager:NotificationManager = getSystemService(Context
            .NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    //change title, text, icon etc of notification from here
    private fun sendNotification_intruder() {
        println("intruderrrrrrrrrrrrrrrrr")
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_sadece_kafa)
            .setContentTitle("XOR&BEYOND")
            .setContentText("Intruder Alert!!!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo_sadece_kafa2))
        with(NotificationManagerCompat.from(this)) {
            notify(notifId, builder.build())
        }
    }

    private fun sendNotification_anomalous() {
        println("anomalllllllll")
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_sadece_kafa)
            .setContentTitle("XOR&BEYOND")
            .setContentText("Anomalous Activity")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(notifId, builder.build())
        }

        println("sonnn")
    }

}