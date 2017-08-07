package com.shanghaigm.dms.view.activity.ck;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.AllocationAddChooseUndefaultInfo;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.view.activity.BaseActivity;

import java.util.ArrayList;

public class AllocationAddChooseUndefaultActivity extends BaseActivity {
    private ArrayList<AllocationAddChooseUndefaultInfo> list = new ArrayList<>();
    private ArrayList<AllocationAddChooseUndefaultInfo> addList = new ArrayList<>();
    private static String TAG = "AllocationAddChoose";
    private ListView listView;
    private GetTextAdapter textAdapter;
    private Button btnSave, btnBack;
    public static String UNDEFAULT_ADD_INFO = "add_allocation";
    public static String IS_ADD = "is_add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocation_add_choose_undefault);

        initView();
        initData();
        setUpView();
    }

    private void setUpView() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getNum() > 0) {
//                        addList.add(new AllocationAddChooseUndefaultInfo(list.get(0).getAssembly(),list.get(i).getConfig_information(), list.get(i).getPrice(), list.get(i).getNum(), list.get(i).getRemarks()));
                    }
                }
                Log.i(TAG, "onClick: " + addList.size());
                if (addList.size() > 0) {
//                    Intent intent = new Intent(AllocationAddChooseUndefaultActivity.this, OrderAddActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean(AllocationAddChooseUndefaultActivity.IS_ADD, true);
//                    bundle.putSerializable(AllocationAddChooseUndefaultActivity.UNDEFAULT_ADD_INFO, addList);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AllocationAddChooseUndefaultActivity.this, "数量大于0才可更改", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_allocation_choose_undefault);
        btnSave = (Button) findViewById(R.id.btn_allocation_save);
        btnBack = (Button) findViewById(R.id.btn_allocation_back);
    }

    /**
     *
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        list = (ArrayList<AllocationAddChooseUndefaultInfo>) bundle.getSerializable(OrderDetailInfoAllocation.UNDEFALT_ALLOCATION_INFO);
        textAdapter = new GetTextAdapter(this, list);
        listView.setAdapter(textAdapter);
    }

    /**
     * @param type
     * @param str
     */
    private void saveAddAllocationInfo(int type, String str, int position) {
        switch (type) {
            case 1:
                list.get(position).setPrice(Double.parseDouble(str));
                break;
            case 2:
                list.get(position).setNum(Integer.parseInt(str));
                break;
            case 3:
                list.get(position).setRemarks(str);
                break;

        }
    }

    public class GetTextAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<AllocationAddChooseUndefaultInfo> dataList;
        private LayoutInflater inflater;

        public GetTextAdapter(Context context, ArrayList<AllocationAddChooseUndefaultInfo> dataList) {
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_allocation_choose_undefault, null);
                holder = new ViewHolder(convertView, position);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AllocationAddChooseUndefaultInfo info = dataList.get(position);
            holder.system.setText(info.getConfig_information());
            holder.price.setText(info.getPrice().toString());
            holder.num.setText(info.getNum() + "");
            holder.remarks.setText(info.getRemarks());
            return convertView;
        }

        private class ViewHolder {
            private TextView system;
            private EditText price, num, remarks;

            /**
             * @param v
             * @param position
             */
            public ViewHolder(View v, int position) {
                system = (TextView) v.findViewById(R.id.text_allocation_system);
                price = (EditText) v.findViewById(R.id.edt_allocation_price);
                num = (EditText) v.findViewById(R.id.edt_allocation_num);
                remarks = (EditText) v.findViewById(R.id.edt_allocation_remarks);
                price.setTag(position);         //存位置
                num.setTag(position);
                remarks.setTag(position);
                price.addTextChangedListener(new TextSwitcher(1, this));
                num.addTextChangedListener(new TextSwitcher(2, this));
                remarks.addTextChangedListener(new TextSwitcher(3, this));
            }
        }

        class TextSwitcher implements TextWatcher {
            private ViewHolder mHolder;
            private int type;

            public TextSwitcher(int type, ViewHolder mHolder) {
                this.mHolder = mHolder;
                this.type = type;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    switch (type) {
                        case 1:
                            if (!s.toString().equals(".")) {
                                int position = (int) mHolder.price.getTag();
                                ((AllocationAddChooseUndefaultActivity) context).saveAddAllocationInfo(1, s.toString(), position);
                            }
                            break;
                        case 2:
                            int position1 = (int) mHolder.num.getTag();
                            ((AllocationAddChooseUndefaultActivity) context).saveAddAllocationInfo(2, s.toString(), position1);
                            break;
                        case 3:
                            int position2 = (int) mHolder.remarks.getTag();
                            ((AllocationAddChooseUndefaultActivity) context).saveAddAllocationInfo(3, s.toString(), position2);
                            break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }
    }
}
