package com.chen.develop.zhihudaily.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.chen.common.App.BaseActivity;
import com.chen.common.DB.CacheDbHelper;
import com.chen.develop.zhihudaily.Adapter.MainNewsItemAdapter;
import com.chen.develop.zhihudaily.Config.Interface;
import com.chen.develop.zhihudaily.Fragment.MainFragment;
import com.chen.develop.zhihudaily.Fragment.MenuFragment;
import com.chen.develop.zhihudaily.Fragment.NewsFragment;
import com.chen.develop.zhihudaily.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import butterknife.Bind;

public class MainActivity extends BaseActivity {
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
//    @Bind(R.id.menu_fragment)
     MenuFragment menu_fragment;
    @Bind(R.id.fl_content)
    FrameLayout fl_context;
    @Bind(R.id.adcontainer)
    RelativeLayout adcontainer;

    private String curId = "latest";
    private String date;
    private boolean isLoading = false;
    private Handler handler = new Handler();
    private boolean isLight = true;
    private MainNewsItemAdapter mAdapter;
    private Toolbar toolbar;
    private String url = Interface.LATESTNEWS;
    private SharedPreferences sp;
    private static CacheDbHelper dbHelper;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {
        dbHelper = CacheDbHelper.getHelper(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        isLight = sp.getBoolean("isLight", true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setStatusBarColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (curId.equals("latest")) {
                        ((MainFragment) getSupportFragmentManager().findFragmentByTag("latest")).ScrollTop();
                    } else {
                        ((NewsFragment) getSupportFragmentManager().findFragmentByTag("news")).ScrollTop();
                    }
                } catch (Exception e) {

                }

            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).
                replace(R.id.fl_content, new MainFragment(), "latest").
                commit();
        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ((MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment)).getCityInfo();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void loadData() {
        getDeviceInfo(this);
    }


    public void setToolbarTitle(String text) {
        toolbar.setTitle(text);
        setSupportActionBar(toolbar);
    }

    public boolean isLight() {
        return isLight;
    }

    public void setCurId(String id) {
        curId = id;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.getItem(0).setTitle(sp.getBoolean("isLight", true) ? "夜间模式" : "日间模式");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_islight) {
            isLight = !isLight;
            item.setTitle(isLight ? "夜间模式" : "日间模式");
            toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
            setStatusBarColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
            if (curId.equals("latest")) {
                ((MainFragment) getSupportFragmentManager().findFragmentByTag("latest")).updateTheme();
            } else {
                ((NewsFragment) getSupportFragmentManager().findFragmentByTag("news")).updateTheme();
            }
            ((MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment)).updateTheme();
            sp.edit().putBoolean("isLight", isLight).apply();
        } else if (item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerVisible(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);//关闭抽屉
            } else {
                drawer.openDrawer(GravityCompat.START);//打开抽屉

            }
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeMenu() {
        drawer.closeDrawers();
    }

    public static CacheDbHelper getCacheDbHelper() {
        return dbHelper;
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
