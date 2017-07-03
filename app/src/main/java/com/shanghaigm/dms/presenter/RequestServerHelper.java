package com.shanghaigm.dms.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.shanghaigm.dms.DmsApplication;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 2017/5/24.
 */

public class RequestServerHelper {
    private static final String TAG = "RequestServerHelper";

    private static Map<String, String> header;

    /**
     * 获取请求头
     * @return 请求头
     */
    public static Map<String, String> getRequestHeader(){
        DmsApplication app = DmsApplication.getInstance();
        if(header == null){
            header = new HashMap<>();
        }
//        header.put("access_token", app.getAccessToken());
//        header.put("buId", app.getUserName());

        header.put("loginName", app.getAccount());
        header.put(" jobCode", app.getJobCode());
        header.put("password",app.getPassWord());
//        header.put("storeCode", app.getStoreCode());
//        header.put("sign", app.getSignCode());

        return header;
    }

    /**
     * 检测网络是否可用
     * @return true可用，false不可用
     */
    static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
            Toast.makeText(context, "请连接网络", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * 处理请求服务器成功的信息
     *
     * @param context 上下文
     * @param responseJson 服务器返回的json值
     * @return 处理后的json
     */
    public static  String requestServerSuccessHandle(Context context, String responseJson){
        String resultJson = parseResponseJson(context, responseJson);
        return resultJson;
    }

    /**
     *
     * 处理请求DOSS服务器成功的信息
     * @param context 上下文
     * @param responseJson 服务器返回的json值
     * @return 处理后的json
     */
    public static String requestServerSuccessDossHandle(Context context, String responseJson){
        String resultJson = parseResponseJsonDoss(context, responseJson);
        return resultJson;
    }

    /**
     *
     * 处理请求雅邦驱动服务器成功的信息
     * @param context 上下文
     * @param responseJson 服务器返回的json值
     * @return 处理后的json
     */
    public static String requestServerSuccessYbqdHandle(Context context, String responseJson){
        String resultJson = parseResponseJsonYbqd(context, responseJson);
        return resultJson;
    }

    /**
     * 处理请求服务器失败的信息
     *
     * @param context 上下文
     * @param statusCode 错误代码
     * @param message 错误信息
     * @param url 请求的url
     */
    public static void requestServerFailHandle(Context context, int statusCode, String message, String url){

        if(statusCode == RequestServer.FAIL_CODE_UNAUTHORIZED){
        //TODO
            // 注销
//            CommonUtils.logOut(context);

            Toast.makeText(context, "账号已过期，请重新登录", Toast.LENGTH_SHORT).show();
        } else if(statusCode == RequestServer.FAIL_CODE_FORBIDDEN) {
            // 注销
//            CommonUtils.logOut(context);
            Toast.makeText(context,"服务器拒绝请求，请重新登录", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "服务器连接失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析服务器返回的json
     *
     * 成功
     * {
     * 	"returnCode": "1",
     * 	"returnMessage": "SUCCESS",
     * 	"result": "结果"
     * }
     * 失败
     * {
     * 	"returnCode": "-1",
     * 	"returnMessage": "错误类型",
     * 	"result": "错误信息"
     * }
     * @param context 上下文
     * @param responseJson 服务器返回的json
     * @return 处理后的json
     */
    private static String parseResponseJson(Context context, String responseJson) {
        String resultJson = null;
        try {
            JSONObject jsonObject = new JSONObject(responseJson);
            int returnCode = jsonObject.getInt("returnCode");
            if (returnCode == 1) {
                // 服务器返回 1，获取数据成功
                resultJson = jsonObject.getString("result");
                if("null".equals(resultJson.toLowerCase())){ //{"returnCode":"1","returnMessage":"success","result":null}
                    resultJson = "";
                }
                Log.d(TAG, "result length :" + resultJson.length());
            } else {
                // 服务器返回-1，数据获取错误
                String returnMessage = jsonObject.getString("returnMessage");
                Toast.makeText(context, returnMessage, Toast.LENGTH_SHORT).show();
//				Toast.makeText(context, context.getString(R.string.response_data_error),
//						Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(context, "数据解析错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return resultJson;
    }

    /**
     *
     * 解析DOSS服务器返回的json
     *
     * 成功
     * {
     *  "data":[{},{}],
     *  "errorMsg":"",
     *  "totalPages":"",
     *  "resultCount":"",
     *  "isSuccess":100
     * }
     *
     * 失败
     * {
     *  "data":null,
     *  "errorMsg":" parameter brandId is error ",
     *  "isSuccess":60001}
     *
     * @param context 上下文
     * @param responseJson 服务器返回的json
     * @return data 的json
     */
    private static String parseResponseJsonDoss(Context context, String responseJson) {

        String resultJson = null;
        try {
            JSONObject jsonObject = new JSONObject(responseJson);
            int returnCode = jsonObject.getInt("isSuccess");
            if (returnCode == 100) {
                // 服务器返回 1，获取数据成功
                resultJson = jsonObject.getString("data");
                if("null".equals(resultJson.toLowerCase())){ //{"isSuccess":"100","errorMsg":"","baseData":null}
                    resultJson = "";
                }
                Log.d(TAG, "result length :" + resultJson.length());
            } else {
                // 服务器返回-1，数据获取错误
                String returnMessage = jsonObject.getString("errorMsg");
//				Toast.makeText(context, returnMessage, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "第三方系统错误",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(context,"第三方系统错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return resultJson;
    }

    /**
     *
     * 解析雅邦驱动服务器返回的json
     *
     * 成功
     * {"ret":200,"msg":"ok","data":[]}
     *
     * 失败
     * {"ret":10001,"msg":"ok","data":[]}
     *
     * @param context 上下文
     * @param responseJson 服务器返回的json
     * @return data 的json
     */
    private static String parseResponseJsonYbqd(Context context, String responseJson) {

        String resultJson = null;
        try {
            JSONObject jsonObject = new JSONObject(responseJson);
            int returnCode = jsonObject.getInt("ret");
            if (returnCode == 200) {
                // 服务器返回 200，获取数据成功
                resultJson = jsonObject.getString("data");
                if("null".equals(resultJson.toLowerCase())){
                    resultJson = "";
                }
                Log.d(TAG, "result length :" + resultJson.length());
            } else {
                // 服务器返回 其他，数据获取错误
                String returnMessage = jsonObject.getString("msg");
                Toast.makeText(context,"第三方系统错误",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(context, "第三方系统错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return resultJson;
    }
}
