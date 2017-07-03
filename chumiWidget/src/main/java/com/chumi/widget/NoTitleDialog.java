package com.chumi.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * 没有标题的对话框，可以塞入自定义视图
 * Created by CHUMI.Jim on 2016/12/5.
 */

public class NoTitleDialog extends Dialog {
    private int mLayoutId;

    //类似于自定义View，必须实现一个非默认的构造方法
    public NoTitleDialog(Context context, int mLayoutId) {
        super(context, R.style.noTitleNoBackgroundDialog);
        this.mLayoutId = mLayoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框显示哪个布局文件
        setContentView(mLayoutId);
    }
}
