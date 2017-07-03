package com.shanghaigm.dms.view.fragment.mm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.FragmentOrderDetailInfoOneBinding;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoOne;
import com.shanghaigm.dms.view.fragment.BaseFragment;

public class OrderDetailInfoOneFragment extends BaseFragment {
    private DmsApplication app;
    private static String TAG = "OrderDetailInfoOneFragment";
    private EditText customName;
    private FragmentOrderDetailInfoOneBinding bind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_detail_info_one, container, false);
        bind = DataBindingUtil.bind(v);
        initData();
        return v;
    }

    private void initView(View v) {
//        customName = (EditText) v.findViewById(R.id.customName);
    }

    private void initData() {
        app = DmsApplication.getInstance();
        OrderDetailInfoOne info = new OrderDetailInfoOne(app.getOrderDetailInfoBean());
        bind.setInfo(info);
    }

    public static OrderDetailInfoOneFragment getInstance() {
        OrderDetailInfoOneFragment fragment = new OrderDetailInfoOneFragment();
        return fragment;
    }
}
