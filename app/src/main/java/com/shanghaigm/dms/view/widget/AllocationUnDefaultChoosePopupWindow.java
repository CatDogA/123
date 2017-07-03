package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.AllocationAddChooseUndefaultInfo;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/6/22.
 */

public class AllocationUnDefaultChoosePopupWindow extends PopupWindow {
    private DmsApplication app = DmsApplication.getInstance();
    private static String TAG = "AllocationUnDefaultChoosePopupWindow";
    //saveList用来先存着选配数据,list接收listview传来的选配数据,appInfolist用来全局储存,saveUndefaultList存储用来刷新界面的数据
    private ArrayList<AllocationAddChooseUndefaultInfo> list, saveList, appInfoList;
    private ArrayList<OrderDetailInfoAllocation> saveUndefaultList;
    private int position;
    private GetTextAdapter adapter;
    private ListView listView;
    private Button btnEnSure, btnBack;
    private Handler handler;
    private int type;

    public AllocationUnDefaultChoosePopupWindow(final Context context, final int position, int divideNum, final ArrayList<AllocationAddChooseUndefaultInfo> list, final ArrayList<OrderDetailInfoAllocation> listToChange, final Handler handler, int type) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View popupView = layoutInflater.inflate(R.layout.pop_allocation_add, null, false);
        this.setContentView(popupView);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(h / divideNum);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.position = position;
        this.list = list;
        this.handler = handler;
        this.type = type;
        saveList = new ArrayList<>();
        this.saveList = list;
        btnEnSure = (Button) popupView.findViewById(R.id.btn_allocation_save);
        btnBack = (Button) popupView.findViewById(R.id.btn_allocation_back);
        ListView lv = (ListView) popupView.findViewById(R.id.list_allocation_choose_undefault);
        adapter = new GetTextAdapter(context, list);
        lv.setAdapter(adapter);
        btnEnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appInfoList = new ArrayList<AllocationAddChooseUndefaultInfo>();
                saveUndefaultList = new ArrayList<>();  //存储已经选配好的数据
                Log.i(TAG, "onClick: saveList" + saveList.size() + "saveUndefaultList" + saveUndefaultList.size() + "listToChange" + listToChange.size() + "list" + list.size());
                for (int i = 0; i < saveList.size(); i++) {
                    if (saveList.get(i).getNum() > 0) {
                        appInfoList.add(saveList.get(i));
                        saveUndefaultList.add(new OrderDetailInfoAllocation(listToChange.get(position).getAssembly(), listToChange.get(position).getEntry_name(), saveList.get(i).getConfig_information(), saveList.get(i).getPrice(), saveList.get(i).getNum() + "", saveList.get(i).getRemarks(), list.size(), list, saveList.get(i).getStandard_id()));
                    }
                }
                Log.i(TAG, "onClick: " + list.size() + "    " + saveList.size() + "   " + appInfoList.size() + "     " + saveUndefaultList.size());
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
                hidePopup();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePopup();
            }
        });
    }

    public void showPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public void hidePopup(MmPopupWindow popupWindow) {
        popupWindow.dismiss();
    }

    public void hidePopup() {
        this.dismiss();
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
                price.addTextChangedListener(new GetTextAdapter.TextSwitcher(1, this));
                num.addTextChangedListener(new GetTextAdapter.TextSwitcher(2, this));
                remarks.addTextChangedListener(new GetTextAdapter.TextSwitcher(3, this));
            }
        }

        class TextSwitcher implements TextWatcher {
            private ViewHolder mHolder;
            private int type;

            public TextSwitcher(int type, GetTextAdapter.ViewHolder mHolder) {
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
                                int position = (int) mHolder.price.getTag();            //选配item第几行
                                saveAddAllocationInfo(1, s.toString(), position);
                            }
                            break;
                        case 2:
                            int position1 = (int) mHolder.num.getTag();
                            saveAddAllocationInfo(2, s.toString(), position1);
                            break;
                        case 3:
                            int position2 = (int) mHolder.remarks.getTag();
                            saveAddAllocationInfo(3, s.toString(), position2);
                            break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }
    }

    public void saveAddAllocationInfo(int type, String str, int position) {
        switch (type) {
            case 1:
                saveList.get(position).setPrice(Double.parseDouble(str));
                break;
            case 2:
                saveList.get(position).setNum(Integer.parseInt(str));
                break;
            case 3:
                saveList.get(position).setRemarks(str);
                break;

        }
    }

    //利用接口回调配合handler改变listview
    //1.定义一个回调接口
    public interface CallBack {
        /**
         * @param changeList
         * @param position
         */
        void getResult(ArrayList<OrderDetailInfoAllocation> saveUndefaultList, int position);//将要回调的数据作为参数写在接口方法中
    }

    /**
     * @param callBack position:listview需要修改的条目
     *                 saveUndefaultList:用来修改的内容
     */
    //2.接口回调
    public void getListViewInfo(CallBack callBack) {                 //以接口为参数，并将数据传入接口方法
        callBack.getResult(saveUndefaultList, position);
    }
}
