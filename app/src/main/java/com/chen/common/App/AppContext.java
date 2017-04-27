package com.chen.common.App;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.chen.common.Utils.PreferencesUtils;
import com.chen.develop.zhihudaily.R;

/**
 * Created by chen.zhiwei on 2016/6/13 0013.
 */
public class AppContext extends Application {
    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        //本地存储
        PreferencesUtils.initPrefs(this);
//        CacheDbHelper.getHelper(this);
    }
    /**
     * @return 获取实例
     */
    public static AppContext getInstance() {
        return instance;
    }
    /**
     * 页面跳转
     *
     * @param context
     * @param activity
     * @param bundle
     */
    public void intentJump(Activity context, Class<?> activity, Bundle bundle) {
        intentJump(context, activity, bundle, -1);
    }

    public void intentJump(Activity context, Class<?> activity, Bundle bundle, int flag) {
        if (context == null || activity == null || bundle == null)
            return;
        Intent intent = new Intent(context, activity);
        if (flag != -1){
            intent.addFlags(flag);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
        setGoActivityAnim(context);
    }

    /**
     * 页面跳转
     *
     * @param context
     * @param activity
     */
    public void intentJump(Activity context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        setGoActivityAnim(context);
    }
    /**
     * @param context
     * @throws
     * @Description: 设置进入下一个界面的动画
     */
    public static void setGoActivityAnim(Activity context) {
        context.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    /**
     * 返回上一个界面的动画
     */
    public static void setBackActivityAnim(Activity context) {
        context.overridePendingTransition(R.anim.slide_out_to_left_from_right, R.anim.slide_in_from_right);
    }
}
