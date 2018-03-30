package com.android.zd112.ui.view.picker.entity;

/**
 * Created by etongdai on 2018/3/9.
 */

public class County extends Area implements LinkageThird{
    private String cityId;

    public County() {
        super();
    }

    public County(String areaName) {
        super(areaName);
    }

    public County(String areaId, String areaName) {
        super(areaId, areaName);
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
