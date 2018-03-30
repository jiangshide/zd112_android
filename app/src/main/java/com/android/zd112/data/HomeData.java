package com.android.zd112.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by etongdai on 2018/1/23.
 */

public class HomeData extends BaseData {

    @SerializedName("res")
    public Res res;

    public class Res implements Serializable {
        @SerializedName("banner")
        public List<Banner> banners;
        @SerializedName("nation")
        public List<Nation> nations;
    }

    public class Banner implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("link")
        public String link;
        @SerializedName("icon")
        public String icon;
        @SerializedName("descript")
        public String descript;
        @SerializedName("createId")
        public int createId;
        @SerializedName("UpdateId")
        public int UpdateId;
        @SerializedName("createTime")
        public long createTime;
        @SerializedName("updateTime")
        public long updateTime;
    }

    public class Nation implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("icon")
        public String icon;
        @SerializedName("createId")
        public int createId;
        @SerializedName("updateId")
        public int updateId;
        @SerializedName("createTime")
        public long createTime;
        @SerializedName("updateTime")
        public long updateTime;
    }
}
