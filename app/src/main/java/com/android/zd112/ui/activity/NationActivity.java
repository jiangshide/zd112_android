package com.android.zd112.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.zd112.R;
import com.android.zd112.ui.view.picker.AddressPickTask;
import com.android.zd112.ui.view.picker.entity.City;
import com.android.zd112.ui.view.picker.entity.County;
import com.android.zd112.ui.view.picker.entity.Province;

public class NationActivity extends BaseActivity {

    private Button nationAreaBtn;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.activity_nation);
        topView("民族");
        nationAreaBtn = viewId(R.id.nationAreaBtn);
    }

    @Override
    protected void setListener() {
        nationAreaBtn.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                show("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    show(province.getAreaName() + city.getAreaName());
                } else {
                    show(province.getAreaName() + city.getAreaName() + county.getAreaName());
                }
            }
        });
        task.execute("贵州", "毕节", "纳雍");
    }
}
