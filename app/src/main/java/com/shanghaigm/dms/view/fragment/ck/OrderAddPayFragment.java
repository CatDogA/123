package com.shanghaigm.dms.view.fragment.ck;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.FragmentOrderAddPayBinding;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoTwo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.view.activity.ck.ChangeLetterAddActivity;
import com.shanghaigm.dms.view.activity.ck.OrderAddActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.MmPopupWindow;

import java.util.ArrayList;
import java.util.Calendar;

public class OrderAddPayFragment extends BaseFragment {
    private OrderDetailInfoTwo orderDetailInfoTwo = OrderAddActivity.addPayInfo;
    private EditText payment_method, payment_method_remarks, delivery_time, freight, service_fee, contract_price, carriage, invoice_amount, billing_requirements;
    private static OrderAddPayFragment orderAddPayFragment = null;
    private ImageView imgPayMethod;
    private FragmentOrderAddPayBinding binding;
    public OrderDetailInfoTwo info;

    public OrderAddPayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_add_pay, container, false);
        binding = DataBindingUtil.bind(v);
        initView(v);
        setUpView();
        setData();
        return v;
    }

    private void setUpView() {
        imgPayMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> info = new ArrayList<PopListInfo>();
                info.add(new PopListInfo("全款"));    //1
                info.add(new PopListInfo("分期"));    //2
                info.add(new PopListInfo("按揭"));    //3
                MmPopupWindow popPayMethod = new MmPopupWindow(getActivity(), payment_method, info, 3);
                popPayMethod.showPopup(payment_method);
            }
        });
        pickDate(delivery_time);
    }

    private void initView(View v) {
        OrderAddActivity.isPayShow = true;
        imgPayMethod = (ImageView) v.findViewById(R.id.img_pay_method);
        payment_method = (EditText) v.findViewById(R.id.payment_method);
        payment_method.addTextChangedListener(new TextOrderAddPaySwitcher(1));
        payment_method_remarks = (EditText) v.findViewById(R.id.payment_method_remarks);
        payment_method_remarks.addTextChangedListener(new TextOrderAddPaySwitcher(2));
        delivery_time = (EditText) v.findViewById(R.id.delivery_time);
        delivery_time.addTextChangedListener(new TextOrderAddPaySwitcher(3));
        freight = (EditText) v.findViewById(R.id.freight);
        freight.addTextChangedListener(new TextOrderAddPaySwitcher(4));
        service_fee = (EditText) v.findViewById(R.id.service_fee);
        service_fee.addTextChangedListener(new TextOrderAddPaySwitcher(5));
        contract_price = (EditText) v.findViewById(R.id.contract_price);
        contract_price.addTextChangedListener(new TextOrderAddPaySwitcher(6));
        carriage = (EditText) v.findViewById(R.id.carriage);
        carriage.addTextChangedListener(new TextOrderAddPaySwitcher(7));
        invoice_amount = (EditText) v.findViewById(R.id.invoice_amount);
        invoice_amount.addTextChangedListener(new TextOrderAddPaySwitcher(8));
        billing_requirements = (EditText) v.findViewById(R.id.billing_requirements);
        billing_requirements.addTextChangedListener(new TextOrderAddPaySwitcher(9));
    }

    public void setInfo() {
        if (OrderAddActivity.addPayInfo == null) {
            OrderAddActivity.addPayInfo = new OrderDetailInfoTwo("","","","","","","","","");
        }
        OrderAddActivity.addPayInfo.setPayment_method(payment_method.getText().toString());
        OrderAddActivity.addPayInfo.setPayment_method_remarks(payment_method_remarks.getText().toString());
        OrderAddActivity.addPayInfo.setDelivery_time(delivery_time.getText().toString());
        OrderAddActivity.addPayInfo.setFreight(freight.getText().toString());
        OrderAddActivity.addPayInfo.setService_fee(service_fee.getText().toString());
        OrderAddActivity.addPayInfo.setContract_price(contract_price.getText().toString());
        OrderAddActivity.addPayInfo.setCarriage(carriage.getText().toString());
        OrderAddActivity.addPayInfo.setInvoice_amount(invoice_amount.getText().toString());
        OrderAddActivity.addPayInfo.setBilling_requirements(billing_requirements.getText().toString());

    }

    //点击"日期"按钮布局 设置日期
    private void pickDate(final EditText edt) {
        final int[] mYear = {0};
        final int[] mMonth = {0};
        final int[] mDay = {0};
        final Calendar calendar = Calendar.getInstance();
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        mYear[0] = year;
                        mMonth[0] = month;
                        mDay[0] = day;
                        edt.setText(new StringBuilder().append(mYear[0]).append("-")
                                .append((mMonth[0] + 1) < 10 ? "0" + (mMonth[0] + 1) : (mMonth[0] + 1))
                                .append("-")
                                .append((mDay[0] < 10) ? "0" + mDay[0] : mDay[0]));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setData() {
        if (OrderAddActivity.flag == 1) {
            info = new OrderDetailInfoTwo(app.getOrderDetailInfoBean());
            binding.setInfo(info);
        }
        if (OrderAddActivity.flag == 0) {
            binding.setInfo(new OrderDetailInfoTwo());
        }
    }

    private class TextOrderAddPaySwitcher implements TextWatcher {
        private int index;

        public TextOrderAddPaySwitcher(int index) {
            this.index = index;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s != null) {
                String str = s.toString();
                switch (index) {
                    case 1:
                        orderDetailInfoTwo.setPayment_method(str);
                        break;
                    case 2:
                        orderDetailInfoTwo.setPayment_method_remarks(str);
                        break;
                    case 3:
                        orderDetailInfoTwo.setDelivery_time(str);
                        break;
                    case 4:
                        orderDetailInfoTwo.setFreight(str);
                        break;
                    case 5:
                        orderDetailInfoTwo.setService_fee(str);
                        break;
                    case 6:
                        orderDetailInfoTwo.setContract_price(str);
                        break;
                    case 7:
                        orderDetailInfoTwo.setCarriage(str);
                        break;
                    case 8:
                        orderDetailInfoTwo.setInvoice_amount(str);
                        break;
                    case 9:
                        Log.i("str", "onTextChanged:str            " + str);
                        orderDetailInfoTwo.setBilling_requirements(str);
                        break;
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public static OrderAddPayFragment getInstance() {
        if (orderAddPayFragment == null) {
            orderAddPayFragment = new OrderAddPayFragment();
        }
        return orderAddPayFragment;
    }

    public interface CallOrderInfoTwo {
        void getInfoTwo(OrderDetailInfoTwo orderInfoTwo);
    }

    public void getOrderInfoTwo(CallOrderInfoTwo call) {
        call.getInfoTwo(orderDetailInfoTwo);
    }
}
