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

    //大多数的get请求
    public static void getCommonRequest(String url, Map<String, Object> params, DisposeDataListener listener) {
        getRequest(url, params, listener);
    }

    //有int
    public static void getReportRequest(String url, Map<String, Object> params, DisposeDataListener listener) {
        CommonOkHttpClient.get(CommonRequest.createGetRequestInt(url, params), new DisposeDataHandle(listener));
    }

    public static void getCommonReportRequest(String url, Map<String, Object> params, DisposeDataListener listener) {
        getReportRequest(url, params, listener);
    }
}
