package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.view.adapter.ListAdapter;

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
    private Handler mHandler;
    private AllocationUnDefaultChoosePopupWindow popupWindow;
    private ArrayList<OrderDetailInfoAllocation> saveList = new ArrayList<>();

    public AllocationTable(final Context context, final ArrayList<OrderDetailInfoAllocation> data, final Button btn, final int type,Handler mHandler) {
        super(context);
        this.data = data;
        this.context = context;
        this.mHandler = mHandler;
        LayoutInflater lf = LayoutInflater.from(context);
        View view = lf.inflate(R.layout.table_allocation, this, true);
        initHandler();
        adapter = new ListAdapter(context, R.layout.list_item_allocation_add, BR.info, data);
        for (OrderDetailInfoAllocation info : data) {
            saveList.add(info);
        }
        list = (ListView) view.findViewById(R.id.listview);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (data.get(position).getMatchLength() > 0) {
                    popupWindow = new AllocationUnDefaultChoosePopupWindow(context, position, 1, data.get(position).getList(), data, handler,1);
                    popupWindow.showPopup(btn);
                }
            }
        });
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                HandlerRefresh(popupWindow, list, data, saveList);
            }
        };
    }

    private void HandlerRefresh(AllocationUnDefaultChoosePopupWindow popupWindow, final ListView listview, final ArrayList<OrderDetailInfoAllocation> infoList, final ArrayList<OrderDetailInfoAllocation> saveList) {
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
                    Log.i(TAG, "getResult: saveList    " + saveList.size() + "     infoList     " + infoList.size() + "    toDeleteList    " + toDeleteList.size());
                    for (OrderDetailInfoAllocation info : toDeleteList) {
                        infoList.remove(info);
                    }
                    Log.i(TAG, "getResult: saveList    " + saveList.size() + "     infoList     " + infoList.size() + "    toDeleteList    " + toDeleteList.size());
                    for (OrderDetailInfoAllocation info : saveList) {
                        if (info.getStandard_id() == id) {
                            infoList.add(info);
                        }
                    }
                    Log.i(TAG, "getResult: saveList    " + saveList.size() + "     infoList     " + infoList.size() + "    toDeleteList    " + toDeleteList.size());

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
                            Log.i(TAG, "getStandard_id()     " + id);
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
                //此时的infoList就是要传给后台的数据
                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putSerializable(AllocationTable.GET_ALLOCATION_DATA,infoList);
                msg.setData(bundle);
                msg.sendToTarget();

                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
            }
        });
    }

}
