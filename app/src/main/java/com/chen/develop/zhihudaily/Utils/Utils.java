package com.chen.develop.zhihudaily.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.widget.Toast;

import com.chen.develop.zhihudaily.App.AppContext;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by chen.zhiwei on 2016/6/13.
 */
public class Utils {
    private static Toast toast;

    /**
     * 判断是否后台运行
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    //判断字符串是否为空
    public static boolean isEmpty(String dostr) {
        if (dostr == null || "".equals(dostr.trim()) || "null".equals(dostr) || dostr == " ") {
            return true;
        }
        return false;
    }


    /**
     * 判断sd卡是否存在
     *
     * @return
     */
    public static boolean getSDCardStatus() {

        String state = android.os.Environment.getExternalStorageState();
        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
                return true;
            }
        }
        return false;
    }

    //显示toast提示
    public static void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(AppContext.getInstance(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
    public static void showToast(int resID) {
        if (toast == null) {
            toast = Toast.makeText(AppContext.getInstance(), resID, Toast.LENGTH_SHORT);
        } else {
            toast.setText(resID);
        }
        toast.show();
    }

    //url参数格式化为/xxx/xxx/xxx
    public static String parameterFormat( final HashMap<String,String> parameters) {
        StringBuilder result = new StringBuilder();
        Iterator var4 = parameters.entrySet().iterator();

        while(var4.hasNext()) {
            NameValuePair parameter = (NameValuePair)var4.next();
            String encodedName = parameter.getName();
            String encodedValue = parameter.getValue();
            //result.append(encodedName);
            if(isEmpty(encodedValue)){
                encodedValue = "-1";
            }
            if(encodedValue != null) {
                result.append(encodedValue);
                result.append("/");
            }
        }

        return result.toString();
    }


    public static String getChannel(Context context) {
        String channel =null;

        SharedPreferences sharedPreferences = context.getSharedPreferences("UMSAPPCHANNEL",Context.MODE_PRIVATE);
        channel = sharedPreferences.getString("UMSAPPCHANNEL",null);
        if (channel != null) {
            return channel;
        }

        final String start_flag = "META-INF/jyall_jsjyw_channel_";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(start_flag)) {
                    channel = entryName.replace(start_flag, "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (channel == null || channel.length() <= 0) {
            channel = "22";//读不到渠道号就默认官方渠道
        }
        sharedPreferences.edit().putString("UMSAPPCHANNEL" ,channel).commit();
        return channel;
    }

    /**
     * @param d
     * @return
     * double为整数时不带小数点
     */
    public static String  doubleTrans(double d){
        if(Math.round(d)-d==0){
            return String.valueOf((int)d);
        }
        DecimalFormat df = new DecimalFormat("###.##");
        df.format(d);
        return  df.format(d);
    }
    /**
     * JS
     * 字符串解码
     * @param src
     * @return
     */
    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src
                            .substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src
                            .substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }
    /**
     * Will throw AssertionError, if expression is not true
     *
     * @param expression    result of your asserted condition
     * @param failedMessage message to be included in error log
     * @throws AssertionError
     */
    public static void asserts(final boolean expression, final String failedMessage) {
        if (!expression) {
            throw new AssertionError(failedMessage);
        }
    }

    /**
     * Will throw IllegalArgumentException if provided object is null on runtime
     *
     * @param argument object that should be asserted as not null
     * @param name     name of the object asserted
     * @throws IllegalArgumentException
     */
    public static <T> T notNull(final T argument, final String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " should not be null!");
        }
        return argument;
    }
}
