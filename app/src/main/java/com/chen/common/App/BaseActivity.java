package com.chen.common.App;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chen.common.Utils.LogUtils;
import com.mcxiaoke.packer.helper.PackerNg;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by chen.zhiwei on 2016/6/13 0013.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Activity mContext;//上下文
    public static String TAG = null;
    public static Bundle savedInstanceState;
    private String mPageName = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mPageName = getAppPackageName(mContext);
        TAG = this.getClass().getSimpleName();
        if (getContentViewLayoutId() != 0) {
            LogUtils.e(TAG, "getContentViewLayoutId():" + getContentViewLayoutId());
            setContentView(getContentViewLayoutId());
        } else {
            LogUtils.e(TAG, "getContentViewLayoutId():" + getContentViewLayoutId());
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
         MobclickAgent.setSessionContinueMillis(3000);
        final String market = PackerNg.getMarket(mContext);
// 或者使用 PackerNg.getMarket(Context,defaultValue)
// 之后就可以使用了，比如友盟可以这样设置
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(mContext, "581adedda40fa35143000b8c", market));
        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
        this.savedInstanceState = savedInstanceState;
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        //添加注解
        ButterKnife.bind(this);
        initViewsAndEvents();
        loadData();
    }

    /***
     * 绑定资源文件ID
     *
     * @return
     */
    protected abstract int getContentViewLayoutId();

    /***
     * 绑定视图组件
     */
    protected abstract void initViewsAndEvents();


    /***
     * 加载数据
     */
    protected abstract void loadData();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注解
        ButterKnife.unbind(this);
    }


    // /对于好多应用，会在程序中杀死 进程，这样会导致我们统计不到此时Activity结束的信息，
    // /对于这种情况需要调用 'MobclickAgent.onKillProcess( Context )'
    // /方法，保存一些页面调用的数据。正常的应用是不需要调用此方法的。
    private void Hook() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setPositiveButton("退出应用", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MobclickAgent.onKillProcess(mContext);

                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });
        builder.setNeutralButton("后退一下", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        builder.setNegativeButton("点错了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        builder.show();
    }

    public static String getAppPackageName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        Log.d("lixx", "当前应用:" + componentInfo.getPackageName());
        return componentInfo.getPackageName();
    }
}
