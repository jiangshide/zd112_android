package com.android.zd112.ui.view.picker.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by etongdai on 2018/3/9.
 */

public class Province extends Area implements LinkageFirst<City>{
    private List<City> cities = new ArrayList<>();

    public Province() {
        super();
    }

    public Province(String areaName) {
        super(areaName);
    }

    public Province(String areaId, String areaName) {
        super(areaId, areaName);
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public List<City> getSeconds() {
        return cities;
    }
}
