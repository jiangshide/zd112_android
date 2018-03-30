package com.android.zd112.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.android.zd112.R;
import com.android.zd112.ui.activity.BaseActivity;
import com.android.zd112.utils.Constant;
import com.android.zd112.utils.LocationUtils;
import com.android.zd112.utils.LogUtils;
import com.android.zd112.utils.PermissionUtils;

/**
 * Created by etongdai on 2018/1/22.
 */

public class Splash extends BaseActivity {

    private ImageView splash;
    private int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void initView(Bundle savedInstanceState) {
        LogUtils.e("------------initView");
        setView(R.layout.splash);
        splash = viewId(R.id.splash);
    }

    @Override
    protected void setListener() {
        LogUtils.e("--------------listener");
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        LogUtils.e("-------------processLogic");
        splash.setImageResource(R.drawable.test_image);
        PermissionUtils.checkAndRequestMorePermissions(this, Constant.PERMISSIONS, PERMISSION_ACCESS_COARSE_LOCATION, new PermissionUtils.PermissionRequestSuccessCallBack() {
            @Override
            public void onHasPermission() {
                goHome();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        goHome();
    }

    public void goHome() {
        LocationUtils.INSTANCE.initLocation(getBaseContext()).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Splash.this.startActivity(new Intent(Splash.this, MainActivity.class));
                Splash.this.finish();
            }
        }, 2000);

    }
}
