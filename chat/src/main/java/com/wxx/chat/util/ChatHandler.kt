package com.wxx.chat.util

import android.os.Handler
import android.os.Looper

@Suppress("unused")
object ChatHandler {

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    fun runUiThread(runnable: Runnable) {
        handler.post(runnable)
    }

    fun runUiThread(task: () -> Unit) {
        handler.post { task() }
    }
}