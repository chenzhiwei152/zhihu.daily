package com.chen.common.Utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by chen.zhiwei on 2016/6/13 0013.
 */
public class LogUtils {
    static boolean debug = true;
    public static String customTagPrefix = "GoldenHome_";

    static {
        try {
//            SharedPreferences share = AppContext.getInstance().getSharedPreferences("LoginActivity", Activity.MODE_WORLD_READABLE);
//            String type = share.getString("ChangeAdress", "1");
//            if("1".equals(type)){
//                debug  = false;
//            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static int maxLogSize = 3000;

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":"
                + tag;
        return tag;
    }

    public static void d(String msg) {
        if(null == msg || "".equals(msg)) return;
        if (debug) {

            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);

            // 处理log 过长无法完整打印的问题
            for (int i = 0; i <= msg.length() / maxLogSize; i++) {

                int start = i * maxLogSize;

                int end = (i + 1) * maxLogSize;

                end = end > msg.length() ? msg.length() : end;

                Log.d(tag, msg.substring(start, end));

            }
        }
    }

    public static void d(String content, Throwable tr) {
        if(null == content || "".equals(content)) return;
        if (!debug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, content, tr);
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void i(String msg) {
        if(null == msg || "".equals(msg)) return;
        if (debug) {

            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);

            for (int i = 0; i <= msg.length() / maxLogSize; i++) {

                int start = i * maxLogSize;

                int end = (i + 1) * maxLogSize;

                end = end > msg.length() ? msg.length() : end;

                Log.i(tag, msg.substring(start, end));

            }
        }
    }

    public static void i(String content, Throwable tr) {
        if(null == content || "".equals(content)) return;
        if (!debug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.i(tag, content, tr);
    }
    public static void e(String msg) {
        if(null == msg || "".equals(msg)) return;
        if (debug) {

            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);

            for (int i = 0; i <= msg.length() / maxLogSize; i++) {

                int start = i * maxLogSize;

                int end = (i + 1) * maxLogSize;

                end = end > msg.length() ? msg.length() : end;

                Log.e(tag, msg.substring(start, end));

            }
        }
    }

    public static void e(String content, Throwable tr) {
        if(null == content || "".equals(content)) return;
        if (!debug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.e(tag, content, tr);
    }

    public static void e(String tag,String msg){
        if(!debug)return;
        Log.e(tag,msg);
    }
}
