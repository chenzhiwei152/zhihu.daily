package com.chen.develop.zhihudaily.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


/**
 * Created by chenzhiwei on 2016/6/16.
 */
public class HttpUtils {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, ResponseHandlerInterface responseHandler) {
        client.get(Constant.BASEURL + url, responseHandler);
        Log.d("sb","url:"+Constant.BASEURL + url);
    }
    public static void get(String url,boolean all, ResponseHandlerInterface responseHandler) {
        client.get(url, responseHandler);
        Log.d("sb","url:" + url);
    }
    public static void getImage(String url, ResponseHandlerInterface responseHandler) {
        client.get(url, responseHandler);
        Log.d("sb","ImageUrl:"+url);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
