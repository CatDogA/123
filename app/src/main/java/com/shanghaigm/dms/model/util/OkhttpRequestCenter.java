package com.shanghaigm.dms.model.util;

import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.chumi.widget.http.okhttp.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.chumi.widget.http.okhttp.RequestParams;

import java.util.Map;

/**
 * Created by Tom on 2017/7/11.
 * 存所有请求
 */

public class OkhttpRequestCenter {
    //全string
    public static void getRequest(String url, RequestParams params, DisposeDataListener listener) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener));
    }

    //有int
    public static void getRequest(String url, Map<String, Object> params, DisposeDataListener listener) {
        CommonOkHttpClient.get(CommonRequest.createGetRequestInt(url, params), new DisposeDataHandle(listener));
    }

    //日报查询列表
    public static void ReportQuery(String url, Map<String, Object> params, DisposeDataListener listener) {
        getRequest(url, params, listener);
    }
    public static void ReportDetailQuery(String url, Map<String, Object> params, DisposeDataListener listener){
        getRequest(url, params, listener);
    }
}
