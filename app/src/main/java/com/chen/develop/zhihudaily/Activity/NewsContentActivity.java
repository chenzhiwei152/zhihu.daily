package com.chen.develop.zhihudaily.Activity;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.chen.common.App.BaseActivity;
import com.chen.common.Utils.ImageLoaderManager;
import com.chen.common.Utils.WebUtils;
import com.chen.develop.zhihudaily.Bean.Content;
import com.chen.develop.zhihudaily.Bean.StoriesBean;
import com.chen.common.DB.WebCacheDbHelper;
import com.chen.common.NetUtils.ParserUtils;
import com.chen.develop.zhihudaily.R;
import com.chen.common.Utils.Constant;
import com.chen.common.Utils.HttpUtils;
import com.chen.common.Utils.TextHttpResponseHandler;
import com.chen.common.View.RevealBackgroundView;

import java.io.Serializable;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class NewsContentActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener {

    @Bind(R.id.iv)
    ImageView iv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @Bind(R.id.webview)
    WebView mWebView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.revealBackgroundView)
    RevealBackgroundView revealBackgroundView;
    private boolean isLight = true;
    private StoriesBean entity;
    private String mFrom = "";
    private WebCacheDbHelper dbHelper;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_news_content;
    }


    @Override
    protected void initViewsAndEvents() {
        dbHelper = new WebCacheDbHelper(this, 1);
        //toolbar
        Bundle bundle = getIntent().getExtras();
        try {
            Serializable serializable = bundle.getSerializable("entity");
            entity = (StoriesBean) serializable;
            mFrom = bundle.getString("from");
            isLight = bundle.getBoolean("isLight", true);
        } catch (Exception e) {

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        //floatingactionbutton
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        collapsingToolbarLayout.setTitle(entity.getTitle());
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));


        //webview
        mWebView = (WebView) findViewById(R.id.webview);
//        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }
        });

//请求异步
        getNewsContext();
        setupRevealBackground(savedInstanceState);
    }

    private void getNewsContext() {
        if (HttpUtils.isNetworkConnected(this)) {
            String url = null;
            if (TextUtils.isEmpty(mFrom)) {
                url = Constant.CONTENT + entity.getId();
            } else {
                url = Constant.CONTENT + entity.getId();
            }

            HttpUtils.get(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    responseString = responseString.replaceAll("'", "''");
                    db.execSQL("replace into Cache(newsId,json) values(" + entity.getId() + ",'" + responseString + "')");
                    db.close();
                    parseJson(responseString);
                }
            });
        } else {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from Cache where newsId = " + entity.getId(), null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseJson(json);
            }
            cursor.close();
            db.close();
        }
    }

    private void parseJson(String json) {
        if (!TextUtils.isEmpty(json)) {

            Content detailBean = ParserUtils.parser(json, Content.class);

            try {
                ImageLoaderManager.getInstance(this).display(this,detailBean.getTheme().getThumbnail(), iv);
//                            imageloader.displayImage(detailBean.getRecommenders().get(0).getAvatar(), fab, options);

            } catch (Exception e) {

            }

            String data = WebUtils.buildHtmlWithCss(detailBean.getBody(), detailBean.getCss(), !isLight);
            mWebView.loadDataWithBaseURL(WebUtils.BASE_URL, data, WebUtils.MIME_TYPE, WebUtils.ENCODING, WebUtils.FAIL_URL);
        }
    }

    @Override
    protected void loadData() {

    }

    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (mWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            mWebView.goBack();   //goBack()表示返回webView的上一页面

            return true;
        }
        finish();
        return false;
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        revealBackgroundView.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(Constant.START_LOCATION);
            revealBackgroundView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    revealBackgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                    revealBackgroundView.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            revealBackgroundView.setToFinishedFrame();
        }
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            appBarLayout.setVisibility(View.VISIBLE);
            setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @TargetApi(21)
    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If both system bars are black, we can remove these from our layout,
            // removing or shrinking the SurfaceFlinger overlay required for our views.
            Window window = this.getWindow();
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(statusBarColor);
        }
    }
}
