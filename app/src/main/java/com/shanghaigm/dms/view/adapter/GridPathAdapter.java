package com.shanghaigm.dms.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanghaigm.dms.R;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/9/8.
 */

public class GridPathAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> paths;

    public GridPathAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img_grid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context)
                .load(paths.get(position))
                .into(holder.img);
        return convertView;
    }

    private class ViewHolder {
        ImageView img;
    }
}
