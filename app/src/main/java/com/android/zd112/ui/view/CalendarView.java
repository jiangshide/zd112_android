package com.android.zd112.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.android.zd112.ui.view.calendar.CalendarAdapter;

import java.lang.reflect.Field;

/**
 * Created by etongdai on 2017/11/17.
 */

public class CalendarView extends ViewPager {
    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setViewPagerScrollSpeed( );
    }

    private CalendarAdapter.RefreshListener listener;

    public void setListener(CalendarAdapter.RefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        //刷新，滚动到相应的位置
        if (visibility == VISIBLE) {//在viewpager显示前，刷新
            if (listener != null) {
                listener.refreshListener(CalendarView.this);
            }
        }
    }

    public void pagerNext() {
        this.setCurrentItem(this.getCurrentItem() + 1);
    }

    public void pagerLast() {
        this.setCurrentItem(this.getCurrentItem() - 1);
    }

    public void setCurrDate() {
        this.setCurrentItem(1200, true);
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void setViewPagerScrollSpeed() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(CalendarView.this.getContext());
            mScroller.set(CalendarView.this, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 0;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
