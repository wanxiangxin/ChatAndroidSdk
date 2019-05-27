package com.wxx.chat.socket

import com.google.gson.Gson
import com.wxx.chat.message.ChatMessage
import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import java.nio.CharBuffer
import java.nio.charset.Charset

class ChatEncoder: MessageToMessageEncoder<ChatMessage>() {
    private val gson by lazy { Gson() }
    override fun encode(ctx: ChannelHandlerContext?, msg: ChatMessage?, out: MutableList<Any>?) {
        val content = gson.toJson(msg)

        out?.add(ByteBufUtil.encodeString(ctx?.alloc(), CharBuffer.wrap(content), Charset.forName("GBK")))
    }
}