package com.android.zd112.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by etongdai on 2018/3/7.
 */

public class MyLetterListView extends View {
    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private String[] mLetter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;

    public MyLetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyLetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLetterListView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#40000000"));
        }
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / mLetter.length;
        for (int i = 0; i < mLetter.length; i++) {
            paint.setColor(Color.parseColor("#8c8c8c"));
            paint.setTextSize(26);
            // paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
			/*if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}*/
            float xPos = width / 2 - paint.measureText(mLetter[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(mLetter[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    public void setLetter(String[] letter) {
        int mLetterSize = mLetter.length;
        int letterSize = letter.length;
        String[] temp = new String[mLetterSize + letterSize];
        System.arraycopy(letter, 0, temp, 0, letterSize);
        System.arraycopy(mLetter, 0, temp, letterSize, mLetterSize);
        mLetter = temp;
        this.invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * mLetter.length);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < mLetter.length) {
                        listener.onTouchingLetterChanged(mLetter[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < mLetter.length) {
                        listener.onTouchingLetterChanged(mLetter[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }
}
