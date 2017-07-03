package com.shanghaigm.dms.view.fragment.ck;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.ck.HomeActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;


public class HomeFragment extends BaseFragment implements View.OnClickListener{
    private LinearLayout orderText, modifyText;
    private RelativeLayout rl_end;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ck_fragment_home, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        orderText = (LinearLayout) view.findViewById(R.id.home_order_text);
        orderText.setOnClickListener(this);
        modifyText = (LinearLayout) view.findViewById(R.id.home_modify_text);
        modifyText.setOnClickListener(this);
        rl_end = (RelativeLayout) view.findViewById(R.id.rl_end);
        rl_end.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_order_text:
                ((HomeActivity) getActivity()).chooseFragment(1);
                break;
            case R.id.home_modify_text:
                ((HomeActivity) getActivity()).chooseFragment(2);
                break;
            case R.id.rl_end:
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
                app.OutOfApp();
                break;
        }
    }
}
