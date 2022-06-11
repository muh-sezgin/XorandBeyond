package com.example.xorbeyond

import android.app.*
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.xorbeyond.databinding.ActivityMainBinding
import com.example.xorbeyond.databinding.FragmentFirstBinding
import com.google.android.exoplayer2.util.NotificationUtil.createNotificationChannel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val CHANNEL_ID = "channel_id"
    private val notifId = 101



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(applicationInfo.name)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        GlobalScope.launch {
            delay(1000)
            sendNotification()
        }

    }


    private fun createNotificationChannel() {
        val name = "Notification Title"
        val descriptionText = "Example Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel:NotificationChannel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_sadece_kafa)
            .setContentTitle("XOR&BEYOND")
            .setContentText("Intruder Alert!!!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(notifId, builder.build())
        }
    }

}