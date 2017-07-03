package com.shanghaigm.dms.view.fragment.mm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.mm.MatchingBean;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.view.adapter.ListAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailAllocationInfoFragment extends BaseFragment {
    private ListView listview;
    private ListAdapter adapter;
    
    private DmsApplication app;
    private ArrayList<OrderDetailInfoAllocation> allocations;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_detail_allocation_info, container, false);
        initView(v);
        setUpView();
        return v;

    }

    private void setUpView() {
        app = DmsApplication.getInstance();
        allocations = new ArrayList<>();
        List<MatchingBean> list = app.getMatchingBeanArrayList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                MatchingBean bean = list.get(i);
                OrderDetailInfoAllocation allocation = new OrderDetailInfoAllocation(bean.assembly, bean.entry_name, bean.config_information, bean.num + "", bean.remarks, bean.isdefault);
                allocations.add(allocation);
            }
        }
        adapter = new ListAdapter(getActivity(), R.layout.list_item_allocation, BR.info, allocations);
        listview.setAdapter(adapter);
    }

    private void initView(View v) {
        listview = (ListView) v.findViewById(R.id.list_allocation);
    }

    public static OrderDetailAllocationInfoFragment getInstance() {
        OrderDetailAllocationInfoFragment fragment = new OrderDetailAllocationInfoFragment();
        return fragment;
    }
}
