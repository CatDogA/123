package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.common.CustomManageInfo;
import com.shanghaigm.dms.model.entity.common.TableInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.CommonOkHttpClient;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.activity.ck.WeekReportAddActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28.
 */

public class AreaComCustomSearchPop extends BaseActivity {
    private static String TAG = "OrderAddNamePopWindow";
    public static String GET_CUSTOM_INFO = "GET_CUSTOM_INFO";
    private TextView txt_page_num;
    private LoadingDialog dialog;
    private Button btnSearch, btnSure;
    private ImageView vp_right, vp_left;
    private ArrayList<TableInfo> tableInfos;
    private ImageView img_first, img_last;
    private WrapHeightViewPager vp;
    private TablePagerAdapter adapter;
    private JSONArray areaArray, modelArray, natureArray, typeArray;
    private RelativeLayout.LayoutParams lp = new RelativeLayout.
            LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    private Boolean isQuery = false;        //是否已经查询
    private int page, pages;       //显示页数,总页数
    private DmsApplication app;
    private ArrayList<CustomInfoTable> tables;
    private String customName;
    private EditText customNatureEdt, customTypeEdt, customLevel1, customLevel2, customNameEdt;
    private int flag = 0;
    private RadioGroup radioGroup;
    private RelativeLayout rlBack,rlEnd;
    private int inOutIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_area_com_custom_name_search);
        initIntent();
        initView();
        setUpView();
    }

    private void initIntent() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            switch (b.getString(GET_CUSTOM_INFO)) {
                case "week":
                    flag = 1;
                    break;
                case "day":
                    flag = 2;
                    break;
                case "day2":
                    flag = 3;
                    break;
            }
        }
    }

    private void setUpView() {
        txt_page_num.setText("页数:" + "0" + "/" + pages);
        initViewPager();
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(flag==2){
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId==R.id.rd_inside){
                        inOutIndex = 1;
                    }
                    if(checkedId==R.id.rd_out){
                        inOutIndex = 2;
                    }
                }
            });
            radioGroup.check(R.id.rd_inside);
        }
        customNatureEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> param = new HashMap<>();
                param.put("filed", "crm_company_nature");
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_CUSTOM_INFO, param, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        natureArray = (JSONArray) responseObj;
                        ArrayList<PopListInfo> natures = new ArrayList<>();
                        try {
                            natures.add(new PopListInfo(""));
                            for (int i = 0; i < natureArray.length(); i++) {
                                natures.add(new PopListInfo(natureArray.getJSONObject(i).getString("date_value")));
                            }
                            MmPopupWindow naturePopup = new MmPopupWindow(context, customNatureEdt, natures, 3);
                            naturePopup.showPopup(customNatureEdt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        Log.i(TAG, "onFailure: " + reasonObj.toString());
                    }
                });
            }
        });
        customTypeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> param = new HashMap<>();
                param.put("filed", "crm_company_type");
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_CUSTOM_INFO, param, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        typeArray = (JSONArray) responseObj;
                        ArrayList<PopListInfo> natures = new ArrayList<>();
                        try {
                            natures.add(new PopListInfo(""));
                            for (int i = 0; i < typeArray.length(); i++) {
                                natures.add(new PopListInfo(typeArray.getJSONObject(i).getString("date_value")));
                            }
                            MmPopupWindow typePopup = new MmPopupWindow(context, customTypeEdt, natures, 3);
                            typePopup.showPopup(customTypeEdt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        Log.i(TAG, "onFailure: " + reasonObj.toString());
                    }
                });
            }
        });
        customLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> level1s = new ArrayList<PopListInfo>();
                level1s.add(new PopListInfo(""));
                level1s.add(new PopListInfo("1"));
                level1s.add(new PopListInfo("2"));
                level1s.add(new PopListInfo("3"));
                level1s.add(new PopListInfo("4"));
                MmPopupWindow levelPopup = new MmPopupWindow(context, customLevel1, level1s, 3);
                levelPopup.showPopup(customLevel1);
            }
        });
        customLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> level1s = new ArrayList<PopListInfo>();
                level1s.add(new PopListInfo(""));
                level1s.add(new PopListInfo("A"));
                level1s.add(new PopListInfo("B"));
                level1s.add(new PopListInfo("C"));
                level1s.add(new PopListInfo("D"));
                MmPopupWindow levelPopup = new MmPopupWindow(context, customLevel2, level1s, 3);
                levelPopup.showPopup(customLevel2);
            }
        });
        vp_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page > 0 && isQuery) {   //已查询，且不为第一页
                    page--;
                    requestOrderInfo(4);
                    setPages(page + 1, pages);
                }
            }
        });
        vp_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page < pages - 1 && isQuery) {
                    page++;
                    requestOrderInfo(5);
                    setPages(page + 1, pages);
                }
            }
        });
        img_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuery) {
                    page = 0;
                    requestOrderInfo(2);
                    setPages(page + 1, pages);
                }
            }
        });
        img_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuery) {
                    page = pages - 1;
                    requestOrderInfo(3);
                    setPages(page + 1, pages);
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;
                if (tableInfos != null) {
                    tableInfos.clear();
                }
                requestOrderInfo(1);
            }
        });
    }

    private void initView() {
        vp = (WrapHeightViewPager) findViewById(R.id.vp_search_order_add);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSure = (Button) findViewById(R.id.btn_sure);
        vp_right = (ImageView) findViewById(R.id.viewpager_right);
        vp_left = (ImageView) findViewById(R.id.viewpager_left);
        txt_page_num = (TextView) findViewById(R.id.pages_num);
        dialog = new LoadingDialog(this, "正在加载");
        img_first = (ImageView) findViewById(R.id.viewpager_first);
        img_last = (ImageView) findViewById(R.id.viewpager_last);
        app = DmsApplication.getInstance();
        customTypeEdt = (EditText) findViewById(R.id.edt_custom_type);
        customLevel1 = (EditText) findViewById(R.id.edt_custom_level);
        customLevel2 = (EditText) findViewById(R.id.edt_custom_level2);
        customNatureEdt = (EditText) findViewById(R.id.edt_custom_nature);
        customNameEdt = (EditText) findViewById(R.id.edt_custom_name);

        radioGroup = (RadioGroup) findViewById(R.id.rg_plan);
        //标题
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);
        rlEnd = (RelativeLayout) findViewById(R.id.rl_out);
        rlEnd.setVisibility(View.GONE);
    }

    private void requestOrderInfo(final int type) {
        //如果有，直接显示
        if (type != 1) {       //已经查询过
            tables.clear();
            if (tableInfos.size() > 0) {
                if (tableInfos.get(page).isAdded) {    //满足即取出显示返回
                    for (TableInfo tableInfo : tableInfos) {
                        tables.add((CustomInfoTable) tableInfo.table);
                    }
                    adapter.notifyDataSetChanged();     //刷新完毕就无需再走下一步
                    vp.setCurrentItem(page);
                    return;
                }
            }
        }
        dialog.showLoadingDlg();
        String customName = customNameEdt.getText().toString();
        String sCustomLevel1 = customLevel1.getText().toString();
        String sCustomLevel2 = customLevel2.getText().toString();
        String customNature = customNatureEdt.getText().toString();
        String customType = customTypeEdt.getText().toString();
        Object natureCode = null;
        Object typeCode = null;
        if (!customNature.equals("")) {
            natureCode = getParam(natureArray, customNature, "date_value", "date_key");
        }
        if (!customType.equals("")) {
            typeCode = getParam(typeArray, customType, "date_value", "date_key");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("company_name", customName);
        if (typeCode != null) {
            params.put("company_type", typeCode.toString());
        }
        if (natureCode != null) {
            params.put("company_nature", natureCode.toString());
        }
        params.put("level_num", sCustomLevel1);
        params.put("level_letter", sCustomLevel2);
        params.put("page", page + 1);
        params.put("rows", 10);
        params.put("login_name", app.getAccount());
        String url = Constant.URL_AREA_COM_CUSTOM_INFO_LIST;
        if (flag == 2) {
            url = Constant.URL_AREA_COM_CUSTOM_INFO_LIST_DAY;
            params.put("is_jihua",inOutIndex);
        }
        Log.i(TAG, "requestOrderInfo: " + app.getAccount() + "        " + page);
        Log.i(TAG, "requestOrderInfo: " + Constant.URL_AREA_COM_CUSTOM_INFO_LIST);
        CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(url, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                JSONObject object = (JSONObject) responseObj;
                Log.i(TAG, "onSuccess: " + object.toString());
                try {
                    int total = object.getInt("total");
                    JSONArray rows = object.getJSONArray("rows");
                    if (total == 0) {
                        Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
                        tables.clear();
                        tableInfos.clear();
                        CustomInfoTable table = new CustomInfoTable(AreaComCustomSearchPop.this, new ArrayList<CustomManageInfo>(), flag);
                        tables.add(table);
                    }
                    if (total % 10 == 0) {
                        pages = total / 10;
                    } else {
                        pages = total / 10 + 1;
                    }

                    ArrayList<CustomManageInfo> customInfos = new ArrayList<>();
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject searchInfo = rows.getJSONObject(i);
                        String customType = "";
                        String customLevel1 = "";
                        String customLevel2 = "";
                        if(!searchInfo.optString("company_type_value").equals("null")){
                            customType = searchInfo.optString("company_type_value");
                        }
                        if(!searchInfo.optString("level_num").equals("null") ){
                            customLevel1 = searchInfo.optString("level_num");
                        }
                        if(!searchInfo.optString("level_letter").equals("null") ){
                            customLevel2 = searchInfo.optString("level_letter");
                        }
                        if (flag == 2) {
                            customInfos.add(new CustomManageInfo(searchInfo.optString("company_name"), searchInfo.optString("company_nature_value"),customType
                                    ,customLevel1+customLevel2,
                                    "", "", searchInfo.getInt("custome_id")));
                        }else {
                            customInfos.add(new CustomManageInfo(searchInfo.optString("company_name"), searchInfo.optString("company_nature_value"),
                                    customType, customLevel1+customLevel2,
                                    "", "", searchInfo.getInt("custome_id")));
                        }

                    }
                    CustomInfoTable table = new CustomInfoTable(AreaComCustomSearchPop.this, customInfos, flag);
                    table.setLayoutParams(lp);

                    //加入infos,更新数据
                    if (total != 0) {
                        tables.clear();
                        //加入空的tables占位
                        if (type == 1) {
                            tableInfos.clear();
                            for (int i = 0; i < pages; i++) {
                                tableInfos.add(new TableInfo(pages, new CustomInfoTable(AreaComCustomSearchPop.this, new ArrayList<CustomManageInfo>(), flag), false));
                            }
                            isQuery = true;
                        }
                        tableInfos.remove(page);
                        tableInfos.add(page, new TableInfo(page, table, true));
                        for (TableInfo tableInfo : tableInfos) {
                            tables.add((CustomInfoTable) tableInfo.table);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    vp.setAdapter(adapter);
                    vp.setCurrentItem(page);
                    //查询时，设置页数
                    if (type == 1 && total != 0) {
                        setPages(page + 1, pages);
                    }
                    if (total == 0) {
                        setPages(0, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                dialog.dismissLoadingDlg();
                Toast.makeText(context, "无数据", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void initViewPager() {
        tableInfos = new ArrayList<>();
        //添加空的table
        CustomInfoTable table = new CustomInfoTable(AreaComCustomSearchPop.this, new ArrayList<CustomManageInfo>(), flag);
        table.setLayoutParams(lp);
        tables = new ArrayList<>();
        tables.add(table);
        adapter = new TablePagerAdapter(context, tables);
        vp.setAdapter(adapter);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
    }

    private void setPages(int page, int pages) {
        txt_page_num.setText("页数:" + page + "/" + pages);
    }

    /**
     * Created by Tom on 2017/6/26
     */

    public class TablePagerAdapter extends PagerAdapter {
        private ArrayList<CustomInfoTable> tables;
        private int mChildCount = 0;
        private Context context;

        public TablePagerAdapter(Context context, ArrayList<CustomInfoTable> tables) {
            this.context = context;
            this.tables = tables;
        }

        @Override
        public int getCount() {
            if (tables != null && tables.size() > 0) {
                return tables.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(tables.get(position));
            return tables.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    private Object getParam(JSONArray array, String text, String name, String code) {
        String id = "";
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                if (text.equals(object.getString(name))) {
                    id = object.getString(code);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return id;
    }
}
