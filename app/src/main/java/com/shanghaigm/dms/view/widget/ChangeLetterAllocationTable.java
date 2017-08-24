package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterAllocationInfo;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/8/7.
 */

public class ChangeLetterAllocationTable extends LinearLayout {
    private Context context;
    private ListView listView;
    private final String TAG = "ChangeLetterAllocation";
    private ArrayList<ChangeLetterAllocationInfo> saveList;//存储输入信息
    private AddAllocationAdapter adapter;
    private ScrollView scrollView;

    public ChangeLetterAllocationTable(Context context, final ArrayList<ChangeLetterAllocationInfo> saveList, final ScrollView scrollView) {
        super(context);
        this.context = context;
        this.saveList = saveList;
        this.scrollView = scrollView;
        LayoutInflater lf = LayoutInflater.from(context);
        View v = lf.inflate(R.layout.table_change_letter_allocation, this, true);
        listView = (ListView) v.findViewById(R.id.list_change_letter_allocation);
        adapter = new AddAllocationAdapter(context, this.saveList);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private class AddAllocationAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<ChangeLetterAllocationInfo> dataList;
        private LayoutInflater inflater;

        public AddAllocationAdapter(Context context, ArrayList<ChangeLetterAllocationInfo> dataList) {
            this.context = context;
            this.dataList = dataList;
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
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
                convertView = inflater.inflate(R.layout.list_item_change_letter_allocation, null);
                holder = new ViewHolder(convertView, position);
                convertView.setTag(holder);    //把convertview和holder绑定
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.edtAllocationPro.setText(saveList.get(position).getConfig_item());
            holder.edtChangeContent.setText(saveList.get(position).getChange_content());
            holder.edtPrice.setText(saveList.get(position).getPrice() + "");
            holder.edtManHour.setText(saveList.get(position).getMan_hour() + "");
            return convertView;
        }

        class ViewHolder {
            private EditText edtAllocationPro;
            private EditText edtChangeContent;
            private EditText edtPrice;
            private EditText edtManHour;
            private ImageView imgAdd, imgDelete;

            public ViewHolder(View v, final int position) {
                edtAllocationPro = (EditText) v.findViewById(R.id.edt_allocation_pro);
                edtChangeContent = (EditText) v.findViewById(R.id.edt_change_content);
                edtPrice = (EditText) v.findViewById(R.id.edt_price);
                edtManHour = (EditText) v.findViewById(R.id.edt_man_hour);
                imgAdd = (ImageView) v.findViewById(R.id.img_add);
                imgDelete = (ImageView) v.findViewById(R.id.img_delete);

                edtAllocationPro.setTag(position);
                edtChangeContent.setTag(position);
                edtPrice.setTag(position);
                edtManHour.setTag(position);

                outOfScroll(edtAllocationPro);
                outOfScroll(edtChangeContent);
                outOfScroll(edtPrice);
                outOfScroll(edtManHour);

                edtAllocationPro.addTextChangedListener(new AllocationTextListener(1, this));
                edtChangeContent.addTextChangedListener(new AllocationTextListener(2, this));
                edtPrice.addTextChangedListener(new AllocationTextListener(3, this));
                edtManHour.addTextChangedListener(new AllocationTextListener(4, this));
                imgAdd.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveList.add(position + 1, new ChangeLetterAllocationInfo("", "", "", ""));
                        adapter.notifyDataSetChanged();
                    }
                });
                imgDelete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onClick:imgDelete        " + position);
                        if (saveList.size() > 1) {
                            saveList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }
        private void outOfScroll(EditText editText){
            editText.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
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
                        position = (int) holder.edtAllocationPro.getTag();
                        saveEdtInfo(1, s.toString(), position);
                        break;
                    case 2:
                        position = (int) holder.edtChangeContent.getTag();
                        saveEdtInfo(2, s.toString(), position);
                        break;
                    case 3:
                        position = (int) holder.edtPrice.getTag();
                        saveEdtInfo(3, s.toString(), position);
                        break;
                    case 4:
                        position = (int) holder.edtManHour.getTag();
                        saveEdtInfo(4, s.toString(), position);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }
    }

    private void saveEdtInfo(int type, String str, int position) {
        if (saveList.get(0) != null) {
            switch (type) {
                case 1:
                    Log.i(TAG, "saveEdtInfo:        " + position);
                    saveList.get(position).setConfig_item(str);
                    break;
                case 2:
                    saveList.get(position).setChange_content(str);
                    break;
                case 3:
                    saveList.get(position).setPrice(str);
                    break;
                case 4:
                    saveList.get(position).setMan_hour(str);
                    break;
            }
        }
    }
}
