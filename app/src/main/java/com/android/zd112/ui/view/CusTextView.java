package com.android.zd112.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by etongdai on 2018/3/6.
 */

public class CusTextView extends TextView {

    public CusTextView(Context context) {
        super(context);
    }

    public CusTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CusTextView(Context context, AttributeSet attributeSet, int deyStyle) {
        super(context, attributeSet, deyStyle);
    }

    public void setText(String txt, boolean isScroll) {
        this.setText(txt);
        if (isScroll) {
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
            setSingleLine(true);
            setSelected(true);
            setFocusable(true);
            setFocusableInTouchMode(true);
        }
    }
}
