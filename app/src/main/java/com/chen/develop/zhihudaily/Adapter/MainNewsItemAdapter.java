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

import com.chen.common.App.AppContext;
import com.chen.common.Utils.Constant;
import com.chen.common.Utils.ImageLoaderManager;
import com.chen.common.Utils.LogUtils;
import com.chen.common.Utils.PreUtils;
import com.chen.common.View.RippleView;
import com.chen.develop.zhihudaily.Activity.LatestContentActivity;
import com.chen.develop.zhihudaily.Activity.MainActivity;
import com.chen.develop.zhihudaily.Bean.StoriesBean;
import com.chen.develop.zhihudaily.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class MainNewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<StoriesBean> entities;
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;

    public MainNewsItemAdapter(Context context) {
        this.context = context;
        this.entities = new ArrayList<>();
        isLight = ((MainActivity) context).isLight();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public MainNewsItemAdapter(Context context, List<StoriesBean> items) {
        this.context = context;
        this.entities = new ArrayList<>();
        this.entities.addAll(items);
        isLight = ((MainActivity) context).isLight();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<StoriesBean> items) {
        this.entities.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        entities.clear();
        notifyDataSetChanged();
    }

    public static List<StoriesBean> getEntities() {
        return entities;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtils.e("onCreateViewHolder");
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.main_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        LogUtils.e("onBindViewHolder");
        String readSeq = PreUtils.getStringFromDefault(context, "read", "");
        if (readSeq.contains(entities.get(position).getId() + "")) {

            ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(R.color.clicked_tv_textcolor));
        } else {
            ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(isLight ? android.R.color.black : android.R.color.white));
        }
        ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(isLight ? android.R.color.black : android.R.color.white));
        ((ImageViewHolder) viewHolder).ll_item_list.setBackgroundColor(context.getResources().getColor(isLight ? R.color.light_news_item : R.color.dark_news_item));
        ((ImageViewHolder) viewHolder).fl_latest_item.setBackgroundResource(isLight ? R.drawable.item_background_selector_light : R.drawable.item_background_selector_dark);
        ((ImageViewHolder) viewHolder).tv_topic.setTextColor(context.getResources().getColor(isLight ? R.color.light_news_topic : R.color.dark_news_topic));
        StoriesBean entity = entities.get(position);
        if (entity.getType() == Constant.TOPIC) {
//            ((FrameLayout) ((ImageViewHolder) viewHolder).tv_topic.getParent()).setBackgroundColor(Color.TRANSPARENT);
            ((ImageViewHolder) viewHolder).tv_title.setVisibility(View.GONE);
            ((ImageViewHolder) viewHolder).iv_title.setVisibility(View.GONE);
            ((ImageViewHolder) viewHolder).tv_topic.setVisibility(View.VISIBLE);
            ((ImageViewHolder) viewHolder).rv_ripper.setVisibility(View.GONE);
            ((ImageViewHolder) viewHolder).tv_topic.setText(entity.getTitle());
        } else {
//            ((FrameLayout) ((ImageViewHolder) viewHolder).tv_topic.getParent()).setBackgroundResource(isLight ? R.drawable.item_background_selector_light : R.drawable.item_background_selector_dark);
            ((ImageViewHolder) viewHolder).tv_topic.setVisibility(View.GONE);
            ((ImageViewHolder) viewHolder).rv_ripper.setVisibility(View.VISIBLE);
            ((ImageViewHolder) viewHolder).rv_ripper.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    Log.d("ImageViewHolder", "onClick--> position = " +position);
                    int[] startingLocation = new int[2];
                    rippleView.getLocationOnScreen(startingLocation);
                    startingLocation[0] += rippleView.getWidth() / 2;
                    StoriesBean storiesEntity = new StoriesBean();
                    storiesEntity.setId(MainNewsItemAdapter.getEntities().get(position).getId());
                    storiesEntity.setTitle(MainNewsItemAdapter.getEntities().get(position).getTitle());
                    Bundle bundle = new Bundle();
                    bundle.putIntArray(Constant.START_LOCATION, startingLocation);
                    bundle.putSerializable("entity",storiesEntity);
                    bundle.putBoolean("isLight", ((MainActivity) context).isLight());
                    LogUtils.e("isLight:"+((MainActivity) context).isLight());
                    AppContext.getInstance().intentJump((Activity) MainNewsItemAdapter.context,LatestContentActivity.class,bundle);
                }
            });
            ((ImageViewHolder) viewHolder).tv_title.setVisibility(View.VISIBLE);
            ((ImageViewHolder) viewHolder).iv_title.setVisibility(View.VISIBLE);
            ((ImageViewHolder) viewHolder).tv_title.setText(entity.getTitle());
            ImageLoaderManager.getInstance(context).display(context,entity.getImages().get(0), ((ImageViewHolder) viewHolder).iv_title);
        }
    }

    @Override
    public int getItemCount() {
        try {
        } catch (Exception e) {

        }

        return entities == null ? 0 : entities.size();
    }


    public void updateTheme() {
        isLight = ((MainActivity) context).isLight();
        notifyDataSetChanged();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_topic)
        TextView tv_topic;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.iv_title)
        ImageView iv_title;
        @Bind(R.id.rv_ripper)
        RippleView rv_ripper;
        @Bind(R.id.ll_item_list)
                LinearLayout ll_item_list;
        @Bind(R.id.fl_latest_item)
        FrameLayout fl_latest_item;

        ImageViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int[] startingLocation = new int[2];
//                    v.getLocationOnScreen(startingLocation);
//                    startingLocation[0] += v.getWidth() / 2;
//                    StoriesBean storiesEntity = new StoriesBean();
//                    storiesEntity.setId(MainNewsItemAdapter.getEntities().get(getPosition()).getId());
//                    storiesEntity.setTitle(MainNewsItemAdapter.getEntities().get(getPosition()).getTitle());
//                    Bundle bundle = new Bundle();
//                    bundle.putIntArray(Constant.START_LOCATION, startingLocation);
//                    bundle.putSerializable("entity",storiesEntity);
//                    bundle.putBoolean("isLight", ((MainActivity) context).isLight());
//                    LogUtils.e("isLight:"+((MainActivity) context).isLight());
//                    AppContext.getInstance().intentJump((Activity) MainNewsItemAdapter.context,LatestContentActivity.class,bundle);
                }
            });
        }
    }

}
