package com.android.zd112.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.zd112.R;

/**
 * Created by etongdai on 2017/11/17.
 */

public class MineFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view = inflater.inflate(R.layout.tab_fragment_mine, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView headIconImg = view.findViewById(R.id.headIconImg);
//        Glide.with(this).load("http://10.20.7.21:8089/static/app/qrImgUrl/236a4c32aaccbb1b9_qr.png").transition(new GenericTransitionOptions<Drawable>()).into(headIconImg);
    }
}
