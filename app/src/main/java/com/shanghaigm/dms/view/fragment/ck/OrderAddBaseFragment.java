package com.shanghaigm.dms.view.fragment.ck;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.chumi.widget.http.okhttp.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.chumi.widget.http.okhttp.RequestParams;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.ck.AllocationAddChooseUndefaultInfo;
import com.shanghaigm.dms.model.entity.ck.OrderAddSearchInfo;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoOne;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.view.activity.ck.AllocationAddChooseUndefaultActivity;
import com.shanghaigm.dms.view.activity.ck.OrderAddActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.MmPopupWindow;
import com.shanghaigm.dms.view.widget.OrderAddNamePopWindow;
import com.shanghaigm.dms.view.widget.SearchTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderAddBaseFragment extends BaseFragment {
    private OrderDetailInfoOne orderDetailInfoOne = new OrderDetailInfoOne();
    private static String TAG = "OrderAddBaseFragment";
    private EditText edtModel, edtCustomerName, edtPhone, edtMobilePhone, edtProvince, edtCity, edtCounty, edtDetailAddress, edtSex, edtFixPhone, edt_terminal_customer_name, edt_terminal_customer_tel, edt_terminal_customer_address, edt_number, edt_battery_system, edt_battery_number, edt_licensing_address, edt_endurance_mileage, edt_ekg, edt_battery_manufacturer, edt_color_determine;
    private ImageView imgModel;
    private ImageView imgSearch;
    private ImageView imgSex;
    private ImageView imgColorDetermine;
    private LoadingDialog dialog;
    private JSONArray modelArray;
    private static int resultCount = 0;
    private int assemblyCount;    //计总成数量
    private static OrderAddBaseFragment orderAddBaseFragment = null;
    private ArrayList<ArrayList<OrderDetailInfoAllocation>> allAssemblyList;
    private String assemblyStr;   //防止总成信息为空
    private Handler handler;
    private int model_Id, customerCode;
    private String companyName;

    public OrderAddBaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_add_base, container, false);
        initView(v);
        initData();
        setUpView();
        return v;
    }

    private void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                OrderAddSearchInfo info = (OrderAddSearchInfo) bundle.getSerializable(OrderAddNamePopWindow.GET_INFO);
                edtCustomerName.setText(info.getName());
                edtMobilePhone.setText(info.getTel());
                companyName = info.getCompany();
                customerCode = info.getCustomerCode();
                Log.i(TAG, "handleMessage: " + info.getDetailed_address());
                String[] str = info.getDetailed_address().split(",");
                if (str.length == 3) {
                    edtProvince.setText(str[0]);
                    edtCity.setText(str[0]);
                    edtCounty.setText(str[1]);
                    edtDetailAddress.setText(str[2]);
                }
                if (str.length == 4) {
                    edtProvince.setText(str[0]);
                    edtCity.setText(str[1]);
                    edtCounty.setText(str[2]);
                    edtDetailAddress.setText(str[3]);
                }
            }
        };
    }

    private void setUpView() {
        //检测选择的车型,并获取总成整体数据
        edtModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, final int count) {
                String model = edtModel.getText().toString();
                try {
                    if (!model.equals("") && modelArray != null) {
                        for (int i = 0; i < modelArray.length(); i++) {
                            if (model.equals(modelArray.getJSONObject(i).getString("models_name"))) {
                                final int modelId = modelArray.getJSONObject(i).getInt("models_Id");
                                model_Id = modelId;
                                Map<String, Object> params = new HashMap<>();
                                params.put("models_Id", modelId);
                                dialog.showLoadingDlg();
                                CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_ASSEMBLY_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
                                    @Override
                                    public void onSuccess(Object responseObj) {
                                        dialog.dismissLoadingDlg();
                                        JSONObject assembly = (JSONObject) responseObj;
                                        try {
                                            JSONArray arrayAssembly = assembly.getJSONArray("resultEntity");
                                            assemblyStr = arrayAssembly.get(0).toString();
                                            Log.i(TAG, "onSuccess: " + responseObj.toString());
                                            //貌似不能够同意请求，只能获取总成的名称
                                            ArrayList<String> assemblyList = new ArrayList<String>();
                                            allAssemblyList = new ArrayList<ArrayList<OrderDetailInfoAllocation>>();
                                            assemblyCount = arrayAssembly.length();
                                            ArrayList<String> names = new ArrayList<String>();
                                            for (int i = 0; i < arrayAssembly.length(); i++) {
                                                assemblyList.add(arrayAssembly.get(i).toString());
                                                getAssemblyListInfo(modelId, arrayAssembly.get(i).toString());
                                                names.add(arrayAssembly.get(i).toString());
                                            }
                                            ((OrderAddActivity) getActivity()).setAssemblyNames(names);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Object reasonObj) {

                                    }
                                }));
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        imgModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showLoadingDlg();
                CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_GET_MODELS, null), new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        try {
                            ArrayList<PopListInfo> models = new ArrayList<>();
                            JSONObject object = new JSONObject(responseObj.toString());
                            modelArray = object.getJSONArray("resultEntity");
                            for (int i = 0; i < modelArray.length(); i++) {
                                models.add(new PopListInfo(modelArray.getJSONObject(i).getString("models_name")));
                            }
                            MmPopupWindow modelPopup = new MmPopupWindow(getActivity(), edtModel, models, 4);
                            modelPopup.showPopup(edtModel);
                            dialog.dismissLoadingDlg();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                }));
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderAddNamePopWindow pop_add_name = new OrderAddNamePopWindow(getActivity(), handler);
                pop_add_name.showPopup(edtCustomerName);
            }
        });
        imgSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> popSexList = new ArrayList<PopListInfo>();
                popSexList.add(new PopListInfo("男"));
                popSexList.add(new PopListInfo("女"));
                MmPopupWindow popSex = new MmPopupWindow(getActivity(), edtSex, popSexList, 3);
                popSex.showPopup(edtSex);
            }
        });

        imgColorDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> popColorList = new ArrayList<PopListInfo>();
                popColorList.add(new PopListInfo("5个工作日内"));    //2
                popColorList.add(new PopListInfo("7个工作日内"));    //3
                popColorList.add(new PopListInfo("已确认"));           //1
                MmPopupWindow popColorDetermine = new MmPopupWindow(getActivity(), edt_color_determine, popColorList, 3);
                popColorDetermine.showPopup(edt_color_determine);
            }
        });

    }

    private void getAssemblyListInfo(int modelId, final String assembly) {
        Map<String, Object> params = new HashMap<>();
        params.put("models_Id", modelId);
        params.put("assembly", assembly);
        dialog.showLoadingDlg();
        CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_ASSEMBLY_LIST, params), new DisposeDataHandle(new DisposeDataListener() {
            /**
             * @param responseObj
             */
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess:     " + responseObj.toString());
                ArrayList<OrderDetailInfoAllocation> list = new ArrayList<>();    //每种标配信息
                JSONObject object = (JSONObject) responseObj;
                try {
                    JSONArray array = object.getJSONArray("resultEntity");
                    JSONObject object1 = array.getJSONObject(0);
                    if (array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject info = array.getJSONObject(i);
                            int id = 0;
                            if (!info.get("standard_id").equals("")) {
                                id = info.getInt("standard_id");
                            }
                            JSONArray matchArray = info.getJSONArray("matchingVO");
                            int matchLength = 0;
                            ArrayList<AllocationAddChooseUndefaultInfo> undefaultInfoList = new ArrayList();
                            if (matchArray != null) {
                                matchLength = matchArray.length();
                                if (matchLength > 0) {
                                    for (int j = 0; j < matchLength; j++) {
                                        //TODO-有没有standard_information,与config_information的差别
                                        undefaultInfoList.add(new AllocationAddChooseUndefaultInfo(matchArray.getJSONObject(j).getString("assembly"), matchArray.getJSONObject(j).getString("config_information"), 0.0, 0, "", id));
                                    }
                                }
                            }
                            list.add(new OrderDetailInfoAllocation(info.getString("assembly"), info.getString("entry_name"), info.getString("standard_information"), "", info.getString("remarks"), matchLength, undefaultInfoList, id));
                        }
                        allAssemblyList.add(list);
                        Log.i(TAG, "onSuccess: " + allAssemblyList.get(0).get(0).getAssembly());
                    } else {
                        list.add(new OrderDetailInfoAllocation(assemblyStr, "", "", "", "", 0, null, 0));
                        allAssemblyList.add(list);
                    }
                    ((OrderAddActivity) getActivity()).setAssemblyList(allAssemblyList);   //用于被改变
                    ((OrderAddActivity)getActivity()).setOriginalList(allAssemblyList);
                    Log.i(TAG, "onSuccess: " + allAssemblyList.size() + "    " + assemblyCount);
                    if (allAssemblyList.size() == assemblyCount) {
                        dialog.dismissLoadingDlg();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        }));
    }

    private void initView(View v) {
        edtModel = (EditText) v.findViewById(R.id.edt_model);
        edtModel.addTextChangedListener(new TextOrderAddBaseSwitcher(12));

        imgModel = (ImageView) v.findViewById(R.id.img_model);
        dialog = new LoadingDialog(getActivity(), "正在加载");
        imgSearch = (ImageView) v.findViewById(R.id.img_custom_name);
        imgSex = (ImageView) v.findViewById(R.id.img_sex);
        imgColorDetermine = (ImageView) v.findViewById(R.id.img_color_determine);

        edtCustomerName = (EditText) v.findViewById(R.id.edt_custom_name);
        edtCustomerName.addTextChangedListener(new TextOrderAddBaseSwitcher(1));

        edtCity = (EditText) v.findViewById(R.id.city);
        edtCity.addTextChangedListener(new TextOrderAddBaseSwitcher(6));
        edtProvince = (EditText) v.findViewById(R.id.province);
        edtProvince.addTextChangedListener(new TextOrderAddBaseSwitcher(5));
        edtPhone = (EditText) v.findViewById(R.id.phone);
        edtPhone.addTextChangedListener(new TextOrderAddBaseSwitcher(3));
        edtMobilePhone = (EditText) v.findViewById(R.id.mobile_phone);
        edtMobilePhone.addTextChangedListener(new TextOrderAddBaseSwitcher(4));
        edtCounty = (EditText) v.findViewById(R.id.county);
        edtCounty.addTextChangedListener(new TextOrderAddBaseSwitcher(7));
        edtDetailAddress = (EditText) v.findViewById(R.id.detail_address);
        edtDetailAddress.addTextChangedListener(new TextOrderAddBaseSwitcher(8));
        edtSex = (EditText) v.findViewById(R.id.edt_sex);
        edtSex.addTextChangedListener(new TextOrderAddBaseSwitcher(2));

        edt_terminal_customer_name = (EditText) v.findViewById(R.id.terminal_customer_name);
        edt_terminal_customer_name.addTextChangedListener(new TextOrderAddBaseSwitcher(9));
        edt_terminal_customer_tel = (EditText) v.findViewById(R.id.terminal_customer_tel);
        edt_terminal_customer_tel.addTextChangedListener(new TextOrderAddBaseSwitcher(10));
        edt_terminal_customer_address = (EditText) v.findViewById(R.id.terminal_customer_address);
        edt_terminal_customer_address.addTextChangedListener(new TextOrderAddBaseSwitcher(11));
        edt_number = (EditText) v.findViewById(R.id.number);
        edt_number.addTextChangedListener(new TextOrderAddBaseSwitcher(13));
        edt_battery_system = (EditText) v.findViewById(R.id.battery_system);
        edt_battery_system.addTextChangedListener(new TextOrderAddBaseSwitcher(14));
        edt_battery_number = (EditText) v.findViewById(R.id.battery_number);
        edt_battery_number.addTextChangedListener(new TextOrderAddBaseSwitcher(15));
        edt_licensing_address = (EditText) v.findViewById(R.id.licensing_address);
        edt_licensing_address.addTextChangedListener(new TextOrderAddBaseSwitcher(16));
        edt_endurance_mileage = (EditText) v.findViewById(R.id.endurance_mileage);
        edt_endurance_mileage.addTextChangedListener(new TextOrderAddBaseSwitcher(17));
        edt_ekg = (EditText) v.findViewById(R.id.ekg);
        edt_ekg.addTextChangedListener(new TextOrderAddBaseSwitcher(18));
        edt_battery_manufacturer = (EditText) v.findViewById(R.id.battery_manufacturer);
        edt_battery_manufacturer.addTextChangedListener(new TextOrderAddBaseSwitcher(19));
        edt_color_determine = (EditText) v.findViewById(R.id.color_determine);
        edt_color_determine.addTextChangedListener(new TextOrderAddBaseSwitcher(20));
    }

    private class TextOrderAddBaseSwitcher implements TextWatcher {
        private int index;

        public TextOrderAddBaseSwitcher(int index) {
            this.index = index;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str = s.toString();
            switch (index) {
                case 1:
                    orderDetailInfoOne.setCustomer_name(str);
                    break;
                case 2:
                    orderDetailInfoOne.setSex(str);
                    break;
                case 3:
                    orderDetailInfoOne.setFixed_telephone(str);
                    break;
                case 4:
                    orderDetailInfoOne.setMobile_phone(str);
                    break;
                case 5:
                    orderDetailInfoOne.setProvince(str);
                    break;
                case 6:
                    orderDetailInfoOne.setCity(str);
                    break;
                case 7:
                    orderDetailInfoOne.setCounty(str);
                    break;
                case 8:
                    orderDetailInfoOne.setDetailed_address(str);
                    break;
                case 9:
                    orderDetailInfoOne.setTerminal_customer_name(str);
                    break;
                case 10:
                    orderDetailInfoOne.setTerminal_customer_tel(str);
                    break;
                case 11:
                    orderDetailInfoOne.setTerminal_customer_address(str);
                    break;
                case 12:
                    orderDetailInfoOne.setModels_name(str);
                    break;
                case 13:
                    orderDetailInfoOne.setNumber(str);
                    break;
                case 14:
                    orderDetailInfoOne.setBattery_system(str);
                    break;
                case 15:
                    orderDetailInfoOne.setBattery_number(str);
                    break;
                case 16:
                    orderDetailInfoOne.setLicensing_addeess(str);
                    break;
                case 17:
                    orderDetailInfoOne.setEndurance_mileage(str);
                    break;
                case 18:
                    orderDetailInfoOne.setEkg(str);
                    break;
                case 19:
                    orderDetailInfoOne.setBattery_manufacturer(str);
                    break;
                case 20:
                    orderDetailInfoOne.setColor_determine(str);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public static OrderAddBaseFragment getInstance() {
        if (orderAddBaseFragment == null) {
            orderAddBaseFragment = new OrderAddBaseFragment();
        }
        return orderAddBaseFragment;
    }

    //接口回调
    public interface CallOrderInfoOne {
        void getOrderInfoOne(OrderDetailInfoOne infoOne, int model_Id, String company_name, int customerCode);
    }

    public void getInfoOne(CallOrderInfoOne call) {
        call.getOrderInfoOne(orderDetailInfoOne, model_Id, companyName, customerCode);
    }
}
