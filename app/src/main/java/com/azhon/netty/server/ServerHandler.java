package com.azhon.netty.server;

import android.util.Log;

import com.azhon.netty.bean.PkgDataBean;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 项目名:    Netty-Android
 * 包名       com.azhon.netty.server
 * 文件名:    ServerHandler
 * 创建时间:  2019-09-06 on 00:13
 * 描述:     TODO 数据处理器
 *
 * @author 阿钟
 */

public class ServerHandler extends SimpleChannelInboundHandler<PkgDataBean> {

    private static final String TAG = "ServerHandler";
    public static List<ChannelHandlerContext> channels = new ArrayList<>();

    /**
     * 当收到数据的回调
     *
     * @param ctx  封装的连接对像
     * @param bean
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PkgDataBean bean) throws Exception {
        switch (bean.getCmd()) {
            case 0x02:
                //响应客户端心跳
                bean.setCmd((byte) 0x03);
                ctx.channel().writeAndFlush(bean);
                break;
            case 0x01://客户端发来消息---回给发送给所有的客户端
                for (ChannelHandlerContext ctx01 : channels) {
                    ctx01.channel().writeAndFlush(bean);
                }
                System.out.println("服务端发送了粘包数据");
                break;
            default:

                break;
        }
//        System.out.println("收到了解码器处理过的数据：" + bean.toString());

    }

    /**
     * 有客户端连接过来的回调
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println(ctx.channel().id().asShortText());
        channels.add(ctx);
        System.out.println("有客户端连接过来：" + ctx.toString());
    }

    /**
     * 有客户端断开了连接的回调
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        channels.remove(ctx);
        System.out.println("有客户端断开了连接：" + ctx.toString());
    }

}