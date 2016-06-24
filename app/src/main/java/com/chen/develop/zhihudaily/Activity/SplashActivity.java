package com.chen.develop.zhihudaily.Activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chen.develop.zhihudaily.App.BaseActivity;
import com.chen.develop.zhihudaily.Bean.SplashBean;
import com.chen.develop.zhihudaily.Config.Interface;
import com.chen.develop.zhihudaily.NetUtils.ParserUtils;
import com.chen.develop.zhihudaily.R;
import com.chen.develop.zhihudaily.Utils.HttpUtils;
import com.chen.develop.zhihudaily.Utils.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class SplashActivity extends BaseActivity {
    private android.os.Handler handler = new android.os.Handler();
    @Bind(R.id.splash_text)
    TextView splash_text;
    @Bind(R.id.splash_ima)
    ImageView splash_ima;
    @Bind(R.id.ll_toast)
    LinearLayout ll_toast;

    @Override
    protected int getContentViewLayoutId() {
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
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_from_bottom);
        ll_toast.startAnimation(animation);
        LoadImage();
    }

    private void LoadImage() {
        HttpUtils.get(Interface.START, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (!TextUtils.isEmpty(responseString)) {
                    SplashBean bean = ParserUtils.parser(responseString, SplashBean.class);
                    ImageLoader.getInstance().displayImage(bean.getImg(), splash_ima);
                    splash_text.setText(bean.getText());

                }
            }
        });
    }
}
