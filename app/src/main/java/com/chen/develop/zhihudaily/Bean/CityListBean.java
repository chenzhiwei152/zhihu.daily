package com.chen.develop.zhihudaily.Bean;

/**
 * Created by chen.zhiwei on 2016-7-4.
 */
public class CityListBean {


    /**
     * id : CN101010100
     * cityEn : beijing
     * cityZh : 北京
     * countryCode : CN
     * countryEn : China
     * countryZh : 中国
     * provinceEn : beijing
     * provinceZh : 北京
     * leaderEn : beijing
     * leaderZh : 北京
     * lat : 39.904989
     * lon : 116.405285
     */

    private String id;
    private String cityEn;
    private String cityZh;
    private String countryCode;
    private String countryEn;
    private String countryZh;
    private String provinceEn;
    private String provinceZh;
    private String leaderEn;
    private String leaderZh;
    private String lat;
    private String lon;
    public String getFirstLetter() {
        return FirstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        FirstLetter = firstLetter;
    }

    private String FirstLetter;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityEn() {
        return cityEn;
    }

    public void setCityEn(String cityEn) {
        this.cityEn = cityEn;
    }

    public String getCityZh() {
        return cityZh;
    }

    public void setCityZh(String cityZh) {
        this.cityZh = cityZh;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getCountryZh() {
        return countryZh;
    }

    public void setCountryZh(String countryZh) {
        this.countryZh = countryZh;
    }

    public String getProvinceEn() {
        return provinceEn;
    }

    public void setProvinceEn(String provinceEn) {
        this.provinceEn = provinceEn;
    }

    public String getProvinceZh() {
        return provinceZh;
    }

    public void setProvinceZh(String provinceZh) {
        this.provinceZh = provinceZh;
    }

    public String getLeaderEn() {
        return leaderEn;
    }

    public void setLeaderEn(String leaderEn) {
        this.leaderEn = leaderEn;
    }

    public String getLeaderZh() {
        return leaderZh;
    }

    public void setLeaderZh(String leaderZh) {
        this.leaderZh = leaderZh;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
