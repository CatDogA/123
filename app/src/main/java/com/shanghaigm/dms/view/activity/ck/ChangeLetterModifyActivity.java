package com.shanghaigm.dms.view.activity.ck;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.ActivityChangeLetterModifyBinding;
import com.shanghaigm.dms.view.activity.BaseActivity;

public class ChangeLetterModifyActivity extends BaseActivity {
    private ActivityChangeLetterModifyBinding binding;
    private TextView title;
    private RelativeLayout rl_end,rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_letter_modify);
        initView();
        initData();
    }
    private void initView() {
        title = (TextView) findViewById(R.id.title_text);
        title.setText("变更函明细");
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());   //获取PID，目前获取自己的也只有该API，否则从/proc中自己的枚举其他进程吧，不过要说明的是，结束其他进程不一定有权限，不然就乱套了。
                System.exit(0);
            }
        });
    }

    private void initData() {
        binding.setInfo(app.getChangeLetterSubDetailInfo());
    }
}
