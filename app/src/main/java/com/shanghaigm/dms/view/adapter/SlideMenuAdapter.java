package com.shanghaigm.dms.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.ck.CustomFileAddActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/20.
 */

public class SlideMenuAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<String> datas;
    private LayoutInflater inflater;
    public SlideMenuAdapter(Context context, ArrayList<String> datas){
        this.context =context;
        this.datas = datas;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_slide_menu,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);       //把holder作为本convertView的tag
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(datas.get(position));
        return convertView;
    }
    class ViewHolder{
        TextView text;
        ViewHolder(View v){
            text = (TextView) v.findViewById(R.id.text_slide);      //获取convertView中的控件
        }
    }
}
