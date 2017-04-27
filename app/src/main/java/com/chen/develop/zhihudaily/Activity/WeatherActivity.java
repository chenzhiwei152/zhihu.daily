package com.chen.develop.zhihudaily.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chen.common.App.AppContext;
import com.chen.common.App.BaseActivity;
import com.chen.common.DB.AllCache;
import com.chen.common.NetUtils.ParserUtils;
import com.chen.common.Utils.HttpUtils;
import com.chen.common.Utils.ImageLoaderManager;
import com.chen.common.Utils.LogUtils;
import com.chen.common.Utils.TextHttpResponseHandler;
import com.chen.common.Utils.Utils;
import com.chen.common.View.ChartView.LineCardOne;
import com.chen.common.View.DialogView.CatLoadingView;
import com.chen.develop.zhihudaily.Bean.WeatherBean;
import com.chen.develop.zhihudaily.Config.Interface;
import com.chen.develop.zhihudaily.R;
import com.db.chart.view.LineChartView;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class WeatherActivity extends BaseActivity {

    private String[] mLabels = null;
    private float[][] mValues = null;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.city)
    TextView city;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.humidity)
    TextView humidity;
    @Bind(R.id.pm2_5)
    TextView pm25;
    @Bind(R.id.pm_data)
    TextView pmData;
    @Bind(R.id.pm2_5_img)
    ImageView pm25Img;
    @Bind(R.id.pm2_5_quality)
    TextView pm25Quality;
    @Bind(R.id.pm2_5_content)
    LinearLayout pm25Content;
    @Bind(R.id.weather_img)
    ImageView weatherImg;
    @Bind(R.id.week_today)
    TextView weekToday;
    @Bind(R.id.temperature)
    TextView temperature;
    @Bind(R.id.climate)
    TextView climate;
    @Bind(R.id.wind)
    TextView wind;
    @Bind(R.id.lc_chart)
    LineChartView lcChart;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.tv_weather_describe)
    TextView tv_weather_describe;
    @Bind(R.id.ll_alarm)
    LinearLayout ll_alarm;
    @Bind(R.id.ll_pm)
    LinearLayout ll_pm;
    @Bind(R.id.tv_alarm_type)
    TextView tv_alarm_type;
    @Bind(R.id.adcontainer)
    RelativeLayout adcontainer;
    private LineCardOne lineCardOne;
    private String mCityId;
    private String mCityName;
    private WeatherBean bean;
    private CatLoadingView mView;
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    protected void initViewsAndEvents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_50BEB4));
        setToolbarTitle("天气");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetWeatherBean();
            }
        });
        mView = new CatLoadingView();

    }

    @Override
    protected void loadData() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        getCityInfo();

    }

    private void getCityInfo() {
        mView.show(getSupportFragmentManager(), "");
        LogUtils.e("city:" + AllCache.get(this).getAsString("city"));
        LogUtils.e("id:" + AllCache.get(this).getAsString("id"));
        LogUtils.e("mCityId:" + mCityId);
        if (TextUtils.isEmpty(AllCache.get(this).getAsString("id"))) {
            mCityId = "CN101010100";
            mCityName = "北京";
            GetWeatherBean();
        } else if (TextUtils.isEmpty(mCityId)) {
            mCityId = AllCache.get(this).getAsString("id");
            mCityName = AllCache.get(this).getAsString("city");
            GetWeatherBean();
        } else if (!TextUtils.isEmpty(mCityId)&&!mCityId.equals(AllCache.get(this).getAsString("id"))) {
            mCityId = AllCache.get(this).getAsString("id");
            mCityName = AllCache.get(this).getAsString("city");
            GetWeatherBean();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_city) {
            AppContext.getInstance().intentJump(this, CityListActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbarTitle(String text) {
        toolbar.setTitle(text);
        setSupportActionBar(toolbar);
    }

    private void SetWeatherForestData() {
        if (bean != null && bean.getHeWeather5().get(0) != null && bean.getHeWeather5().get(0).getDaily_forecast() != null && bean.getHeWeather5().get(0).getDaily_forecast().size() > 0) {
            int forecastSize = bean.getHeWeather5().get(0).getDaily_forecast().size();
            mValues = new float[2][forecastSize];
            mLabels = new String[forecastSize];
            try {
                for (int i = 0; i < forecastSize; i++) {
                    String data[] = bean.getHeWeather5().get(0).getDaily_forecast().get(i).getDate().split("-");
                    mLabels[i] = data[1] + "-" + data[2];
                    mValues[0][i] = (float) Integer.parseInt(bean.getHeWeather5().get(0).getDaily_forecast().get(i).getTmp().getMax());
                    mValues[1][i] = (float) Integer.parseInt(bean.getHeWeather5().get(0).getDaily_forecast().get(i).getTmp().getMin());
                }
            } catch (Exception e) {

            }

        }
    }

    /**
     * 天气
     */
    public void GetWeatherBean() {
        LogUtils.e(Interface.GET_WEATHER_INFO);

        HttpUtils.get(Interface.GET_WEATHER_INFO + mCityId + "&key=f1248f309bec41689c0f1b632ac2a1ca", false, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.e(responseString);
                mView.dismiss();
                Utils.showToast("获取信息失败~");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                mView.dismiss();
                try {
                    bean = ParserUtils.parser(responseString, WeatherBean.class);
                    if (bean != null) {
                        city.setText(mCityName);
//                        time.setText(Utils.GetSystemCurrentTime());//


                        if (bean.getHeWeather5().get(0).getAqi() != null) {
                            if (!TextUtils.isEmpty(bean.getHeWeather5().get(0).getAqi().getCity().getAqi())) {
                                try {
                                    int api = Integer.parseInt(bean.getHeWeather5().get(0).getAqi().getCity().getAqi());
                                    if (api <= 50) {
                                        ll_pm.setBackground(getResources().getDrawable(R.drawable.round_background_1e90ff));
                                    } else if (api > 50 && api <= 150) {
                                        ll_pm.setBackground(getResources().getDrawable(R.drawable.round_background_ffd700));
                                    } else if (api > 150) {
                                        ll_pm.setBackground(getResources().getDrawable(R.drawable.round_background_ff0000));
                                    }

                                } catch (Exception e) {

                                }
                            }
                            pmData.setText("AQI");//pm2.5数据
                            pm25Quality.setText(bean.getHeWeather5().get(0).getAqi().getCity().getAqi());//pm质量
                            ll_pm.setVisibility(View.VISIBLE);

                        } else {
                            ll_pm.setVisibility(View.GONE);
                        }


                        ImageLoaderManager.getInstance(WeatherActivity.this).display(WeatherActivity.this,Interface.WEATHERBASE + bean.getHeWeather5().get(0).getNow().getCond().getCode() + Interface.WEATHERSUFFIX, weatherImg);

//                        weekToday.setText("星期四");  //星期几
                        temperature.setText(bean.getHeWeather5().get(0).getNow().getTmp() + "℃");       //温度
                        climate.setText(bean.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMin() + "-" + bean.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMax() + "℃");        //体感温度

                        humidity.setText(bean.getHeWeather5().get(0).getNow().getWind().getDir());//风力
                        wind.setText(bean.getHeWeather5().get(0).getNow().getWind().getSc() + "级");
                        tv_weather_describe.setText(bean.getHeWeather5().get(0).getNow().getCond().getTxt());


                        SetWeatherForestData();
                        if (lineCardOne == null) {
                            lineCardOne = new LineCardOne((LineChartView) findViewById(R.id.lc_chart), WeatherActivity.this, mLabels, mValues);
                            lineCardOne.init();
                        } else {
                            lineCardOne.SetWeatherBase(mLabels, mValues);
//                            lineCardOne.update();
                        }
                    } else {
                    }

                } catch (Exception e) {
                }

            }
        });
    }

}
