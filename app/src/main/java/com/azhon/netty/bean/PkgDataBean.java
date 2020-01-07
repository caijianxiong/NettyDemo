package com.azhon.netty.bean;

/**
 * 项目名:    Netty-Android
 * 包名       com.azhon.netty.bean
 * 文件名:    PkgDataBean
 * 创建时间:  2019-09-14 on 18:13
 * 描述:     TODO
 *
 * @author 阿钟
 */

public class PkgDataBean {
    //命令字
    private byte cmd;
    //数据长度
    private byte dataLength;
    //数据
    private String data;

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public byte getDataLength() {
        return dataLength;
    }

    public void setDataLength(byte dataLength) {
        this.dataLength = dataLength;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PkgDataBean{" +
                "cmd=" + cmd +
                ", dataLength=" + dataLength +
                ", data='" + data + '\'' +
                '}';
    }
}
