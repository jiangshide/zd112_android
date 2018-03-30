package com.android.zd112.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by etongdai on 2018/3/22.
 */

public class HorizontalViewPager extends ViewPager {
    private int downX;
    private int downY;

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 1、上下滑动，不处理，getParent().requestDisallowInterceptTouchEvent(false)
     * 2、左右滑动
     * 2.1、 当处于第0页时，且手指从左往右，自己不处理
     * 2.2、当处于最后一页时，且手指从右往左，自己不处理
     * 2.3、其他情况，自己处理事件getParent().requestDisallowInterceptTouchEvent(true)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);// 为了让父容器把move事件传递下来
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();

                int diffX = moveX - downX;
                int diffY = moveY - downY;

                if (Math.abs(diffY) > Math.abs(diffX)) {// 1、上下滑动，不处理
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {//  2、左右滑动
                    if (getCurrentItem() == 0 && diffX > 0) {// 2.1、 当处于第0页时，且手指从左往右，自己不处理
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (getCurrentItem() == getAdapter().getCount() - 1 && diffX < 0) {// 2.2、当处于最后一页时，且手指从右往左，自己不处理
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {// 2.3、其他情况，自己处理事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;


            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
