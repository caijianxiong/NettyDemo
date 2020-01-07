package com.azhon.netty.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 项目名:    Netty-Android
 * 包名       com.azhon.netty.base
 * 文件名:    BaseActivity
 * 创建时间:  2019-09-06 on 00:08
 * 描述:     TODO
 *
 * @author 阿钟
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            message(msg.what, msg.obj);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    protected abstract void message(int what, Object obj);
}
