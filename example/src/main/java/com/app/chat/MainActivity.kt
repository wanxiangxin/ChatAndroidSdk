package com.app.chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.wxx.chat.ChatSdk
import com.wxx.chat.listener.IConnectListener
import com.wxx.chat.listener.IPacketListener
import com.wxx.chat.listener.IReceiveMessageListener
import com.wxx.chat.message.BaseMessage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ChatSdk.setConnectListener(object : IConnectListener {
            override fun onConnectSuccess() {
            }

            override fun onConnectError(message: String) {
            }

        })

        ChatSdk.setReceiveMessageListener(object : IReceiveMessageListener {
            override fun onReceiveMessage(message: BaseMessage) {

            }
        })
        btn_connect.setOnClickListener {
            ChatSdk.connect()
        }

        btn_send.setOnClickListener {
            val message = et_content.text.toString()
            if (message.isEmpty()) return@setOnClickListener
            ChatSdk.sendMessage(message, object : IPacketListener() {
                override fun onSuccess() {
                    Toast.makeText(this@MainActivity, "发送成功", Toast.LENGTH_SHORT).show()
                }

                override fun onFail(message: String) {
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}
