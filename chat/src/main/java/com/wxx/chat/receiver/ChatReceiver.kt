package com.wxx.chat.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.wxx.chat.ChatSdk
import com.wxx.chat.util.ChatLogUtil

class ChatReceiver: BroadcastReceiver() {
    var isFirst = true
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        val action = intent.action

        ChatLogUtil.log("收到系统广播>>>$action")
        Toast.makeText(context, "收到系统广播>>>$action", Toast.LENGTH_LONG).show()

        if (ConnectivityManager.CONNECTIVITY_ACTION == action) {
            //网络变化
            if (!isFirst) {
                val connectManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                if (connectManager?.activeNetworkInfo?.isConnected == true) {
                    ChatSdk.connect()
                } else {
                    ChatSdk.reConnect()
                }
            }
            isFirst = false
        } else if (Intent.ACTION_SCREEN_ON == action) {
            //屏幕唤醒
            if (!ChatSdk.isSocketEnable()) {
                ChatSdk.connect()
            }
        }
    }
}