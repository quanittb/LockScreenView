package com.example.lockscreenservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class MyForegroundService : Service() {
    private val mBinder: IBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        val service: MyForegroundService
            get() = this@MyForegroundService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate ")
        Toast.makeText(this, "The service is running", Toast.LENGTH_SHORT).show()
        startForeground(NOTIFICATION_ID, createNotification("The service is running"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        return Service.START_STICKY
    }

    private fun createNotification(message: String): Notification {

        // Get the layouts to use in the custom notification
        val notificationLayout = RemoteViews(getPackageName(), R.layout.notification_layout)
        notificationLayout.setTextViewText(R.id.txtTitle, message)
        val mNotificationManager: NotificationManager?
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 125, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)
        val payableLogo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground)
        mBuilder.setContentTitle("My Service")
            .setContentText(message)
            .setPriority(Notification.PRIORITY_HIGH)
            .setLargeIcon(payableLogo)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setCustomBigContentView(notificationLayout)
        mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = CHANNEL_ID
            val channel =
                NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            mNotificationManager!!.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        return mBuilder.build()
    }

    private fun showNotification(message: String) {
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        mNotificationManager!!.notify(NOTIFICATION_ID, createNotification(message))
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    companion object {
        private const val TAG = "MyForegroundService"
        private const val NOTIFICATION_ID = 2999
        private const val CHANNEL_ID = "MyForegroundService_ID"
        private val CHANNEL_NAME: CharSequence = "MyForegroundService Channel"
    }
}