package com.chen.pinyin;

import android.text.TextUtils;

import com.chen.develop.zhihudaily.Bean.CityListBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator<CityListBean> {

	public int compare(CityListBean o1, CityListBean o2) {//@--# 升序
		if (o1.getFirstLetter().equals("@") || o2.getFirstLetter().equals("#")) {
			return -1;
		} else if (o1.getFirstLetter().equals("#")
				|| o2.getFirstLetter().equals("@")) {
			return 1;
		} else {
			if (!TextUtils.isEmpty(o1.getFirstLetter()) && !TextUtils.isEmpty(o2.getFirstLetter())){
				return o1.getFirstLetter().compareTo(o2.getFirstLetter());
			}else {
				return o1.getFirstLetter().compareTo(o2.getFirstLetter());
			}
		}
	}

}
