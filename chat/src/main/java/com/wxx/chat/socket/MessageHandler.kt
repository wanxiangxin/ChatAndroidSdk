package com.wxx.chat.socket

import com.wxx.chat.message.BindMessage
import com.wxx.chat.message.PingMessage
import com.wxx.chat.util.ChatHandler
import com.wxx.chat.util.ChatLauncher
import com.wxx.chat.util.ChatLogUtil
import com.wxx.chat.util.ChatUtil
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.IdleStateEvent

class MessageHandler: SimpleChannelInboundHandler<String>() {
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: String?) {
        decode(msg)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        super.exceptionCaught(ctx, cause)
        // 手机网络断开会触发到这里
        ctx?.close()
        val errorMessage = cause?.message ?: ""
        ChatLogUtil.log("断开链接>>>$errorMessage")
        ctx?.channel()?.eventLoop()?.shutdownGracefully()
        ChatLauncher.postConnectFail(errorMessage)
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
        ChatLogUtil.log("断开链接")
        ctx?.channel()?.close()
        ctx?.channel()?.eventLoop()?.shutdownGracefully()
        ChatLauncher.postConnectFail("未知错误")
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        super.channelActive(ctx)
        ctx?.channel()?.writeAndFlush(BindMessage(ChatUtil.getDeviceId()))
        ChatLauncher.postConnectSuccess()
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        super.userEventTriggered(ctx, evt)
        if (evt is IdleStateEvent) {
            ChatLogUtil.log("发送心跳>>>")
            ctx?.channel()?.writeAndFlush(PingMessage())
        }
    }

    private fun decode(dataBuf: String?) {
        ChatLogUtil.log("收到消息>>>$dataBuf")
        ChatHandler.runUiThread{
            ChatLauncher.postMessageReceive(dataBuf ?: "")
        }
    }
}