package com.chen.common.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.chen.common.Utils.LogUtils;
import com.chen.develop.zhihudaily.Bean.CityListBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wwjun.wang on 2015/8/19.
 */
public class CacheDbHelper extends OrmLiteSqliteOpenHelper {
    private static CacheDbHelper instance;
    private Context context;
    private static int Vsersion = 1;
    private Map<String, Dao> daos = new HashMap<String, Dao>();

    public CacheDbHelper(Context context, int version) {
        super(context, "cache.db", null, version);
        this.context = context;
        this.Vsersion = version;
//        dbHelper=new CacheDbHelper(context,version);
    }

    //    public static CacheDbHelper getCacheDbHelper() {
//
//        return dbHelper;
//    }
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        db.execSQL("create table if not exists CacheList (id INTEGER primary key autoincrement,date INTEGER unique,json text)");
//        db.execSQL("create table if not exists CityList (_id INTEGER primary key autoincrement,CityTag text,city text)");
        try {
            TableUtils.clearTable(connectionSource, CityListBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LogUtils.e("数据库表单创建！");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.clearTable(connectionSource, CityListBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(sqLiteDatabase, connectionSource);
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized CacheDbHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (CacheDbHelper.class) {
                if (instance == null)
                    instance = new CacheDbHelper(context, Vsersion);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}
