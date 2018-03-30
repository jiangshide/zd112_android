package com.android.zd112.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.android.zd112.R;
import com.android.zd112.ui.activity.BaseActivity;
import com.android.zd112.ui.adapter.TabFragmentAdapter;
import com.android.zd112.ui.fragment.CircleFragment;
import com.android.zd112.ui.fragment.HomeFragment;
import com.android.zd112.ui.fragment.MineFragment;
import com.android.zd112.ui.fragment.PublishFragment;
import com.android.zd112.ui.fragment.ShopFragment;
import com.android.zd112.ui.view.DialogView;
import com.android.zd112.ui.view.TabContainerView;
import com.android.zd112.ui.view.UnLockView;

/**
 * Created by etongdai on 2018/1/22.
 */

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mMenusView;
    private TabContainerView mTabLayout;

    private int ICONS_RES[][] = {{R.mipmap.tab_home, R.mipmap.tab_home_selected}, {
            R.mipmap.tab_circle,
            R.mipmap.tab_circle_selected}, {
            R.mipmap.tab_publish,
            R.mipmap.tab_publish_selected}, {
            R.mipmap.tab_shop,
            R.mipmap.tab_shop_selected}, {
            R.mipmap.tab_mine,
            R.mipmap.tab_mine_selected}};

    private Fragment[] fragments = {
            new HomeFragment(),
            new CircleFragment(),
            new PublishFragment(),
            new ShopFragment(),
            new MineFragment()
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.activity_main);
        mMenusView = viewId(R.id.tab_pager);
        mTabLayout = viewId(R.id.ll_tab_container);
        showDialog(this, R.style.BottomDialog, R.layout.dialog_psw).setGravity(Gravity.BOTTOM).setAnim(R.style.BottomDialog_Animation).setOutside(false).show();
    }

    @Override
    protected void setListener() {
        mTabLayout.setOnPageChangeListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        TabFragmentAdapter mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragments);
        mMenusView.setOffscreenPageLimit(1);
        mMenusView.setAdapter(mAdapter);
        mTabLayout.initContainer(getResStringArr(R.array.tab_main_title), ICONS_RES, getResArr(R.array.tab_main_colors), true);
        int width = getResources().getDimensionPixelSize(R.dimen.tab_icon_width);
        int height = getResources().getDimensionPixelSize(R.dimen.tab_icon_height);
        mTabLayout.setContainerLayout(R.layout.tab_container_view, R.id.iv_tab_icon, R.id.tv_tab_text, width, height);

        mTabLayout.setViewPager(mMenusView);

        mMenusView.setCurrentItem(getIntent().getIntExtra("tab", 0));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (int index = 0, len = fragments.length; index < len; index++) {
            fragments[index].onHiddenChanged(index != position);
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        DialogView dialogView = new DialogView(this, R.style.DialogTheme).setListener(new DialogView.DialogOnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onDialogClick(Button surce, Button cancel) {
                finishAffinity();
            }
        });
        dialogView.setOutsideClose(false).show();
//        super.onBackPressed();
    }


    @Override
    public void onView(View view) {
        super.onView(view);
        UnLockView unLockView = view.findViewById(R.id.unLockView);
        unLockView.setOnGestureDoneListener(new UnLockView.OnGestureDoneListener() {
            @Override
            public boolean isValidGesture(int pointCount) {
                return true;
            }

            @Override
            public void inputOK(UnLockView view, String psw) {
                if (psw.equals("123")) {
                    closeDialog();
                } else {
                    show("this psw error!");
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
