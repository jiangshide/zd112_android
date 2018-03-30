package com.android.zd112;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.android.zd112.data.net.NetApi;
import com.android.zd112.utils.Constant;
import com.android.zd112.utils.ErrorLog;
import com.android.zd112.utils.LogUtils;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by etongdai on 2018/1/23.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks,ErrorLog.ExeceptionHandler{

    private static App mInstance;
    private NetApi mNetApi;
    private int mCount;

    @Override
    public void onCreate() {
        super.onCreate();
        ErrorLog.install(this);
        registerActivityLifecycleCallbacks(this);
        mInstance = this;
        initNetApi();
    }

    public static App getInstance() {
        return mInstance;
    }

    public void initNetApi() {
        mNetApi = new Retrofit.Builder().baseUrl(Constant.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(NetApi.class);
    }

    public NetApi getmNetApi() {
        return mNetApi;
    }

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, R.color.colorWhite);
                return new BezierCircleHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        mCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (mCount > 0) {
            mCount--;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public boolean isBackground() {
        return mCount <= 0;
    }

    @Override
    public void handlerException(final Thread thread, final Throwable throwable) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    throwable.printStackTrace();
                    LogUtils.e("exception~thread:", thread, " | throwable:", throwable);
                } catch (Throwable t) {
                    throwable.printStackTrace();
                }
            }
        });
    }
}
