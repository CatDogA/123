package com.shanghaigm.dms.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shanghaigm.dms.model.entity.mm.PaperInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 * 不侧滑判断
 */

public class NotSlideMenuAdpter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private int mLayoutId;
    private int mVariableId;
    private List<PaperInfo> mList;

    /**
     * 构造函数
     * @param mContext 上下文
     * @param mLayoutId item布局的资源id
     * @param mVariableId 系统自动生成，根据实体类直接从外部传入
     * @param mList 数据集合
     */
    public NotSlideMenuAdpter(Context mContext, int mLayoutId, int mVariableId, List<PaperInfo> mList) {
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
    public int getViewTypeCount() {
        // menu type count
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
        // current menu type  用于判断是否可侧滑
        int type = 1;
        if (mList.get(position).getSlide()) {
            type = 0;
        }
        return type;
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
