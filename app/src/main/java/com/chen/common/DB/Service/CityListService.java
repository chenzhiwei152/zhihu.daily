package com.chen.common.DB.Service;

import android.content.Context;

import com.chen.common.DB.CacheDbHelper;
import com.chen.common.Utils.LogUtils;
import com.chen.develop.zhihudaily.Bean.CityListBean;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by chenzhiwei 2016/7/4.
 * <p/>
 * 城市列表服务
 */
public class CityListService {
    public static CityListService instance;
    Context context;
    CacheDbHelper helper;
    Dao dao;

    public CityListService(Context context) throws SQLException {
        this.context = context;
        helper = CacheDbHelper.getHelper(context);
        dao = helper.getDao(CityListBean.class);
        LogUtils.e("获取dao对象");
    }

    public static CityListService getInstance(Context context) {
        if (null == instance) {
            try {
                instance = new CityListService(context);
            } catch (SQLException sQLException) {
                LogUtils.e(sQLException.getMessage());
            }

        }
        return instance;
    }

//    /**
//     * @param
//     */
//    public void save(CityListBean userInfo) {
//        try {
//            List<CityListBean> list = dao.queryForAll();
//            if (null != list  && list.size() > 0) {
//                dao.delete(list);
//            }
//            dao.create(userInfo);
//        } catch (SQLException e) {
//            LogUtils.e(e.getMessage());
//        }
//    }
    /**
     * @param list
     * @throws SQLException 保存 城市信息
     */
    public void saveOrUpdateCityList(List<CityListBean> list) throws Exception {

        if (null != list && list.size() > 0) {
//            deleteAllCities();
//            try{
//
//                dao.executeRaw(
//                        "ALTER TABLE `city` ADD COLUMN prov  DEFAULT null;");
//            }catch (Exception e){
//            }

            for (CityListBean city : list) {
                if (null != city) {
                    if (dao==null){
                        LogUtils.e("获取dao对象失败");
                    }
                    dao.callBatchTasks(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return null;
                        }
                    });
                    dao.create(city);
                }

            }
        }
    }
//    /**
//     * @param list
//     * @throws SQLException 保存 城市信息
//     */
//    public void saveOrUpdateCityList(CityListBean list) throws SQLException {
//
//        if (null != list && list.getCity_info().size() > 0) {
////            deleteAllCities();
//            try{
//
//                dao.executeRaw(
//                        "ALTER TABLE `city` ADD COLUMN cityNameQP  DEFAULT null;");
//            }catch (Exception e){
//            }
//
//            for (CityInfoBean city : list.getCity_info()) {
//                if (null != city) {
//                    dao.create(city);
//                }
//
//            }
//        }
//    }
    /**
     * @return
     * @throws SQLException 获取 全部 城市
     */
    public List<CityListBean> getAllCities() throws SQLException {
        return dao.queryForAll();
    }



}