package com.wxx.chat.listener

abstract class IPacketListener {

    val createTime = System.currentTimeMillis()

    abstract fun onSuccess()

    abstract fun onFail(message: String)
}