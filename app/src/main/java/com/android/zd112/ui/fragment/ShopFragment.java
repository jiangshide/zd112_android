package com.android.zd112.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.zd112.R;
import com.bumptech.glide.Glide;

/**
 * Created by etongdai on 2017/11/20.
 */

public class FindFragment extends BaseFragment {

    private ImageView gifTest;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.tab_fragment_find);
        gifTest = viewId(R.id.gifTest);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        Glide.with(this).load("http://10.20.7.21:8083/static/gif/test.gif").into(gifTest);
    }
}
