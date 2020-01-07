package com.azhon.netty.client;

import com.azhon.netty.bean.PkgDataBean;
import com.azhon.netty.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 项目名:    Netty-Android
 * 包名       com.azhon.netty.client
 * 文件名:    ClientEncoder
 * 创建时间:  2019-09-07 on 23:28
 * 描述:     TODO 发送出去的数据编码器
 *
 * @author 阿钟
 */

public class ClientEncoder extends MessageToByteEncoder<PkgDataBean> {

    private static final String TAG = "ClientEncoder";

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, PkgDataBean data, ByteBuf byteBuf) throws Exception {
        //根据数据包协议，生成byte数组
        byte[] bytes = {0x2A, data.getCmd(), data.getDataLength()};
        byte[] dataBytes = data.getData().getBytes();
        //分隔符
        byte[] delimiter = "$".getBytes();
        //将所有数据合并成一个byte数组
        byte[] all = ByteUtil.byteMergerAll(bytes, dataBytes, new byte[]{0x2A}, delimiter);

        //发送数据
        byteBuf.writeBytes(all);
    }
}