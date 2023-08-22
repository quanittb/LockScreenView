package com.example.lockscreenservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.concurrent.locks.Lock

class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_SCREEN_ON) {
            // Màn hình đã được bật lên, gửi Intent để khởi động dịch vụ
            val serviceIntent = Intent(context, LockScreenService::class.java)
            context?.startService(serviceIntent)
        }
    }
}
