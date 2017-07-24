package com.shanghaigm.dms.model.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.shanghaigm.dms.model.util.HTTP.mapToParam;

///**
// * Created by Tom on 2017/7/20.
// */
//
public class HttpDownLoad {
    private Map<String, String> params;
    private String url;
    private String filePath;
    private static String TAG = "HttpDownLoad";

    public HttpDownLoad(Map<String, String> params, String url, String filePath) {
        this.params = params;
        this.url = url;
        this.filePath = filePath;
    }

    public String downloadFile() throws IOException {
//        String uploadUrl = "http://192.168.2.111:8080/HsbServlet/DownloadFile";
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(path.getPath() + "/report_cp");
        if(!file.exists()){
            file.mkdir();
        }
        File picFile = new File(file,filePath);
        Log.i(TAG, "downloadFile: " + file.getPath()+"          "+picFile.getPath());
        OutputStream os = new BufferedOutputStream(new FileOutputStream(picFile));
        String uploadUrl = url + mapToParam(params);
        final URL url = new URL(uploadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream is = new BufferedInputStream(connection.getInputStream());
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);// 必须使用write(buffer, 0, len){Writes
        }
        os.flush();
        os.close();
        is.close();
        connection.disconnect();
        return file.getPath();
    }
//    private static String TAG = "HttpDownLoad";
//    /**
//     * 连接url
//     */
//    private String urlstr;
//    /**
//     * sd卡目录路径
//     */
//    private String sdcard;
//    /**
//     * http连接管理类
//     */
//    private HttpURLConnection urlcon;
//    //参数
//    private Map<String, String> params = new HashMap<>();
//
//    public HttpDownLoad(String url, Map<String, String> params) {
//        this.urlstr = url;
//        this.params = params;
//        //获取设备sd卡目录
//        this.sdcard = Environment.getExternalStorageDirectory() + "/";
//        urlcon = getConnection();
//    }
//
//    /*
//     * 读取网络文本
//     */
//    public String downloadAsString() {
//        StringBuilder sb = new StringBuilder();
//        String temp = null;
//        try {
//            InputStream is = urlcon.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            while ((temp = br.readLine()) != null) {
//                sb.append(temp);
//            }
//            br.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sb.toString();
//    }
//
//    /*
//     * 获取http连接处理类HttpURLConnection
//     */
//    private HttpURLConnection getConnection() {
//        URL url;
//        HttpURLConnection urlcon = null;
//        try {
//            //获取URL
//            url = new URL(urlstr + mapToParam(params));
//            urlcon = (HttpURLConnection) url.openConnection();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return urlcon;
//    }
//
//    /*
//     * 获取连接文件长度。
//     */
//    public int getLength() {
//        return urlcon.getContentLength();
//    }
//
//    /*
//     * 写文件到sd卡 demo
//     * 前提需要设置模拟器sd卡容量，否则会引发EACCES异常
//     * 先创建文件夹，在创建文件
//     */
//    public int down2sd(String dir, String filename, downhandler handler) {
//        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        StringBuilder sb = new StringBuilder(path.toString())
//                .append("/" + dir);         //文件夹路径
//        File file = new File(sb.toString());
//        Log.i(TAG, sb.toString());
//
////        file = new File(sb.toString());
//
//        FileOutputStream fos = null;
//        try {
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            InputStream is = urlcon.getInputStream();
////            //创建文件
//////            file.createNewFile();/
//            fos = new FileOutputStream(file);
//            byte[] buf = new byte[1024];
//            while ((is.read(buf)) != -1) {
//                fos.write(buf);
//                //同步更新数据
//                handler.setSize(buf.length);
//            }
//            is.close();
////        } catch (Exception e) {
////            return 0;
////        } finally {
////            try {
////                fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        }
//        return 1;
//    }
//
//    /*
//     * 内部回调接口类
//     */
//    public abstract static class downhandler {
//        public abstract void setSize(int size);
//    }
}
