package com.wxx.chat.message

import com.google.gson.annotations.Expose

class BaseMessage: ChatMessage {

    val sendSatrtTime = System.currentTimeMillis()
    var sendEndTime: Long? = null

    var content: String = ""

    override
    val chatType: ChatMessageType = ChatMessageType.MSG_TYPE_NORMAL
}