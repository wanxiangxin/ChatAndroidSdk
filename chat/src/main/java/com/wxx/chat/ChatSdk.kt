package com.wxx.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.wxx.chat.listener.IConnectListener
import com.wxx.chat.listener.IPacketListener
import com.wxx.chat.listener.IReceiveMessageListener
import com.wxx.chat.message.BaseMessage
import com.wxx.chat.receiver.ChatReceiver
import com.wxx.chat.socket.SocketClient
import com.wxx.chat.util.ChatLauncher

@SuppressLint("StaticFieldLeak")
@Suppress("unused")
object ChatSdk {

    lateinit var mContext: Context
    private val socketClient by lazy { SocketClient() }

    @JvmStatic
    fun init(context: Context) {
        mContext = context

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        context.registerReceiver(ChatReceiver(), intentFilter)

        connect()
    }

    @JvmStatic
    fun connect() {
        socketClient.connect()
    }

    @JvmStatic
    fun reConnect() {
        socketClient.reConnect()
    }

    @JvmStatic
    fun setReceiveMessageListener(listener: IReceiveMessageListener) {
        ChatLauncher.receiveMessageListener = listener
    }

    @JvmStatic
    fun setConnectListener(listener: IConnectListener) {
        ChatLauncher.connectListener = listener
    }

    @JvmStatic
    fun sendMessage(message: String?, listener: IPacketListener?) {
        if (message == null) return
        socketClient.sendMessage(BaseMessage().apply {
            content = message
        }, listener)
    }

    fun isSocketEnable(): Boolean {
        return socketClient.isSocketEnable()
    }
}