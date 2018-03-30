package com.android.zd112.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zd112.App;
import com.android.zd112.R;
import com.android.zd112.data.net.NetApi;
import com.android.zd112.ui.view.DialogView;

/**
 * Created by etongdai on 2018/1/22.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener,DialogView.DialogViewListener {

    protected NetApi mNetApi;
    protected View mView;
    protected TextView mRedDotTxt;
    protected final int LOADING = 1;
    protected final int LOADING_FINISH = 2;
    protected final int LOADING_RETRY = 3;
    protected DialogView mDialogView;

    protected <VT extends View> VT viewId(@IdRes int id) {
        return (VT) mView.findViewById(id);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetApi = App.getInstance().getmNetApi();
        if (mView == null) {
            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
    }

    protected void topView(String title) {
        topView(R.mipmap.back, title, -1);
    }

    protected void topView(int leftIcon, String title, int rightIcon) {
        if (leftIcon > 0) {
            viewId(R.id.topLeftBtn).setOnClickListener(this);
            ImageView topLeftIcon = viewId(R.id.topLeftIcon);
            topLeftIcon.setBackgroundResource(leftIcon);
            mRedDotTxt = viewId(R.id.redDotTxt);
            showDot(0);
        }
        TextView topCenterTxt = viewId(R.id.topCenterTxt);
        topCenterTxt.setText(title);
        if (rightIcon > 0) {
            ImageView topRightBtn = viewId(R.id.topRightBtn);
            topRightBtn.setBackgroundResource(rightIcon);
            topRightBtn.setOnClickListener(this);
        }
    }

    protected void showDot(int num) {
        if (mRedDotTxt != null && num > 0) {
            mRedDotTxt.setText(num);
            mRedDotTxt.setVisibility(View.VISIBLE);
        } else {
            mRedDotTxt.setVisibility(View.GONE);
        }
    }

    protected int[] getResArr(int arr){
        return getResources().getIntArray(arr);
    }

    protected String[] getResStringArr(int arr){
        return getResources().getStringArray(arr);
    }

    protected void setView(@LayoutRes int layoutResID) {
        mView = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(mView);
    }

    protected void requestTips(int isLoading) {
        LinearLayout errorTips = viewId(R.id.errorTips);
        ImageView retryTips = viewId(R.id.retryTips);
        switch (isLoading) {
            case LOADING:
                retryTips.setVisibility(View.GONE);
                errorTips.setVisibility(View.VISIBLE);
                break;
            case LOADING_RETRY:
                retryTips.setVisibility(View.VISIBLE);
                errorTips.setVisibility(View.GONE);
                break;
            case LOADING_FINISH:
                retryTips.setVisibility(View.GONE);
                errorTips.setVisibility(View.GONE);
                break;
        }
    }

    protected void show(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topLeftBtn:
                this.finish();
                break;
        }
    }

    protected DialogView showDialog(Context context, int themeStyle, int layout) {
        closeDialog();
        mDialogView = new DialogView(context, themeStyle, layout, this);
        return mDialogView;
    }

    protected void closeDialog() {
        if (mDialogView != null) {
            mDialogView.dismiss();
        }
        mDialogView = null;
    }

    @Override
    public void onView(View view) {
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void processLogic(Bundle savedInstanceState);
}
