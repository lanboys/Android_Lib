package com.juzhongke.jzkmarketing.comm.utils.picker.bean;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * @author 蓝兵
 * @email lan_bing2013@163.com
 * @time 2017/5/26  21:39
 */
public class ChainAreaBean {

    private ArrayList<ProvinceBean> china;

    public static ChainAreaBean objectFromData(String str) {

        return new Gson().fromJson(str, ChainAreaBean.class);
    }

    public ArrayList<ProvinceBean> getChina() {
        return china;
    }

    public void setChina(ArrayList<ProvinceBean> china) {
        this.china = china;
    }

}
