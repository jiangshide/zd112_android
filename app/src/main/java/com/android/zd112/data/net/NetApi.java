package com.android.zd112.data.net;

import com.android.zd112.data.BaseData;
import com.android.zd112.data.FileData;
import com.android.zd112.data.HomeData;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by etongdai on 2018/1/23.
 */

public interface NetApi {

    @GET("/api/home")
    Call<HomeData> getHomeData();

    @Multipart
    @POST("/api/home")
    Call<HomeData> getHomeData(@PartMap Map<String,Object> map);

    @Multipart
    @POST("/api/upload")
    Call<FileData> upload(@Part MultipartBody.Part file);

    @Multipart
    @POST("/api/upload/multipart")
    Call<FileData> upload(@PartMap Map<String, RequestBody> files);

}
