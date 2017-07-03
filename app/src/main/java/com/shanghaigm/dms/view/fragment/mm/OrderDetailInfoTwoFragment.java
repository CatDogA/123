package com.shanghaigm.dms.view.fragment.mm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.FragmentOrderDetailInfoOneBinding;
import com.shanghaigm.dms.databinding.FragmentOrderDetailInfoTwoBinding;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoTwo;
import com.shanghaigm.dms.view.fragment.BaseFragment;

public class OrderDetailInfoTwoFragment extends BaseFragment {
    private FragmentOrderDetailInfoTwoBinding binding;
    private DmsApplication app;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_order_detail_info_two, container, false);
          binding = DataBindingUtil.bind(v);
        initData();
        return v;
    }

    private void initData() {
        app = DmsApplication.getInstance();
        OrderDetailInfoTwo info = new OrderDetailInfoTwo(app.getOrderDetailInfoBean());
        binding.setInfo(info);
    }

    public static OrderDetailInfoTwoFragment getInstance(){
        OrderDetailInfoTwoFragment fragment = new OrderDetailInfoTwoFragment();
        return fragment;
    }
}
