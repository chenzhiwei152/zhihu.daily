package com.lhh.ptrrv.library.impl;

import android.support.v7.widget.RecyclerView;

/**
 * Created by chen.zhiwei on 2016/6/16 0016.
 */
public interface OnScrollListener {
    void onScrollStateChanged(RecyclerView recyclerView, int newState);

    void onScrolled(RecyclerView recyclerView, int dx, int dy);

    //old-method, like listview 's onScroll ,but it's no use ,right ? by linhonghong 2015.10.29
    void onScroll(RecyclerView recyclerView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}
