package com.azhon.netty.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;


/**
 * Created by SongJie on 11/15 0015.
 */
public class logUtil {
    private static Object mLockFile = new Object();
    private static String folderPath ;
    private static String logFileName;
    private static boolean isWeiteFile = false;
    /**
     * 等级越高打印越多
     * 1级打印提示信息，正式版本
     * 2级打印错误信息，测试版本
     * 3级打印调试信息，程序员调试版本
     */
    private static int debugLevel = 3;

    public static void initLog() {

    }

    public static void setBugLevel(int level){
        debugLevel = level;
        if (level>1){
            isWeiteFile=true;
        }
    }
    public static void d_1(String TAG,String msg){
        String str = "❶cjx❶❶❶❶❶"+msg;
        if(debugLevel>0){
            Log.i(TAG,str);
        }
    }

    public static void d_2(String TAG,String msg){
        String str = "❷cjx❷❷❷❷❷"+msg;
        if(debugLevel>1){
            Log.i(TAG,str);
        }
    }

    public static void d_3(String TAG,String msg){
        String str = "❸cjx❸❸❸❸❸"+msg;
        if(debugLevel>2){
            Log.i(TAG,str);
        }
    }

    public static void d_e(String TAG,Throwable ex){
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String str_error = "@cjx@@@@@@@@"+"name = "+ex.getClass().getName()+"\n message = "+writer.toString();
        Log.e(TAG,str_error);
    }


}
