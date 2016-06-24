package com.chen.develop.zhihudaily.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by chen.zhiwei on 2016/6/13 0013.
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        mActivity = getActivity();
        if (view == null) {
            view = inflater.inflate(getContentViewLayoutId(), null);
            ButterKnife.bind(this, view);
            initViewsAndEvents();
            loadData();
            return view;

        } else {
            Object obj = view.getParent();
            if (obj != null && obj instanceof ViewGroup) {
                ((ViewGroup) obj).removeView(view);
            }
            return view;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            ButterKnife.unbind(this);
        } catch (Exception e) {

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
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
}
