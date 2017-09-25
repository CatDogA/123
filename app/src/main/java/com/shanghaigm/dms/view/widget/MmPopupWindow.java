package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.view.adapter.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/5/23.
 */

public class MmPopupWindow extends PopupWindow{
    private OnItemClickListener listener;
    private ArrayList<PopListInfo> list;
    private EditText editText;
    private ListAdapter adapter;
    private String choice1,choice2,choice3,choice4,choice5,choice6;
    private Context context;
    //todo-动态控制pop位置
    private int x,y;

    public MmPopupWindow(Context context, final EditText editText, final ArrayList<PopListInfo> list,int divideNum){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View popupView = layoutInflater.inflate(R.layout.mm_list_popup,null,false);
        this.setContentView(popupView);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(h/divideNum);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.context = context;
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.list = list;
        ListView lv = (ListView) popupView.findViewById(R.id.pop_list);
        adapter = new ListAdapter(context,R.layout.list_item_pop, BR.str,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(list.get(position).getStr());
                MmPopupWindow.this.dismiss();
            }
        });
    }

    //为省市选择封装两个构造方法
//    public MmPopupWindow(final Context context, final EditText editText, final ArrayList<PopListInfo> list, final ProvincialCitysInfo info){
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View popupView = layoutInflater.inflate(R.layout.mm_list_popup,null,false);
//        this.setContentView(popupView);
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int h = wm.getDefaultDisplay().getHeight();
//        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        this.setHeight(h/3);
//        this.setFocusable(true);
//        this.setOutsideTouchable(true);
//        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        this.update();
//
//        this.list = list;
//        ListView lv = (ListView) popupView.findViewById(R.id.pop_list);
//        adapter = new ListAdapter(context,R.layout.list_item_pop, BR.str,list);
//        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
//                int provinceId = info.resultEntity.get(position+1).id;
//                Map<String,Object> params = new HashMap<>();
//                params.put("pid",provinceId);
//                CommonOkHttpClient.get(CommonRequest.createGetRequestInt(Constant.URL_GET_PROVINCE_CITY,params), new DisposeDataHandle(new DisposeDataListener() {
//                    @Override
//                    public void onSuccess(Object responseObj) {
//                        Log.i("luo", "onSuccess: "+responseObj.toString());
//                        JSONObject object = (JSONObject) responseObj;
//                        try {
//                            JSONArray array = object.getJSONArray("resultEntity");
//                            ArrayList<PopListInfo> citys = new ArrayList<>();
//                            for(int i=0;i<array.length();i++){
//                                citys.add(new PopListInfo(array.getJSONObject(i).getString("name")));
//                            }
//                            MmPopupWindow provinceWindow = MmPopupWindow.this;
//                            MmPopupWindow cityPopUp = new MmPopupWindow(context,provinceWindow,list.get(position).getStr(),editText,citys);
//                            cityPopUp.showPopupWindow(editText);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onFailure(Object reasonObj) {
//
//                    }
//                }));
//            }
//        });
//    }
//
//    public MmPopupWindow(Context context, final MmPopupWindow provinceWindow, final String provinceName, final EditText editText, final ArrayList<PopListInfo> list){
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View popupView = layoutInflater.inflate(R.layout.mm_list_popup,null,false);
//        this.setContentView(popupView);
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int h = wm.getDefaultDisplay().getHeight();
//        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        this.setHeight(h/3);
//        this.setFocusable(true);
//        this.setOutsideTouchable(true);
//        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        this.update();
//        this.list = list;
//        ListView lv = (ListView) popupView.findViewById(R.id.pop_list);
//        adapter = new ListAdapter(context,R.layout.list_item_pop, BR.str,list);
//        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                editText.setText(provinceName+list.get(position).getStr());
//                MmPopupWindow.this.dismiss();
//                provinceWindow.dismiss();
//            }
//        });
//    }


    public MmPopupWindow(final Context context, final EditText editText, final String choice1, final String choice2, final String choice3, final String choice4, final String choice5, final String choice6){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View popupView = layoutInflater.inflate(R.layout.mm_popup,null,false);
        this.editText = editText;
        //设置PopupWindow的view
        this.setContentView(popupView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.choice5 = choice5;
        this.choice6 = choice6;

        TextView popChoice1 = (TextView) popupView.findViewById(R.id.mm_pop_choice1);
        TextView popChoice2 = (TextView) popupView.findViewById(R.id.mm_pop_choice2);
        TextView popChoice3 = (TextView) popupView.findViewById(R.id.mm_pop_choice3);
        TextView popChoice4 = (TextView) popupView.findViewById(R.id.mm_pop_choice4);
        TextView popChoice5 = (TextView) popupView.findViewById(R.id.mm_pop_choice5);
        TextView popChoice6 = (TextView) popupView.findViewById(R.id.mm_pop_choice6);

        if(choice1==null){
            popChoice1.setVisibility(View.GONE);
        }else{
            popChoice1.setText(choice1);
            popChoice1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(choice1);
                    MmPopupWindow.this.dismiss();
                }
            });
        }
        if(choice2==null){
            popChoice2.setVisibility(View.GONE);
        }else{
            popChoice2.setText(choice2);
            popChoice2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(choice2);
                    MmPopupWindow.this.dismiss();
                }
            });
        }
        if(choice3==null){
            popChoice3.setVisibility(View.GONE);
        }else{
            popChoice3.setText(choice3);
            popChoice3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(choice3);
                    MmPopupWindow.this.dismiss();
                }
            });
        }
        if(choice4==null){
            popChoice4.setVisibility(View.GONE);
        }else{
            popChoice4.setText(choice1);
            popChoice4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(choice4);
                    MmPopupWindow.this.dismiss();
                }
            });
        }
        if(choice5==null){
            popChoice5.setVisibility(View.GONE);
        }else{
            popChoice5.setText(choice1);
            popChoice5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(choice5);
                    MmPopupWindow.this.dismiss();
                }
            });
        }
        if(choice6==null){
            popChoice6.setVisibility(View.GONE);
        }else{
            popChoice6.setText(choice1);
            popChoice6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(choice6);
                    MmPopupWindow.this.dismiss();
                }
            });
        }
    }

    public void showPopup(View parent){
        if(!this.isShowing()){
            this.showAsDropDown(parent,0, 0);
        }else {
            this.dismiss();
        }
    }
    public void showPopupWindow(View parent){
        if(!this.isShowing()){
            this.showAsDropDown(parent,parent.getWidth(),100);
        }else {
            this.dismiss();
        }
    }
    public void hidePopup(MmPopupWindow popupWindow){
        popupWindow.dismiss();
    }
    public void hidePopup(){
        this.dismiss();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void popupChoice1OnClick();
        void popupChoice2OnClick();
        void popupChoice3OnClick();
        void popupChoice4OnClick();
        void popupChoice5OnClick();
        void popupChoice6OnClick();
    }
}
