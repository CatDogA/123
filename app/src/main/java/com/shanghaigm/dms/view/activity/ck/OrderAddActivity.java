package com.shanghaigm.dms.view.activity.ck;

import android.app.FragmentManager;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
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
import com.shanghaigm.dms.model.util.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.chumi.widget.http.okhttp.RequestParams;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.ck.AllocationAddChooseUndefaultInfo;
import com.shanghaigm.dms.model.entity.mm.MatchingBean;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoBean;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoOne;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoTwo;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.activity.common.LoginActivity;
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

public class OrderAddActivity extends BaseActivity {
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
    private ArrayList<OrderDetailInfoAllocation> singleAllocationList;      //仅代表目前所在的配置项

    private ArrayList<OrderDetailInfoAllocation> customAddList;             //自定义信息
    public static ArrayList<OrderDetailInfoAllocation> originList;        //所有原始信息
    public static ArrayList<AllocationAddChooseUndefaultInfo> undefaultInfos;  //所有选配信息
    public static OrderDetailInfoTwo addPayInfo;     //第二页

    private ArrayList<String> assemblyNames;
    private OrderDetailInfoOne addBaseInfo = null;
    private int modelId, customer_code;
    private String companyName, orderNumber, sex1;
    public static int flag = 0;   //判断是否为新增,0新增，1修改
    private JSONArray allocationArray, customerArray;
    private JSONObject paramObject;
    private int orderId;     //储存保存成功后的order_id
    public static Boolean isPayShow;    //判断第二页是否显示过

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add2);
        initIntent();
        initView();
        initData();
        setUpView();
    }

    private void initIntent() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getInt(PaperInfo.ORDER_MODIFY) == 1) {
                flag = 1;
            }
        } else {
            flag = 0;
        }
    }

    private void initView() {
        titleText = (TextView) findViewById(R.id.title_text);
        tabLayout = (TabLayout) findViewById(R.id.mm_order_detail_tab);
        saveBtn = (Button) findViewById(R.id.order_pass_button);
        submitBtn = (Button) findViewById(R.id.order_return_back_button);
        if (flag == 0) {
            submitBtn.setEnabled(false);
        }
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
                goToActivity(LoginActivity.class);
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
                        Log.i(TAG, "getOrderInfoOne:customerCode            "+customerCode);
                        customer_code = customerCode;
                        sex1 = sex;
                    }
                });
                if (isPayShow) {
                    OrderAddPayFragment payFragment = OrderAddPayFragment.getInstance();
                    payFragment.setInfo();
                } else {
                    if (app.getOrderDetailInfoBean() != null && flag == 1) {
                        addPayInfo = new OrderDetailInfoTwo(app.getOrderDetailInfoBean());
                    }
                }
                if (flag == 0) {
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
                                String url = Constant.URL_ORDER_ADD;
                                if (isPayShow) {
                                    addOrder(url);
                                } else {
                                    Toast.makeText(OrderAddActivity.this, getText(R.string.full_filled), Toast.LENGTH_SHORT).show();
                                }
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
                if (flag == 1) {
                    orderNumber = app.getOrderDetailInfoBean().resultEntity.order_number;
                    String url = Constant.URL_ORDER_MODIFY;
                    addOrder(url);
                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                if (flag == 0) {
                    params.put("id", orderId);
                }
                if (flag == 1) {
                    params.put("id", app.getOrderDetailInfoBean().resultEntity.order_id);
                }
                params.put("loginName", app.getAccount());
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_COMMIT_ORDER, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess:      " + responseObj);
                        dialog.dismissLoadingDlg();
                        JSONObject result = (JSONObject) responseObj;
                        try {
                            int resultEntity = result.getInt("resultEntity");
                            if (resultEntity == 1) {
                                Toast.makeText(OrderAddActivity.this, getText(R.string.sub_info_success), Toast.LENGTH_SHORT).show();
                                Bundle b = new Bundle();
                                b.putInt(HomeActivity.ORDER_LETTER_SUB, 1);
                                goToActivity(HomeActivity.class, b);
                                finish();
                            }
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

    //1.保存、修改
    private void addOrder(String url) {
        OrderAddBaseFragment baseFragment = OrderAddBaseFragment.getInstance();
        OrderAddPayFragment payFragment = OrderAddPayFragment.getInstance();
        //先全部满足上面的条件才会进行else
        if (baseFragment.edtCustomerName.getText().toString().equals("")) {
            Toast.makeText(this, "请选择客户信息", Toast.LENGTH_SHORT).show();
        } else if (baseFragment.edtModel.getText().toString().equals("")) {
            Toast.makeText(this, "请选择车型", Toast.LENGTH_SHORT).show();
        } else if (baseFragment.edt_licensing_address.getText().toString().equals("")) {
            Toast.makeText(this, "请填写上牌地点", Toast.LENGTH_SHORT).show();
        } else if (baseFragment.edt_color_determine.getText().toString().equals("")) {
            Toast.makeText(this, "请选择颜色", Toast.LENGTH_SHORT).show();
        } else if (baseFragment.edt_number.getText().toString().equals("")) {
            Toast.makeText(this, "请填数字", Toast.LENGTH_SHORT).show();
        } else if(baseFragment.edt_road_condition.getText().toString().equals("")){
            Toast.makeText(this, "请填路况", Toast.LENGTH_SHORT).show();
        }else if(baseFragment.edt_normal_speed.getText().toString().equals("")){
            Toast.makeText(this, "请填常用车速", Toast.LENGTH_SHORT).show();
        }else {
            if (isPayShow) {
                if (payFragment.payment_method.getText().toString().equals("")) {
                    Toast.makeText(this, "请选择付款方式", Toast.LENGTH_SHORT).show();
                    return;
                } else if (payFragment.freight.getText().toString().equals("")) {
                    Toast.makeText(this, "请填写合同价", Toast.LENGTH_SHORT).show();
                    return;
                }else if(payFragment.service_fee.getText().toString().equals("")){
                    Toast.makeText(this, "请填写劳务费", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Double.parseDouble(payFragment.contract_price.getText().toString())<0){
                    Toast.makeText(this, "扣除劳务费后合同价不小于0", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (orderNumber != null) {
                //前两页信息   order
                paramObject = new JSONObject();
                JSONArray paramArray = new JSONArray();
                try {
                    if (flag == 0) {
                        paramObject.put("order_number", orderNumber);
                    }
                    paramObject.put("customerCode", customer_code);
//                    paramObject.put("company_name", companyName);
//                    paramObject.put("customer_name", addBaseInfo.getCustomer_name());
//                    paramObject.put("mobile_phone", addBaseInfo.getMobile_phone());
//                    paramObject.put("province", OrderAddBaseFragment.provinceId);
//                    paramObject.put("city", OrderAddBaseFragment.cityId);
//                    paramObject.put("county", OrderAddBaseFragment.countyId);

                    paramObject.put("terminal_customer_name", addBaseInfo.getTerminal_customer_name());
                    paramObject.put("terminal_customer_tel", addBaseInfo.getTerminal_customer_tel());
                    paramObject.put("terminal_customer_address", addBaseInfo.getTerminal_customer_address());
                    paramObject.put("number", addBaseInfo.getNumber());

                    paramObject.put("battery_system", addBaseInfo.getBattery_system());
                    paramObject.put("battery_number", addBaseInfo.getBattery_number());
                    paramObject.put("endurance_mileage", addBaseInfo.getEndurance_mileage());
                    paramObject.put("ekg", addBaseInfo.getEkg());
                    paramObject.put("licensing_addeess", addBaseInfo.getLicensing_addeess());


                    paramObject.put("freight", addPayInfo.getFreight());
                    paramObject.put("delivery_time", addPayInfo.getDelivery_time());
                    paramObject.put("payment_method_remarks", addPayInfo.getPayment_method_remarks());
                    paramObject.put("service_fee", addPayInfo.getService_fee());
                    paramObject.put("contract_price", addPayInfo.getContract_price());
                    paramObject.put("carriage", addPayInfo.getCarriage());
                    paramObject.put("invoice_amount", addPayInfo.getInvoice_amount());
                    paramObject.put("billing_requirements", addPayInfo.getBilling_requirements());

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
                    paramObject.put("road_condition", addBaseInfo.getRoad_condition());
                    paramObject.put("normal_speed", addBaseInfo.getNormal_speed());
                    paramObject.put("remark", "");
                    if (flag == 1) {
                        OrderDetailInfoBean.ResultEntity entity = app.getOrderDetailInfoBean().resultEntity;
                        paramObject.put("order_id", entity.order_id + "");
//                    paramObject.put("company_name", app.getOrderDetailInfoBean().resultEntity.company_name);
                        if (OrderAddBaseFragment.address == null) {
                            paramObject.put("address", app.getOrderDetailInfoBean().resultEntity.address);
                        }
//                    paramObject.put("payment_method", app.getOrderDetailInfoBean().resultEntity.payment_method);
                    }
                    paramArray.put(paramObject);

                    //第三页信息    standardVo
                    //1.取出整体信息
                    //不改变，直接上传
                    JSONObject allocationObject;
                    allocationArray = new JSONArray();
                    for (OrderDetailInfoAllocation info : originList) {
                        allocationObject = new JSONObject();
                        allocationObject.put("assembly", info.getAssemblyName());
                        allocationObject.put("entry_name", info.getEntry_name());
                        allocationObject.put("standard_information", info.getConfig_information());
                        Double cost_change = 0.0;
//                    if (!info.getCost_change().equals("")) {
//                        cost_change = Double.parseDouble(info.getCost_change());
//                    }
                        allocationObject.put("cost_change", "");
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
                                    obj.put("cost_change", info2.getPrice() + "");
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

                    //自定义
                    customerArray = new JSONArray();
                    for (OrderDetailInfoAllocation info : customAddList) {
                        if(info.getAssembly().equals("") || info.getEntry_name().equals("") || info.getConfig_information().equals("")){
                            Log.i(TAG, "addOrder: "+"不可传");
                        }else {
                            JSONObject customerObject = new JSONObject();
                            Log.i(TAG, "addOrder:            " + info.getAssembly());
                            customerObject.put("assembly", info.getAssembly());
                            customerObject.put("entry_name", info.getEntry_name());
                            customerObject.put("config_information", info.getConfig_information());
                            customerObject.put("num", info.getNum());
                            customerObject.put("cost_change", "");
                            customerObject.put("remarks", info.getRemarks());
                            customerObject.put("supporting_id", 0);
                            customerObject.put("isother", 0);
                            customerArray.put(customerObject);
                        }
                    }
                    //提交请求
                    final Map<String, Object> requestParams = new HashMap<>();
                    if (flag == 0) {
                        requestParams.put("order", paramArray.toString());
                    }
                    if (flag == 1) {
                        requestParams.put("orders", paramArray.toString());
                    }
                    Log.i(TAG, "addOrder:originList.size()        " + originList.size());
                    Log.i(TAG, "onClick:        " + paramArray.toString());
                    Log.i(TAG, "onClick:allocationArray      " + allocationArray.toString());
                    Log.i(TAG, "addOrder:customerArray           " + customerArray.toString());

                    requestParams.put("standardVo", allocationArray.toString());
                    requestParams.put("matching", customerArray.toString());
                    requestParams.put("loginName", app.getAccount());
                    requestParams.put("job_code", app.getJobCode());
                    dialog.showLoadingDlg();
                    OkhttpRequestCenter.getCommonPostRequest(url, requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            Log.i(TAG, "onSuccess:responseObj        " + responseObj.toString());
                            JSONObject result = (JSONObject) responseObj;
                            try {
                                orderId = result.getInt("resultEntity");
                                if (orderId != -1) {
                                    Toast.makeText(OrderAddActivity.this, getText(R.string.save_info_success), Toast.LENGTH_SHORT).show();
                                    saveBtn.setEnabled(false);
                                    submitBtn.setEnabled(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        if(flag==1){
            customer_code = app.getOrderDetailInfoBean().resultEntity.customer_code;
        }
        //进入即清空
        isPayShow = false;
        assemblyNames = new ArrayList<>();
        customAddList = new ArrayList<>();
        if (app.getMatchingBeanArrayList() != null) {
            for (MatchingBean bean : app.getMatchingBeanArrayList()) {
                if (bean.isother == 0) {
                    OrderDetailInfoAllocation allocation = new OrderDetailInfoAllocation(bean.assembly, bean.entry_name, bean.config_information, bean.num + "", bean.remarks, bean.isdefault);
                    customAddList.add(allocation);
                }
            }
        }
        Log.i(TAG, "initData: customAddList         " + customAddList.size());
        if (assemblyList == null) {
            assemblyList = new ArrayList<>();
        } else {
            assemblyList.clear();
        }
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

    public ArrayList<OrderDetailInfoAllocation> getSingleAllocationList() {
        return singleAllocationList;
    }

    public void setSingleAllocationList(ArrayList<OrderDetailInfoAllocation> singleAllocationList) {
        this.singleAllocationList = singleAllocationList;
    }
}
