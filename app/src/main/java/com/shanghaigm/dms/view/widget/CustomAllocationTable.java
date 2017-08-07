package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.view.adapter.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/6/29.
 */

public class CustomAllocationTable extends LinearLayout {
    private static String TAG = "CustomAllocation";
    public static String GET_CUSTOM_ALLOCATIN_INFO = "get_custom_allocation_info";
    private Context context;
    private ListView listView;
    private ArrayList<OrderDetailInfoAllocation> saveList;//存储输入信息
    private AddAllocationAdapter adapter;
    private ArrayList<String> systems;

    public CustomAllocationTable(Context context, ArrayList<OrderDetailInfoAllocation> saveList, ArrayList<String> systems) {
        super(context);
        this.context = context;
        this.saveList = saveList;
        this.systems = systems;
        LayoutInflater lf = LayoutInflater.from(context);
        View v = lf.inflate(R.layout.table_custom_allocation, this, true);
        listView = (ListView) v.findViewById(R.id.list_custom_allocation);
        adapter = new AddAllocationAdapter(context, saveList);
        listView.setAdapter(adapter);
    }

    public void AddItem() {
        saveList.add(new OrderDetailInfoAllocation());
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    private class AddAllocationAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<OrderDetailInfoAllocation> dataList;
        private LayoutInflater inflater;

        public AddAllocationAdapter(Context context, ArrayList<OrderDetailInfoAllocation> dataList) {
            this.context = context;
            this.dataList = dataList;
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
//            Log.i(TAG, "getCount: " + dataList.size());
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.include_allocation_add_bar, null);
                holder = new ViewHolder(convertView, position);
                convertView.setTag(holder);    //把convertview和holder绑定
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final ArrayList<PopListInfo> names = new ArrayList<>();
            if (systems != null) {
                for (String name : systems) {
                    names.add(new PopListInfo(name));
                }
                holder.edtSystem.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MmPopupWindow pop = new MmPopupWindow(context, holder.edtSystem, names, 4);
                        pop.showPopup(holder.edtSystem);
                    }
                });
            } else {
                holder.edtSystem.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "请选择车型", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            holder.edtSystem.setText(saveList.get(position).getAssembly());
            holder.edtProName.setText(saveList.get(position).getEntry_name());
            holder.edtConfig.setText(saveList.get(position).getConfig_information());
            holder.edtNum.setText(saveList.get(position).getNum());
            holder.edtRemarks.setText(saveList.get(position).getRemarks());
            return convertView;
        }

        class ViewHolder {
            private EditText edtSystem;
            private EditText edtProName;
            private EditText edtConfig;
            private EditText edtNum;
            private EditText edtRemarks;

            public ViewHolder(View v, int position) {
                edtSystem = (EditText) v.findViewById(R.id.edit_system);
                edtProName = (EditText) v.findViewById(R.id.edt_assembly);
                edtConfig = (EditText) v.findViewById(R.id.edt_config);
                edtNum = (EditText) v.findViewById(R.id.edt_num);
                edtRemarks = (EditText) v.findViewById(R.id.edt_remarks);

                edtSystem.setTag(position);
                edtProName.setTag(position);
                edtConfig.setTag(position);
                edtNum.setTag(position);
                edtRemarks.setTag(position);

                edtSystem.addTextChangedListener(new AllocationTextListener(1, this));
                edtProName.addTextChangedListener(new AllocationTextListener(2, this));
                edtConfig.addTextChangedListener(new AllocationTextListener(3, this));
                edtNum.addTextChangedListener(new AllocationTextListener(4, this));
                edtRemarks.addTextChangedListener(new AllocationTextListener(5, this));
            }
        }

        class AllocationTextListener implements TextWatcher {
            private int type;   //判断edt
            private ViewHolder holder;

            public AllocationTextListener(int type, ViewHolder holder) {
                this.type = type;
                this.holder = holder;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int position = 0;
                switch (type) {
                    case 1:
                        position = (int) holder.edtSystem.getTag();
                        saveEdtInfo(1, s.toString(), position);
                        break;
                    case 2:
                        position = (int) holder.edtProName.getTag();
                        saveEdtInfo(2, s.toString(), position);
                        break;
                    case 3:
                        position = (int) holder.edtConfig.getTag();
                        saveEdtInfo(3, s.toString(), position);
                        break;
                    case 4:
                        position = (int) holder.edtNum.getTag();
                        saveEdtInfo(4, s.toString(), position);
                        break;
                    case 5:
                        position = (int) holder.edtRemarks.getTag();
                        saveEdtInfo(5, s.toString(), position);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }
    }

    private void saveEdtInfo(int type, String str, int position) {
        switch (type) {
            case 1:
                saveList.get(position).setEntry_name(str);
                break;
            case 2:
                saveList.get(position).setAssembly(str);
                break;
            case 3:
                saveList.get(position).setConfig_information(str);
                break;
            case 4:
                saveList.get(position).setNum(str);
                break;
            case 5:
                saveList.get(position).setRemarks(str);
                break;
        }

    }
}
