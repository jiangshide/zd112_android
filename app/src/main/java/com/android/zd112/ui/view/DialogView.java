package com.android.zd112.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.zd112.R;

/**
 * Created by etongdai on 2018/2/28.
 */

public class DialogView extends Dialog {

    private View mView;
    private DialogViewListener mDialogViewListener;
    private TextView mContent;
    private Button mSure, mCancel;
    private DialogOnClickListener mDialogOnClickListener;

    public DialogView(@NonNull Context context, int style) {
        super(context, style);
    }

    public DialogView(@NonNull Context context, int style, int layout, DialogViewListener dialogViewListener) {
        super(context, style);
        this.mView = LayoutInflater.from(context).inflate(layout, null);
        this.mDialogViewListener = dialogViewListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mView != null) {
            setContentView(mView);
        } else {
            setContentView(R.layout.dialog_default);
            defaultView();
        }
        if (mView != null) {
            ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
            layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
            mView.setLayoutParams(layoutParams);
        }
        if (mDialogViewListener != null) {
            mDialogViewListener.onView(mView);
        }
    }

    private void defaultView() {
        mContent = this.findViewById(R.id.dialogContent);
        mSure = this.findViewById(R.id.dialogSure);
        mCancel = this.findViewById(R.id.dialogCancel);
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogOnClickListener != null) {
                    dismiss();
                    mDialogOnClickListener.onDialogClick(mSure, mCancel);
                }
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogOnClickListener != null) {
                    dismiss();
                }
            }
        });
    }

    public DialogView setAttr() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        return this;
    }

    public DialogView setGravity(int gravity) {
        getWindow().setGravity(gravity);
        return this;
    }

    public DialogView setAnim(int anim) {
        getWindow().setWindowAnimations(anim);
        return this;
    }

    public DialogView setOutside(boolean isCanceledOnTouchOutside) {
        setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        return this;
    }

    public DialogView setContent(String content) {
        if (mContent != null) {
            mContent.setText(content);
        }
        return this;
    }

    public DialogView setSure(String sure) {
        if (mSure != null) {
            mSure.setText(sure);
        }
        return this;
    }

    public DialogView setCancel(String cancel) {
        if (mCancel != null) {
            mCancel.setText(cancel);
        }
        return this;
    }

    public DialogView setListener(DialogOnClickListener l) {
        this.mDialogOnClickListener = l;
        return this;
    }

    public DialogView setOutsideClose(boolean isClose) {
        this.setCanceledOnTouchOutside(isClose);
        return this;
    }

    public interface DialogViewListener {
        void onView(View view);
    }

    public interface DialogOnClickListener {
        void onDialogClick(Button surce, Button cancel);
    }
}
