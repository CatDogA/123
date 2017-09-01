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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.FragmentInfo;
import com.shanghaigm.dms.view.fragment.common.HomeFragment;
import com.shanghaigm.dms.view.fragment.mm.ChangeBillReviewFragment;
import com.shanghaigm.dms.view.fragment.mm.OrderReviewFragment;

import java.util.ArrayList;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    public static String ORDER_BILL_REFRESH = "order_bill_refresh";
    private FragmentManager fm;
    private HomeFragment mHomeFragment;
    private OrderReviewFragment mOrderFragment;
    private ChangeBillReviewFragment mChangeBillReviewFragment;
    private LinearLayout text_home, text_order, text_modify;
    public Boolean useOrder;    //是否使用订单审核
    public Boolean useBill;     //是否使用更改单审核

    public Boolean isBackClick;     //判断是否点击回退
    public ArrayList<FragmentInfo> fragmentInfos;       //回退fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mm_activity_home);
        //初始化控件
        initView();
        showHome();
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
        text_home = (LinearLayout) findViewById(R.id.home_tab_text);
        text_home.setOnClickListener(this);
        text_order = (LinearLayout) findViewById(R.id.order_tab_text);
        text_order.setOnClickListener(this);
        text_modify = (LinearLayout) findViewById(R.id.modify_tab_text);
        text_modify.setOnClickListener(this);
        useOrder = true;
        useBill = true;
        isBackClick = false;
        fragmentInfos = new ArrayList<>();
        if (fragmentInfos.size() > 0) {
            fragmentInfos.clear();
        }
    }

    private void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.addToBackStack(null);
        switch (v.getId()) {
            case R.id.home_tab_text:
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

            case R.id.order_tab_text:
                if (useOrder) {
                    setSelected();
                    text_order.setSelected(true);
                    hideFragment(mHomeFragment, fragmentTransaction);
                    hideFragment(mChangeBillReviewFragment, fragmentTransaction);
                    if (mOrderFragment == null) {
                        mOrderFragment = OrderReviewFragment.getInstance();
                        //隐藏其他两个fragment
                        fragmentTransaction.add(R.id.layout_content, mOrderFragment);
                    } else {
                        fragmentTransaction.show(mOrderFragment);
                    }
                } else {
                    Toast.makeText(this, "无此功能", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.modify_tab_text:
                if (useBill) {
                    setSelected();
                    text_modify.setSelected(true);
                    hideFragment(mOrderFragment, fragmentTransaction);
                    hideFragment(mHomeFragment, fragmentTransaction);
                    if (mChangeBillReviewFragment == null) {
                        mChangeBillReviewFragment = new ChangeBillReviewFragment();
                        //隐藏其他两个fragment
                        fragmentTransaction.add(R.id.layout_content, mChangeBillReviewFragment);
                    } else {
                        fragmentTransaction.show(mChangeBillReviewFragment);
                    }
                } else {
                    Toast.makeText(this, "无此功能", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        fragmentTransaction.commit();
    }

    //重置所有文本的选中状态
    private void setSelected() {
        text_home.setSelected(false);
        text_order.setSelected(false);
        text_modify.setSelected(false);
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
                text_order.setSelected(true);
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
                text_modify.setSelected(true);
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
            Log.i("homeactivity", "onKeyDown:        "+fragmentInfos.size());
            if(fragmentInfos.size()>0){
                chooseFragment(fragmentInfos.get(fragmentInfos.size() - 1).flag - 1);
                fragmentInfos.remove(fragmentInfos.size() - 1);
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
