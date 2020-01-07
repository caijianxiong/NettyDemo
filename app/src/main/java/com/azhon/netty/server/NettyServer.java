package com.azhon.netty.server;


import com.azhon.netty.Config;
import com.azhon.netty.bean.PkgDataBean;
import com.azhon.netty.util.ByteUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyServer {
    private static final String TAG = "NettyServer";
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public static void main(String[] a) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.startServer();
    }

    /**
     * 启动tcp服务端
     */
    public void startServer() {
        try {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //分隔符
                        ByteBuf delimiter = Unpooled.copiedBuffer("$".getBytes());

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //解决粘包
                            pipeline.addLast(new DelimiterBasedFrameDecoder(65535, delimiter));
                            //添加发送数据编码器
                            pipeline.addLast(new ServerEncoder());
                            //添加解码器，对收到的数据进行解码
                            pipeline.addLast(new ServerDecoder());
                            //添加数据处理
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            //服务器启动辅助类配置完成后，调用 bind 方法绑定监听端口，调用 sync 方法同步等待绑定操作完成
            ChannelFuture f = b.bind(Config.PORT).sync();
//            handler.obtainMessage(0, "TCP 服务启动成功 PORT = " + Config.PORT).sendToTarget();
            System.out.println("TCP 服务启动成功 PORT = " + Config.PORT);
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
