package com.sonobi.sonobimobileads;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jgo on 10/11/17.
 */

public class AdView extends ViewGroup {

    public AdView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        View view;
        if((view = this.getChildAt(0)) != null && view.getVisibility() == View.VISIBLE) {
            int var7 = view.getMeasuredWidth();
            int var8 = view.getMeasuredHeight();
            int var9 = (i2 - i - var7) / 2;
            int var10 = (i3 - i1 - var8) / 2;
            view.layout(var9, var10, var9 + var7, var10 + var8);
        }
    }
}
