package com.android.zd112.data.net;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.android.zd112.App;
import com.android.zd112.R;
import com.android.zd112.data.HomeData;
import com.android.zd112.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by etongdai on 2018/1/23.
 */

public class NewApiData {
    public static View getHomeView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        App.getInstance().getmNetApi().getHomeData().enqueue(new Callback<HomeData>() {
            @Override
            public void onResponse(Call<HomeData> call, Response<HomeData> response) {
                HomeData homeData = response.body();
                LogUtils.e("name:", homeData.version);
            }

            @Override
            public void onFailure(Call<HomeData> call, Throwable t) {
                LogUtils.e("----t:", t);
            }
        });
        return view;
    }
}
