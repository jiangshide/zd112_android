package com.android.zd112.ui.view.calendar;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by etongdai on 2017/11/17.
 */

public class CalendarAdapter extends PagerAdapter{
    /**
     * 选中时间：xx-xx-xx
     * */
    public static String selectTime = "";

    /**
     * 是否显示农历,默认为不显示
     */
    protected boolean mIsShowNongli = false;

    public void setShowNongli(boolean isShowNongli){
        this.mIsShowNongli = isShowNongli;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    public interface RefreshListener {
        void refreshListener(ViewPager viewPager);
    }
}
