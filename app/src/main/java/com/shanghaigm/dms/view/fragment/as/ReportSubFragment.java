package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.ModelInfo;
import com.shanghaigm.dms.model.entity.as.ReportQueryInfoBean;
import com.shanghaigm.dms.model.entity.ck.FragmentInfo;
import com.shanghaigm.dms.model.entity.common.TableInfo;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.as.HomeActivity;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;
import com.shanghaigm.dms.view.activity.as.ReportUpdateActivity;
import com.shanghaigm.dms.view.adapter.TablePagerAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.MmPopupWindow;
import com.shanghaigm.dms.view.widget.ReviewTable;
import com.shanghaigm.dms.view.widget.WrapHeightViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReportSubFragment extends BaseFragment {
    private static ReportSubFragment fragment;
    public static String FILE_DATA_CLEAR = "file_data_clear";
    private Button btn_add;
    private Button btn_query;
    private static String TAG = "OrderReviewFragment";
    private ImageView vpRight, vpLeft;
    private EditText edt_model, edt_car_sign, edt_state, edt_id;
    private TextView pageNumText;
    private LoadingDialog dialog;

    private ArrayList<ModelInfo> modelInfos;
    private ArrayList<TableInfo> tableInfos;
    private ImageView img_first, img_last;
    private WrapHeightViewPager vp;
    private TablePagerAdapter adapter;
    private RelativeLayout.LayoutParams lp = new RelativeLayout.
            LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    private Boolean isQuery = false;        //是否已经查询
    private int page, pages;       //显示页数,总页数
    private DmsApplication app;
    private ArrayList<ReviewTable> tables;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_sub, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle b = new Bundle();
//                b.putString(ReportSubFragment.FILE_DATA_CLEAR, "clear");
//                goToActivity(ReportAddActivity.class, b);
                goToActivity(ReportUpdateActivity.class);
            }
        });
        edt_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_MODELS, null, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        JSONObject object = (JSONObject) responseObj;
                        try {
                            JSONArray array_model = object.getJSONArray("resultEntity");
                            ArrayList<PopListInfo> list_model_pop = new ArrayList<PopListInfo>();
                            list_model_pop.add(new PopListInfo(""));
                            modelInfos = new ArrayList<ModelInfo>();
                            for (int i = 0; i < array_model.length(); i++) {
                                String id = array_model.getJSONObject(i).getString("models_Id");
                                String model_name = array_model.getJSONObject(i).getString("models_name");
                                list_model_pop.add(new PopListInfo(model_name));
                                modelInfos.add(new ModelInfo(id, model_name));
                            }
                            MmPopupWindow pop_model = new MmPopupWindow(getActivity(), edt_model, list_model_pop, 4);
                            pop_model.showPopup(edt_model);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
            }
        });
        edt_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] s = new String[]{"已提交", "未提交", "已驳回"};
                ArrayList<PopListInfo> infos = new ArrayList<PopListInfo>();
                infos.add(new PopListInfo(""));
                for (int i = 0; i < s.length; i++) {
                    infos.add(new PopListInfo(s[i]));
                }
                MmPopupWindow popupWindow = new MmPopupWindow(getActivity(), edt_state, infos, 5);
                popupWindow.showPopup(edt_state);
            }
        });
        initViewPager();
        vpLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page > 0 && isQuery) {   //已查询，且不为第一页
                    page--;
                    requestOrderInfo(4);
                    setPages(page + 1, pages);
                }
            }
        });
        vpRight.setOnClickListener(new View.OnClickListener() {
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
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;
                if (tableInfos != null) {
                    tableInfos.clear();
                }
                requestOrderInfo(1);
            }
        });
        reQuery(edt_car_sign);
        reQuery(edt_id);
        reQuery(edt_model);
        reQuery(edt_state);
    }

    private void requestOrderInfo(final int type) {

        //如果有，直接显示
        if (type != 1) {       //已经查询过
            tables.clear();
            if (tableInfos.size() > 0) {
                if (tableInfos.get(page).isAdded) {    //满足即取出显示返回
                    for (TableInfo tableInfo : tableInfos) {
                        tables.add((ReviewTable) tableInfo.table);
                    }
                    adapter.notifyDataSetChanged();     //刷新完毕就无需再走下一步
                    vp.setAdapter(adapter);
                    vp.setCurrentItem(page);
                    return;
                }
            }
        }

        dialog.showLoadingDlg();
        String model = "", state = "", car_id = "";
        String model_id = "";
        if (!edt_model.getText().toString().equals("")) {
            model = edt_model.getText().toString();
            for (ModelInfo info : modelInfos) {
                if (info.model_name.equals(model)) {
                    model_id = info.model_Id;
                }
            }
        }
        int stateId = -1;
        if (!edt_state.getText().toString().equals("")) {
            state = edt_state.getText().toString();
            stateId = getState(state);
        }
        String report_id = "";
        if (!edt_id.getText().toString().equals("")) {
            report_id = edt_id.getText().toString();
        }
        if (!edt_car_sign.getText().toString().equals("")) {
            car_id = edt_car_sign.getText().toString();
        }
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            object.put("feedback_date", "");
            object.put("daily_code", report_id);
            object.put("models_Id", model_id);
            if (stateId == -1) {
                object.put("state", "");
            } else {
                object.put("state", stateId);
            }
            object.put("car_sign", car_id);
            array.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("page", page + 1 + "");
        params.put("rows", "8");
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        params.put("roleCode", app.getRoleCode());
        params.put("dailyStr", array.toString());
        Log.i(TAG, "requestOrderInfo: " + app.getJobCode());
        OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_QUERY_REPORT_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                dialog.dismissLoadingDlg();
                ReportQueryInfoBean info = GsonUtil.GsonToBean(responseObj.toString(), ReportQueryInfoBean.class);
                List<ReportQueryInfoBean.ResultEntity.ReportInfo> rows = info.resultEntity.rows;
                int total = info.resultEntity.total;
                if (total == 0) {
                    Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                    tables.clear();
                    tableInfos.clear();
                    ReviewTable table = new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 5);
                    tables.add(table);
                }
                if (total % 8 == 0) {
                    pages = total / 8;
                } else {
                    pages = total / 8 + 1;
                }
                ArrayList<PaperInfo> paperInfos = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    ReportQueryInfoBean.ResultEntity.ReportInfo reportInfo = rows.get(i);
                    String daily_code = reportInfo.daily_code;
                    String model = reportInfo.models_name;
                    String car_sign = reportInfo.car_sign;
                    int state = reportInfo.state;
                    int daily_id = reportInfo.daily_id;
                    paperInfos.add(new PaperInfo(getActivity(), daily_code, model, car_sign, state, daily_id, 8));
                }
                ReviewTable table = new ReviewTable(getActivity(), paperInfos, 8);
                table.setLayoutParams(lp);
                //加入infos,更新数据
                if (total != 0) {
                    tables.clear();
                    //加入空的tables占位
                    if (type == 1) {
                        tableInfos.clear();
                        for (int i = 0; i < pages; i++) {
                            tableInfos.add(new TableInfo(pages, new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 5), false));
                        }
                        isQuery = true;
                    }
                    tableInfos.remove(page);
                    tableInfos.add(page, new TableInfo(page, table, true));
                    for (TableInfo tableInfo : tableInfos) {
                        tables.add((ReviewTable) tableInfo.table);
                    }
                }
                Log.i(TAG, "onSuccess:size          " + tables.size() + "     tableinfos       " + tableInfos.size());
                adapter.notifyDataSetChanged();
                vp.setAdapter(adapter);
                Log.i(TAG, "onSuccess:page          " + page);
                vp.setCurrentItem(page);
                //查询时，设置页数
                if (type == 1 && total != 0) {
                    setPages(page + 1, pages);
                }
                if (total == 0) {
                    setPages(0, 0);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private int getState(String s) {
        int state = 0;
        switch (s) {
            case "未提交":
                state = 1;
                break;
            case "已提交":
                state = 2;
                break;
            case "已驳回":
                state = 3;
                break;
        }
        return state;
    }

    private void reQuery(EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isQuery = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setPages(int page, int pages) {
        pageNumText.setText("页数:" + page + "/" + pages);
    }

    private void initViewPager() {

        tableInfos = new ArrayList<>();
        ReviewTable table = new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 8);
        table.setLayoutParams(lp);
        tables = new ArrayList<>();
        tables.add(table);
        adapter = new TablePagerAdapter(getActivity(), tables);
        vp.setAdapter(adapter);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
    }

    private void initView(View v) {
        btn_add = (Button) v.findViewById(R.id.btn_add);
        btn_query = (Button) v.findViewById(R.id.btn_query);

        pageNumText = (TextView) v.findViewById(R.id.pages_num);
        vp = (WrapHeightViewPager) v.findViewById(R.id.report_query_table);
        vpLeft = (ImageView) v.findViewById(R.id.viewpager_left);
        vpRight = (ImageView) v.findViewById(R.id.viewpager_right);

        edt_car_sign = (EditText) v.findViewById(R.id.edt_car_id);
        edt_model = (EditText) v.findViewById(R.id.edt_model);
        edt_state = (EditText) v.findViewById(R.id.edt_state);
        edt_id = (EditText) v.findViewById(R.id.edt_report_id);

        img_first = (ImageView) v.findViewById(R.id.viewpager_first);
        img_last = (ImageView) v.findViewById(R.id.viewpager_last);
        app = DmsApplication.getInstance();
        dialog = new LoadingDialog(getActivity(), "正在加载");
    }

    public void refreshTable() {
        page = 0;
        if (tableInfos != null) {
            tableInfos.clear();
        }
        requestOrderInfo(1);
    }

    public static ReportSubFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportSubFragment();
        }
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isHidden()) {
            if (!((HomeActivity) getActivity()).isBackClick) {
                ((HomeActivity) getActivity()).fragmentInfos.add(new FragmentInfo(2));
            }
            ((HomeActivity) getActivity()).isBackClick = false;
        }
    }
}
