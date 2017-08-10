package com.shanghaigm.dms.view.activity.ck;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.ActivityChangeLetterQueryBinding;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterAllocationInfo;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.adapter.ListAdapter;

import java.util.ArrayList;

public class ChangeLetterQueryDetailActivity extends BaseActivity {
    private ActivityChangeLetterQueryBinding binding;
    private TextView title;
    private RelativeLayout rl_back, rl_end;
    private ListView listView;
    private ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_letter_query);
        initData();
        initView();
        initIntent();
    }

    private void initIntent() {
        Bundle b = getIntent().getExtras();
        ArrayList<ChangeLetterAllocationInfo> infos = (ArrayList<ChangeLetterAllocationInfo>) b.getSerializable(PaperInfo.CHANGE_LETTER_INFO);
        Log.i(TAG, "initIntent: infos           "+infos.size());
        adapter = new ListAdapter(this, R.layout.list_item_change_letter_allocation_query, BR.info, infos);
        listView.setAdapter(adapter);
    }

    private void initView() {
        title.setText("变更函明细");
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp();
            }
        });
    }

    private void initData() {
        binding.setInfo(app.getChangeLetterSubDetailInfo());
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        title = (TextView) findViewById(R.id.title_text);
        listView = (ListView) findViewById(R.id.list_view);
    }
}
