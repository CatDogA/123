package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.ReportQueryInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;
import com.shanghaigm.dms.view.adapter.TablePagerAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.ReviewTable;
import com.shanghaigm.dms.view.widget.WrapHeightViewPager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReportSubFragment extends BaseFragment {
    private static ReportSubFragment fragment;
    private Button btn_add;
    private Button btn_query;
    private static String TAG = "OrderReviewFragment";
    private ImageView vpRight, vpLeft;
    private JSONArray areaArray, modelArray, stateArray = new JSONArray();

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
                goToActivity(ReportAddActivity.class);
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
        Map<String, Object> params = new HashMap<>();
        params.put("page", page + "");
        params.put("rows", "8");
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
        dialog = new LoadingDialog(getActivity(), "正在加载");
    }

    public static ReportSubFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportSubFragment();
        }
        return fragment;
    }
}
