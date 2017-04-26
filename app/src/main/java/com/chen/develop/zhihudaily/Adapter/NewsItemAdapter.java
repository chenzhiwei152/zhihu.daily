package com.chen.develop.zhihudaily.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chen.common.Utils.ImageLoaderManager;
import com.chen.develop.zhihudaily.Activity.MainActivity;
import com.chen.develop.zhihudaily.Activity.NewsContentActivity;
import com.chen.common.App.AppContext;
import com.chen.develop.zhihudaily.Bean.StoriesBean;
import com.chen.develop.zhihudaily.Bean.StoriesEntity;
import com.chen.develop.zhihudaily.R;
import com.chen.common.Utils.Constant;
import com.chen.common.Utils.PreUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by wwjun.wang on 2015/8/14.
 */
public class NewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<StoriesEntity> entities;
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;

    public NewsItemAdapter(Context context, List<StoriesEntity> items) {
        this.context = context;
        entities = items;
        isLight = ((MainActivity) context).isLight();
        isLight = ((MainActivity) context).isLight();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<StoriesEntity> items) {
        this.entities.addAll(items);
        notifyDataSetChanged();
    }
    public static List<StoriesEntity> getEntities() {
        return entities;
    }
    public NewsItemAdapter(Context context) {
        this.context = context;
        this.entities = new ArrayList<>();
        isLight = ((MainActivity) context).isLight();
        isLight = ((MainActivity) context).isLight();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void ClearData() {
        if (entities != null) {

            entities.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        String readSeq = PreUtils.getStringFromDefault(context, "read", "");
        if (readSeq.contains(entities.get(position).getId() + "")) {
            ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(R.color.clicked_tv_textcolor));
        } else {
            ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(isLight ? android.R.color.black : android.R.color.white));
        }

        ((ImageViewHolder) viewHolder).ll_item_news.setBackgroundColor(context.getResources().getColor(isLight ? R.color.light_news_item : R.color.dark_news_item));
        ((ImageViewHolder) viewHolder).fl_news_item.setBackgroundResource(isLight ? R.drawable.item_background_selector_light : R.drawable.item_background_selector_dark);
        StoriesEntity entity = entities.get(position);
        ((ImageViewHolder) viewHolder).tv_title.setText(entity.getTitle());
        if (entity.getImages() != null) {
            ((ImageViewHolder) viewHolder).iv_title.setVisibility(View.VISIBLE);
            ImageLoaderManager.getInstance(context).display(context,entity.getImages().get(0), ((ImageViewHolder) viewHolder).iv_title);
        } else {
            ((ImageViewHolder) viewHolder).iv_title.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }


    public void updateTheme() {
        isLight = ((MainActivity) context).isLight();
        notifyDataSetChanged();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.iv_title)
        ImageView iv_title;
        @Bind(R.id.ll_item_news)
        LinearLayout ll_item_news;
        @Bind(R.id.fl_news_item)
        FrameLayout fl_news_item;

//        TextView tv_title;
//        ImageView iv_title;

        ImageViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ImageViewHolder", "onClick--> position = " + getPosition());
                    int[] startingLocation = new int[2];
                    v.getLocationOnScreen(startingLocation);
                    startingLocation[0] += v.getWidth() / 2;
                    StoriesBean storiesEntity = new StoriesBean();
                    storiesEntity.setId(NewsItemAdapter.getEntities().get(getPosition()).getId());
                    storiesEntity.setTitle(NewsItemAdapter.getEntities().get(getPosition()).getTitle());
                    Bundle bundle = new Bundle();
                    bundle.putIntArray(Constant.START_LOCATION, startingLocation);
                    bundle.putSerializable("entity", storiesEntity);
                    bundle.putString("from","theme");
                    bundle.putBoolean("isLight", ((MainActivity) context).isLight());
                    AppContext.getInstance().intentJump((Activity) NewsItemAdapter.context, NewsContentActivity.class, bundle);
                }
            });
        }
    }
}
