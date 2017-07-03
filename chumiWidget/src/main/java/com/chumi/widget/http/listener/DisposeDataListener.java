package com.chumi.widget.http.listener;

/**
 * Created by Administrator on 2017/5/25.
 */

public interface DisposeDataListener {
    /**
     * 请求成功回调事件处理
     */
    public void onSuccess(Object responseObj);

    /**
     * 请求失败回调事件处理
     */
    public void onFailure(Object reasonObj);
}
