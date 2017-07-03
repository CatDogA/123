package com.shanghaigm.dms.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 用于MVVM中ListView绑定数据的Adapter，非常通用，没有特殊需求的话，只会用到这一个适配器
 * Created by CHUMI.Jim on 2016/11/9.
 */

public class ListAdapter<T> extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private int mLayoutId;
    private int mVariableId;
    private List<T> mList;

    /**
     * 构造函数
     * @param mContext 上下文
     * @param mLayoutId item布局的资源id
     * @param mVariableId 系统自动生成，根据实体类直接从外部传入
     * @param mList 数据集合
     */
    public ListAdapter(Context mContext, int mLayoutId, int mVariableId, List<T> mList) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.mVariableId = mVariableId;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding dataBinding;
        if (convertView == null){
            dataBinding = DataBindingUtil.inflate(mInflater, mLayoutId, parent, false);
        } else {
            dataBinding = DataBindingUtil.getBinding(convertView);
        }
        dataBinding.setVariable(mVariableId, getItem(position));
        return dataBinding.getRoot();
    }
}
