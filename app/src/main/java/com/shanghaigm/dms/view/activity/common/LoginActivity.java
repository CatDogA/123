package com.shanghaigm.dms.view.activity.common;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.chumi.widget.http.okhttp.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.chumi.widget.http.okhttp.RequestParams;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.SharedPreferencesUtil;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.activity.ck.HomeActivity;
import com.shanghaigm.dms.view.widget.MmPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends BaseActivity {
    private static String TAG = "LoginActivity";
    public static final String POSITION_FLAG = "position_flag";
    public static final String DB_ACCOUNT_INFO = "account_info";
    private String jobCode, jobName, roleCode;
    public DmsApplication app = DmsApplication.getInstance();
    private MmPopupWindow jobListPop;
    private Button btnLogin;
    private EditText edtAccount;
    private EditText edtPassWord;
    private EditText roleEdt;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setUpView();
        initData();
    }

    private void initData() {
        jobCode = app.getJobCode();
        //从sharedPreferencesUtil中取出
        String info = SharedPreferencesUtil.getString(this, DB_ACCOUNT_INFO);
        if (info != null) {
            String[] data = info.split(SharedPreferencesUtil.DB_SPLIT_FLAG);
            if (data.length > 2) {
                edtAccount.setText(data[0]);
                edtPassWord.setText(data[1]);
                jobCode = data[2];
            }
        }
    }

    private void setUpView() {

        edtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                roleEdt.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        roleEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRoles(1);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goToActivity(TestActivity.class);

                requestAccessToken();
            }
        });
        //enter键自动换行到密码
        edtAccount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    edtPassWord.requestFocus();
                    edtPassWord.setSelection(edtPassWord.getText().length());
                    return true;
                }
                return false;
            }
        });
        edtAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    getRoles(2);
                }
            }
        });
    }

    private void initView() {
        btnLogin = (Button) findViewById(R.id.login_button);
        edtAccount = (EditText) findViewById(R.id.name_edit);
        edtPassWord = (EditText) findViewById(R.id.pass_edit);
        roleEdt = (EditText) findViewById(R.id.role_edit);
        dialog = new LoadingDialog(context, "正在加载");
    }

    private void login(String roleCode) {
        String account = edtAccount.getText().toString();
        String password = edtPassWord.getText().toString();
        this.roleCode = roleCode;
        saveUserInfo(account, password, jobCode, roleCode);
        //存入sharedPreferences
        String data = account + SharedPreferencesUtil.DB_SPLIT_FLAG + password + SharedPreferencesUtil.DB_SPLIT_FLAG + jobCode;
        SharedPreferencesUtil.putString(LoginActivity.this, DB_ACCOUNT_INFO, data);
        Log.i(TAG, "login: " + roleCode);
        switch (roleCode) {
            case "ywy":
                goToActivity(com.shanghaigm.dms.view.activity.as.HomeActivity.class);
//                goToActivity(HomeActivity.class);
                finish();
                break;
            case "ssbspy":
            case "regional_Manager":
            case "order_handler":
            case "logistics_handler":
            case "technology_handler_one":
            case "technology_handler_two":
            case "purchase_handler":
            case "production_management_handler":
            case "financial_examiner":
            case "finance_director":
            case "sales_department_handler":
            case "general_sales_manager":
            case "general_manager":
            case "quality_department_handler":
            case "test_manager":
            case "xsfzjl":
                //同名冲突
                goToActivity(com.shanghaigm.dms.view.activity.mm.HomeActivity.class);
                finish();
                break;
            default:
                Toast.makeText(this, "抱歉没有对应岗位", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /*
    * 请求待办事项
    * */
    private void requestTasks() {
        RequestParams params = new RequestParams();
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_GET_HOME_DATA, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i("luo", "onSuccess: " + "task" + responseObj.toString());
            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.i("luo", "onFailure: " + reasonObj.toString());
            }
        }));
    }

    /*
    *   获取accessToken，也就是是否可登录
    * */
    private void requestAccessToken() {
        Log.i(TAG, "requestAccessToken:     " + edtPassWord.getText().toString() + "   " + jobCode);
        dialog.showLoadingDlg();
        RequestParams params = new RequestParams();
        if (edtAccount.getText().toString() != null && jobCode != null && edtPassWord.getText().toString() != null && roleEdt.getText().toString() != null && !roleEdt.getText().toString().equals("null")) {
            params.put("loginName", edtAccount.getText().toString());
            params.put("jobCode", jobCode);
            params.put("password", edtPassWord.getText().toString());
            CommonOkHttpClient.post(new CommonRequest().createPostRequest(Constant.URL_GET_ACCESS_TOKEN, params), new DisposeDataHandle(new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    dialog.dismissLoadingDlg();
                    Log.i("luo", "onSuccess: requestAccessToken" + responseObj.toString());
                    try {
                        JSONObject object = new JSONObject(responseObj.toString());
                        String resultCode = object.getString("resultCode");
                        JSONObject resultEntity = object.getJSONObject("resultEntity");
                        if (resultCode.equals("100")) {
                            String roleCode = resultEntity.getString("role_code");
                            if (resultCode != "") {
                                login(roleCode);
                            } else {
                                Toast.makeText(LoginActivity.this, "请选择职位", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "请输入完整正确信息", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Object reasonObj) {
                    Toast.makeText(LoginActivity.this, "密码或用户名错误", Toast.LENGTH_SHORT).show();
                }
            }));
        } else {
            Toast.makeText(this, "请填完整信息", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    *   保存用户名
    * */
    private void saveUserInfo(String account, String passWord, String jobCode, String roleCode) {
        app.setAccount(account);
        app.setPassWord(passWord);
        app.setJobCode(jobCode);
        app.setRoleCode(roleCode);
    }

    private void getRoles(final int flag) {
        dialog.showLoadingDlg();
        RequestParams param = new RequestParams();
        param.put("loginName", edtAccount.getText().toString());
        CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_GET_JOB_LIST, param), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                try {
                    Log.i("luo", "onSuccess: " + responseObj.toString());
                    ArrayList<String> jobCodes = new ArrayList<String>();
                    ArrayList<PopListInfo> jobList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    JSONArray resutEntity = jsonObject.getJSONArray("resultEntity");
                    for (int i = 0; i < resutEntity.length(); i++) {
                        PopListInfo info = new PopListInfo(resutEntity.getJSONObject(i).getString("job_name"));
                        jobCodes.add(resutEntity.getJSONObject(i).getString("job_code"));
                        jobList.add(info);
                    }
                    if (flag == 1) {
                        if (jobList.size() == 1) {
                            roleEdt.setText(resutEntity.getJSONObject(0).getString("job_name"));
                            jobCode = jobCodes.get(0);
                        } else {
                            initPopWindow(jobList);
                        }
                    }
                    if (flag == 2) {
                        if (jobList.size() == 1) {
                            roleEdt.setText(resutEntity.getJSONObject(0).getString("job_name"));
                            jobCode = jobCodes.get(0);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "用户名错误", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                Toast.makeText(LoginActivity.this, "用户名错误", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void initPopWindow(ArrayList<PopListInfo> list) {
        jobListPop = new MmPopupWindow(this, roleEdt, list, 5);
        jobListPop.showPopup(roleEdt);
    }
}
