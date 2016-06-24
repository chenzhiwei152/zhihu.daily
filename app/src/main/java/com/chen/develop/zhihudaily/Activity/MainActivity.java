package com.chen.develop.zhihudaily.Activity;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.chen.develop.zhihudaily.Adapter.MainNewsItemAdapter;
import com.chen.develop.zhihudaily.App.BaseActivity;
import com.chen.develop.zhihudaily.Config.Interface;
import com.chen.develop.zhihudaily.DB.CacheDbHelper;
import com.chen.develop.zhihudaily.Fragment.MainFragment;
import com.chen.develop.zhihudaily.Fragment.MenuFragment;
import com.chen.develop.zhihudaily.Fragment.NewsFragment;
import com.chen.develop.zhihudaily.R;
import com.chen.develop.zhihudaily.Utils.LogUtils;
import com.chen.develop.zhihudaily.View.Kanner;

import butterknife.Bind;

public class MainActivity extends BaseActivity {
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    //    @Bind(R.id.menu_fragment)
    private MenuFragment menu_fragment;
    @Bind(R.id.fl_content)
    FrameLayout fl_context;
    private String curId = "latest";
    private Kanner kanner;
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
        dbHelper = new CacheDbHelper(this, 1);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        isLight = sp.getBoolean("isLight", true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setStatusBarColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                LogUtils.e("onDrawerOpened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                LogUtils.e("onDrawerClosed");
            }
        };
        drawer_layout.setDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

//       drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        toggle.setDrawerIndicatorEnabled(false);
//        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//
//                    drawer.closeDrawer(GravityCompat.START);
//                } else {
//                    drawer.openDrawer(GravityCompat.START);
//                }
//                LogUtils.e("菜单点击事件");
//            }
//        });
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).
                replace(R.id.fl_content, new MainFragment(), "latest").
                commit();

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void loadData() {


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
            item.setTitle(isLight?"夜间模式":"日间模式");
            toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
            setStatusBarColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
            if (curId.equals("latest")) {
                ((MainFragment) getSupportFragmentManager().findFragmentByTag("latest")).updateTheme();
            } else {
                ((NewsFragment) getSupportFragmentManager().findFragmentByTag("news")).updateTheme();
            }
            ((MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment)).updateTheme();
            sp.edit().putBoolean("isLight", isLight).apply();
        }else if (item.getItemId() == R.id.home){
            if (drawer.isDrawerVisible(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);//关闭抽屉
            } else {
                drawer.openDrawer(GravityCompat.START);//打开抽屉
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeMenu() {
        drawer.closeDrawers();
    }
    public static CacheDbHelper getCacheDbHelper() {
        return dbHelper;
    }
}
