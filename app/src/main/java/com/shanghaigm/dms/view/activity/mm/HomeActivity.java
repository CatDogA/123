package com.shanghaigm.dms.view.activity.mm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.FragmentInfo;
import com.shanghaigm.dms.view.fragment.common.HomeFragment;
import com.shanghaigm.dms.view.fragment.mm.ChangeBillReviewFragment;
import com.shanghaigm.dms.view.fragment.mm.OrderReviewFragment;
import com.shanghaigm.dms.view.widget.TabBottom;

import java.util.ArrayList;

public class HomeActivity extends FragmentActivity {
    public static String ORDER_BILL_REFRESH = "order_bill_refresh";
    private FragmentManager fm;
    private HomeFragment mHomeFragment;
    private OrderReviewFragment mOrderFragment;
    private ChangeBillReviewFragment mChangeBillReviewFragment;
    private LinearLayout text_home;
    public Boolean useOrder;    //是否使用订单审核
    public Boolean useBill;     //是否使用更改单审核
    private LinearLayout llTabs;
    public Boolean isBackClick;     //判断是否点击回退
    public ArrayList<FragmentInfo> fragmentInfos;       //回退fragment
    private TabBottom tab1, tab2;
    private LinearLayout.LayoutParams lp;
    public static int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mm_activity_home);
        //初始化控件
        initView();
        showHome();
    }

    @Override
    protected void onStart() {
        super.onStart();
        chooseFragment(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle b = intent.getExtras();
        if (b != null) {
            if (b.getInt(HomeActivity.ORDER_BILL_REFRESH) == 1) {
                mOrderFragment.refresh();
            }
            if (b.getInt(HomeActivity.ORDER_BILL_REFRESH) == 2) {
                mChangeBillReviewFragment.refresh();
            }
        }

    }

    private void showHome() {
        text_home.setSelected(true);
        //添加默认显示的frgment
        mHomeFragment = new HomeFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.layout_content, mHomeFragment);
        fragmentTransaction.commit();
    }

    public void initView() {
        lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        text_home = (LinearLayout) findViewById(R.id.home_tab_text);
        llTabs = (LinearLayout) findViewById(R.id.ll_tabs);
        useOrder = true;
        useBill = true;
        isBackClick = false;
        fragmentInfos = new ArrayList<>();
        if (fragmentInfos.size() > 0) {
            fragmentInfos.clear();
        }
        tab1 = new TabBottom(this, "订单审核", R.drawable.tab_order_review);
        tab2 = new TabBottom(this, "更改函审核", R.drawable.tab_bill_review);
        tab1.setLayoutParams(lp);
        tab2.setLayoutParams(lp);
        llTabs.addView(tab1);
        llTabs.addView(tab2);
        text_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(1);
            }
        });
        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(2);
            }
        });
        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(3);

            }
        });

    }

    public void setTab(int flag) {
        switch (flag) {
            case 1:
                tab1.setTxt("合同审核");
                tab2.setVisibility(View.INVISIBLE);
                tab2.setEnabled(false);
                break;
            case 2:
                tab1.setTxt("合同审核");
                tab2.setTxt("更改单审核");
                break;
            case 3:
                tab2.setTxt("更改单审核");
                break;
            case 4:
                tab2.setVisibility(View.INVISIBLE);
                tab2.setEnabled(false);
                break;
            case 5:
                tab1.setTxt("更改单审核");
                tab2.setVisibility(View.INVISIBLE);
                tab2.setEnabled(false);
                break;
        }
    }

    private void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

    private void click(int type) {
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Intent i = new Intent(this,CenterActivity.class);
        Bundle bundle = new Bundle();
        switch (type) {
            case 1:
                setSelected();
                text_home.setSelected(true);
                hideFragment(mOrderFragment, fragmentTransaction);
                hideFragment(mChangeBillReviewFragment, fragmentTransaction);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content, mHomeFragment);
                } else {
                    fragmentTransaction.show(mHomeFragment);
                }
                break;
            case 2:
                switch (flag){
                    case 0:
                    case 3:
                    case 4:
                        setSelected();
                        tab1.setSelected(true);
                        hideFragment(mHomeFragment, fragmentTransaction);
                        hideFragment(mChangeBillReviewFragment, fragmentTransaction);
                        if (mOrderFragment == null) {
                            mOrderFragment = OrderReviewFragment.getInstance();
                            //隐藏其他两个fragment
                            fragmentTransaction.add(R.id.layout_content, mOrderFragment);
                        } else {
                            fragmentTransaction.show(mOrderFragment);
                        }
                        break;
                    case 1:
                    case 2:
                        bundle.putInt(com.shanghaigm.dms.view.fragment.common.HomeFragment.CONTRACT_OR_LETTER, 2);
                        i.putExtras(bundle);
                        startActivity(i);
                        break;
                    case 5:
                        setSelected();
                        tab1.setSelected(true);
                        hideFragment(mOrderFragment, fragmentTransaction);
                        hideFragment(mHomeFragment, fragmentTransaction);
                        if (mChangeBillReviewFragment == null) {
                            mChangeBillReviewFragment = new ChangeBillReviewFragment();
                            //隐藏其他两个fragment
                            fragmentTransaction.add(R.id.layout_content, mChangeBillReviewFragment);
                        } else {
                            fragmentTransaction.show(mChangeBillReviewFragment);
                        }
                        break;
//                    case 3:
//                        bundle.putInt(com.shanghaigm.dms.view.fragment.common.HomeFragment.CONTRACT_OR_LETTER, 2);
//                        goToActivity(CenterActivity.class, bundle);
//                        break;
                }

                break;
            case 3:
                switch (flag){
                    case 0:
                        bundle.putInt(com.shanghaigm.dms.view.fragment.common.HomeFragment.CONTRACT_OR_LETTER, 3);
                        i.putExtras(bundle);
                        startActivity(i);
                        break;
                    case 2:
                    case 3:
                        setSelected();
                        tab2.setSelected(true);
                        hideFragment(mOrderFragment, fragmentTransaction);
                        hideFragment(mHomeFragment, fragmentTransaction);
                        if (mChangeBillReviewFragment == null) {
                            mChangeBillReviewFragment = new ChangeBillReviewFragment();
                            //隐藏其他两个fragment
                            fragmentTransaction.add(R.id.layout_content, mChangeBillReviewFragment);
                        } else {
                            fragmentTransaction.show(mChangeBillReviewFragment);
                        }
                        break;
                }
                break;
        }
        fragmentTransaction.commit();
    }

    //重置所有文本的选中状态
    private void setSelected() {
        text_home.setSelected(false);
        tab1.setSelected(false);
        tab2.setSelected(false);
    }

    public void chooseFragment(int i) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.addToBackStack(null);
        switch (i) {
            case 0:
                setSelected();
                text_home.setSelected(true);
                hideFragment(mOrderFragment, fragmentTransaction);
                hideFragment(mChangeBillReviewFragment, fragmentTransaction);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content, mHomeFragment);
                } else {
                    fragmentTransaction.show(mHomeFragment);
                }
                break;
            case 1:
                setSelected();
                tab1.setSelected(true);
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mChangeBillReviewFragment, fragmentTransaction);
                if (mOrderFragment == null) {
                    mOrderFragment = new OrderReviewFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content, mOrderFragment);
                } else {
                    fragmentTransaction.show(mOrderFragment);
                }
                break;
            case 2:
                setSelected();
                tab2.setSelected(true);
                hideFragment(mOrderFragment, fragmentTransaction);
                hideFragment(mHomeFragment, fragmentTransaction);
                if (mChangeBillReviewFragment == null) {
                    mChangeBillReviewFragment = new ChangeBillReviewFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content, mChangeBillReviewFragment);
                } else {
                    fragmentTransaction.show(mChangeBillReviewFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    public void unUseButton(int type) {
        switch (type) {
            case 1:
                useOrder = false;
                break;
            case 2:
                useBill = false;
                break;
        }
    }

    public void back() {
        onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(0, 0, 0, 0, 0));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            isBackClick = true;
            Log.i("homeactivity", "onKeyDown:        " + fragmentInfos.size());
            if (fragmentInfos.size() > 0) {
                chooseFragment(fragmentInfos.get(fragmentInfos.size() - 1).flag - 1);
                fragmentInfos.remove(fragmentInfos.size() - 1);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
