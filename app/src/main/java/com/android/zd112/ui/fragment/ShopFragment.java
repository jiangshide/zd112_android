package com.android.zd112.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.zd112.R;
import com.android.zd112.data.ShopData;
import com.android.zd112.data.ShopMenuData;
import com.android.zd112.ui.adapter.CommAdapter;
import com.android.zd112.ui.view.CircleImageView;
import com.android.zd112.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by etongdai on 2017/11/20.
 */

public class ShopFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ImageView shopTopLeftIcon;
    private EditText shopTopLeftEdit;
    private CircleImageView shopMenuLeftImg;
    private TextView shopMenuLeftTxt;
    private ListView shopMenuLeftList, shopListView;
    private LinearLayout shopMenuLeftL;
    private final int MENU_HIDD = 0;
    private final int MENU_LIST = 1;
    private final int MENU_BTN = 2;

    private List<ShopMenuData> list;
    private CountDownTimer countDownTimer;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.tab_fragment_shop);
        shopTopLeftIcon = viewId(R.id.shopTopLeftIcon);
        shopTopLeftEdit = viewId(R.id.shopTopLeftEdit);
        shopMenuLeftTxt = viewId(R.id.shopMenuLeftTxt);
        shopMenuLeftList = viewId(R.id.shopMenuLeftList);
        shopListView = viewId(R.id.shopListView);
        shopMenuLeftImg = viewId(R.id.shopMenuLeftImg);
        shopMenuLeftL = viewId(R.id.shopMenuLeftL);
    }

    private void showMenu(int show) {
        switch (show) {
            case MENU_HIDD:
                shopMenuLeftList.setVisibility(View.GONE);
                shopMenuLeftL.setVisibility(View.GONE);
                break;
            case MENU_BTN:
                shopMenuLeftList.setVisibility(View.GONE);
                shopMenuLeftL.setVisibility(View.VISIBLE);
                break;
            case MENU_LIST:
                shopMenuLeftList.setVisibility(View.VISIBLE);
                shopMenuLeftL.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void setListener() {
        shopTopLeftIcon.setOnClickListener(this);
        shopMenuLeftL.setOnClickListener(this);
        shopMenuLeftList.setOnItemClickListener(this);
        shopListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                showMenu(MENU_HIDD);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                countDownTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        showMenu(MENU_BTN);
                    }
                }.start();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        String[] name = {"阿昌族", "白族", "保安族", "布朗族", "布依族", "藏族", "朝鲜族", "达翰尔族", "傣族", "昂德族", "东乡族", "侗族", "独龙族", "俄罗斯族", "鄂伦春族", "鄂温克族", "高山族", "哈尼族", "哈萨克族", "汉族", "赫哲族", "回族", "基诺族", "京族",
                "景颇族", "柯尔克孜族", "拉祜族", "黎族", "傈僳族", "珞巴族", "满族", "毛南族", "门巴族", "蒙古族", "苗族", "仫佬族", "纳西族", "怒族", "普米族", "羌族", "撒拉族", "畲族", "水族", "塔吉克族", "塔塔尔族", "土家族", "图族", "佤族", "维吾尔族", "乌孜别克族", "锡伯族", "瑶族", "彝族", "仡佬族", "裕固族", "壮族"};
        list = new ArrayList<>();
        for (String s : name) {
            ShopMenuData shopMenuData = new ShopMenuData();
            shopMenuData.name = s;
            shopMenuData.icon = R.mipmap.ic_launcher;
            list.add(shopMenuData);
        }
        shopMenuLeftList.setAdapter(new CommAdapter<ShopMenuData>(getActivity(), list, R.layout.activity_nation_item) {
            @Override
            protected void convertView(int position, View item, ShopMenuData shopMenuData) {
                CircleImageView nationListViewItemImg = get(item, R.id.nationListViewItemImg);
                nationListViewItemImg.setImageResource(shopMenuData.icon);
                TextView nationListViewItemTxt = get(item, R.id.nationListViewItemTxt);
                nationListViewItemTxt.setText(shopMenuData.name);
                LogUtils.e("------------name:", shopMenuData.name);
            }
        });

        List<ShopData> shopList = new ArrayList<>();
        for (String s : name) {
            ShopData shopData = new ShopData();
            shopData.name = s;
            shopList.add(shopData);
        }
        shopListView.setAdapter(new CommAdapter<ShopData>(getActivity(), shopList, R.layout.tab_fragment_shop_item) {
            @Override
            protected void convertView(int position, View item, ShopData shopData) {
                TextView shopListViewItemTxt = get(item, R.id.shopListViewItemTxt);
                shopListViewItemTxt.setText(shopData.name);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        shopMenuLeftTxt.setText(list.get(position).name);
        showMenu(MENU_BTN);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.shopMenuLeftL:
                showMenu(MENU_LIST);
                break;
        }
    }
}
