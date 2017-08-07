package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
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
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;
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
    private EditText edt_model, edt_car_sign, edt_state,edt_id;
    private TextView pageNumText;
    private WrapHeightViewPager vp;
    private ArrayList<PaperInfo> papers;//每页数据
    private int pages = 0;//页数
    private int page = 1;//第几页
    private ArrayList<ReviewTable> tables = new ArrayList<>();//表集合
    private TablePagerAdapter pagerAdapter;
    private DmsApplication app = DmsApplication.getInstance();
    private RelativeLayout.LayoutParams lp = new RelativeLayout.
            LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    private Boolean IsQuery = true;//判断是查询还是更多
    private Boolean IsMore = false;//判断可否请求更多
    private LoadingDialog dialog;
    private ArrayList<ModelInfo> modelInfos;

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
                Bundle b = new Bundle();
                b.putString(ReportSubFragment.FILE_DATA_CLEAR,"clear");
                goToActivity(ReportAddActivity.class,b);
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
                for (int i = 0; i < s.length; i++) {
                    infos.add(new PopListInfo(s[i]));
                }
                MmPopupWindow popupWindow = new MmPopupWindow(getActivity(), edt_state, infos, 5);
                popupWindow.showPopup(edt_state);
            }
        });
        pageNumText.setText("页数:" + "0" + "/" + pages);
        initViewPager();
        vpLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsMore) {
                    int currentItem = vp.getCurrentItem();
                    if (currentItem + 1 == pages) {
                        vpRight.setImageResource(R.mipmap.right_switch_pre);
                    }
                    if (currentItem > 0) {
                        vp.setCurrentItem(--currentItem);
                    }
                    if (currentItem == 0) {
                        vpLeft.setImageResource(R.mipmap.left_switch);
                    }
                    setPages(currentItem + 1, pages);
                }
            }
        });
        vpRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsQuery = false;
                if (IsMore) {
                    int currentItem = vp.getCurrentItem();
                    if (currentItem == 0) {
                        vpLeft.setImageResource(R.mipmap.left_switch_pre);
                    }
                    Log.i(TAG, "onClick:     pages   " + pages + "        currentItem     " + currentItem);
                    if (currentItem + 2 == pages) {
                        vpRight.setImageResource(R.mipmap.right_switch);
                    }
                    Log.i(TAG, "onClick: " + "  currentItem   " + currentItem + "  page   " + page + "   ismore " + IsMore);
                    if (currentItem + 2 == page) {
                        if (page > pages) {
                            Toast.makeText(getActivity(), "已是最后一页", Toast.LENGTH_SHORT).show();
                            vpRight.setImageResource(R.mipmap.right_switch);
                            return;
                        }
                        requestOrderInfo(IsQuery);
                    } else {
                        vp.setCurrentItem(++currentItem);
                        setPages(currentItem + 1, pages);
                    }
                    Log.i(TAG, "onClick: " + "currentItem" + currentItem + "     page" + page);
                }
            }
        });
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsQuery = true;
                IsMore = true;
                page = 1;
                requestOrderInfo(IsQuery);
            }
        });
    }

    private void requestOrderInfo(Boolean isQuery) {
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
        if(!edt_id.getText().toString().equals("")){
            report_id = edt_id.getText().toString();
        }
        if(!edt_car_sign.getText().toString().equals("")){
            car_id = edt_car_sign.getText().toString();
        }
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            object.put("feedback_date","");
            object.put("daily_code",report_id);
            object.put("models_Id", model_id);
            if(stateId==-1){
                object.put("state","");
            }else {
                object.put("state",stateId);
            }
            object.put("car_sign",car_id);
            array.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("page", page + "");
        params.put("rows", "8");
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        params.put("roleCode", app.getRoleCode());
        params.put("dailyStr",array.toString());
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
                if (IsQuery) {
                    tables.clear();
                    tables.add(table);
                    setPages(1, pages);
                    page++;
                } else if (IsMore && !IsQuery) {
                    if (vp.getCurrentItem() + 1 >= pages) {
                        Toast.makeText(getActivity(), "已是最后一页", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tables.add(table);
                    setPages(tables.size(), pages);
                    page++;
                } else {
                    Toast.makeText(getActivity(), "请先查询", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "onSuccess:page " + page);
                Log.i(TAG, "onSuccess:tables " + tables.size());
                pagerAdapter.notifyDataSetChanged();
                vp.setAdapter(pagerAdapter);
                vp.setCurrentItem(page);
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

    private void setPages(int page, int pages) {
        pageNumText.setText("页数:" + page + "/" + pages);
    }

    private void initViewPager() {
        papers = new ArrayList<>();
        ReviewTable table = new ReviewTable(getActivity(), papers, 8);
        table.setLayoutParams(lp);
        tables.add(table);
        pagerAdapter = new TablePagerAdapter(getActivity(), tables);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
        vp.setOnClickListener(null);
        vp.setAdapter(pagerAdapter);
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
        dialog = new LoadingDialog(getActivity(), "正在加载");
    }
    public void refreshTable(){
        IsQuery = true;
        IsMore = true;
        page = 1;
        requestOrderInfo(IsQuery);
    }
    public static ReportSubFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportSubFragment();
        }
        return fragment;
    }
}
