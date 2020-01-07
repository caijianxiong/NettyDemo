package com.azhon.netty.client;

import android.util.Log;

import com.azhon.netty.bean.PkgDataBean;

import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 项目名:    Netty-Android
 * 包名       com.azhon.netty.client
 * 文件名:    ClientDecoder
 * 创建时间:  2019-09-06 on 00:13
 * 描述:     TODO 解码器，对服务端端的数据进行解析
 *
 * @author 阿钟
 */

public class ClientDecoder extends ByteToMessageDecoder {
    private static final String TAG = "ClientDecoder";

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readableBytes();
        //收到的数据包
        byte[] data = byteBuf.readBytes(length).array();
        //判断数据包是不是一个正确的数据包
        if (data[0] == 0x2A && data[0] == data[data.length - 1]) {
            PkgDataBean bean = new PkgDataBean();
            bean.setCmd(data[1]);
            bean.setDataLength(data[2]);
            byte[] bytes = Arrays.copyOfRange(data, 3, 3 + bean.getDataLength());
            bean.setData(new String(bytes));
            //将数据传递给下一个Handler，也就是在NettyServer给ChannelPipeline添加的处理器
            list.add(bean);
        } else {
            Log.e(TAG, "客户端数据解析失败");
        }
    }
}
