package com.android.zd112

import android.app.Application
import com.android.zd112.utils.LogUtils

/**
 * Created by etongdai on 2017/11/17.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.e("-------------init")
    }
}