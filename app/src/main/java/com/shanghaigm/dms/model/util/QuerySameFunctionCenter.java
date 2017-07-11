package com.shanghaigm.dms.model.util;

import android.widget.ImageView;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.widget.WrapHeightViewPager;

/**
 * Created by Tom on 2017/7/11.
 * 存查询页方法，目前看来并不好用
 */

public class QuerySameFunctionCenter {
    public void VpLeft(Boolean IsMore, int pages, WrapHeightViewPager vp, ImageView vpRight, ImageView vpLeft) {
        if (IsMore) {
            int currentItem = vp.getCurrentItem();
            if (currentItem + 1 == pages) {
                vpRight.setImageResource(R.mipmap.right_switch_pre);
            }
            if (currentItem > 0) {
                vp.setCurrentItem(--currentItem);
            }
            if (currentItem == 0) {
                vpLeft.setImageResource(R.mipmap.left_switch);
            }
        }
    }

}
