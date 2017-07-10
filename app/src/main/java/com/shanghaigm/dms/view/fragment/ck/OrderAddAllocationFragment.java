package com.shanghaigm.dms.view.fragment.ck;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.chumi.widget.dialog.LoadingDialog;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.view.activity.ck.OrderAddActivity;
import com.shanghaigm.dms.view.adapter.ListAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.AllocationUnDefaultChoosePopupWindow;

import java.util.ArrayList;

public class OrderAddAllocationFragment extends BaseFragment {
    public Handler mHandler;
    private static String TAG = "OrderAddAllocation";
    private EditText edtSystem, edtAssembly, edtConfig, edtNum, edtReMarks;
    private Button btnNewEnergy, btnCarBody, btnElectric, btnUnderPan;
    private LinearLayout newEnergyLL, carBodyLL, electricLL, underPanLL;
    private ImageView imgSystem;
    private String[] arraySystem = {"新能源", "底盘", "车身", "电器"};
    private ArrayList<PopListInfo> listSystem;
    private ListView listNewEnergy, listCarBody, listElectric, listUnderPan;
    private ListAdapter adapter;
    private ArrayList<ArrayList<OrderDetailInfoAllocation>> allAssemblyList = new ArrayList<>();
    //储存实时改变的list和原始的list
    private ArrayList<OrderDetailInfoAllocation> newEnergyList, carBodyList, electricList, underPanList, saveNewEnergyList, saveCarBodyList, saveElectricList, saveUnderPanList;
    private static OrderAddAllocationFragment orderAddAllocationFragment = null;
    private AllocationUnDefaultChoosePopupWindow popNewEnergy, popCarBody, popElectric, popUnderPan;

    public OrderAddAllocationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_add_allocation, container, false);
        initView(v);
        initData();
        return v;
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
        } else {// 重新显示到最前端中
            initData();
        }
    }

    private void initData() {
        //handler接收数据，用来刷新界面的
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        HandlerRefresh(popNewEnergy, listNewEnergy, newEnergyList, saveNewEnergyList);

                        break;
                    case 2:
                        HandlerRefresh(popCarBody, listCarBody, carBodyList, saveCarBodyList);
                        break;
                    case 3:
                        HandlerRefresh(popUnderPan, listUnderPan, underPanList, saveUnderPanList);
                        break;
                    case 4:
                        HandlerRefresh(popElectric, listElectric, electricList, saveElectricList);
                        break;
                }
            }
        };
//        listSystem = new ArrayList<>();
//        for (int i = 0; i < arraySystem.length; i++) {
//            listSystem.add(new PopListInfo(arraySystem[i]));
//        }
        if (((OrderAddActivity) getActivity()).getAssemblyList() != null) {
            allAssemblyList = ((OrderAddActivity) getActivity()).getAssemblyList();
            Log.i(TAG, "initData: " + allAssemblyList.size());
            setUpView();
        } else {
            Toast.makeText(getActivity(), "请选择车型", Toast.LENGTH_SHORT).show();
        }
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
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
            }
        });
    }

    private void setUpView() {
        allGone(btnCarBody, btnElectric, btnNewEnergy, btnUnderPan, newEnergyLL, carBodyLL, underPanLL, electricLL);
        for (int i = 0; i < allAssemblyList.size(); i++) {
            if (allAssemblyList.get(i).get(0).getAssembly().toString().equals("新能源")) {
                btnNewEnergy.setVisibility(View.VISIBLE);
                newEnergyList = allAssemblyList.get(i);
                saveNewEnergyList = new ArrayList<>();     //改变指向，使saveList和list不同。
                for (OrderDetailInfoAllocation info : newEnergyList) {
                    saveNewEnergyList.add(info);
                }
            }
            if (allAssemblyList.get(i).get(0).getAssembly().toString().equals("车身")) {
                btnCarBody.setVisibility(View.VISIBLE);
                carBodyList = allAssemblyList.get(i);
                saveCarBodyList = new ArrayList<>();
                for (OrderDetailInfoAllocation info : carBodyList) {
                    saveCarBodyList.add(info);
                }
            }
            if (allAssemblyList.get(i).get(0).getAssembly().toString().equals("底盘")) {
                btnUnderPan.setVisibility(View.VISIBLE);
                underPanList = allAssemblyList.get(i);
                saveUnderPanList = new ArrayList<>();
                for (OrderDetailInfoAllocation info : underPanList) {
                    saveUnderPanList.add(info);
                }
            }
            if (allAssemblyList.get(i).get(0).getAssembly().toString().equals("电器")) {
                btnElectric.setVisibility(View.VISIBLE);
                electricList = allAssemblyList.get(i);
                saveElectricList = new ArrayList<>();
                for (OrderDetailInfoAllocation info : electricList) {
                    saveElectricList.add(info);
                }
            }
        }

        btnNewEnergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowList(newEnergyList);
            }
        });
        btnCarBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowList(carBodyList);
            }
        });

        btnUnderPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowList(underPanList);
            }
        });

        btnElectric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowList(electricList);
            }
        });

        listNewEnergy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (newEnergyList.get(position).getMatchLength() > 0) {
                    popNewEnergy = new AllocationUnDefaultChoosePopupWindow(getActivity(), position, 3, newEnergyList.get(position).getList(), newEnergyList, mHandler, 1);
                    popNewEnergy.showPopup(btnNewEnergy);
                }
            }
        });
        listCarBody.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (carBodyList.get(position).getMatchLength() > 0) {
                    popCarBody = new AllocationUnDefaultChoosePopupWindow(getActivity(), position, 3, carBodyList.get(position).getList(), carBodyList, mHandler, 2);
                    popCarBody.showPopup(btnCarBody);
                }
            }
        });
        listUnderPan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (underPanList.get(position).getMatchLength() > 0) {
                    popUnderPan = new AllocationUnDefaultChoosePopupWindow(getActivity(), position, 3, underPanList.get(position).getList(), underPanList, mHandler, 3);
                    popUnderPan.showPopup(btnUnderPan);
                }
            }
        });
        listElectric.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (electricList.get(position).getMatchLength() > 0) {
                    popElectric = new AllocationUnDefaultChoosePopupWindow(getActivity(), position, 3, electricList.get(position).getList(), electricList, mHandler, 4);
                    popElectric.showPopup(btnElectric);
                }
            }
        });
    }


    private void initView(View v) {

        listNewEnergy = (ListView) v.findViewById(R.id.list_new_energy);
        listCarBody = (ListView) v.findViewById(R.id.list_car_body);
        listElectric = (ListView) v.findViewById(R.id.list_electric_appliance);
        listUnderPan = (ListView) v.findViewById(R.id.list_under_pan);
        btnNewEnergy = (Button) v.findViewById(R.id.btn_new_energy);
        btnCarBody = (Button) v.findViewById(R.id.btn_car_body);
        btnElectric = (Button) v.findViewById(R.id.btn_electric_appliance);
        btnUnderPan = (Button) v.findViewById(R.id.btn_under_pan);

        newEnergyLL = (LinearLayout) v.findViewById(R.id.new_energy_ll);
        carBodyLL = (LinearLayout) v.findViewById(R.id.car_body_ll);
        underPanLL = (LinearLayout) v.findViewById(R.id.under_pan_ll);
        electricLL = (LinearLayout) v.findViewById(R.id.electric_appliance_ll);

        allAssemblyList = new ArrayList<>();
    }


    private void ShowList(ArrayList<OrderDetailInfoAllocation> list) {
        adapter = new ListAdapter(getActivity(), R.layout.list_item_allocation_add, BR.info, list);
        switch (list.get(0).getAssembly()) {
            case "新能源":
                if (newEnergyLL.getVisibility() == View.VISIBLE) {
                    newEnergyLL.setVisibility(View.GONE);
                } else if (newEnergyLL.getVisibility() == View.GONE) {
                    ShowAndHide(newEnergyLL, carBodyLL, electricLL, underPanLL);
                    listNewEnergy.setAdapter(adapter);
                }
                break;
            case "车身":
                if (carBodyLL.getVisibility() == View.VISIBLE) {
                    carBodyLL.setVisibility(View.GONE);
                } else if (carBodyLL.getVisibility() == View.GONE) {
                    ShowAndHide(carBodyLL, newEnergyLL, electricLL, underPanLL);
                    listCarBody.setAdapter(adapter);
                }

                break;
            case "电器":
                if (electricLL.getVisibility() == View.VISIBLE) {
                    electricLL.setVisibility(View.GONE);
                } else if (electricLL.getVisibility() == View.GONE) {
                    ShowAndHide(electricLL, carBodyLL, newEnergyLL, underPanLL);
                    listElectric.setAdapter(adapter);
                }

                break;
            case "底盘":
                if (underPanLL.getVisibility() == View.VISIBLE) {
                    underPanLL.setVisibility(View.GONE);
                } else if (underPanLL.getVisibility() == View.GONE) {
                    ShowAndHide(underPanLL, carBodyLL, electricLL, newEnergyLL);
                    listUnderPan.setAdapter(adapter);
                }
                break;
        }
    }

    private void ShowAndHide(View v1, View v2, View v3, View v4) {
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);
        v4.setVisibility(View.GONE);
    }

    public static OrderAddAllocationFragment getInstance() {
        if (orderAddAllocationFragment == null) {
            orderAddAllocationFragment = new OrderAddAllocationFragment();
        }
        return orderAddAllocationFragment;
    }

    private void allGone(View v1, View v2, View v3, View v4, View v5, View v6, View v7, View v8) {
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);
        v4.setVisibility(View.GONE);
        v5.setVisibility(View.GONE);
        v6.setVisibility(View.GONE);
        v7.setVisibility(View.GONE);
        v8.setVisibility(View.GONE);
    }
}
