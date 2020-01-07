package com.azhon.netty.client;

import android.os.Handler;
import android.util.Log;

import com.azhon.netty.Config;
import com.azhon.netty.util.logUtil;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 项目名:    Netty-Android
 * 包名       com.azhon.netty.client
 * 文件名:    NettyClient
 * 创建时间:  2019-09-07 on 22:45
 * 描述:     TODO
 *
 * @author 阿钟
 */

public class NettyClient {

    private static final String TAG = "NettyClient";
    private static NettyClient nettyClient;


    private Channel channel;
    private Handler handler;

    public static NettyClient getInstance() {
        if (nettyClient == null) {
            nettyClient = new NettyClient();
        }
        return nettyClient;
    }

    /**
     * 需要在子线程中发起连接
     */
    private NettyClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }).start();
    }

    /**
     * 获取与服务端的连接
     */
    public static Channel getChannel() {
        if (nettyClient == null) {
            return null;
        }
        return nettyClient.channel;
    }

    /**
     * 连接服务端
     */
    public void connect() {
        try {
            NioEventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap()
                    // 指定channel类型
                    .channel(NioSocketChannel.class)
                    // 指定EventLoopGroup
                    .group(group)
                    // 指定Handler
                    .handler(new ChannelInitializer<SocketChannel>() {
                        //分隔符
                        ByteBuf delimiter = Unpooled.copiedBuffer("$".getBytes());

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new IdleStateHandler(10, 0, 0));
                            //解决粘包
                            pipeline.addLast(new DelimiterBasedFrameDecoder(65535, delimiter));
                            //添加发送数据编码器
                            pipeline.addLast(new ClientEncoder());
                            //添加收到的数据解码器
                            pipeline.addLast(new ClientDecoder());
                            //添加数据处理器
                            pipeline.addLast(new ClientHandler(NettyClient.this,handler));
                        }
                    });
            // 连接到服务端
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(Config.IP, Config.PORT));
            // 添加连接状态监听
            channelFuture.addListener(new ConnectListener(this));
            //获取连接通道
            channel = channelFuture.sync().channel();
            handler.obtainMessage(0, "连接成功").sendToTarget();
        } catch (Exception e) {
            handler.obtainMessage(0, "连接失败").sendToTarget();
            logUtil.d_2(TAG, "连接失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
