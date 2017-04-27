package com.chen.develop.zhihudaily.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chen.common.App.AppContext;
import com.chen.common.DB.AllCache;
import com.chen.common.NetUtils.ParserUtils;
import com.chen.common.Utils.Constant;
import com.chen.common.Utils.HttpUtils;
import com.chen.common.Utils.ImageLoaderManager;
import com.chen.common.Utils.JsonHttpResponseHandler;
import com.chen.common.Utils.LogUtils;
import com.chen.common.Utils.PreUtils;
import com.chen.common.Utils.TextHttpResponseHandler;
import com.chen.common.Utils.Utils;
import com.chen.develop.zhihudaily.Activity.MainActivity;
import com.chen.develop.zhihudaily.Activity.WeatherActivity;
import com.chen.develop.zhihudaily.Bean.NewsListItem;
import com.chen.develop.zhihudaily.Bean.WeatherBean;
import com.chen.develop.zhihudaily.Config.Interface;
import com.chen.develop.zhihudaily.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class MenuFragment extends BaseFragment implements OnClickListener {
    private ListView lv_item;
    private TextView tv_download, tv_main, tv_backup, tv_login;
    private LinearLayout ll_menu, ll_home;
    // private static String[] ITEMS = { "日常心理学", "用户推荐日报", "电影日报", "不许无聊",
    // "设计日报", "大公司日报", "财经日报", "互联网安全", "开始游戏", "音乐日报", "动漫日报", "体育日报" };
    private List<NewsListItem> items;
    private Handler handler = new Handler();
    private boolean isLight = true;
    private NewsTypeAdapter mAdapter;
    private ImageView iv_weather_state;
    private TextView tv_weather;
    @Bind(R.id.tv_loacation)
    TextView tv_loacation;
    @Bind(R.id.tv_temp)
    TextView tv_temp;
    @Bind(R.id.ll_weather)
    LinearLayout ll_weather;
    private String mCityId;
    private String mCityName;
    private WeatherBean bean;

    private void parseJson(JSONObject response) {
        try {
            JSONArray itemsArray = response.getJSONArray("others");
            for (int i = 0; i < itemsArray.length(); i++) {
                NewsListItem newsListItem = new NewsListItem();
                JSONObject itemObject = itemsArray.getJSONObject(i);
                newsListItem.setTitle(itemObject.getString("name"));
                newsListItem.setId(itemObject.getString("id"));
                items.add(newsListItem);
            }
            mAdapter = new NewsTypeAdapter();
            lv_item.setAdapter(mAdapter);
            updateTheme();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.menu;
    }

    @Override
    protected void initViewsAndEvents() {
        ll_menu = (LinearLayout) view.findViewById(R.id.ll_menu);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
        tv_backup = (TextView) view.findViewById(R.id.tv_backup);
        tv_download = (TextView) view.findViewById(R.id.tv_download);
        iv_weather_state = (ImageView) view.findViewById(R.id.iv_weather_state);
        tv_weather = (TextView) view.findViewById(R.id.tv_weather);
        ll_home = (LinearLayout) view.findViewById(R.id.ll_home);
        tv_download.setOnClickListener(this);
        tv_main = (TextView) view.findViewById(R.id.tv_main);
        ll_home = (LinearLayout) view.findViewById(R.id.ll_home);
        ll_home.setOnClickListener(this);
        ll_weather.setOnClickListener(this);
        ll_menu.setOnClickListener(this);
        lv_item = (ListView) view.findViewById(R.id.lv_item);
        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                getFragmentManager()
                        .beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                        .replace(
                                R.id.fl_content,
                                new NewsFragment(items.get(position)
                                        .getId(), items.get(position).getTitle()), "news").commit();

                ((MainActivity) mActivity).setCurId(items.get(position).getId());
                ((MainActivity) mActivity).closeMenu();


            }
        });
        ll_home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).
                        replace(R.id.fl_content, new MainFragment(), "latest").
                        commit();
                ((MainActivity) mActivity).closeMenu();
            }
        });
    }

    @Override
    protected void loadData() {
//        isLight = MainActivity.;
        items = new ArrayList<NewsListItem>();
        if (HttpUtils.isNetworkConnected(getActivity())) {
            HttpUtils.get(Constant.THEMES, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    String json = response.toString();
                    PreUtils.putStringToDefault(getActivity(), Constant.THEMES, json);
                    parseJson(response);
                }
            });
        } else {
            String json = PreUtils.getStringFromDefault(getActivity(), Constant.THEMES, "");
            try {
                JSONObject jsonObject = new JSONObject(json);
                parseJson(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        getCityInfo();

    }

    public void getCityInfo() {
        if (TextUtils.isEmpty(mCityId) || TextUtils.isEmpty(AllCache.get(getActivity()).getAsString("id"))) {
            mCityId = "CN101010100";
            mCityName = "北京";
            GetWeatherBean(mCityId);
        } else if (!mCityId.equals(AllCache.get(getActivity()).getAsString("id"))) {
            mCityId = AllCache.get(getActivity()).getAsString("id");
            mCityName = AllCache.get(getActivity()).getAsString("city");
            GetWeatherBean(mCityId);
        }


    }

    /**
     * 天气
     */
    public void GetWeatherBean(String city) {
        LogUtils.e(Interface.GET_WEATHER_INFO);

        HttpUtils.get(Interface.GET_WEATHER_INFO + city + "&key=f1248f309bec41689c0f1b632ac2a1ca", false, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.e(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {

                    bean =  ParserUtils.parser(responseString, WeatherBean.class);
                    if (bean != null) {
                        ll_weather.setVisibility(View.VISIBLE);
                        //天气描述
                        tv_weather.setText(bean.getHeWeather5().get(0).getNow().getCond().getTxt());
                        //温度
                        tv_temp.setText(bean.getHeWeather5().get(0).getNow().getTmp() + "℃");
                        //对应的图片
                        ImageLoaderManager.getInstance(getActivity()).display(getActivity(),Interface.WEATHERBASE + bean.getHeWeather5().get(0).getNow().getCond().getCode() + Interface.WEATHERSUFFIX, iv_weather_state);
                        tv_loacation.setText(mCityName);

                    } else {
                        ll_weather.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    LogUtils.e("解析失败:"+e.getMessage());
                }

            }
        });
    }

    public class NewsTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.menu_item, parent, false);
            }
            TextView tv_item = (TextView) convertView
                    .findViewById(R.id.tv_item);
            tv_item.setTextColor(getResources().getColor(isLight ? R.color.light_menu_listview_textcolor : R.color.dark_menu_listview_textcolor));
            tv_item.setText(items.get(position).getTitle());
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                ((MainActivity) getActivity()).closeMenu();
                break;
            case R.id.ll_weather:
                Bundle bundle = new Bundle();
                AppContext.getInstance().intentJump(getActivity(), WeatherActivity.class, bundle);
                ((MainActivity) getActivity()).closeMenu();
                break;
            case R.id.ll_menu:
                Utils.showToast("敬请期待~");
                break;
        }
    }

    public void updateTheme() {
        isLight = ((MainActivity) getActivity()).isLight();
        ll_menu.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_header : R.color.dark_menu_header));
        tv_login.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tv_backup.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tv_download.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        ll_home.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_index_background : R.color.dark_menu_index_background));
        lv_item.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_listview_background : R.color.dark_menu_listview_background));
        ll_weather.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_listview_background : R.color.dark_menu_listview_background));
        mAdapter.notifyDataSetChanged();
    }
}
