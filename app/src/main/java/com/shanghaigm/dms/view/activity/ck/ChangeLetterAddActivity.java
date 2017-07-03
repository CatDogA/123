package com.shanghaigm.dms.view.activity.ck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanghaigm.dms.R;

import org.w3c.dom.Text;

public class ChangeLetterAddActivity extends AppCompatActivity {
    private TextView title;
    private RelativeLayout rl_back, rl_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_letter_add);
        initView();
        setUpView();
    }

    private void setUpView() {
        title.setText("变更函填写");
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());   //获取PID，目前获取自己的也只有该API，否则从/proc中自己的枚举其他进程吧，不过要说明的是，结束其他进程不一定有权限，不然就乱套了。
                System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
            }
        });
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_text);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
    }
}
