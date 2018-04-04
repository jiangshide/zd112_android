package com.android.zd112.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zd112.App;
import com.android.zd112.R;
import com.android.zd112.data.net.NetApi;
import com.android.zd112.ui.view.DialogView;
import com.android.zd112.ui.view.refresh.RefreshLoadLayout;

/**
 * Created by etongdai on 2017/11/20.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener, DialogView.DialogViewListener, RefreshLoadLayout.OnRefreshListener {

    protected NetApi mNetApi;
    protected View mView;
    protected boolean mIsLoadedData = false;
    protected TextView mRedDotTxt;
    protected DialogView mDialogView;

    protected <VT extends View> VT viewId(@IdRes int id) {
        return (VT) mView.findViewById(id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetApi = App.getInstance().getmNetApi();
    }

    protected void topView(String title) {
        topView(-1, title, -1);
    }

    protected void topView(int leftIcon, String title) {
        topView(leftIcon, title, -1);
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

    protected void show(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
    }

    protected void requestTips() {
        ProgressBar errorTips = viewId(R.id.errorTips);
        LinearLayout retryTips = viewId(R.id.retryTips);

    }

    private void handleOnVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (!mIsLoadedData) {
                mIsLoadedData = true;
                onLazyLoadOnce();
            }
            onVisibleToUser();
        } else {
            onInvisibleToUser();
        }
    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    protected void onLazyLoadOnce() {
    }

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    protected void onVisibleToUser() {
    }

    /**
     * 对用户不可见时触发该方法
     */
    protected void onInvisibleToUser() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        return mView;
    }

    protected void setView(@LayoutRes int layoutResID) {
        mView = LayoutInflater.from(getContext()).inflate(layoutResID, null);
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void processLogic(Bundle savedInstanceState);

    @Override
    public void onClick(View v) {
    }

    protected String[] getResStringArr(int arr) {
        return getResources().getStringArray(arr);
    }

    protected void scrollTxt(TextView textView, String txt) {
        textView.setText(TextUtils.isEmpty(txt) ? "" : txt);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setSelected(true);
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
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

    @Override
    public void onRefresh() {
    }

}
