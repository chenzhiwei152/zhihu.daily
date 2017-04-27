package com.chen.develop.zhihudaily.weather;

import com.chen.common.NetUtils.ParserUtils;
import com.chen.common.Utils.HttpUtils;
import com.chen.common.Utils.LogUtils;
import com.chen.common.Utils.TextHttpResponseHandler;
import com.chen.develop.zhihudaily.Bean.WeatherBean;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by chen.zhiwei on 2016-6-29.
 */
public class GetWeatherInfo {
    private static String httpUrl = "http://apis.baidu.com/heweather/weather/free?city=beijing";
    //    String jsonResult = request(httpUrl, httpArg);
    BufferedReader reader = null;
    String result = null;
    StringBuffer sbf = new StringBuffer();
//    static WeatherBean bean = null;
    private static List<WeatherBean> bean=new ArrayList<WeatherBean>();

    /**
     * @param :请求接口
     * @param :参数
     * @return 返回结果
     */

    public static List<WeatherBean> GetWeatherBean(String city) {
//        httpUrl += "?city=beijing";
        LogUtils.e(httpUrl);

        HttpUtils.get(httpUrl, false, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.e(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONObject jbAll = new JSONObject(responseString.toString());

                    String sss=jbAll.getString("HeWeather data service 3.0");
                    LogUtils.e("sss:"+sss);

                    bean= (List<WeatherBean>) ParserUtils.parseArray(sss,WeatherBean.class);


                } catch (Exception e) {
                    LogUtils.e("获取大小失败");
                }

            }
        });
        return bean;
    }


}
