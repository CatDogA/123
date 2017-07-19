package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.OrderAddSearchInfo;
import com.shanghaigm.dms.view.adapter.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/6/25.
 * 用户名电话搜索
 */

public class SearchPopupWindow extends PopupWindow {
    private static String TAG = "SearchPopupWindow";
    private ListView listView;
    private ListAdapter adapter;
    private EditText edtName, edtTel;
    private Button btnSure;
    private ArrayList<OrderAddSearchInfo> listData = new ArrayList<>();
    private String nameStr = "", telStr = "";

    public SearchPopupWindow(Context context, final ArrayList<String> list) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.pop_search, null, false);
        this.setContentView(v);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(h / 2);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        edtName = (EditText) v.findViewById(R.id.edt_name);
        edtTel = (EditText) v.findViewById(R.id.edt_tel);
        btnSure = (Button) v.findViewById(R.id.btn_sure);
        listView = (ListView) v.findViewById(R.id.pop_search_list);
        final ArrayList<OrderAddSearchInfo> list1 = new ArrayList<>();
        for (OrderAddSearchInfo info : list1) {
            listData.add(info);
        }
        adapter = new ListAdapter(context, R.layout.list_item_pop_search, BR.info, listData);
        listView.setAdapter(adapter);
        listView.setSelector(R.color.colorBlue);
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameStr = s.toString();
                if (!nameStr.equals("") && telStr.equals("")) {
                    listData.clear();
                    for (int i = 0; i < list1.size(); i++) {
                        if (list1.get(i).getName().substring(0, nameStr.length()).equals(nameStr)) {
                            listData.add(list1.get(i));
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }
                    }
                } else if (nameStr.equals("") && telStr.equals("")) {
                    listData.clear();
                    for (OrderAddSearchInfo info : list1) {
                        listData.add(info);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                } else if (nameStr.equals("") && !telStr.equals("")) {
                    listData.clear();
                    for (int i = 0; i < list1.size(); i++) {
                        if (list1.get(i).getTel().substring(0, telStr.length()).equals(telStr)) {
                            listData.add(list1.get(i));
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }
                    }
                    //都有查询数据
                } else if (!nameStr.equals("") && !telStr.equals("")) {
                    listData.clear();
                    for (int i = 0; i < list1.size(); i++) {
                        if (list1.get(i).getTel().substring(0, telStr.length()).equals(telStr) && list1.get(i).getName().substring(0, nameStr.length()).equals(nameStr)) {
                            listData.add(list1.get(i));
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                telStr = s.toString();
                if (nameStr.equals("") && !telStr.equals("")) {
                    listData.clear();
                    for (int i = 0; i < list1.size(); i++) {
                        if (list1.get(i).getTel().substring(0, telStr.length()).equals(telStr)) {
                            listData.add(list1.get(i));
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }
                    }
                } else if (nameStr.equals("") && telStr.equals("")) {
                    listData.clear();
                    for (OrderAddSearchInfo info : list1) {
                        listData.add(info);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                } else if (!nameStr.equals("") && telStr.equals("")) {
                    listData.clear();
                    for (int i = 0; i < list1.size(); i++) {
                        if (list1.get(i).getName().substring(0, nameStr.length()).equals(nameStr)) {
                            listData.add(list1.get(i));
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }
                    }
                } else if (!nameStr.equals("") && !telStr.equals("")) {
                    listData.clear();
                    for (int i = 0; i < list1.size(); i++) {
                        if (list1.get(i).getTel().substring(0, telStr.length()).equals(telStr) && list1.get(i).getName().substring(0, nameStr.length()).equals(nameStr)) {
                            listData.add(list1.get(i));
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
}
