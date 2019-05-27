package com.wxx.chat.listener

import com.wxx.chat.message.BaseMessage

interface IReceiveMessageListener {
    fun onReceiveMessage(message: BaseMessage)
}