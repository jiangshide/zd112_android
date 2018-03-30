package com.android.zd112.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.zd112.R;
import com.android.zd112.view.BannerView;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;

/**
 * Created by etongdai on 2017/11/17.
 */

public class HomeFragment extends BaseFragment {

    private View view;
    private BannerView homeBanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view = inflater.inflate(R.layout.tab_fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeBanner = (BannerView) viewId(R.id.homeBanner);
//        Glide.with(this).load("http://www.nowpre.etongdai.org/u/cms/www/201712/08134958s4te.jpg").transition(new GenericTransitionOptions<Drawable>()).into(img);
    }
}
