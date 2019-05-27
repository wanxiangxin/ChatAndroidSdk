package com.wxx.chat.util

import com.wxx.chat.listener.IConnectListener
import com.wxx.chat.listener.IReceiveMessageListener
import com.wxx.chat.message.BaseMessage

object ChatLauncher {

    var connectListener: IConnectListener? = null
    var receiveMessageListener: IReceiveMessageListener? = null

    fun postConnectSuccess() {
        connectListener?.onConnectSuccess()
    }

    fun postConnectFail(message: String) {
        connectListener?.onConnectError(message)
    }

    fun postMessageReceive(message: String) {
        receiveMessageListener?.onReceiveMessage(BaseMessage().apply {
            content = message
        })
    }
}