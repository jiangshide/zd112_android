package com.android.zd112.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by etongdai on 2018/1/22.
 */

public class BaseData implements Serializable {

    @SerializedName("code")
    public int code;

    @SerializedName("date")
    public long date;

    @SerializedName("version")
    public String version;

    @SerializedName("msg")
    public String msg;

}
