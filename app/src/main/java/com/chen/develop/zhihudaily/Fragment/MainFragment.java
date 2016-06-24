package com.chen.develop.zhihudaily.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.chen.develop.zhihudaily.Activity.LatestContentActivity;
import com.chen.develop.zhihudaily.Activity.MainActivity;
import com.chen.develop.zhihudaily.Adapter.MainNewsItemAdapter;
import com.chen.develop.zhihudaily.App.AppContext;
import com.chen.develop.zhihudaily.Bean.NewsBeforeBean;
import com.chen.develop.zhihudaily.Bean.NewsLatestBean;
import com.chen.develop.zhihudaily.Bean.StoriesBean;
import com.chen.develop.zhihudaily.Config.Interface;
import com.chen.develop.zhihudaily.NetUtils.ParserUtils;
import com.chen.develop.zhihudaily.R;
import com.chen.develop.zhihudaily.Utils.Constant;
import com.chen.develop.zhihudaily.Utils.HttpUtils;
import com.chen.develop.zhihudaily.Utils.LogUtils;
import com.chen.develop.zhihudaily.Utils.TextHttpResponseHandler;
import com.chen.develop.zhihudaily.View.Kanner;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by chen.zhiwei on 2016/6/22 0022.
 */
public class MainFragment extends BaseFragment {


    @Bind(R.id.sf_listview)
    PullToRefreshRecyclerView sf_listview;


    private String curId = "latest";
    private Kanner kanner;
    private String date;
    private boolean isLoading = false;
    private Handler handler = new Handler();
    private boolean isLight = true;
    private MainNewsItemAdapter mAdapter;
    private Toolbar toolbar;
    private String url = Interface.LATESTNEWS;


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_main_layout;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected void loadData() {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.kanner, sf_listview, false);//headview,广告栏
        kanner = (Kanner) header.findViewById(R.id.kanner);
        kanner.setOnItemClickListener(new Kanner.OnItemClickListener() {
            @Override
            public void click(View v, NewsLatestBean.TopStoriesBean entity) {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                StoriesBean storiesEntity = new StoriesBean();
                storiesEntity.setId(entity.getId());
                storiesEntity.setTitle(entity.getTitle());
                Bundle bundle = new Bundle();
                bundle.putIntArray(Constant.START_LOCATION, startingLocation);
                bundle.putSerializable("entity", storiesEntity);
                bundle.putBoolean("isLight", isLight);
                AppContext.getInstance().intentJump(getActivity(), LatestContentActivity.class, bundle);
            }


        });

//增加headview
        sf_listview.addHeaderView(header);


        sf_listview.setSwipeEnable(true);//open swipe
        sf_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        sf_listview.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                LogUtils.e("onLoadMoreItems", "加载更多~");
                loadMore(Interface.BEFORE + date);
            }
        });
        sf_listview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e("onRefresh", "刷新数据~");
                loadLatest();
            }
        });
        sf_listview.setLoadMoreCount(5);


        mAdapter = new MainNewsItemAdapter(getActivity());
        sf_listview.setAdapter(mAdapter);

        sf_listview.setScrollListener(new PullToRefreshRecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i1) {

            }

            @Override
            public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                try {
                    if (firstVisibleItem > 0) {
                        if (mAdapter != null) {
                            if (mAdapter.getEntities().get(firstVisibleItem - 1).getType() == Constant.TOPIC) {
//动态修改TooBar的标题
                                ((MainActivity) mActivity).setToolbarTitle(mAdapter.getEntities().get(firstVisibleItem - 1).getTitle());

                            }
                        }

                    } else {
                        ((MainActivity) mActivity).setToolbarTitle("首页");
                    }


                } catch (Exception e) {

                }
            }
        });

        loadLatest();
    }

    public void loadLatest() {
        isLoading = true;
        if (HttpUtils.isNetworkConnected(getActivity())) {
            HttpUtils.get(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    LogUtils.e(responseString);
                    sf_listview.setOnRefreshComplete();
                    sf_listview.onFinishLoading(true, false);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, final String responseString) {
                    SQLiteDatabase db = MainActivity.getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + Constant.LATEST_COLUMN + ",' " + responseString + "')");
                    db.close();
                    parseLatestJson(responseString);
                }
            });
        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + Constant.LATEST_COLUMN, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            } else {
                isLoading = false;
            }
            cursor.close();
            db.close();
        }
    }

    private void parseLatestJson(String json) {
        if (!TextUtils.isEmpty(json)) {

            final NewsLatestBean latest = ParserUtils.parser(json, NewsLatestBean.class);
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if (latest != null) {
                        date = latest.getDate() + "";
                        kanner.setTopEntities(latest.getTop_stories());
                        List<StoriesBean> storiesEntities = NewsLatestBean.getStories();
                        StoriesBean topic = new StoriesBean();
                        topic.setType(Constant.TOPIC);
                        topic.setTitle("今日热闻");
                        storiesEntities.add(0, topic);
//                                    mAdapter = new MainNewsItemAdapter(MainActivity.this,storiesEntities);
//                                    sf_listview.setAdapter(mAdapter);
                        mAdapter.ClearData();
                        mAdapter.addList(storiesEntities);
                        isLoading = false;
                        sf_listview.setOnRefreshComplete();
                        sf_listview.onFinishLoading(true, false);
                    }
                }
            });

        }

    }

    private void loadMore(final String url) {
        isLoading = true;
        if (HttpUtils.isNetworkConnected(getActivity())) {
            HttpUtils.get(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    LogUtils.e(responseString);
                    sf_listview.setOnRefreshComplete();
                    sf_listview.onFinishLoading(true, false);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + date + ",' " + responseString + "')");
                    db.close();
                    parseBeforeJson(responseString);
                }

            });
        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + date, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJson(json);
            } else {
                db.delete("CacheList", "date < " + date, null);
                isLoading = false;
                Snackbar sb = Snackbar.make(sf_listview, "没有更多的离线内容了~", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(((MainActivity) mActivity).isLight() ? android.R.color.holo_blue_dark : android.R.color.black));
                sb.show();
            }
            cursor.close();
            db.close();
        }
    }
private void parseBeforeJson(String json){
    if (!TextUtils.isEmpty(json)) {

        final NewsBeforeBean beforeBean = ParserUtils.parser(json, NewsBeforeBean.class);
        handler.post(new Runnable() {
            @Override
            public void run() {

                if (beforeBean != null) {
                    date = beforeBean.getDate() + "";
                    List<StoriesBean> storiesEntities = beforeBean.getStories();
                    StoriesBean topic = new StoriesBean();
                    topic.setType(Constant.TOPIC);
                    topic.setTitle(convertDate(date));
                    storiesEntities.add(0, topic);
                    mAdapter.addList(storiesEntities);
                    isLoading = false;
                    sf_listview.setOnRefreshComplete();
                    sf_listview.onFinishLoading(true, false);
                }
            }
        });

    }
}
    private String convertDate(String date) {
        String result = date.substring(0, 4);
        result += "年";
        result += date.substring(4, 6);
        result += "月";
        result += date.substring(6, 8);
        result += "日";
        return result;
    }

    public void updateTheme() {
        mAdapter.updateTheme();
    }
}
