package com.chen.develop.zhihudaily.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chen.common.App.AppContext;
import com.chen.common.App.AppManager;
import com.chen.common.DB.AllCache;
import com.chen.develop.zhihudaily.Activity.CityListActivity;
import com.chen.develop.zhihudaily.R;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.security.SecureRandom;

/**
 * Created by chen.zhiwei on 2016-7-5.
 */
public class CityListWithHeadersAdapter extends CityListAdapter<RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,  int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(getItem(position).getCityZh());
        final int po=position;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCache.get(AppContext.getInstance()).put("city",getItem(po).getCityZh());
                AllCache.get(AppContext.getInstance()).put("id",getItem(po).getId());
                AppManager.getAppManager().finishActivity(CityListActivity.instance);
            }
        });
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getFirstLetter().charAt(0);
    }

    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(String.valueOf(getItem(position).getFirstLetter()));
        holder.itemView.setBackgroundColor(getRandomColor());
    }

    private int getRandomColor() {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(150, new float[]{
                rgen.nextInt(359), 1, 1
        });
    }

}