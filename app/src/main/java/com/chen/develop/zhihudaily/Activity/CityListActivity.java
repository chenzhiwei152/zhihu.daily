package com.chen.develop.zhihudaily.Activity;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.chen.common.App.BaseActivity;
import com.chen.common.DB.AllCache;
import com.chen.common.DB.CacheDbHelper;
import com.chen.common.NetUtils.ParserUtils;
import com.chen.common.Utils.HttpUtils;
import com.chen.common.Utils.TextHttpResponseHandler;
import com.chen.common.Utils.Utils;
import com.chen.common.View.DialogView.CatLoadingView;
import com.chen.develop.zhihudaily.Adapter.CityListWithHeadersAdapter;
import com.chen.develop.zhihudaily.Adapter.DividerDecoration;
import com.chen.develop.zhihudaily.Bean.CityListBean;
import com.chen.develop.zhihudaily.Config.Interface;
import com.chen.develop.zhihudaily.R;
import com.chen.pinyin.PinyinComparator;
import com.chen.pinyin.PinyinHelper;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class CityListActivity extends BaseActivity implements OnQuickSideBarTouchListener {


    @Bind(R.id.pfc_listview)
    RecyclerView pfcListview;
    @Bind(R.id.quickSideBarTipsView)
    QuickSideBarTipsView quickSideBarTipsView;
    @Bind(R.id.quickSideBarView)
    QuickSideBarView quickSideBarView;
    private Toolbar toolbar;
    public static Activity instance;
    SQLiteDatabase db;
    private Context context;
    private static CacheDbHelper dbHelper;
    //    CityListBean cityListBean;
    private List<CityListBean> cityInfoList;
    HashMap<String, Integer> letters = new HashMap<>();
    private CatLoadingView mView;

    private Handler handler = new Handler();

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_city_list;
    }

    @Override
    protected void initViewsAndEvents() {
        context = this;
        instance = this;
        dbHelper = new CacheDbHelper(this, 1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mView = new CatLoadingView();
        mView.show(getSupportFragmentManager(), "");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_50BEB4));
        setToolbarTitle("切换城市");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void setToolbarTitle(String text) {
        toolbar.setTitle(text);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void loadData() {

        //设置监听
        quickSideBarView.setOnQuickSideBarTouchListener(this);


        //设置列表数据和浮动header
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pfcListview.setLayoutManager(layoutManager);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //城市数据
                try {
                    cityInfoList = (List<CityListBean>) AllCache.get(context).get("cityListBean");

                    if (cityInfoList == null) {
                        getCityList();
                    } else {
                        SetData();
                    }
                } catch (Exception e) {
                    getCityList();
                }
            }
        }, 1000);


    }

    private void getCityList() {
        HttpUtils.get(Interface.WEATHERCITYLISTSEARCH, false, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mView.dismiss();
                Utils.showToast("接口异常");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (!TextUtils.isEmpty(responseString)) {

                    cityInfoList = (List<CityListBean>) ParserUtils.parseArray(responseString, CityListBean.class);
                    AllCache.get(context).put("cityListBean", (Serializable) cityInfoList);
                    SetData();
                }
            }
        });
    }

    private void SetData() {
//        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    if (cityInfoList != null) {
// Add the sticky headers decoration
                        CityListWithHeadersAdapter adapter = new CityListWithHeadersAdapter();

                        List<CityListBean> cities = cityInfoList;

                        ArrayList<String> customLetters = new ArrayList<>();

                        int position = 0;

                        for (int i = 0; i < cities.size(); i++) {

                            String letter = PinyinHelper.getShortPinyin(cities.get(i).getCityEn()).charAt(0) + "";
                            cityInfoList.get(i).setFirstLetter(letter);
                        }
                        cities = cityInfoList;
                        Collections.sort(cities, new PinyinComparator());
                        cities = cityInfoList;
                        position = 0;
                        for (CityListBean city : cities) {
                            String letter = city.getFirstLetter();
                            //如果没有这个key则加入并把位置也加入
                            if (!letters.containsKey(letter)) {
                                letters.put(letter, position);
                                customLetters.add(letter);
                            }
                            position++;
                        }
                        //不自定义则默认26个字母
                        quickSideBarView.setLetters(customLetters);


                        adapter.addAll(cities);
                        pfcListview.setAdapter(adapter);

                        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
                        pfcListview.addItemDecoration(headersDecor);

                        // Add decoration for dividers between list items
                        pfcListview.addItemDecoration(new DividerDecoration(CityListActivity.this));
                        mView.dismiss();

                    }
                }
//            });
//        } catch (Exception e) {
//            LogUtils.e(e.getMessage());
//        }


//    }

    @Override
    public void onLetterChanged(String letter, int position, int itemHeight) {
        quickSideBarTipsView.setText(letter, position, itemHeight);
        //有此key则获取位置并滚动到该位置
        if (letters.containsKey(letter)) {
            pfcListview.scrollToPosition(letters.get(letter));
        }
    }

    @Override
    public void onLetterTouching(boolean touching) {
        //可以自己加入动画效果渐显渐隐
        quickSideBarTipsView.setVisibility(touching ? View.VISIBLE : View.INVISIBLE);
    }

    public void finishThis() {
        this.finish();
    }
}
