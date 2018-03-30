package com.android.zd112.ui.view.calendar;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zd112.R;
import com.android.zd112.utils.DateUtils;
import com.android.zd112.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by etongdai on 2017/11/17.
 */

public class MonthCalendarAdapter extends CalendarAdapter{
    private List<View> mViews;
    private Context mContext;

    private Handler mHandler = null;
    private int mGrayColor;
    private Drawable mBlueDrawable;
    private Drawable mGrayDrawable;
    private Drawable mWhiteDrawable;
    private int mBlueColor;
    private int mWhiteColor;
    private final Drawable mRedDrawable;

    private TextView mNongli;
    private TextView mGongli;
    private ImageView mPoint;

    private String strToDay = "";
    private ArrayList<String> list = new ArrayList<>();
    private String[] mDefaultDateList;

    private int mChildCount = 0;
    private boolean mIsUpdate;

    public MonthCalendarAdapter(List<View> views, Context context, ArrayList<String> list) {
        this.mViews = views;
        this.mContext = context;
        this.list = list;
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        strToDay = DateUtils.getTagTimeStr(today);
        selectTime = DateUtils.getTagTimeStr(today);

        mBlueColor = context.getResources().getColor(R.color.colorPrimary);
        mGrayColor = context.getResources().getColor(R.color.colorGrayPrimary);
        mWhiteColor = context.getResources().getColor(R.color.colorWhite);
        mBlueDrawable = context.getResources().getDrawable(R.drawable.blue);
        mGrayDrawable = context.getResources().getDrawable(R.drawable.gray);
        mRedDrawable = context.getResources().getDrawable(R.drawable.red);
        mWhiteDrawable = context.getResources().getDrawable(R.drawable.white);
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public void setDefaultDate(String[] dateList) {
        this.mDefaultDateList = dateList;
        mIsUpdate = false;
        if (dateList != null && dateList.length > 0){
            mIsUpdate = true;
            notifyDataSetChanged();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if(mChildCount > 0 && mIsUpdate){
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return 2400;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewGroup view = (ViewGroup) mViews.get(position % mViews.size());
        int index = container.indexOfChild(view);
        if (index != -1) {
            container.removeView(view);
        }
        try {
            container.addView(view);
        } catch (Exception e) {

        }
        refresh(view, position, list);
        return view;
    }

    public void getTimeList(ArrayList<String> list) {
        this.list = list;
    }

    /**
     * 提供对外的刷新接口
     */
    public void refresh(ViewGroup view, int position, ArrayList<String> list) {
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        int month = 1200 - position;
        today.add(Calendar.MONTH, -month);
        view.setTag(today.get(Calendar.MONTH) + "");
        today.add(Calendar.DAY_OF_MONTH, -(today.get(Calendar.DAY_OF_MONTH) - 1));


        int day_of_week = today.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        today.add(Calendar.DATE, -day_of_week);
        render(view, today);
    }

    private void initView(ViewGroup dayOfWeek) {
        mGongli = dayOfWeek.findViewById(R.id.gonglis);
        mPoint = dayOfWeek.findViewById(R.id.point_view);
        mNongli = dayOfWeek.findViewById(R.id.nonglis);
    }

    private ViewGroup mView;
    private Calendar mToday;

    private void render(final ViewGroup view1, final Calendar today) {
        this.mView = view1;
        this.mToday = today;
        for (int b = 0; b < 6; b++) {
            final ViewGroup view = (ViewGroup) view1.getChildAt(b);
            for (int a = 0; a < 7; a++) {
                final int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
                final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(a);
                initView(dayOfWeek);
                mGongli.setText(dayOfMonth + "");
                String str = "";
                try {
                    str = new CalendarUtil().getChineseDay(today.get(Calendar.YEAR),
                            today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
                } catch (Exception e) {
                    LogUtils.e("e:", e.getMessage());
                }
                if (str.equals("初一")) {//如果是初一，显示月份
                    str = new CalendarUtil().getChineseMonth(today.get(Calendar.YEAR),
                            today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
                }
                if (mIsShowNongli) {
                    mNongli.setVisibility(View.VISIBLE);
                } else {
                    mNongli.setVisibility(View.GONE);
                }
                mNongli.setText(str);
                if (list.contains(DateUtils.getTagTimeStr(today))) {
                    mPoint.setVisibility(View.VISIBLE);
                    mPoint.setImageResource(R.drawable.calendar_item_point);
                } else {
                    mPoint.setVisibility(View.INVISIBLE);
                }
                dayOfWeek.setTag(DateUtils.getTagTimeStr(today));

                dayOfWeek.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDefaultDateList != null) {
                            String currData = dayOfWeek.getTag().toString();
                            for (String date : mDefaultDateList) {
                                if (date.equals(currData)) {
                                    Message message = new Message();
//                                    message.what = CalendarView.CLICK_DAY;
                                    message.obj = dayOfWeek.getTag().toString();
                                    mHandler.sendMessage(message);
                                    break;
                                }
                            }
                        }
                        @SuppressLint("ResourceType") Animator anim = AnimatorInflater.loadAnimator(mContext, R.anim.scale);
                        dayOfWeek.invalidate();
                        anim.setTarget(dayOfWeek);
                        anim.start();
                    }
                });
                if (strToDay.equals(DateUtils.getTagTimeStr(today))) {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(mRedDrawable);
                    mGongli.setTextColor(mBlueColor);
                    mNongli.setTextColor(mGrayColor);
                    if (!selectTime.equals(strToDay)) {
                        today.add(Calendar.DATE, 1);
                        continue;
                    }
                } else {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(mWhiteDrawable);
                    mGongli.setTextColor(mBlueColor);
                }
                //不是当前月的显示为灰色
                if ((Integer.parseInt((String) view1.getTag())) != today.get(Calendar.MONTH)) {
                    mGongli.setTextColor(mGrayColor);
                    if ((Integer.parseInt((String) view1.getTag())) > today.get(Calendar.MONTH)) {
                        //下个月
                        dayOfWeek.setOnClickListener(lastLister);
                    } else {
                        //上个月
                        dayOfWeek.setOnClickListener(nextLister);
                    }
                    today.add(Calendar.DATE, 1);
                    continue;
                } else {
                    dayOfWeek.setAlpha(1.0f);
                }
                //如果是选中天的话显示为红色
                if (selectTime.equals(DateUtils.getTagTimeStr(today))) {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(mRedDrawable);
                    mGongli.setTextColor(mWhiteColor);
                    mNongli.setTextColor(mWhiteColor);

                    if (strToDay.equals(DateUtils.getTagTimeStr(today))) {
                        dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(mRedDrawable);
                        mGongli.setTextColor(mWhiteColor);
                        mNongli.setTextColor(mWhiteColor);
                    }

                    day = dayOfWeek;
                    if (MonthCalendarAdapter.this.mHandler != null) {
                        Message message = mHandler.obtainMessage();
                        message.obj = b;
//                        message.what = CalendarView.change2;
                        mHandler.sendMessage(message);
                    }
                    tag = selectTime;
                } else {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(mWhiteDrawable);
                    mGongli.setTextColor(mBlueColor);
                    mNongli.setTextColor(mGrayColor);
                }
                changeState(dayOfWeek, today);
                today.add(Calendar.DATE, 1);
            }
        }
    }

    private boolean isCurrMonth() {
        return (Integer.parseInt((String) mView.getTag())) == mToday.get(Calendar.MONTH);
    }

    private void changeState(ViewGroup dayOfWeek, Calendar today) {
        if (mDefaultDateList != null) {
            String currDate = DateUtils.getTagTimeStr(today);
            for (String date : mDefaultDateList) {
                if (date.equals(currDate)) {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(mBlueDrawable);
                    mGongli.setTextColor(mWhiteColor);
                    mNongli.setTextColor(mWhiteColor);
                }
            }
        }
    }

    View.OnClickListener nextLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isCurrMonth()) {
                return;
            }
            if (MonthCalendarAdapter.this.mHandler != null) {
                Message message = mHandler.obtainMessage();
//                message.what = CalendarView.pagerNext;
                mHandler.sendMessage(message);
            }
        }
    };
    View.OnClickListener lastLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isCurrMonth()) {
                return;
            }
            if (MonthCalendarAdapter.this.mHandler != null) {
                Message message = mHandler.obtainMessage();
//                message.what = CalendarView.pagerLast;
                mHandler.sendMessage(message);
            }
        }
    };

    private ViewGroup day = null;

    public ViewGroup getDay() {
        return day;
    }

    private String tag = "";

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        CalendarAdapter.selectTime = selectTime;
    }
}
