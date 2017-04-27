package com.chen.develop.zhihudaily.Activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chen.common.App.BaseActivity;
import com.chen.common.NetUtils.ParserUtils;
import com.chen.common.Utils.HttpUtils;
import com.chen.common.Utils.ImageLoaderManager;
import com.chen.common.Utils.TextHttpResponseHandler;
import com.chen.develop.zhihudaily.Bean.SplashBean;
import com.chen.develop.zhihudaily.Config.Interface;
import com.chen.develop.zhihudaily.R;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;



public class SplashActivity extends BaseActivity {
    private android.os.Handler handler = new android.os.Handler();
    @Bind(R.id.splash_text)
    TextView splash_text;
    @Bind(R.id.splash_ima)
    ImageView splash_ima;
    @Bind(R.id.ll_toast)
    RelativeLayout ll_toast;

    @Override
    protected int getContentViewLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_scrolling;
    }

    @Override
    protected void initViewsAndEvents() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    @Override
    protected void loadData() {
        LoadImage();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_from_bottom);
        ll_toast.startAnimation(animation);

    }

    private void LoadImage() {
        HttpUtils.get(Interface.BASEURL_1+Interface.START,false, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (!TextUtils.isEmpty(responseString)) {
                    try {
                        SplashBean bean = ParserUtils.parser(responseString, SplashBean.class);
                        ImageLoaderManager.getInstance(SplashActivity.this).display(SplashActivity.this,bean.getCreatives().get(0).getUrl(), splash_ima);
                        splash_text.setText("");
                    } catch (Exception w) {

                    }



                }
            }
        });
    }
}
