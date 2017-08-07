package com.shanghaigm.dms.view.fragment.ck;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoTwo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.view.activity.ck.OrderAddActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.MmPopupWindow;

import java.util.ArrayList;

public class OrderAddPayFragment extends BaseFragment {
    private OrderDetailInfoTwo orderDetailInfoTwo = OrderAddActivity.addPayInfo;
    private EditText payment_method, payment_method_remarks, delivery_time, freight, service_fee, contract_price, carriage, invoice_amount, billing_requirements;
    private static OrderAddPayFragment orderAddPayFragment = null;
    private ImageView imgPayMethod;

    public OrderAddPayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_add_pay, container, false);
        initView(v);
        setUpView();
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
    }

    private void initView(View v) {
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
                    orderDetailInfoTwo.setBilling_requirements(str);
                    break;
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
