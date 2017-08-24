package com.shanghaigm.dms.view.fragment.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.as.HomeActivity;
import com.shanghaigm.dms.view.activity.mm.ContractReviewOrChangeLetterReviewActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.BillReviewButton;
import com.shanghaigm.dms.view.widget.ContractReviewButton;
import com.shanghaigm.dms.view.widget.LetterReviewButton;
import com.shanghaigm.dms.view.widget.LetterSubButton;
import com.shanghaigm.dms.view.widget.OrderReviewButton;
import com.shanghaigm.dms.view.widget.OrderSubButton;
import com.shanghaigm.dms.view.widget.ReportQueryButton;
import com.shanghaigm.dms.view.widget.ReportSubButton;


public class HomeFragment extends BaseFragment {
    private static HomeFragment fragment;
    private LinearLayout ll_btn;
    private OrderSubButton btn_order_sub;
    private OrderReviewButton btn_order_review;
    private LetterSubButton btn_letter_sub;
    private LetterReviewButton btn_letter_review;
    private BillReviewButton btn_bill_review;
    private ContractReviewButton btn_contract_review;
    private ReportSubButton btn_report_sub;
    private ReportQueryButton btn_report_review;
    private LinearLayout.LayoutParams lp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home2, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        switch (app.getRoleCode()) {
            case "ssbspy":
            case "regional_Manager":
            case "order_handler":
                ll_btn.addView(btn_order_review);
                ll_btn.addView(btn_letter_review);
                break;
            case "test_manager":
            case "logistics_handler":
                ll_btn.addView(btn_contract_review);
                break;
            case "technology_handler_one":
            case "technology_handler_two":
            case "purchase_handler":
            case "production_management_handler":
            case "sales_department_handler":
                ll_btn.addView(btn_contract_review);
                ll_btn.addView(btn_bill_review);
                break;
            case "financial_examiner":
            case "general_manager":
            case "general_sales_manager":
                ll_btn.addView(btn_order_review);
                ll_btn.addView(btn_contract_review);
                ll_btn.addView(btn_bill_review);
//                for (int i = 0; i < 1; i++) {
//                    TextView tv = new TextView(getActivity());
//                    tv.setLayoutParams(lp);
//                    ll_btn.addView(tv);
//                }
                break;
            case "xsfzjl":
            case "finance_director":
                ll_btn.addView(btn_order_review);
//                for (int i = 0; i < 3; i++) {
//                    TextView tv = new TextView(getActivity());
//                    tv.setLayoutParams(lp);
//                    ll_btn.addView(tv);
//                }
                break;
            case "quality_department_handler":
                ll_btn.addView(btn_bill_review);
//                for (int i = 0; i < 3; i++) {
//                    TextView tv = new TextView(getActivity());
//                    tv.setLayoutParams(lp);
//                    ll_btn.addView(tv);
//                }
                break;
            case "ywy":
                ll_btn.addView(btn_order_sub);
                ll_btn.addView(btn_letter_sub);
//                for (int i = 0; i < 2; i++) {
//                    TextView tv = new TextView(getActivity());
//                    tv.setLayoutParams(lp);
//                    ll_btn.addView(tv);
//                }
                break;
            case "out_service":
                ll_btn.addView(btn_report_sub);
                ll_btn.addView(btn_report_review);
//                for (int i = 0; i < 2; i++) {
//                    TextView tv = new TextView(getActivity());
//                    tv.setLayoutParams(lp);
//                    ll_btn.addView(tv);
//                }
                break;
            case "fwjl":
                ll_btn.addView(btn_report_review);
//                for (int i = 0; i < 3; i++) {
//                    TextView tv = new TextView(getActivity());
//                    tv.setLayoutParams(lp);
//                    ll_btn.addView(tv);
//                }
                break;
        }
        btn_order_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((com.shanghaigm.dms.view.activity.ck.HomeActivity) getActivity()).chooseFragment(1);
            }
        });
        btn_order_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).chooseFragment(1);
            }
        });
        btn_letter_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((com.shanghaigm.dms.view.activity.ck.HomeActivity) getActivity()).chooseFragment(2);
            }
        });
        btn_letter_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(com.shanghaigm.dms.view.fragment.mm.HomeFragment.CONTRACT_OR_LETTER, 3);
                goToActivity(ContractReviewOrChangeLetterReviewActivity.class, bundle);
            }
        });
        btn_bill_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).chooseFragment(2);
            }
        });
        btn_contract_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(com.shanghaigm.dms.view.fragment.mm.HomeFragment.CONTRACT_OR_LETTER, 2);
                goToActivity(ContractReviewOrChangeLetterReviewActivity.class, bundle);
            }
        });
        btn_report_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).selectFragment(1);
            }
        });
        btn_report_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).selectFragment(2);
            }
        });
    }

    //    btn_order_sub,btn_order_review,btn_letter_sub,btn_letter_review,btn_bill_review,btn_contract_review,btn_report_sub,btn_report_review;
    private void initView(View v) {
        ll_btn = (LinearLayout) v.findViewById(R.id.ll_btn);
        lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        btn_order_sub = new OrderSubButton(getActivity());
        btn_order_sub.setLayoutParams(lp);
        btn_order_review = new OrderReviewButton(getActivity());
        btn_order_review.setLayoutParams(lp);
        btn_letter_sub = new LetterSubButton(getActivity());
        btn_letter_sub.setLayoutParams(lp);
        btn_letter_review = new LetterReviewButton(getActivity());
        btn_letter_review.setLayoutParams(lp);
        btn_bill_review = new BillReviewButton(getActivity());
        btn_bill_review.setLayoutParams(lp);
        btn_contract_review = new ContractReviewButton(getActivity());
        btn_contract_review.setLayoutParams(lp);
        btn_report_sub = new ReportSubButton(getActivity());
        btn_report_sub.setLayoutParams(lp);
        btn_report_review = new ReportQueryButton(getActivity());
        btn_report_review.setLayoutParams(lp);
        //占位
//        for (int i = 0; i < 3; i++) {
//            TextView tv = new TextView(getActivity());
//            tv.setLayoutParams(lp);
//            ll_btn.addView(tv);
//        }
    }

    public static HomeFragment getInstance() {
        if (fragment == null) {
            fragment = new HomeFragment();
        }
        return fragment;
    }
}
