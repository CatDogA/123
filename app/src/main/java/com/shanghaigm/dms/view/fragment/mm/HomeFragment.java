package com.shanghaigm.dms.view.fragment.mm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.mm.ContractReviewOrChangeLetterReviewActivity;
import com.shanghaigm.dms.view.activity.mm.HomeActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;


public class HomeFragment extends BaseFragment {
    private LinearLayout orderAndChangeBillLL, orderAndChangeLetterLL, contractAndChangeBillLL, orderReviewLL, conTractReviewLL, changeLetterReviewLL, changeBillReview, orderReviewLL1, changeBillReview1;
    public static String CONTRACT_OR_LETTER = "type";
    private RelativeLayout rl_end;
    private static String TAG = "HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mm_fragment_home, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        Log.i(TAG, "setUpView: " + app.getRoleCode());
        switch (app.getRoleCode()) {
            case "ssbspy":
            case "regional_Manager":
            case "order_handler":
            case "out_service":
                ViewGone(orderAndChangeBillLL);
                ViewGone(contractAndChangeBillLL);
                break;
            case "logistics_handler":
//                ViewGone(orderAndChangeLetterLL);
//                ViewGone(orderAndChangeBillLL);
//                ViewGone(changeBillReview);
//                break;
            case "test_manager":
                ViewGone(orderAndChangeBillLL);
                ViewGone(orderAndChangeLetterLL);
                ViewGone(changeBillReview);
                break;
            case "technology_handler_one":
            case "technology_handler_two":
            case "purchase_handler":
            case "production_management_handler":
            case "sales_department_handler":
                ViewGone(orderAndChangeBillLL);
                ViewGone(orderAndChangeLetterLL);
                break;
            case "financial_examiner":
            case "general_manager":
            case "general_sales_manager":
                ViewGone(orderAndChangeLetterLL);
                changeBillReview.setVisibility(View.INVISIBLE);
                break;
            case "finance_director":
                ViewGone(orderAndChangeBillLL);
                ViewGone(contractAndChangeBillLL);
                ViewGone(changeLetterReviewLL);
                break;
            case "quality_department_handler":
                ViewGone(orderAndChangeLetterLL);
                ViewGone(contractAndChangeBillLL);
                orderReviewLL1.setVisibility(View.GONE);
                break;
            case "xsfzjl":
                ViewGone(contractAndChangeBillLL);
                ViewGone(orderAndChangeBillLL);
                ViewGone(changeLetterReviewLL);
                break;
        }
        orderReviewLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).chooseFragment(1);
            }
        });
        changeBillReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).chooseFragment(2);
            }
        });

        conTractReviewLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(HomeFragment.CONTRACT_OR_LETTER, 2);
                goToActivity(ContractReviewOrChangeLetterReviewActivity.class, bundle);
            }
        });

        changeLetterReviewLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(HomeFragment.CONTRACT_OR_LETTER, 3);
                goToActivity(ContractReviewOrChangeLetterReviewActivity.class, bundle);
            }
        });

        orderReviewLL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).chooseFragment(1);
            }
        });

        changeBillReview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).chooseFragment(2);
            }
        });

        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
    }

    private void ViewGone(View v) {
        v.setVisibility(View.GONE);
    }

    private void initView(View v) {
        orderReviewLL = (LinearLayout) v.findViewById(R.id.order_review_ll);
        conTractReviewLL = (LinearLayout) v.findViewById(R.id.contract_review_ll);
        changeLetterReviewLL = (LinearLayout) v.findViewById(R.id.change_letter_review_ll);
        changeBillReview = (LinearLayout) v.findViewById(R.id.change_bill_review_ll);

        orderReviewLL1 = (LinearLayout) v.findViewById(R.id.order_review_ll1);
        changeBillReview1 = (LinearLayout) v.findViewById(R.id.change_bill_review_ll1);
        orderAndChangeLetterLL = (LinearLayout) v.findViewById(R.id.order_change_letter_ll);
        contractAndChangeBillLL = (LinearLayout) v.findViewById(R.id.contract_change_bill_ll);
        orderAndChangeBillLL = (LinearLayout) v.findViewById(R.id.order_change_bill_ll);
        rl_end = (RelativeLayout) v.findViewById(R.id.rl_end);
    }

}
