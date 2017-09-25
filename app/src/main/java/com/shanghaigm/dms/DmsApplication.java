package com.shanghaigm.dms;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.shanghaigm.dms.model.entity.as.SaveUsedPaths;
import com.shanghaigm.dms.model.entity.ck.AllocationAddChooseUndefaultInfo;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterSubDetailInfo;
import com.shanghaigm.dms.model.entity.common.MainData;
import com.shanghaigm.dms.model.entity.mm.ChangeBillDetailInfo;
import com.shanghaigm.dms.model.entity.mm.ChangeLetterDetailInfo;
import com.shanghaigm.dms.model.entity.mm.ContractDetailInfo;
import com.shanghaigm.dms.model.entity.mm.MatchingBean;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoOne;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoBean;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.activity.common.LoginActivity;

import java.util.ArrayList;

/**
 * Created by LuoXiaoBin on 2017/4/28.
 *
 * @function 1.整个程序的入口
 * 2.初始化工作
 * 3.为整个应用提供上下文
 * 4.主要储存全局用到的数据，不要存太多，会溢出的，而且占用空间
 */

public class DmsApplication extends Application {
    private static DmsApplication mApplication = null;
    private MainData mainData;
    //几种详情信息
    private OrderDetailInfoBean orderDetailInfoBean;
    private ArrayList<MatchingBean> matchingBeanArrayList;
    private int flow_detail_id;     //各个审核
    private String contract_id;                   //合同审核
    private ChangeLetterDetailInfo changeLetterDetailInfo;
    private ContractDetailInfo contractDetailInfo;
    private ChangeBillDetailInfo changeBillDetailInfo;
    private ChangeLetterSubDetailInfo changeLetterSubDetailInfo;
    private ArrayList<BaseActivity> activities;
    private final String TAG = "DmsApplication";
    public String picPath;      //拍摄照片之后存储路径

    //    public ArrayList<SaveUsedPaths> usedPaths = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        //Manifest.xml中配置了android:name="DmsApplication"
        // 之后只需撰写DmsApplication.getInstanse()即可获取获取应用程序的单例
        // 而不需要设置一个app静态变量去设置，具体实现还需要等后续实际使用时验证
        mApplication = this;
        mainData = new MainData();
    }

    public void endApp(Context context) {
        Log.i(TAG, "endApp:         " + activities.size());
//        for (int i = 0; i < activities.size(); i++) {
//            activities.get(i).finish();   //保留登录
//        }
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }

    public Boolean isSoftKeyBoardOpen() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();

    }

    public void addActivity(BaseActivity activity) {
        if (activities == null) {
            activities = new ArrayList<>();
        }
        Log.i(TAG, "addActivity: " + activities.size());
        activities.add(activity);
    }

    public static DmsApplication getInstance() {
        if (mApplication == null) {
            mApplication = new DmsApplication();
        }
        return mApplication;
    }

    /**
     * 控制输入小数位数
     *
     * @param edt
     * @param num
     * @param flag 判断是否小于1
     */
    public void controlDot(EditText edt, final int num, final Boolean flag) {
        edt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (flag) {
                    if (!(temp.equals("") || temp.equals("."))) {
                        Double n = Double.parseDouble(temp);
                        Log.i(TAG, "afterTextChanged:n       " + n);
                        if (n >= 1) {
                            if (posDot > 0) {
                                edt.delete(0, posDot - 1);
                                Toast.makeText(DmsApplication.this, "数字必须小于1", Toast.LENGTH_SHORT).show();
                            } else {
                                edt.clear();
                                Toast.makeText(DmsApplication.this, "数字必须小于1", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > num) {
                    edt.delete(posDot + num + 1, posDot + num + 2);
                    Toast.makeText(DmsApplication.this, "小数限制为:" + num + "位", Toast.LENGTH_SHORT).show();
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }

    public String getAccount() {
        return mainData.getAccount();
    }

    public void setAccount(String account) {
        mainData.setAccount(account);
    }

    public String getPassWord() {
        return mainData.getPassword();
    }

    public void setPassWord(String passWord) {
        mainData.setPassword(passWord);
    }

    public String getJobCode() {
        return mainData.getJobCode();
    }

    public void setJobCode(String jobCode) {
        mainData.setJobCode(jobCode);
    }

    public String getRoleCode() {
        return mainData.getRoleCode();
    }

    public void setRoleCode(String roleCode) {
        mainData.setRoleCode(roleCode);
    }


    public ChangeLetterDetailInfo getChangeLetterDetailInfo() {
        return changeLetterDetailInfo;
    }

    public void setChangeLetterDetailInfo(ChangeLetterDetailInfo changeLetterDetailInfo) {
        this.changeLetterDetailInfo = changeLetterDetailInfo;
    }

    public ContractDetailInfo getContractDetailInfo() {
        return contractDetailInfo;
    }

    public void setContractDetailInfo(ContractDetailInfo contractDetailInfo) {
        this.contractDetailInfo = contractDetailInfo;
    }

    public OrderDetailInfoBean getOrderDetailInfoBean() {
        return orderDetailInfoBean;
    }

    public void setOrderDetailInfoBean(OrderDetailInfoBean orderDetailInfoBean) {
        this.orderDetailInfoBean = orderDetailInfoBean;
    }

    public ChangeBillDetailInfo getChangeBillDetailInfo() {
        return changeBillDetailInfo;
    }

    public void setChangeBillDetailInfo(ChangeBillDetailInfo changeBillDetailInfo) {
        this.changeBillDetailInfo = changeBillDetailInfo;
    }

    public ChangeLetterSubDetailInfo getChangeLetterSubDetailInfo() {
        return changeLetterSubDetailInfo;
    }

    public void setChangeLetterSubDetailInfo(ChangeLetterSubDetailInfo changeLetterSubDetailInfo) {
        this.changeLetterSubDetailInfo = changeLetterSubDetailInfo;
    }

    public void OutOfApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public ArrayList<MatchingBean> getMatchingBeanArrayList() {
        return matchingBeanArrayList;
    }

    public void setMatchingBeanArrayList(ArrayList<MatchingBean> matchingBeanArrayList) {
        this.matchingBeanArrayList = matchingBeanArrayList;
    }

    public int getFlow_detail_id() {
        return flow_detail_id;
    }

    public void setFlow_detail_id(int flow_detail_id) {
        this.flow_detail_id = flow_detail_id;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }
}
