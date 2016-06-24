package com.chen.develop.zhihudaily.Fragment;

import android.annotation.SuppressLint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.chen.develop.zhihudaily.Activity.MainActivity;
import com.chen.develop.zhihudaily.Adapter.NewsItemAdapter;
import com.chen.develop.zhihudaily.Bean.News;
import com.chen.develop.zhihudaily.Bean.NewsLatestBean;
import com.chen.develop.zhihudaily.Bean.StoriesEntity;
import com.chen.develop.zhihudaily.NetUtils.ParserUtils;
import com.chen.develop.zhihudaily.R;
import com.chen.develop.zhihudaily.Utils.Constant;
import com.chen.develop.zhihudaily.Utils.HttpUtils;
import com.chen.develop.zhihudaily.Utils.LogUtils;
import com.chen.develop.zhihudaily.Utils.TextHttpResponseHandler;
import com.chen.develop.zhihudaily.View.Kanner;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wwjun.wang on 2015/8/14.
 */
@SuppressLint("ValidFragment")
public class NewsFragment extends BaseFragment {
    private ImageLoader mImageLoader;
    private PullToRefreshRecyclerView lv_news;
    //    private ImageView iv_title;
//    private TextView tv_title;
    private String urlId;
    //    private News news;
    private NewsItemAdapter mAdapter;
    private String title;
    private Kanner kanner;

    public NewsFragment(String id, String title) {
        urlId = id;
        this.title = title;
    }

    private void parseJson(String responseString) {
//        Gson gson = new Gson();
//        news = gson.fromJson(responseString, News.class);
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//        tv_title.setText(news.getDescription());
//        mImageLoader.displayImage(news.getImage(), iv_title, options);
//        mAdapter = new NewsItemAdapter(mActivity, news.getStories());
//        lv_news.setAdapter(mAdapter);
    }

//    public void updateTheme() {
//        mAdapter.updateTheme();
//    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.news_layout;
    }

    @Override
    protected void initViewsAndEvents() {
        ((MainActivity) getActivity()).setToolbarTitle(title);
        mImageLoader = ImageLoader.getInstance();
        lv_news = (PullToRefreshRecyclerView) view.findViewById(R.id.lv_news);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.kanner, lv_news, false);//headview,广告栏
        kanner = (Kanner) header.findViewById(R.id.kanner);

//增加headview
        lv_news.addHeaderView(header);

        lv_news.setSwipeEnable(true);//open swipe
        lv_news.setLayoutManager(new LinearLayoutManager(getActivity()));
        lv_news.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                LogUtils.e("onLoadMoreItems", "加载更多~");
            }
        });
        lv_news.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e("onRefresh", "刷新数据~");
                loadData();

//                mHandler.sendEmptyMessageDelayed(MSG_CODE_REFRESH, TIME);
            }
        });


//        lv_news.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int[] startingLocation = new int[2];
//                view.getLocationOnScreen(startingLocation);
//                startingLocation[0] += view.getWidth() / 2;
//                StoriesEntity entity = (StoriesEntity) parent.getAdapter().getItem(position);
//                Intent intent = new Intent(getActivity(), LatestContentActivity.class);
//                intent.putExtra(Constant.START_LOCATION, startingLocation);
//                intent.putExtra("entity", entity);
//                intent.putExtra("isLight", ((MainActivity) getActivity()).isLight());
//
//                String readSequence = PreUtils.getStringFromDefault(getActivity(), "read", "");
//                String[] splits = readSequence.split(",");
//                StringBuffer sb = new StringBuffer();
//                if (splits.length >= 200) {
//                    for (int i = 100; i < splits.length; i++) {
//                        sb.append(splits[i] + ",");
//                    }
//                    readSequence = sb.toString();
//                }
//
//                if (!readSequence.contains(entity.getId() + "")) {
//                    readSequence = readSequence + entity.getId() + ",";
//                }
//                PreUtils.putStringToDefault(getActivity(), "read", readSequence);
//                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
//                tv_title.setTextColor(getResources().getColor(R.color.clicked_tv_textcolor));
//
//                startActivity(intent);
//                getActivity().overridePendingTransition(0, 0);
//            }
//        });
        lv_news.setScrollListener(new PullToRefreshRecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i1) {

            }

            @Override
            public void onScroll(RecyclerView recyclerView, int i, int i1, int i2) {

            }
        });
        mAdapter = new NewsItemAdapter(mActivity);
        lv_news.setAdapter(mAdapter);

    }

    @Override
    protected void loadData() {
        if (HttpUtils.isNetworkConnected(getActivity())) {
            HttpUtils.get(Constant.THEMENEWS + urlId, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    LogUtils.e(responseString);
                    lv_news.setOnRefreshComplete();
                    lv_news.onFinishLoading(true, false);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
//                    SQLiteDatabase db = ((MainActivity) getActivity()).getCacheDbHelper().getWritableDatabase();
//                    db.execSQL("replace into CacheList(date,json) values(" + (Constant.BASE_COLUMN + Integer.parseInt(urlId)) + ",' " + responseString + "')");
//                    db.close();
//                    parseJson(responseString);
                    if (!TextUtils.isEmpty(responseString)) {

                        News news = ParserUtils.parser(responseString, News.class);
                        List<StoriesEntity> storiesEntities = news.getStories();
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .cacheInMemory(true)
                                .cacheOnDisk(true)
                                .build();
//                        tv_title.setText(news.getDescription());
//                        mImageLoader.displayImage(news.getImage(), iv_title, options);
                        List<NewsLatestBean.TopStoriesBean> list = new ArrayList<NewsLatestBean.TopStoriesBean>();
                        NewsLatestBean.TopStoriesBean bean = new NewsLatestBean.TopStoriesBean();
                        bean.setTitle(news.getDescription());
                        bean.setImage(news.getImage());
                        list.add(bean);
                        kanner.setTopEntities(list);


                        mAdapter.ClearData();
                        mAdapter.addList(storiesEntities);
//                        isLoading = false;
                        lv_news.setOnRefreshComplete();
                        lv_news.onFinishLoading(false, false);
                    }


                }

            });
        } else {
//            SQLiteDatabase db = ((MainActivity) getActivity()).getCacheDbHelper().getReadableDatabase();
//            Cursor cursor = db.rawQuery("select * from CacheList where date = " + (Constant.BASE_COLUMN + Integer.parseInt(urlId)), null);
//            if (cursor.moveToFirst()) {
//                String json = cursor.getString(cursor.getColumnIndex("json"));
//                parseJson(json);
//            }
//            cursor.close();
//            db.close();
        }
    }
    public void updateTheme() {
        mAdapter.updateTheme();
    }
}
