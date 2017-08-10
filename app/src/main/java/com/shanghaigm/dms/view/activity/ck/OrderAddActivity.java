package com.shanghaigm.dms.view.activity.ck;

import android.app.FragmentManager;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.ck.OrderAddAllocation2Fragment;
import com.shanghaigm.dms.view.fragment.ck.OrderAddBaseFragment;
import com.shanghaigm.dms.view.fragment.ck.OrderAddPayFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<ArrayList<OrderDetailInfoAllocation>> originalList;   //所有原始选配信息(无用)
    private ArrayList<OrderDetailInfoAllocation> singleAllocationList;      //仅代表目前所在的配置项

    private ArrayList<OrderDetailInfoAllocation> customAddList;             //自定义信息
    public static ArrayList<OrderDetailInfoAllocation> originList;        //所有原始信息
    public static ArrayList<AllocationAddChooseUndefaultInfo> undefaultInfos;  //所有选配信息
    public static OrderDetailInfoTwo addPayInfo;     //第二页

    private ArrayList<String> assemblyNames;
    private OrderDetailInfoOne addBaseInfo = null;
    private int modelId, customer_code;
    private String companyName, orderNumber, sex1;


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
                ((OrderAddBaseFragment) fragments.get(0)).getInfoOne(new OrderAddBaseFragment.CallOrderInfoOne() {
                    @Override
                    public void getOrderInfoOne(OrderDetailInfoOne infoOne, int model_Id, String company_name, int customerCode, String sex) {
                        addBaseInfo = infoOne;
                        modelId = model_Id;
                        companyName = company_name;
                        customer_code = customerCode;
                        sex1 = sex;
                    }
                });
                ((OrderAddPayFragment) fragments.get(1)).getOrderInfoTwo(new OrderAddPayFragment.CallOrderInfoTwo() {
                    @Override
                    public void getInfoTwo(OrderDetailInfoTwo orderInfoTwo) {
                        addPayInfo = orderInfoTwo;
                    }
                });

                RequestParams params = new RequestParams();
                params.put("loginName", app.getAccount());
                dialog.showLoadingDlg();
                CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_GET_ORDER_NUMBER, params), new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        JSONObject object = (JSONObject) responseObj;
                        try {
                            JSONObject resultEntity = object.getJSONObject("resultEntity");
                            orderNumber = resultEntity.getString("order_number");
                            addOrder();
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

    private void addOrder() {
        if (orderNumber != null) {
            //前两页信息   order
            JSONObject paramObject = new JSONObject();
            JSONArray paramArray = new JSONArray();
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
                paramObject.put("battery_number", addBaseInfo.getBattery_number());
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
                paramObject.put("province", OrderAddBaseFragment.provinceId);
                paramObject.put("city", OrderAddBaseFragment.cityId);
                paramObject.put("county", OrderAddBaseFragment.countyId);
                paramObject.put("models_Id", modelId + "");
                Log.i(TAG, "addOrder:getColor_determine      " + addBaseInfo.getColor_determine());
                paramObject.put("color_determine", getColorDeterMine(""));
                if (addBaseInfo.getColor_determine() != null) {
                    paramObject.put("color_determine", getColorDeterMine(addBaseInfo.getColor_determine()));
                }
                if (addPayInfo.getPayment_method() != null) {
                    paramObject.put("payment_method", getPayMethod(addPayInfo.getPayment_method()));
                } else {
                    paramObject.put("payment_method", "");
                }
                paramObject.put("order_id", "");
                paramObject.put("customer_code", customer_code + "");
                paramObject.put("detailed_address", addBaseInfo.getDetailed_address());
                paramObject.put("battery_manufacturer", addBaseInfo.getBattery_manufacturer());
                paramObject.put("address", OrderAddBaseFragment.address);
                paramObject.put("road_condition", "不晓得");
                paramObject.put("normal_speed", "11");
                paramObject.put("remark", "晓得不");
                paramArray.put(paramObject);

                //第三页信息    standardVo
                //1.取出整体信息
                //不改变，直接上传
                JSONObject allocationObject;
                JSONArray allocationArray = new JSONArray();
                for (OrderDetailInfoAllocation info : originList) {
                    allocationObject = new JSONObject();
                    allocationObject.put("assembly", info.getAssembly());
                    allocationObject.put("entry_name", info.getEntry_name());
                    allocationObject.put("standard_information", info.getConfig_information());
                    Double cost_change = 0.0;
                    if (!info.getCost_change().equals("")) {
                        cost_change = Double.parseDouble(info.getCost_change());
                    }
                    allocationObject.put("cost_change", cost_change);
                    int supporting_id = 0;
                    if (isNumeric(info.getSupporting_id()) && !info.getSupporting_id().equals("")) {
                        supporting_id = Integer.parseInt(info.getSupporting_id());
                    }
                    allocationObject.put("supporting_id", supporting_id);
                    JSONArray array = new JSONArray();
                    if (undefaultInfos != null && undefaultInfos.size() > 0) {
                        for (AllocationAddChooseUndefaultInfo info2 : undefaultInfos) {
                            if (info2.getStandard_id() == info.getStandard_id()) {
                                JSONObject obj = new JSONObject();
                                obj.put("assembly", info2.getAssembly());
                                obj.put("entry_name", info2.getEntry_name());
                                obj.put("config_information", info2.getConfig_information());
                                obj.put("num", info2.getNum() + "");
                                obj.put("cost_change", info2.getPrice());
                                obj.put("remarks", info2.getRemarks());
                                obj.put("isother", 1);
                                obj.put("matching_id", info2.getMatching_id() + "");
                                int supporting_id2 = 0;
                                if (isNumeric(info2.getSupporting_id()) && !info2.getSupporting_id().equals("")) {
                                    supporting_id2 = Integer.parseInt(info2.getSupporting_id());
                                }
                                obj.put("supporting_id", supporting_id2);
                                array.put(obj);
                            }
                        }
                    }
                    allocationObject.put("matching", array);
                    allocationArray.put(allocationObject);
                }
                Log.i(TAG, "onClick:singleAllocationList     " + singleAllocationList.size());
                //2.获取要添加作为matching属性的信息
                ArrayList<OrderDetailInfoAllocation> listForChange = new ArrayList<OrderDetailInfoAllocation>();
                for (OrderDetailInfoAllocation changeInfo : singleAllocationList) {
                    if (!changeInfo.getNum().equals("")) {
                        if (Integer.parseInt(changeInfo.getNum()) > 0) ;
                        listForChange.add(changeInfo);
                    }
                }
                Log.i(TAG, "onClick:allocationArray      " + allocationArray.toString());
                Log.i(TAG, "onClick:        " + paramArray.toString());
                //自定义
                JSONArray customerArray = new JSONArray();
                JSONObject customerObject = new JSONObject();
                for (OrderDetailInfoAllocation info : customAddList) {
                    customerObject.put("assembly", info.getAssembly());
                    customerObject.put("entry_name", info.getEntry_name());
                    customerObject.put("config_information", info.getConfig_information());
                    customerObject.put("num", info.getNum());
                    customerObject.put("cost_change", 100.0);
                    customerObject.put("remarks", info.getRemarks());
                    customerObject.put("supporting_id", 0);
                    customerObject.put("isother", 0);
                    customerArray.put(customerObject);
                }
                Log.i(TAG, "addOrder:customerArray           " + customerArray.toString());
                //提交请求
                final Map<String, Object> requestParams = new HashMap<>();
                requestParams.put("order", paramArray.toString());
//                requestParams.put("standardVo", "[]");
//                requestParams.put("matching", "[]");
                requestParams.put("standardVo", allocationArray.toString());
                requestParams.put("matching", customerArray.toString());
                requestParams.put("loginName", app.getAccount());
                OkhttpRequestCenter.getCommonPostRequest(Constant.URL_ORDER_ADD, requestParams, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess:responseObj        " + responseObj.toString());
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //自增信息
        }
    }

    private boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
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
            default:
                str = "";
                break;
        }
        return str;
    }

    private void initData() {
        //进入即清空
        assemblyNames = new ArrayList<>();
        customAddList = new ArrayList<>();
//        customAddList.add(new OrderDetailInfoAllocation("", "", "", "", "", "", "", "", 0, null, 0));
        singleAllocationList = new ArrayList<>();
        addPayInfo = new OrderDetailInfoTwo("", "", "", "", "", "", "", "", "");
        if (originList != null) {
            originList.clear();
        }
        if (undefaultInfos != null) {
            undefaultInfos.clear();
        }
        tabLayout.setSelectedTabIndicatorColor(Color.GRAY);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tabLayout.addTab(tabLayout.newTab().setText("基本信息").setTag(0));
        tabLayout.addTab(tabLayout.newTab().setText("付款方式").setTag(1));
        tabLayout.addTab(tabLayout.newTab().setText("选配").setTag(2));

        fragments = new ArrayList<>();
        fragments.add(OrderAddBaseFragment.getInstance());
        fragments.add(OrderAddPayFragment.getInstance());
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

    public ArrayList<OrderDetailInfoAllocation> getSingleAllocationList() {
        return singleAllocationList;
    }

    public void setSingleAllocationList(ArrayList<OrderDetailInfoAllocation> singleAllocationList) {
        this.singleAllocationList = singleAllocationList;
    }
}
