package com.chen.develop.zhihudaily.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chen.common.NetUtils.ParserUtils;
import com.chen.common.Utils.Constant;
import com.chen.common.Utils.HttpUtils;
import com.chen.common.Utils.ImageLoaderManager;
import com.chen.common.Utils.LogUtils;
import com.chen.common.Utils.TextHttpResponseHandler;
import com.chen.develop.zhihudaily.Activity.MainActivity;
import com.chen.develop.zhihudaily.Adapter.NewsItemAdapter;
import com.chen.develop.zhihudaily.Bean.News;
import com.chen.develop.zhihudaily.Bean.NewsLatestBean;
import com.chen.develop.zhihudaily.Bean.StoriesEntity;
import com.chen.develop.zhihudaily.R;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wwjun.wang on 2015/8/14.
 */
@SuppressLint("ValidFragment")
public class NewsFragment extends BaseFragment {
    private PullToRefreshRecyclerView lv_news;
    //    private ImageView iv_title;
//    private TextView tv_title;
    private String urlId;
    //    private News news;
    private NewsItemAdapter mAdapter;
    private String title;
    private ConvenientBanner kanner;

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
        lv_news = (PullToRefreshRecyclerView) view.findViewById(R.id.lv_news);
//        View header = LayoutInflater.from(getActivity()).inflate(R.layout.kanner, lv_news, false);//headview,广告栏
//        kanner = (ConvenientBanner) header.findViewById(R.id.convenientBanner);
//
////增加headview
//        lv_news.addHeaderView(header);

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
//                        tv_title.setText(news.getDescription());
//                        mImageLoader.displayImage(news.getImage(), iv_title, options);
                        List<NewsLatestBean.TopStoriesBean> list = new ArrayList<NewsLatestBean.TopStoriesBean>();
                        NewsLatestBean.TopStoriesBean bean = new NewsLatestBean.TopStoriesBean();
                        bean.setTitle(news.getDescription());
                        bean.setImage(news.getImage());
                        list.add(bean);
//                        kanner.setTopEntities(list);
                        initAD(list);

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

    public void ScrollTop() {
        if (lv_news != null) {
            lv_news.smoothScrollToPosition(lv_news.getTop());
        }
    }

    /**
     * 广告栏定义图片地址
     */
    public class LocalImageHolderView implements Holder<NewsLatestBean.TopStoriesBean> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, NewsLatestBean.TopStoriesBean data) {
            ImageLoaderManager.getInstance(getActivity()).display(getActivity(),data.getImage(),imageView);
        }
    }

    private void initAD(final List<NewsLatestBean.TopStoriesBean> storiesList) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.kanner, lv_news, false);//headview,广告栏
        kanner = (ConvenientBanner) header.findViewById(R.id.convenientBanner);

//自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        kanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, storiesList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.dot_blur, R.mipmap.dot_focus})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT).startTurning(3000);
        //设置翻页的效果，不需要翻页效果可用不设
        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
//        convenientBanner.setManualPageable(false);//设置不能手动影响

//        kanner.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
////                int[] startingLocation = new int[2];
////                v.getLocationOnScreen(startingLocation);
////                startingLocation[0] += v.getWidth() / 2;
//                StoriesBean storiesEntity = new StoriesBean();
//                storiesEntity.setId(storiesList.get(position).getId());
//                storiesEntity.setTitle(storiesList.get(position).getTitle());
//                Bundle bundle = new Bundle();
////                bundle.putIntArray(Constant.START_LOCATION, startingLocation);
//                bundle.putSerializable("entity", storiesEntity);
//                bundle.putBoolean("isLight", isLight);
//                AppContext.getInstance().intentJump(getActivity(), LatestContentActivity.class, bundle);
//            }
//        });


//增加headview
        lv_news.addHeaderView(header);
    }
}
