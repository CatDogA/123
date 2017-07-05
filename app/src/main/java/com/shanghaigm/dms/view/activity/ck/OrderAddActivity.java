package com.shanghaigm.dms.view.activity.ck;

import android.app.FragmentManager;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.chumi.widget.http.okhttp.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.chumi.widget.http.okhttp.RequestParams;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.ck.AllocationAddChooseUndefaultInfo;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoOne;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoTwo;
import com.shanghaigm.dms.view.activity.common.LoginActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.ck.OrderAddAllocation2Fragment;
import com.shanghaigm.dms.view.fragment.ck.OrderAddAllocationFragment;
import com.shanghaigm.dms.view.fragment.ck.OrderAddBaseFragment;
import com.shanghaigm.dms.view.fragment.ck.OrderAddPayFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.crypto.AEADBadTagException;

import static android.media.CamcorderProfile.get;

public class OrderAddActivity extends AppCompatActivity {
    private static String TAG = "OrderAddActivity";
    private TextView titleText;
    private RelativeLayout rl_back, rl_end;
    private TabLayout tabLayout;
    private ArrayList<BaseFragment> fragments;
    private FragmentManager manager;
    private Button saveBtn, submitBtn;
    private LoadingDialog dialog;
    private DmsApplication app;
    private ArrayList<ArrayList<OrderDetailInfoAllocation>> assemblyList;   //所有修改后的配置信息
    private ArrayList<ArrayList<OrderDetailInfoAllocation>> originalList;   //所有原始选配信息
    private ArrayList<AllocationAddChooseUndefaultInfo> addAssemblyList;
    private ArrayList<OrderDetailInfoAllocation> customAddList;
    private ArrayList<String> assemblyNames;
    private OrderDetailInfoOne addBaseInfo = null;
    private OrderDetailInfoTwo addPayInfo = null;
    private int modelId, customer_code;
    private String companyName, orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add2);
        initView();
        initData();
        setUpView();
    }

    private void initView() {
        titleText = (TextView) findViewById(R.id.title_text);
        tabLayout = (TabLayout) findViewById(R.id.mm_order_detail_tab);
        saveBtn = (Button) findViewById(R.id.order_pass_button);
        submitBtn = (Button) findViewById(R.id.order_return_back_button);
        dialog = new LoadingDialog(this, "正在加载");
        app = DmsApplication.getInstance();
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
    }

    private void setUpView() {
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());   //获取PID，目前获取自己的也只有该API，否则从/proc中自己的枚举其他进程吧，不过要说明的是，结束其他进程不一定有权限，不然就乱套了。
                System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("loginName", app.getAccount());
                dialog.showLoadingDlg();
                CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_GET_ORDER_NUMBER, params), new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        Log.i(TAG, "onSuccess: " + responseObj.toString());
                        JSONObject object = (JSONObject) responseObj;
                        try {
                            JSONObject resultEntity = object.getJSONObject("resultEntity");
                            orderNumber = resultEntity.getString("order_number");
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
                ((OrderAddBaseFragment) fragments.get(0)).getInfoOne(new OrderAddBaseFragment.CallOrderInfoOne() {

                    @Override
                    public void getOrderInfoOne(OrderDetailInfoOne infoOne, int model_Id, String company_name, int customerCode) {
                        addBaseInfo = infoOne;
                        modelId = model_Id;
                        companyName = company_name;
                        customer_code = customerCode;
                    }
                });
                ((OrderAddPayFragment) fragments.get(1)).getOrderInfoTwo(new OrderAddPayFragment.CallOrderInfoTwo() {
                    @Override
                    public void getInfoTwo(OrderDetailInfoTwo orderInfoTwo) {
                        Log.i(TAG, "getInfoTwo: " + orderInfoTwo.getPayment_method());
                        addPayInfo = orderInfoTwo;
                    }
                });

                //前两页信息   order

                JSONObject paramObject = new JSONObject();
                JSONArray paramArray = new JSONArray();
                JSONArray allocations = new JSONArray();    //全部配置信息
                JSONArray originAllocations = new JSONArray();
                try {
                    paramObject.put("company_name", companyName);
                    paramObject.put("order_number", orderNumber);
                    paramObject.put("customer_name", addBaseInfo.getCustomer_name());
                    paramObject.put("mobile_phone", addBaseInfo.getMobile_phone());
                    paramObject.put("terminal_customer_name", addBaseInfo.getTerminal_customer_name());
                    paramObject.put("terminal_customer_tel", addBaseInfo.getTerminal_customer_tel());
                    paramObject.put("terminal_customer_address", addBaseInfo.getTerminal_customer_address());
                    paramObject.put("number", addBaseInfo.getNumber());
                    paramObject.put("freight", addPayInfo.getFreight());
                    paramObject.put("battery_system", addBaseInfo.getBattery_system());
                    paramObject.put("delivery_time", addPayInfo.getDelivery_time());
                    paramObject.put("endurance_mileage", addBaseInfo.getEndurance_mileage());
                    paramObject.put("ekg", addBaseInfo.getEkg());
                    paramObject.put("licensing_addeess", addBaseInfo.getLicensing_addeess());
                    paramObject.put("payment_method_remarks", addPayInfo.getPayment_method_remarks());
                    paramObject.put("service_fee", addPayInfo.getService_fee());
                    paramObject.put("contract_price", addPayInfo.getContract_price());
                    paramObject.put("carriage", addPayInfo.getCarriage());
                    paramObject.put("invoice_amount", addPayInfo.getInvoice_amount());
                    paramObject.put("billing_requirements", addPayInfo.getBilling_requirements());
                    paramObject.put("province", addBaseInfo.getProvince());
                    paramObject.put("city", addBaseInfo.getCity());
                    paramObject.put("county", addBaseInfo.getCounty());
                    paramObject.put("models_Id", modelId);
                    if (addBaseInfo.getColor_determine() != null) {
                        paramObject.put("color_determine", getColorDeterMine(addBaseInfo.getColor_determine()));
                    }
                    if (addPayInfo.getPayment_method() != null) {
                        paramObject.put("payment_method", getPayMethod(addPayInfo.getPayment_method()));
                    }
//                    paramObject.put("order_id", "");
                    paramObject.put("customer_code", customer_code);
                    paramObject.put("detailed_address", addBaseInfo.getDetailed_address());
                    paramObject.put("battery_manufacturer", addBaseInfo.getBattery_manufacturer());
                    paramArray.put(paramObject);

                    //第三页信息    standardVo
//                    if (assemblyList != null) {
//                        for (int i = 0; i < assemblyList.size(); i++) {
//                            ArrayList<OrderDetailInfoAllocation> assemblyInfos = assemblyList.get(i);
//                            JSONObject allocation = new JSONObject();
//                            for (int j = 0; j < assemblyInfos.size(); j++) {
//                                if (!assemblyInfos.get(j).getNum().equals("")) {
//                                    //修改了的数据,>0
//                                    if (Integer.parseInt(assemblyInfos.get(j).getNum()) > 0) {
//                                        allocation.put("assembly", assemblyInfos.get(j).getAssembly());
//                                        allocation.put("xmmc", assemblyInfos.get(j).getEntry_name());
//                                        allocation.put("peizhix", assemblyInfos.get(j).getConfig_information());
//                                        allocation.put("number", assemblyInfos.get(j).getNum());     //是什么类型？
//                                        allocation.put("prices", assemblyInfos.get(j).getPrice());
//                                        allocation.put("remark", assemblyInfos.get(j).getRemarks());
//                                    } else {
//                                    }
//                                } else {
//                                    allocation.put("zongc", assemblyInfos.get(j).getAssembly());
//                                    allocation.put("xmmc", assemblyInfos.get(j).getEntry_name());
//                                    allocation.put("peizhix", assemblyInfos.get(j).getConfig_information());
//                                    allocation.put("number", assemblyInfos.get(j).getNum());
//                                    allocation.put("prices", assemblyInfos.get(j).getPrice());
//                                    allocation.put("remark", assemblyInfos.get(j).getRemarks());
//                                }
//                                allocations.put(allocation);
//                            }
//                        }
//                    }
                    if (assemblyList != null) {
                        for (ArrayList<OrderDetailInfoAllocation> changelist : assemblyList) {
                            Log.i(TAG, "onClick: changelist      " + changelist.size());
                        }
                    }
//
//                    if (originalList != null) {
//                        for (ArrayList<OrderDetailInfoAllocation> originList : originalList) {
//                            Log.i(TAG, "onClick: originList     " + originList.size());
//                        }
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "onClick: getCustomAddList   " + getCustomAddList().size() + "   getEntry_name   " + getCustomAddList().get(0).getEntry_name());
                //提交请求
                RequestParams requestParams = new RequestParams();
                requestParams.put("order", paramObject.toString());
                if (allocations != null) {
                    requestParams.put("standardVo", allocations.toString());
                    requestParams.put("matching", "");
                    requestParams.put("loginName", app.getAccount());
                    CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_ORDER_ADD, requestParams), new DisposeDataHandle(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            Log.i(TAG, "onSuccess:          " + responseObj.toString());
                        }

                        @Override
                        public void onFailure(Object reasonObj) {
                            Log.i(TAG, "onFailure:          " + reasonObj.toString());
                            Toast.makeText(OrderAddActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }));
                }
            }
        });
        titleText.setText(String.format(getString(R.string.ck_order_add)));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = Integer.parseInt(tab.getTag().toString());

                if (!fragments.get(index).isAdded()) {
                    manager.beginTransaction().add(R.id.mm_order_add_fragment_content, fragments.get(index)).commit();
                } else {
                    manager.beginTransaction().show(fragments.get(index)).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int poi = Integer.parseInt(tab.getTag().toString());
                manager.beginTransaction().hide(fragments.get(poi)).commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private String getPayMethod(String payMethod) {
        String s = null;
        switch (payMethod) {
            case "全款":
                s = "1";
                break;
            case "分期":
                s = "2";
                break;
            case "按揭":
                s = "3";
                break;
        }
        return s;
    }

    private String getColorDeterMine(String color_determine) {
        String str = null;
        switch (color_determine) {
            case "已确认":
                str = "1";
                break;
            case "5个工作日内":
                str = "2";
                break;
            case "7个工作日内":
                str = "3";
                break;
        }
        return str;
    }

    private void initData() {
        customAddList = new ArrayList<>();
        customAddList.add(new OrderDetailInfoAllocation());

        tabLayout.setSelectedTabIndicatorColor(Color.GRAY);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tabLayout.addTab(tabLayout.newTab().setText("基本信息").setTag(0));
        tabLayout.addTab(tabLayout.newTab().setText("付款方式").setTag(1));
        tabLayout.addTab(tabLayout.newTab().setText("选配").setTag(2));

        fragments = new ArrayList<>();
        fragments.add(OrderAddBaseFragment.getInstance());
        fragments.add(OrderAddPayFragment.getInstance());
//        fragments.add(OrderAddAllocationFragment.getInstance());
        fragments.add(OrderAddAllocation2Fragment.getInstance());

        manager = getFragmentManager();
        manager.beginTransaction().add(R.id.mm_order_add_fragment_content, fragments.get(0)).commit();
    }

    public ArrayList<ArrayList<OrderDetailInfoAllocation>> getAssemblyList() {
        return assemblyList;
    }

    public void setAssemblyList(ArrayList<ArrayList<OrderDetailInfoAllocation>> assemblyList) {
        this.assemblyList = assemblyList;
    }

    public ArrayList<AllocationAddChooseUndefaultInfo> getAddAssemblyList() {
        return addAssemblyList;
    }

    public void setAddAssemblyList(ArrayList<AllocationAddChooseUndefaultInfo> addAssemblyList) {
        this.addAssemblyList = addAssemblyList;
    }

    public ArrayList<String> getAssemblyNames() {
        return assemblyNames;
    }

    public void setAssemblyNames(ArrayList<String> assemblyNames) {
        this.assemblyNames = assemblyNames;
    }

    public ArrayList<OrderDetailInfoAllocation> getCustomAddList() {
        return customAddList;
    }

    public void setCustomAddList(ArrayList<OrderDetailInfoAllocation> customAddList) {
        this.customAddList = customAddList;
    }

    public ArrayList<ArrayList<OrderDetailInfoAllocation>> getOriginalList() {
        return originalList;
    }

    public void setOriginalList(ArrayList<ArrayList<OrderDetailInfoAllocation>> originalList) {
        this.originalList = originalList;
    }
}
