package com.chen.develop.zhihudaily.App;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chen.develop.zhihudaily.Utils.LogUtils;

import butterknife.ButterKnife;

/**
 * Created by chen.zhiwei on 2016/6/13 0013.
 */
public abstract class BaseActivity extends AppCompatActivity{
    public Activity mContext;//上下文
    public static String TAG = null;
    public static Bundle savedInstanceState;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        TAG = this.getClass().getSimpleName();
        if (getContentViewLayoutId() != 0) {
            LogUtils.e(TAG,"getContentViewLayoutId():"+getContentViewLayoutId());
            setContentView(getContentViewLayoutId());
        } else {
            LogUtils.e(TAG,"getContentViewLayoutId():"+getContentViewLayoutId());
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        this.savedInstanceState=savedInstanceState;
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
    protected void onDestroy() {
        super.onDestroy();
        //解除注解
        ButterKnife.unbind(this);
    }
}
