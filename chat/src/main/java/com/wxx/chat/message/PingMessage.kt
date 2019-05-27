package com.wxx.chat.message

class PingMessage: ChatMessage {
    override var chatType: ChatMessageType = ChatMessageType.MSG_TYPE_PING
}