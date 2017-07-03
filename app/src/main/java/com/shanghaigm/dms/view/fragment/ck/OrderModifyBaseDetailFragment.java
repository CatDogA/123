package com.shanghaigm.dms.view.fragment.ck;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.FragmentOrderAddBaseDetaiBinding;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoOne;
import com.shanghaigm.dms.view.fragment.BaseFragment;


public class OrderModifyBaseDetailFragment extends BaseFragment {
    private DmsApplication app;
    private FragmentOrderAddBaseDetaiBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_add_base_detai, container, false);
        binding = DataBindingUtil.bind(v);
        initData();
        return v;
    }

    public static OrderModifyBaseDetailFragment getInstance(){
        OrderModifyBaseDetailFragment fragment = new OrderModifyBaseDetailFragment();
        return fragment;
    }

    private void initData() {
        app = DmsApplication.getInstance();
        OrderDetailInfoOne info = new OrderDetailInfoOne(app.getOrderDetailInfoBean());
        binding.setInfo(info);
    }

}
