package com.chen.common.NetUtils;

import com.alibaba.fastjson.JSON;
import com.chen.develop.zhihudaily.Bean.BaseBean;
import com.chen.common.Utils.LogUtils;

/**
 * Created by chen.zhiwei @2016/6/13
 */
public class ParserUtils {

    public static BaseBean parse(String content, Class<? extends BaseBean> clazz) {
        BaseBean result = null;
        result = JSON.parseObject(content, clazz);
        return result == null ? new BaseBean() : result;
    }


    public static <T> T parser(String content, Class<T> clazz) {
        try {
            return (T) JSON.parseObject(content, clazz);
        } catch (Exception e) {
            LogUtils.e("ParserUtils", "解析异常");
        }
        return null;
    }


    /**
     * 解析jsonArray
     *
     * @param content
     * @param clazz
     * @return
     */
    public static <T> T parseArray(String content, Class<T> clazz) {
        return (T) JSON.parseArray(content, clazz);
    }

}
