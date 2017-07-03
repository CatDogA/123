package com.shanghaigm.dms.view.activity.ck;

import android.app.FragmentManager;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chumi.widget.dialog.LoadingDialog;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.ck.OrderModifyAllocationDetailFragment;
import com.shanghaigm.dms.view.fragment.ck.OrderModifyBaseDetailFragment;
import com.shanghaigm.dms.view.fragment.ck.OrderModifyPayDetailFragment;

import java.util.ArrayList;

public class OrderModifyActivity extends AppCompatActivity {
    private static String TAG = "OrderDetailActivity";
    private TextView titleText;
    private TabLayout tabLayout;
    private RelativeLayout rl_back, rl_end;
    private ArrayList<BaseFragment> fragments;
    private FragmentManager manager;
    private Button saveBtn, submitBtn;
    private LoadingDialog dialog;
    private DmsApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add);
        initView();
        initData();
        setUpView();
    }

    private void setUpView() {
        titleText.setText(String.format(getString(R.string.ck_order_add)));

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
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

    private void initData() {
        tabLayout.setSelectedTabIndicatorColor(Color.GRAY);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tabLayout.addTab(tabLayout.newTab().setText("基本信息").setTag(0));
        tabLayout.addTab(tabLayout.newTab().setText("付款方式").setTag(1));
        tabLayout.addTab(tabLayout.newTab().setText("选配").setTag(2));

        fragments = new ArrayList<>();
        fragments.add(OrderModifyBaseDetailFragment.getInstance());
        fragments.add(OrderModifyPayDetailFragment.getInstance());
        fragments.add(OrderModifyAllocationDetailFragment.getInstance());

        manager = getFragmentManager();
        manager.beginTransaction().add(R.id.mm_order_add_fragment_content, fragments.get(0)).commit();
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
}
