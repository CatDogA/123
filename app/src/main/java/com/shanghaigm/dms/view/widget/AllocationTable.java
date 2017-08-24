package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.AllocationAddChooseUndefaultInfo;
import com.shanghaigm.dms.model.entity.ck.AllocationModifyInfo;
import com.shanghaigm.dms.model.entity.mm.MatchingBean;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.view.activity.ck.OrderAddActivity;
import com.shanghaigm.dms.view.adapter.ListAdapter;
import com.shanghaigm.dms.view.fragment.ck.OrderAddAllocation2Fragment;
import com.shanghaigm.dms.view.fragment.ck.OrderAddBaseFragment;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/6/27.
 */

public class AllocationTable extends LinearLayout {
    private static String TAG = "AllocationTable";
    public static String GET_ALLOCATION_DATA = "get_data";
    private Context context;
    private ArrayList<OrderDetailInfoAllocation> data;
    private ListAdapter adapter;
    private ListView list;
    private Handler handler;
    private AllocationUnDefaultChoosePopupWindow popupWindow;
    private ArrayList<OrderDetailInfoAllocation> saveList = new ArrayList<>();
    private ArrayList<OrderDetailInfoAllocation> listToSend;
    private int type;
    private DmsApplication app;
    private String assemblyName;

    public AllocationTable(String assemblyName, final Context context, final ArrayList<OrderDetailInfoAllocation> data, final Button btn, final int type, ArrayList<OrderDetailInfoAllocation> singleList, final ScrollView scrollView) {
        super(context);
        this.data = data;
        this.context = context;
        this.type = type;
        this.assemblyName = assemblyName;
        this.listToSend = singleList;
        app = DmsApplication.getInstance();
        LayoutInflater lf = LayoutInflater.from(context);
        View view = lf.inflate(R.layout.table_allocation, this, true);
        list = (ListView) view.findViewById(R.id.listview);
        list.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        initHandler();
        setUpView();

    }

    //data是从车型获取到的标配信息，saveList是从其中抽取出来的内容相同但不耦合的信息
    private void setUpView() {
        //第一次进入的时候，存最原始的信息
        for (OrderDetailInfoAllocation info : data) {
            saveList.add(info);
        }
        //没有点击model
        if (!OrderAddBaseFragment.isModelClick && OrderAddActivity.flag == 1) {
            modifySetUpView();
        }
        adapter = new ListAdapter(context, R.layout.list_item_allocation_add, BR.info, data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (data.get(position).getMatchLength() > 0) {
                    //list即选配信息
                    popupWindow = new AllocationUnDefaultChoosePopupWindow(context, position, 1, data.get(position).getList(), data, handler, 1);
                    popupWindow.showPopup(list);
                }
            }
        });
    }

    //修改
    private void modifySetUpView() {
        //把data还原
        data.clear();
        for (OrderDetailInfoAllocation info : saveList) {
            data.add(info);
        }
//        Log.i(TAG, "modifySetUpView:data                 " + data.size());
        ArrayList<AllocationModifyInfo> allocationModifyInfos = new ArrayList<>();
        //存入以备修改，但凡和原信息不同的数据都提取出来
        if (OrderAddActivity.flag == 1 && app.getMatchingBeanArrayList() != null) {
//            Log.i(TAG, "modifySetUpView:getMatchingBeanArrayList         " + app.getMatchingBeanArrayList().size());
            for (MatchingBean bean : app.getMatchingBeanArrayList()) {
                if (bean.assembly.equals(assemblyName)) {
                    for (OrderDetailInfoAllocation allocation : data) {
                        if (bean.entry_name.equals(allocation.getEntry_name())) {
                            //如果和原始数据不同就存入
                            if ((!bean.cost_change.equals(allocation.getCost_change()) || (!bean.remarks.equals(allocation.getRemarks()) || (!bean.config_information.equals(allocation.getConfig_information()))))) {
                                int count = 0;
                                //如果已经有了，就直接添加
                                for (AllocationModifyInfo allocationModifyInfo : allocationModifyInfos) {
                                    if (allocationModifyInfo.entry_name.equals(bean.entry_name)) {
                                        count++;
                                        //判断是否已存在此条数据
                                        OrderDetailInfoAllocation allocation1 = new OrderDetailInfoAllocation(allocation.getAssemblyName(), allocation.getAssembly(), bean.entry_name, allocation.getStandard_information(), bean.cost_change + "", allocation.getSupporting_id(), bean.num + "", bean.remarks, allocation.getMatchLength(), allocation.getList(), allocation.getStandard_id());
                                        Boolean isAdd = true;
                                        for (OrderDetailInfoAllocation alreadyAllocation : allocationModifyInfo.infos) {
                                            if (allocation1.getCost_change().equals(alreadyAllocation.getCost_change()) && allocation1.getNum().equals(alreadyAllocation.getNum()) && allocation1.getRemarks().equals(alreadyAllocation.getRemarks()) && allocation1.getConfig_information().equals(alreadyAllocation.getConfig_information())) {
                                                //存在,不添加
                                                isAdd = false;
                                            }
                                        }
                                        if (isAdd) {
                                            allocationModifyInfo.infos.add(new OrderDetailInfoAllocation(allocation.getAssemblyName(), allocation.getAssembly(), bean.entry_name, allocation.getStandard_information(), bean.cost_change + "", allocation.getSupporting_id(), bean.num + "", bean.remarks, allocation.getMatchLength(), allocation.getList(), allocation.getStandard_id()));
                                        }
                                    }
                                }
                                if (count == 0) {
                                    ArrayList<OrderDetailInfoAllocation> infos = new ArrayList<>();
                                    infos.add(new OrderDetailInfoAllocation(allocation.getAssemblyName(), allocation.getAssembly(), bean.entry_name, allocation.getStandard_information(), bean.cost_change + "", allocation.getSupporting_id(), bean.num + "", bean.remarks, allocation.getMatchLength(), allocation.getList(), allocation.getStandard_id()));
                                    allocationModifyInfos.add(new AllocationModifyInfo(bean.entry_name, infos));
                                }
                            }
                        }
                    }
                }
            }
        }
        //删除原来的
        ArrayList<OrderDetailInfoAllocation> listToDelete = new ArrayList<>();
        for (AllocationModifyInfo info : allocationModifyInfos) {
            for (OrderDetailInfoAllocation infoAllocation : data) {
                if (info.entry_name.equals(infoAllocation.getEntry_name())) {
                    //但凡entry_name有相同的，就加入删除列表
                    listToDelete.add(infoAllocation);
                }
            }
        }
        for (OrderDetailInfoAllocation info : listToDelete) {
            data.remove(info);
        }
        //添加修改了的
        for (AllocationModifyInfo info : allocationModifyInfos) {
            for (OrderDetailInfoAllocation infoAllocation : info.infos) {
                data.add(infoAllocation);
                if (OrderAddActivity.undefaultInfos == null) {
                    OrderAddActivity.undefaultInfos = new ArrayList<>();
                } else {
                    Double cost_change = 0.0;
                    if (!infoAllocation.getCost_change().equals("")) {
                        cost_change = Double.parseDouble(infoAllocation.getCost_change());
                    }
                    OrderAddActivity.undefaultInfos.add(new AllocationAddChooseUndefaultInfo(infoAllocation.getAssemblyName(), infoAllocation.getAssemblyName(), infoAllocation.getConfig_information(), infoAllocation.getEntry_name(), 1, infoAllocation.getSupporting_id(), 0, cost_change, Integer.parseInt(infoAllocation.getNum()), infoAllocation.getRemarks(), infoAllocation.getStandard_id()));
                }
            }
        }
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //刷新界面的瞬间
                HandlerRefresh(popupWindow, list, data, saveList);
            }
        };
    }

    //删掉原来信息，添加修改信息
    public void HandlerRefresh(AllocationUnDefaultChoosePopupWindow popupWindow, final ListView listview, final ArrayList<OrderDetailInfoAllocation> infoList, final ArrayList<OrderDetailInfoAllocation> saveList) {
        //接口回调
        popupWindow.getListViewInfo(new AllocationUnDefaultChoosePopupWindow.CallBack() {

            @Override
            public void getResult(ArrayList<OrderDetailInfoAllocation> saveUndefaultList, int position) {
                if (saveUndefaultList.size() == 0) {
                    //不论是否第一次，统一处理,删除对应id的条目，添加原始条目。
                    int id = infoList.get(position).getStandard_id();
                    ArrayList<OrderDetailInfoAllocation> toDeleteList = new ArrayList<OrderDetailInfoAllocation>();
                    for (int i = 0; i < infoList.size(); i++) {
                        if (infoList.get(i).getStandard_id() == id) {
                            toDeleteList.add(infoList.get(i));
                        }
                    }
//                    Log.i(TAG, "getResult: saveList    " + saveList.size() + "     infoList     " + infoList.size() + "    toDeleteList    " + toDeleteList.size());
                    for (OrderDetailInfoAllocation info : toDeleteList) {
                        infoList.remove(info);
                    }
//                    Log.i(TAG, "getResult: saveList    " + saveList.size() + "     infoList     " + infoList.size() + "    toDeleteList    " + toDeleteList.size());
                    for (OrderDetailInfoAllocation info : saveList) {
                        if (info.getStandard_id() == id) {
                            infoList.add(info);
                        }
                    }
//                    Log.i(TAG, "getResult: saveList    " + saveList.size() + "     infoList     " + infoList.size() + "    toDeleteList    " + toDeleteList.size());

                } else {
                    //判断是否删除原条目
                    if (infoList.get(position).getNum().equals("")) {
                        //说明第一次修改
                        infoList.remove(infoList.get(position));
                        for (int i = 0; i < saveUndefaultList.size(); i++) {
                            infoList.add(saveUndefaultList.get(i));
                        }
                    } else {
                        //不是第一次
                        //获取已添加的选配条目unDefaultList，进行删除
                        if (saveUndefaultList.size() > 0) {
                            int id = saveUndefaultList.get(0).getStandard_id();
//                            Log.i(TAG, "getStandard_id()     " + id);
                            ArrayList<OrderDetailInfoAllocation> unDefaultList = new ArrayList<>();
                            for (int j = 0; j < infoList.size(); j++) {
                                if (infoList.get(j).getNum().equals("")) {

                                } else if (Integer.parseInt(infoList.get(j).getNum()) > 0) {
                                    if (infoList.get(j).getStandard_id() == id) {
                                        unDefaultList.add(infoList.get(j));
                                    }
                                }
                            }
                            for (int i = 0; i < unDefaultList.size(); i++) {
                                infoList.remove(unDefaultList.get(i));
                            }
                            for (int i = 0; i < saveUndefaultList.size(); i++) {
                                infoList.add(saveUndefaultList.get(i));
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
                //此时的infoList就是要传给后台的数据
                listToSend.clear();
                for (OrderDetailInfoAllocation info : infoList) {
                    listToSend.add(info);
                }
            }
        });
    }
}
