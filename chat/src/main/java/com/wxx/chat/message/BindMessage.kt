package com.wxx.chat.message

class BindMessage(val clientId: String): ChatMessage {
    override var chatType: ChatMessageType = ChatMessageType.MSG_TYPE_BIND
}