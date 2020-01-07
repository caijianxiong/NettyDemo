package com.azhon.netty.client;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

/**
 * 项目名:    Netty-Android
 * 包名       com.azhon.netty.client
 * 文件名:    ConnectListener
 * 创建时间:  2019-09-19 on 00:06
 * 描述:     TODO
 *
 * @author 阿钟
 */

public class ConnectListener implements ChannelFutureListener {

    private static final String TAG = "ConnectListener";
    private NettyClient nettyClient;

    public ConnectListener(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        //连接失败发起重连
        if (!channelFuture.isSuccess()) {
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "连接失败，发起重连");
                    nettyClient.connect();
                }
            }, 5, TimeUnit.SECONDS);
        }
    }

}
