package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Tom on 2017/9/13.
 * 宽满屏，高自适应
 */

public class ResizeImage extends ImageView {
    public ResizeImage(Context context) {
        super(context);
    }

    public ResizeImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec); //高度根据使得图片的宽度充满屏幕计算而得
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        } else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }


    }
}