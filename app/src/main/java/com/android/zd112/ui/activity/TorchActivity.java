package com.android.zd112.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.android.zd112.R;
import com.android.zd112.utils.CameraUtils;

public class TorchActivity extends BaseActivity {

    private Button torchBtn;
    private boolean isOpen;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.activity_torch);
        topView("电筒");
        torchBtn = viewId(R.id.torchBtn);
    }

    @Override
    protected void setListener() {
        torchBtn.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        isOpen = isOpen ? true : false;
        CameraUtils.light(this, isOpen);
    }
}
