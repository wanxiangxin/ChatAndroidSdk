package com.app.chat

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.wxx.chat.ChatSdk

class MyApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        ChatSdk.init(this)
    }
}