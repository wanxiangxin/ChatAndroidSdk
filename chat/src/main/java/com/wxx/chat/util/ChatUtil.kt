package com.wxx.chat.util

import android.annotation.SuppressLint
import android.provider.Settings
import com.wxx.chat.ChatSdk

object ChatUtil {
    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        return Settings.Secure.getString(ChatSdk.mContext.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
    }
}