package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.chumi.widget.http.okhttp.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterAddInfo;
import com.shanghaigm.dms.model.entity.ck.OrderAddSearchInfo;
import com.shanghaigm.dms.model.entity.mm.ChangeLetterInfoBean;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.view.activity.ck.ChangeLetterAddActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/3.
 */

public class ChangeletterAddPopWindow extends PopupWindow {
    public static String GET_INFO = "get_info";
    private OrderAddSearchInfo selectedInfo = new OrderAddSearchInfo();
    private static String TAG = "ChangeletterAddPop";
    private TextView txt_page_num;
    private ArrayList<ChangeLetterAddInfo> searchInfos;//每页数据
    private int pages = 0;//页数
    private int page = 1;//第几页
    private ArrayList<ChangeLetterSearchTable> tables = new ArrayList<>();//表集合
    private TablePagerAdapter pagerAdapter;
    private DmsApplication app = DmsApplication.getInstance();
    private RelativeLayout.LayoutParams lp = new RelativeLayout.
            LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    private Boolean IsQuery = true;//判断是查询还是更多
    private Boolean IsMore = false;//判断可否请求更多
    private LoadingDialog dialog;
    private EditText edt_name, edt_tel;

    private Context context;
    private WrapHeightViewPager vp;
    private Button btnSearch, btnSure;
    private ImageView vp_right, vp_left;

    public ChangeletterAddPopWindow(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.pop_search_info, null, false);
        this.setContentView(v);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(h);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.context = context;
        initView(v);
        setUpView();
    }

    private void setUpView() {
        txt_page_num.setText("页数:" + "0" + "/" + pages);
        initViewPager();
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ChangeLetterAddActivity.changeLetterAddInfo != null) {
                    ChangeLetterAddActivity.setView();
                    dismiss();
                } else {
                    dismiss();
                }
            }
        });
        vp_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsMore) {
                    int currentItem = vp.getCurrentItem();
                    if (currentItem + 1 == pages) {
                        vp_right.setImageResource(R.mipmap.right_switch_pre);
                    }
                    if (currentItem > 0) {
                        vp.setCurrentItem(--currentItem);
                    }
                    if (currentItem == 0) {
                        vp_left.setImageResource(R.mipmap.left_switch);
                    }
                    setPages(currentItem + 1, pages);
                }
            }
        });
        vp_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsQuery = false;
                if (IsMore) {
                    int currentItem = vp.getCurrentItem();
                    if (currentItem == 0) {
                        vp_left.setImageResource(R.mipmap.left_switch_pre);
                    }
                    if (currentItem + 2 == pages) {
                        vp_right.setImageResource(R.mipmap.right_switch);
                    }
                    Log.i(TAG, "onClick: " + "currentItem" + currentItem + "     page" + page);

                    if (currentItem + 2 == page) {
                        if (page > pages) {
                            Toast.makeText(context, "已是最后一页", Toast.LENGTH_SHORT).show();
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
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsQuery = true;
                IsMore = true;
                page = 1;
                requestOrderInfo(IsQuery);
            }
        });
    }

    private void initView(View v) {
        vp = (WrapHeightViewPager) v.findViewById(R.id.vp_search_order_add);
        btnSearch = (Button) v.findViewById(R.id.btn_search);
        btnSure = (Button) v.findViewById(R.id.btn_sure);
        vp_right = (ImageView) v.findViewById(R.id.viewpager_right);
        vp_left = (ImageView) v.findViewById(R.id.viewpager_left);
        txt_page_num = (TextView) v.findViewById(R.id.pages_num);
        dialog = new LoadingDialog(context, "正在加载");
        edt_name = (EditText) v.findViewById(R.id.edt_name);
        edt_name.setVisibility(View.GONE);
        edt_tel = (EditText) v.findViewById(R.id.edt_tel);
        edt_tel.setVisibility(View.GONE);
    }

    private void requestOrderInfo(Boolean isQuery) {
        dialog.showLoadingDlg();
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("rows", 10);
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_QUERY_CHANGE_LETTER_ID_IFNO, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                JSONObject object = (JSONObject) responseObj;
                Log.i(TAG, "onSuccess: " + object.toString());
                try {
                    JSONObject resultEntity = object.getJSONObject("resultEntity");
                    int total = resultEntity.getInt("total");
                    JSONArray rows = resultEntity.getJSONArray("rows");
                    if (total == 0) {
                        Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
                    }
                    if (total % 10 == 0) {
                        pages = total / 10;
                    } else {
                        pages = total / 10 + 1;
                    }
                    ArrayList<ChangeLetterAddInfo> searchInfos = new ArrayList<>();
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject searchInfo = rows.getJSONObject(i);
                        searchInfos.add(new ChangeLetterAddInfo(searchInfo.getString("contract_id"), searchInfo.getString("company_name"), searchInfo.getString("models_name"), searchInfo.getInt("models_Id"), searchInfo.getInt("number"), searchInfo.getString("contract_price"), searchInfo.getString("delivery_time"), searchInfo.getInt("order_id"), searchInfo.getString("order_number"), searchInfo.getString("customer_name"), searchInfo.getString("creator_by"), searchInfo.getString("creator_date")));
                    }
                    ChangeLetterSearchTable table = new ChangeLetterSearchTable(context, searchInfos);
                    table.setLayoutParams(lp);
                    if (IsQuery) {
                        tables.clear();
                        tables.add(table);
                        setPages(1, pages);
                        page++;
                    } else if (IsMore && !IsQuery) {
                        if (vp.getCurrentItem() + 1 >= pages) {
                            Toast.makeText(context, "已是最后一页", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tables.add(table);
                        setPages(tables.size(), pages);
                        page++;
                    } else {
                        Toast.makeText(context, "请先查询", Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, "onSuccess:page " + page);
                    Log.i(TAG, "onSuccess:tables " + tables.size());
                    pagerAdapter.notifyDataSetChanged();
                    vp.setAdapter(pagerAdapter);
                    vp.setCurrentItem(page);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onFailure: " + reasonObj.toString());
            }
        }));
    }

    private void initViewPager() {
        searchInfos = new ArrayList<>();
        ChangeLetterSearchTable table = new ChangeLetterSearchTable(context, searchInfos);
        table.setLayoutParams(lp);
        tables.add(table);
        pagerAdapter = new TablePagerAdapter(context, tables);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
        vp.setOnClickListener(null);
        vp.setAdapter(pagerAdapter);
    }

    private void setPages(int page, int pages) {
        txt_page_num.setText("页数:" + page + "/" + pages);
    }

    public void showPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public static void hidePopup(MmPopupWindow popupWindow) {
        popupWindow.dismiss();
    }

    public void hidePopup() {
        this.dismiss();
    }

    /**
     * Created by Tom on 2017/6/26
     */

    public class TablePagerAdapter extends PagerAdapter {
        private ArrayList<ChangeLetterSearchTable> tables;
        private int mChildCount = 0;
        private Context context;

        public TablePagerAdapter(Context context, ArrayList<ChangeLetterSearchTable> tables) {
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
}
