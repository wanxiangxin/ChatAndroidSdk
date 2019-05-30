# ChatAndroidSdk

#### 介绍
**基于Netty的android端推送sdk**</br>
使用`Kotlin`编写，主模块只需要引用chat模块,可以搭配[服务器代码](https://gitee.com/wanxx/ChatServer.git)

#### 软件架构
基于Netty的推送sdk,包含心跳、断线重连，在每次网络发生变化或者屏幕变亮（Android6.0 之后的屏幕锁机制）会触发是否要重新连接，
socket连接服务器成功之后会自动发送一个登录的协议让服务器将设备id跟channel绑定，也就是按设备推送，
当然你也可以自己修改绑定属性（通过用户id绑定channel、通过自定义tag绑定channel），可随意根据自己的需求扩展，后台通过一个map保存设备的id跟channel。

#### 数据协议
为了方便效果展示，目前使用json的数据格式，如果你想使用protobuf数据协议，那只需要在ChatEncoder里面自己对数据包的处理

#### 使用说明

### 1. 添加依赖：

chat模块使用gson跟netty，如果你的主模块也使用了这两个库，记得版本号使用一致，你的主模块只需在 **dependencies** 里添加 
```
implementation project(path: ':chat')
```

，因为netty的原因，必须要在主模块中加入

```
packagingOptions {
        //屏蔽jar中无用文件
        exclude 'META-INF/INDEX.LIST'
        exclude 'META-INF/io.netty.versions.properties'
}
```

   
![输入图片说明](https://images.gitee.com/uploads/images/2019/0528/105510_702f02d3_938591.png "企业微信截图_15590120799106.png")



### 2. 初始化

在Application中
```
ChatSdk.init(this)
```

 可以使用 TAG=wxx_chat 打印出相关链接信息
![输入图片说明](https://images.gitee.com/uploads/images/2019/0528/151210_e333a4a1_938591.png "企业微信截图_15590274803263.png")

-  **监听服务器连接状态** 
```
ChatSdk.setConnectListener(object : IConnectListener {
    override fun onConnectSuccess() {
        // 连接服务器成功
    }

    override fun onConnectError(message: String) {
        // 与服务器断开连接
    }

})
```

-  **监听收到服务器消息** 
```
ChatSdk.setReceiveMessageListener(object : IReceiveMessageListener {
    override fun onReceiveMessage(message: BaseMessage) {
        // 收到服务器推送的消息了，已经切换在了主线程，自己可以展示通知栏等
    }
})
```

-  **发送消息** 
```
ChatSdk.sendMessage(message, object : IPacketListener() {
    override fun onSuccess() {
        // 发送消息成功
    }

    override fun onFail(message: String) {
        // 发送消息失败，messxinxie为错误信息
    }
})
                    
```