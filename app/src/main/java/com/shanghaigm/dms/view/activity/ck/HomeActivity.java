package com.shanghaigm.dms.view.activity.ck;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.fragment.ck.ChangeLetterSubFragment;
import com.shanghaigm.dms.view.fragment.ck.OrderSubFragment;
import com.shanghaigm.dms.view.fragment.common.HomeFragment;

/**
 * @function 创建首页所有的fragment，以及fragment
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    public static String ORDER_LETTER_SUB = "order_letter_sub";
    private FragmentManager fm;
    private HomeFragment mHomeFragment;
    private OrderSubFragment mOrderFragment;
    private ChangeLetterSubFragment mModifyFragment;

    private LinearLayout text_home, text_order, text_modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ck_activity_home);
        //初始化控件
        initView();
        showHome();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle b = intent.getExtras();
        if(b!=null){
            if(b.getInt(HomeActivity.ORDER_LETTER_SUB)==1){
                mOrderFragment.refresh();
            }
            if(b.getInt(HomeActivity.ORDER_LETTER_SUB)==2){
                mModifyFragment.refresh();
            }
        }
    }

    private void showHome() {
        text_home.setSelected(true);
        //添加默认显示的frgment
        mHomeFragment = new HomeFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.layout_content, mHomeFragment);
        fragmentTransaction.commit();
    }

    private void initView() {
        text_home = (LinearLayout) findViewById(R.id.home_tab_text);
        text_home.setOnClickListener(this);
        text_order = (LinearLayout) findViewById(R.id.order_tab_text);
        text_order.setOnClickListener(this);
        text_modify = (LinearLayout) findViewById(R.id.modify_tab_text);
        text_modify.setOnClickListener(this);
    }

    private void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.home_tab_text:
                setSelected();
                text_home.setSelected(true);
                hideFragment(mOrderFragment, fragmentTransaction);
                hideFragment(mModifyFragment, fragmentTransaction);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content, mHomeFragment);
                } else {
                    fragmentTransaction.show(mHomeFragment);
                }
                break;

            case R.id.order_tab_text:
                setSelected();
                text_order.setSelected(true);
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mModifyFragment, fragmentTransaction);
                if (mOrderFragment == null) {
                    mOrderFragment = OrderSubFragment.getInstance();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content, mOrderFragment);
                } else {
                    fragmentTransaction.show(mOrderFragment);
                }
                break;
            case R.id.modify_tab_text:
                setSelected();
                text_modify.setSelected(true);
                hideFragment(mOrderFragment, fragmentTransaction);
                hideFragment(mHomeFragment, fragmentTransaction);
                if (mModifyFragment == null) {
                    mModifyFragment = new ChangeLetterSubFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content, mModifyFragment);
                } else {
                    fragmentTransaction.show(mModifyFragment);
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
        switch (i) {
            case 1:
                setSelected();
                text_order.setSelected(true);
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mModifyFragment, fragmentTransaction);
                if (mOrderFragment == null) {
                    mOrderFragment = OrderSubFragment.getInstance();
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
                if (mModifyFragment == null) {
                    mModifyFragment = new ChangeLetterSubFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content, mModifyFragment);
                } else {
                    fragmentTransaction.show(mModifyFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }
}
