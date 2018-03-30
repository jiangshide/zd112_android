package com.android.zd112.data;

import android.graphics.drawable.Drawable;

/**
 * Created by etongdai on 2018/1/23.
 */

public class AppInfoData {
    public String name;
    public String pkgName;
    public String channel;
    public int localSign;
    public Drawable icon;
    public String version;
    public int code;
    public int targetSdkVersion;
    public int minSdkVersion;

    @Override
    public String toString() {
        return "name:" + name + " | pkgName:" + pkgName +" | channel:"+channel+" | localSign:"+localSign+ " | icon:" + icon + " | version:" + version + " | code:" + code + " | targetSdkVersion:" + targetSdkVersion + " | minSdkVersion:" + minSdkVersion;
    }
}
