package com.wxx.chat.listener

interface IConnectListener {
    fun onConnectSuccess()

    fun onConnectError(message: String)
}