package com.wxx.chat.socket

import com.wxx.chat.constant.ConfigConstant
import com.wxx.chat.listener.IPacketListener
import com.wxx.chat.message.BaseMessage
import com.wxx.chat.util.ChatHandler
import com.wxx.chat.util.ChatLauncher
import com.wxx.chat.util.ChatLogUtil
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.internal.ChannelUtils
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.AttributeKey
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class SocketClient {

    private val bootstrap by lazy { Bootstrap() }
    private val eventLoopGroup by lazy { NioEventLoopGroup() }
    private var channel: Channel? = null
    private var isConnecting = false

    init {
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel::class.java)
        bootstrap.handler(object : ChannelInitializer<Channel>() {
            override fun initChannel(ch: Channel?) {
                val pipeline = ch?.pipeline() ?: return
                pipeline.addLast(ChatEncoder())
                pipeline.addLast(StringEncoder(Charset.forName("GBK")))
                pipeline.addLast(IdleStateHandler(0,0,4 * 60, TimeUnit.SECONDS))
                pipeline.addLast(MessageHandler())
            }
        })
        bootstrap.option(ChannelOption.TCP_NODELAY, true)
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConfigConstant.CONNECT_TIMEOUT)
    }

    fun connect() {
        val channelFuture = bootstrap.connect(ConfigConstant.IP, ConfigConstant.PORT)
        isConnecting = true
        ChatLogUtil.log("链接服务器中...")
        channelFuture.addListener {
            isConnecting = false
            if (it.isSuccess) {
                ChatLogUtil.log("链接服务器成功")
                ChatLauncher.postConnectSuccess()
                channel = channelFuture.channel()
            } else {
                val errorMessage = it.cause().message ?: ""
                ChatLogUtil.log("链接服务器失败>>>$errorMessage")
                ChatLauncher.postConnectFail(errorMessage)
            }
        }
    }

    fun reConnect() {
        channel?.close()
        connect()
    }

    fun sendMessage(message: BaseMessage?, listener: IPacketListener?) {
        if (message == null) {
            listener?.onFail("要发送的数据为 null")
            return
        }

        if (isSocketEnable()) {
            channel?.writeAndFlush(message)?.addListener {
                if (it.isSuccess) {
                    ChatLogUtil.log("发送消息成功")
                    ChatHandler.runUiThread {
                        listener?.onSuccess()
                    }
                } else {
                    val errorMessage = it.cause().message
                    ChatLogUtil.log("发送消息失败>>>$errorMessage")
                    ChatHandler.runUiThread {
                        listener?.onFail(errorMessage ?: "未知错误")
                    }
                }
            }
        } else {
            listener?.onFail("isActive == false")
        }
    }

    fun isSocketEnable(): Boolean {
        return channel?.isActive == true
    }
}