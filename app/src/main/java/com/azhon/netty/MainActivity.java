package com.azhon.netty;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.azhon.netty.base.BaseActivity;
import com.azhon.netty.bean.PkgDataBean;
import com.azhon.netty.client.NettyClient;
import com.azhon.netty.server.NettyServer;
import com.azhon.netty.server.ServerHandler;

import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private TextView tvLog;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Netty在Android中的应用");
        initView();
    }

    private void initView() {
        tvLog = findViewById(R.id.tv_log);
        etContent = findViewById(R.id.et_content);
        findViewById(R.id.btn_connect).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_pkg).setOnClickListener(this);
    }

    @Override
    protected void message(int what, Object obj) {
        tvLog.append(obj.toString() + "\n");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pkg:
                List<ChannelHandlerContext> channels = ServerHandler.channels;
                for (ChannelHandlerContext ctx : channels) {
                    for (int i = 0; i < 3; i++) {
                        PkgDataBean bean = new PkgDataBean();
                        bean.setCmd((byte) 0x05);
                        bean.setData("粘包的数据：" + i);
                        bean.setDataLength((byte) bean.getData().getBytes().length);
                        ctx.channel().writeAndFlush(bean);
                    }
                }
                Log.d(TAG, "服务端发送了粘包数据");
                break;
            case R.id.btn_connect:
                NettyClient client = NettyClient.getInstance();
                client.setHandler(handler);
                break;
            case R.id.btn_send:
                Channel channel = NettyClient.getChannel();
                if (channel == null) {
                    Toast.makeText(this, "请先连接TCP服", Toast.LENGTH_SHORT).show();
                } else {
                    PkgDataBean bean = new PkgDataBean();
                    bean.setCmd((byte) 0x01);
                    bean.setData(etContent.getText().toString());
                    bean.setDataLength((byte) bean.getData().getBytes().length);
                    channel.writeAndFlush(bean);
                }
                break;
            default:
                break;
        }
    }
}
