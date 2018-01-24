package com.shanghaigm.dms.view.fragment.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.FragmentInfo;
import com.shanghaigm.dms.view.activity.as.HomeActivity;
import com.shanghaigm.dms.view.activity.mm.CenterActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.BillReviewButton;
import com.shanghaigm.dms.view.widget.CommonButton;
import com.shanghaigm.dms.view.widget.ContractReviewButton;
import com.shanghaigm.dms.view.widget.LetterReviewButton;
import com.shanghaigm.dms.view.widget.LetterSubButton;
import com.shanghaigm.dms.view.widget.OrderReviewButton;
import com.shanghaigm.dms.view.widget.OrderSubButton;
import com.shanghaigm.dms.view.widget.ReportQueryButton;
import com.shanghaigm.dms.view.widget.ReportSubButton;


public class HomeFragment extends BaseFragment {
    public static String CONTRACT_OR_LETTER = "type";
    private static HomeFragment fragment;
    private LinearLayout ll_btn, ll_btn2, ll_btn3;
    private OrderSubButton btn_order_sub;
    private OrderReviewButton btn_order_review;
    private LetterSubButton btn_letter_sub;
    private LetterReviewButton btn_letter_review;
    private BillReviewButton btn_bill_review;
    private ContractReviewButton btn_contract_review;
    private ReportSubButton btn_report_sub;
    private ReportQueryButton btn_report_review;
    private LinearLayout.LayoutParams lp;
    private CommonButton btn_custom_file, btn_month_manage, btn_week_manage, btn_day_manage, btn_area_com,
            btn_month_review, btn_week_review, btn_day_review, btn_area_com_review;
    private TextView txt_seat, txt_seat2, txt_seat3;
    private static String TAG = "HomeFragment";

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
            //四种审核
            case "ssbspy":
            case "regional_Manager":
            case "order_handler":
                ll_btn.addView(btn_order_review);
                ll_btn.addView(btn_letter_review);

                ll_btn.addView(btn_custom_file);
                ll_btn.addView(btn_month_manage);
                ll_btn2.addView(btn_month_review);
                ll_btn2.addView(btn_week_manage);
                ll_btn2.addView(btn_week_review);
                ll_btn2.addView(btn_day_manage);
                ll_btn3.addView(btn_day_review);
                ll_btn3.addView(btn_area_com);
                ll_btn3.addView(txt_seat3);
                ll_btn3.addView(txt_seat2);
                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).unUseButton(2);
                com.shanghaigm.dms.view.activity.mm.HomeActivity.flag = 0;
                break;
            case "test_manager":
            case "logistics_handler":
                ll_btn.addView(btn_contract_review);
                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).unUseButton(1);
                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).unUseButton(2);
                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).setTab(1);
                com.shanghaigm.dms.view.activity.mm.HomeActivity.flag = 1;
                break;
            case "technology_handler_one":
            case "technology_handler_two":
            case "purchase_handler":
            case "production_management_handler":
            case "sales_department_handler":
                ll_btn.addView(btn_contract_review);
                ll_btn.addView(btn_bill_review);
                if (app.getRoleCode().equals("sales_department_handler")) {
                    ll_btn3.addView(btn_area_com_review);
                    ll_btn.addView(txt_seat);
                } else {
                    ll_btn.addView(txt_seat);
                    ll_btn.addView(txt_seat2);
                }


                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).setTab(2);
                com.shanghaigm.dms.view.activity.mm.HomeActivity.flag = 2;
                break;
            case "financial_examiner":
            case "general_manager":
            case "general_sales_manager":
                ll_btn.addView(btn_order_review);
                ll_btn.addView(btn_bill_review);

                ll_btn.addView(btn_contract_review);
                ll_btn.addView(txt_seat);
                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).setTab(3);
                com.shanghaigm.dms.view.activity.mm.HomeActivity.flag = 3;
                break;
            case "xsfzjl":
            case "finance_director":
                ll_btn.addView(btn_order_review);
                ll_btn.addView(txt_seat);
                ll_btn.addView(txt_seat2);
                ll_btn.addView(txt_seat3);
                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).setTab(4);
                com.shanghaigm.dms.view.activity.mm.HomeActivity.flag = 4;

                break;
            case "quality_department_handler":
            case "ZLBSHY":
                ll_btn.addView(btn_bill_review);
                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).setTab(5);
                com.shanghaigm.dms.view.activity.mm.HomeActivity.flag = 5;
                ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).unUseButton(1);
                break;
            //订单、更改函提交
            case "ywy":
                ll_btn.addView(btn_order_sub);
                ll_btn.addView(btn_letter_sub);

                ll_btn.addView(btn_custom_file);
                ll_btn.addView(btn_month_manage);
                ll_btn2.addView(btn_week_manage);
                ll_btn2.addView(btn_day_manage);
                ll_btn2.addView(btn_area_com);
                ll_btn2.addView(txt_seat);
                break;
            //日报提交
            case "out_service":
                ll_btn.addView(btn_report_sub);
                ll_btn.addView(btn_report_review);
                break;
            //日报审核
            case "fwjl":
                ll_btn.addView(btn_report_review);

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
                bundle.putInt(com.shanghaigm.dms.view.fragment.common.HomeFragment.CONTRACT_OR_LETTER, 3);
                goToActivity(CenterActivity.class, bundle);
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
                bundle.putInt(com.shanghaigm.dms.view.fragment.common.HomeFragment.CONTRACT_OR_LETTER, 2);
                goToActivity(CenterActivity.class, bundle);
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
        btn_custom_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CONTRACT_OR_LETTER, 9);
                goToActivity(CenterActivity.class, bundle);
            }
        });
        btn_month_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CONTRACT_OR_LETTER, 10);
                goToActivity(CenterActivity.class, bundle);
            }
        });
        btn_week_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CONTRACT_OR_LETTER, 11);
                goToActivity(CenterActivity.class, bundle);
            }
        });
        btn_day_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CONTRACT_OR_LETTER, 12);
                goToActivity(CenterActivity.class, bundle);
            }
        });
        btn_area_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CONTRACT_OR_LETTER, 13);
                goToActivity(CenterActivity.class, bundle);
            }
        });
        btn_month_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CONTRACT_OR_LETTER, 14);
                goToActivity(CenterActivity.class, bundle);
            }
        });
        btn_week_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CONTRACT_OR_LETTER, 15);
                goToActivity(CenterActivity.class, bundle);
            }
        });
        btn_day_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CONTRACT_OR_LETTER, 16);
                goToActivity(CenterActivity.class, bundle);
            }
        });
        btn_area_com_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CONTRACT_OR_LETTER, 17);
                goToActivity(CenterActivity.class, bundle);
            }
        });
    }

    private void initView(View v) {
        ll_btn = (LinearLayout) v.findViewById(R.id.ll_btn);
        ll_btn2 = (LinearLayout) v.findViewById(R.id.ll_btn2);
        ll_btn3 = (LinearLayout) v.findViewById(R.id.ll_btn3);
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
        btn_custom_file = new CommonButton(getActivity(), "客户档案", R.mipmap.custom_file);
        btn_custom_file.setLayoutParams(lp);
//        btn_month_manage,btn_week_manage,btn_day_manage,btn_area_com;
        btn_month_manage = new CommonButton(getActivity(), "月报管理", R.mipmap.month_report);
        btn_month_manage.setLayoutParams(lp);
        btn_week_manage = new CommonButton(getActivity(), "周报管理", R.mipmap.week_report);
        btn_week_manage.setLayoutParams(lp);
        btn_day_manage = new CommonButton(getActivity(), "日报管理", R.mipmap.day_report);
        btn_day_manage.setLayoutParams(lp);
        btn_area_com = new CommonButton(getActivity(), "区域竞品", R.mipmap.area_com);
        btn_area_com.setLayoutParams(lp);
        btn_month_review = new CommonButton(getActivity(), "月报审核", R.mipmap.month_report_r);
        btn_month_review.setLayoutParams(lp);
        btn_week_review = new CommonButton(getActivity(), "周报审核", R.mipmap.week_report_r);
        btn_week_review.setLayoutParams(lp);
        btn_day_review = new CommonButton(getActivity(), "日报审核", R.mipmap.day_report_r);
        btn_day_review.setLayoutParams(lp);
        btn_area_com_review = new CommonButton(getActivity(), "区域竞品审核", R.mipmap.area_com_r
        );
        btn_area_com_review.setLayoutParams(lp);
        txt_seat = new TextView(getActivity());
        txt_seat.setLayoutParams(lp);
        txt_seat2 = new TextView(getActivity());
        txt_seat2.setLayoutParams(lp);
        txt_seat3 = new TextView(getActivity());
        txt_seat3.setLayoutParams(lp);
    }

    public static HomeFragment getInstance() {
        if (fragment == null) {
            fragment = new HomeFragment();
        }
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
        } else {
            switch (app.getRoleCode()) {
                case "out_service":
                case "fwjl":
                    if (!((com.shanghaigm.dms.view.activity.as.HomeActivity) getActivity()).isBackClick) {
                        ((com.shanghaigm.dms.view.activity.as.HomeActivity) getActivity()).fragmentInfos.add(new FragmentInfo(1));
                    }
                    ((com.shanghaigm.dms.view.activity.as.HomeActivity) getActivity()).isBackClick = false;
                    break;
                case "ywy":
                    if (!((com.shanghaigm.dms.view.activity.ck.HomeActivity) getActivity()).isBackClick) {
                        ((com.shanghaigm.dms.view.activity.ck.HomeActivity) getActivity()).fragmentInfos.add(new FragmentInfo(1));
                    }
                    ((com.shanghaigm.dms.view.activity.ck.HomeActivity) getActivity()).isBackClick = false;
                    break;
                default:
                    if (!((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).isBackClick) {
                        ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).fragmentInfos.add(new FragmentInfo(1));
                    }
                    ((com.shanghaigm.dms.view.activity.mm.HomeActivity) getActivity()).isBackClick = false;
                    break;

            }
        }
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        ((com.shanghaigm.dms.view.activity.ck.HomeActivity)getActivity()).setButton(1);
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.i("homefragment", "onStart: "+"start            1");
//        ((com.shanghaigm.dms.view.activity.ck.HomeActivity)getActivity()).fragmentInfos.add(new FragmentInfo(1));
//    }
}
