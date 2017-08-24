package com.shanghaigm.dms.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.shanghaigm.dms.view.widget.ReviewTable;
import com.shanghaigm.dms.view.widget.SearchTable;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/5/17.
 */

public class TablePagerAdapter extends PagerAdapter{
    private ArrayList<ReviewTable> tables;
    private Context context;
    public TablePagerAdapter(Context context,ArrayList<ReviewTable> tables){
        this.context = context;
        this.tables = tables;
    }
    @Override
    public int getCount() {
        return tables.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    /**
     * @param container viewpager容器
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(tables.get(position));
        return tables.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object)   {
        return POSITION_NONE;
    }

}
