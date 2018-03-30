package com.android.zd112.ui.view.picker.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by etongdai on 2018/3/9.
 */

public class City extends Area implements LinkageSecond<County>{
    private String provinceId;
    private List<County> counties = new ArrayList<>();

    public City() {
        super();
    }

    public City(String areaName) {
        super(areaName);
    }

    public City(String areaId, String areaName) {
        super(areaId, areaName);
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public List<County> getCounties() {
        return counties;
    }

    public void setCounties(List<County> counties) {
        this.counties = counties;
    }

    @Override
    public List<County> getThirds() {
        return counties;
    }
}
