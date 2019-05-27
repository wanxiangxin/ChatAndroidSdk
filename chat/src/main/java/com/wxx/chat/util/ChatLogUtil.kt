package com.wxx.chat.util

import android.util.Log

object ChatLogUtil {
    private const val TAG = "wxx_chat"

    fun log(message: Any?) {
        Log.i(TAG, message?.toString())
    }
}