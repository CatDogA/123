package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.shanghaigm.dms.R;

/**
 * Created by Administrator on 2017/8/16.
 */

public class ContractReviewButton extends RelativeLayout {
    public ContractReviewButton(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.button_contract_review, this, true);
    }
}