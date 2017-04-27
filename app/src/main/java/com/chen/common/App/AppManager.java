package com.chen.common.App;

import android.app.Activity;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * activity统一管理器
 *
 * @author dai.fengming
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {

    }

    /****
     * 构建单一实例
     *
     * @return
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加activity到堆栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {

        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /***
     * 获取当前的Activity（堆栈中最后压入的），可能为null，使用时注意判断是否为空
     *
     * @return
     */
    public Activity getCurrentActivity() {
        Activity activity = null;
        if (activityStack != null && activityStack.size() > 0) {
            try {
                activity = activityStack.lastElement();
            } catch (NoSuchElementException e) {
                activity = null;
            }
        }

        return activity;
    }

    /**
     * 结束当前的Activity（堆栈中最后压入的）
     */
    public void finishActivity() {
        Activity activity = null;
        if (activityStack != null && activityStack.size() > 0) {
            try {
                activity = activityStack.lastElement();
            } catch (NoSuchElementException e) {
                activity = null;
            }
        }

        finishActivity(activity);
    }

    /***
     * 结束指定的activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Activity removeActivity = null;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                removeActivity = activity;
                break;
            }
        }
        if (removeActivity != null) {
            finishActivity(removeActivity);
        }
    }

    /**
     * 得到指定的Activity
     *
     * @param cls
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束所有的
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
//    @SuppressWarnings("deprecation")
//    public void AppExit(Context context) {
//        try {
//            NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//            if(nm!=null){
//                //退出应用后删除下载的通知
//                nm.cancel(R.layout.notification_item);
//            }
//            finishAllActivity();
//            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            activityMgr.restartPackage(context.getPackageName());
//            System.exit(0);
//        } catch (Exception e) {
//        }
//    }
}
