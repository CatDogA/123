package com.shanghaigm.dms.view.activity.mm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.fragment.common.HomeFragment;
import com.shanghaigm.dms.view.fragment.mm.ChangeBillReviewFragment;
import com.shanghaigm.dms.view.fragment.mm.OrderReviewFragment;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private FragmentManager fm;
    private HomeFragment mHomeFragment;
    private OrderReviewFragment mOrderFragment;
    private ChangeBillReviewFragment mChangeBillReviewFragment;
    private LinearLayout text_home,text_order,text_modify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mm_activity_home);
        //初始化控件
        initView();
        showHome();
    }

    private void showHome() {
        text_home.setSelected(true);
        //添加默认显示的frgment
        mHomeFragment = new HomeFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.layout_content,mHomeFragment);
        fragmentTransaction.commit();
    }
    public void initView(){
        text_home = (LinearLayout) findViewById(R.id.home_tab_text);
        text_home.setOnClickListener(this);
        text_order = (LinearLayout) findViewById(R.id.order_tab_text);
        text_order.setOnClickListener(this);
        text_modify = (LinearLayout) findViewById(R.id.modify_tab_text);
        text_modify.setOnClickListener(this);
    }
    private void hideFragment(Fragment fragment, FragmentTransaction ft){
        if(fragment != null){
            ft.hide(fragment);
        }
    }
    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch(v.getId()){
            case R.id.home_tab_text:
                setSelected();
                text_home.setSelected(true);
                hideFragment(mOrderFragment,fragmentTransaction);
                hideFragment(mChangeBillReviewFragment,fragmentTransaction);
                if(mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content,mHomeFragment);
                }else{
                    fragmentTransaction.show(mHomeFragment);
                }
                break;

            case R.id.order_tab_text:
                setSelected();
                text_order.setSelected(true);
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mChangeBillReviewFragment,fragmentTransaction);
                if(mOrderFragment == null){
                    mOrderFragment = OrderReviewFragment.getInstance();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content,mOrderFragment);
                }else{
                    fragmentTransaction.show(mOrderFragment);
                }
                break;
            case R.id.modify_tab_text:
                setSelected();
                text_modify.setSelected(true);
                hideFragment(mOrderFragment,fragmentTransaction);
                hideFragment(mHomeFragment,fragmentTransaction);
                if(mChangeBillReviewFragment == null){
                    mChangeBillReviewFragment = new ChangeBillReviewFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content, mChangeBillReviewFragment);
                }else{
                    fragmentTransaction.show(mChangeBillReviewFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }
    //重置所有文本的选中状态
    private void setSelected(){
        text_home.setSelected(false);
        text_order.setSelected(false);
        text_modify.setSelected(false);
    }

    public void chooseFragment(int i){
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch (i){
            case 1:
                setSelected();
                text_order.setSelected(true);
                hideFragment(mHomeFragment,fragmentTransaction);
                hideFragment(mChangeBillReviewFragment,fragmentTransaction);
                if(mOrderFragment == null){
                    mOrderFragment = new OrderReviewFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content,mOrderFragment);
                }else{
                    fragmentTransaction.show(mOrderFragment);
                }
                break;
            case 2:
                setSelected();
                text_modify.setSelected(true);
                hideFragment(mOrderFragment,fragmentTransaction);
                hideFragment(mHomeFragment,fragmentTransaction);
                if(mChangeBillReviewFragment == null){
                    mChangeBillReviewFragment = new ChangeBillReviewFragment();
                    //隐藏其他两个fragment
                    fragmentTransaction.add(R.id.layout_content, mChangeBillReviewFragment);
                }else{
                    fragmentTransaction.show(mChangeBillReviewFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }
}
