package com.android.zd112.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.android.zd112.R;
import com.android.zd112.data.HomeData;
import com.android.zd112.data.HomeListData;
import com.android.zd112.data.HomeListItemData;
import com.android.zd112.data.HomeToolsDdata;
import com.android.zd112.ui.activity.AddressActivity;
import com.android.zd112.ui.activity.ConstellationActivity;
import com.android.zd112.ui.activity.MotionActivity;
import com.android.zd112.ui.activity.NationActivity;
import com.android.zd112.ui.activity.QRScanActivity;
import com.android.zd112.ui.activity.SearchActivity;
import com.android.zd112.ui.activity.TorchActivity;
import com.android.zd112.ui.activity.ZodiacActivity;
import com.android.zd112.ui.adapter.CommAdapter;
import com.android.zd112.ui.view.BannerView;
import com.android.zd112.ui.view.MyGridView;
import com.android.zd112.utils.BadgeUtils;
import com.android.zd112.utils.Constant;
import com.android.zd112.utils.LocationUtils;
import com.android.zd112.utils.LogUtils;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by etongdai on 2017/11/17.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, Callback<HomeData>, OnRefreshListener, OnLoadMoreListener {


    private RefreshLayout refreshLayout;
    private ImageView homeWeatherImg, homeQRImg;
    private TextView homeWeatherTxt, homeLocationTxt, homeSearchTxt, marqueeTxt;
    private BannerView homeBanner;

    private MyGridView homeToolList;
    private ListView homeList;

    private CommAdapter<HomeListData> homeListDataCommAdapter;
    private List<HomeListData> listData;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.tab_fragment_home);
        refreshLayout = viewId(R.id.refreshLayout);

        homeList = viewId(R.id.homeList);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.tab_fragment_home_top, null);
        homeList.addHeaderView(headView);

        homeWeatherImg = viewId(R.id.homeWeatherImg);
        homeQRImg = viewId(R.id.homeQRImg);
        homeWeatherTxt = viewId(R.id.homeWeatherTxt);
        homeLocationTxt = viewId(R.id.homeLocationTxt);
        homeSearchTxt = viewId(R.id.homeSearchTxt);

        homeBanner = viewId(R.id.homeBanner);
        marqueeTxt = viewId(R.id.marqueeTxt);
        homeToolList = viewId(R.id.homeToolList);

        BadgeUtils.setBadgeCount(getActivity().getApplicationContext(), 10, R.mipmap.ic_launcher);
//        Glide.with(this).load("http://www.nowpre.etongdai.org/u/cms/www/201712/08134958s4te.jpg").transition(new GenericTransitionOptions<Drawable>()).into(img);
    }

    @Override
    protected void setListener() {
        refreshLayout.setOnRefreshListener(this);
        homeLocationTxt.setOnClickListener(this);
        homeSearchTxt.setOnClickListener(this);
        homeQRImg.setOnClickListener(this);
        homeBanner.setAutoPlay(true);
        homeBanner.setOnBannerItemClickListener(new BannerView.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Map<String, Object> parmas = new HashMap<>();
                parmas.put("name", "jankey");
                parmas.put("nice", "this is the naice");
                mNetApi.getHomeData(parmas).enqueue(new Callback<HomeData>() {
                    @Override
                    public void onResponse(Call<HomeData> call, Response<HomeData> response) {

                    }

                    @Override
                    public void onFailure(Call<HomeData> call, Throwable t) {

                    }
                });
            }
        });
        LocationUtils.INSTANCE.setLocationListener(new LocationUtils.AmapLocationListener() {
            @Override
            public void onLocation(AMapLocation aMapLocation, Location location) {
                if (aMapLocation != null) {
                    String city = aMapLocation.getCity();
                    if (!TextUtils.isEmpty(city)) {
                        homeLocationTxt.setText(city);
                    }
                }
            }
        });
        homeToolList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), homeToolsDdataList.get(position)._class));
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mNetApi.getHomeData().enqueue(this);
        setBannerUrl();
        setList();
        initTools();
        scrollTxt(marqueeTxt, getString(R.string.marqueeTxt));
    }

    private List<HomeToolsDdata> homeToolsDdataList;

    private void initTools() {
        Class[] toolClass = {NationActivity.class, ZodiacActivity.class, ConstellationActivity.class, TorchActivity.class, MotionActivity.class};
        String name[] = getResStringArr(R.array.tab_main_home);
        int icon[] = {R.drawable.nation, R.drawable.zodiac, R.drawable.constellation, R.drawable.torch, R.drawable.motion};
        homeToolsDdataList = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            HomeToolsDdata homeToolsDdata = new HomeToolsDdata();
            homeToolsDdata._class = toolClass[i];
            homeToolsDdata.name = name[i];
            homeToolsDdata.icon = icon[i];
            homeToolsDdataList.add(homeToolsDdata);
        }

        homeToolList.setAdapter(new CommAdapter<HomeToolsDdata>(getActivity(), homeToolsDdataList, R.layout.tab_fragment_home_tools) {
            @Override
            protected void convertView(int position, View item, HomeToolsDdata homeToolsDdata) {
                ImageView homeToolsIcon = get(item, R.id.homeToolsIcon);
                TextView homeToolsTxt = get(item, R.id.homeToolsTxt);
                homeToolsIcon.setImageResource(homeToolsDdata.icon);
                homeToolsTxt.setText(homeToolsDdata.name);
            }
        });
    }

    @Override
    public void onResponse(Call<HomeData> call, Response<HomeData> response) {
        LogUtils.e("-----------onResponse");
        HomeData homeData = response.body();
        LogUtils.e("----------homeData:", homeData.version);
        setBannerUrl();
        refreshLayout.finishRefresh(true);
    }

    public void setBannerUrl() {
        List<String> urlList = new ArrayList<>();
        for (String url : Constant.bannerUrl) {
            urlList.add(url);
        }

        homeBanner.setViewUrls(urlList);
    }

    private void setList() {
        listData = new ArrayList<>();
        String[] titles = {"热门活动", "猜你喜欢", "每日更新", "学府", "民族", "音乐", "视频", "原创", "题库"};

        for (String s : titles) {
            HomeListData homeListData = new HomeListData();
            homeListData.title = s;
            homeListData.arrs = new ArrayList<>();
            for (String url : Constant.bannerUrl) {
                HomeListItemData homeListItemData = new HomeListItemData();
                homeListItemData.url = url;
                homeListData.arrs.add(homeListItemData);
            }
            listData.add(homeListData);
        }

        homeList.setAdapter(homeListDataCommAdapter = new CommAdapter<HomeListData>(getContext(), listData, R.layout.tab_fragment_home_list) {
            @Override
            protected void convertView(int position, View item, final HomeListData homeListData) {
                TextView homeListTitle = get(item, R.id.homeListTitle);
                MyGridView homeListGrid = get(item, R.id.homeListGrid);
                homeListTitle.setText(homeListData.title);
                homeListGrid.setAdapter(new CommAdapter<HomeListItemData>(getContext(), homeListData.arrs, R.layout.tab_fragment_home_list_item) {
                    @Override
                    protected void convertView(int position, View item, HomeListItemData homeListItemData) {
                        ImageView homeListItemIcon = get(item, R.id.homeListItemIcon);
                        Glide.with(getActivity()).load(homeListItemData.url).transition(new GenericTransitionOptions<Drawable>()).into(homeListItemIcon);
                    }
                });
            }
        });
    }

    @Override
    public void onFailure(Call<HomeData> call, Throwable t) {
        LogUtils.e("------------onFailure");
        refreshLayout.finishRefresh(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeWeatherImg:

                break;
            case R.id.homeLocationTxt:
                startActivity(new Intent(getActivity(), AddressActivity.class));
                break;
            case R.id.homeSearchTxt:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.homeQRImg:
                startActivity(new Intent(getActivity(), QRScanActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mNetApi.getHomeData().enqueue(this);
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore();
    }
}
