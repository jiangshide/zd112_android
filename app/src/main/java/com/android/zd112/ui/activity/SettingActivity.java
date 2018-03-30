package com.android.zd112.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.zd112.R;

public class SettingActivity extends BaseActivity {

    private RelativeLayout settingAppBtn;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.activity_setting);
        topView("设置");
        settingAppBtn = viewId(R.id.settingAppBtn);
    }

    @Override
    protected void setListener() {
        settingAppBtn.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.settingAppBtn:
                startActivity(new Intent(this, InstallAppActivity.class));
                break;
        }
    }
}
