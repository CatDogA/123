package com.shanghaigm.dms.model.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/14.
 */

public class HTTP {
    private static Logger log = Logger.getLogger(HTTP.class);

    /**
     * 向指定URL发送GET方法的请求
     * <p>
     * //     * @param url请求地址
     *
     * @return
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url      发送请求的URL
     * @param mapParam 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String get(String url, Map<String, String> mapParam) {
        StringBuilder result = null;
        BufferedReader in = null;
        try {
            String urlNameString = url + mapToParam(mapParam);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 建立实际的连接
            connection.connect();
            log.debug("调试-访问地址:" + realUrl.toString());

            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                log.debug("调试-响应头:" + key + "=" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            result = new StringBuilder();
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("错误-发送GET请求失败:" + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }
        }
        log.debug("调试-GET请求返回:");
        log.debug(result.toString());
        return result.toString();
    }

    /**
     * 发送POST方法的请求
     *
     * @param url  发送请求的 URL
     * @param data POST的数据
     * @return
     */
    public static String post(String url, String data) {
        return post(url, null, data);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url      发送请求的 URL
     * @param mapParam GET请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param data     POST的数据
     * @return
     */
    public static String post(String url, Map<String, String> mapParam, String data) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = null;
        try {
            String urlNameString = url + mapToParam(mapParam);
            URL realUrl = new URL(urlNameString);
            log.debug("调试-访问地址:" + realUrl.toString());
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Length", Integer.toString(data.length()));

            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            log.debug("调试-POST请求参数:" + data);
            out.print(data);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            result = new StringBuilder();
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("错误-发送POST请求失败:" + e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        log.debug("调试-POST请求返回:");
        log.debug(result.toString());
        return result.toString();
    }

    /**
     * 将map转换为GET请求参数
     *
     * @param map
     * @return
     */
    public static String mapToParam(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        // 减去最后一个"&"
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 得到文件流,下载文件
     *
     * @param url 下载文件地址
     * @return
     */
    public static InputStream getInputStream(String url) {
        return getInputStream(url, null);
    }

    /**
     * 得到文件流,下载文件
     *
     * @param url      下载文件地址
     * @param mapParam 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return
     */
    public static InputStream getInputStream(String url, Map<String, String> mapParam) {
        BufferedReader in = null;
        InputStream inputStream = null;
        try {
            String urlNameString = url + mapToParam(mapParam);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            log.debug("调试-访问地址:" + realUrl.toString());

            // 建立实际的连接
            connection.connect();

            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                log.debug("调试-响应头:" + key + "=" + map.get(key));
            }

            inputStream = connection.getInputStream();

        } catch (Exception e) {
            log.error("错误-发送GET请求失败:" + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return inputStream;
    }

    /**
     * @param url  上传文件地址
     * @param file 请求参数应该是 name1=value1&name2=value2 的形式。
     * @return
     */
    public static String postFile(String url, File file) {
        try {
            return postFile(url, null, file.getName(), new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error("错误-上传文件失败:" + e);
        }
        return null;
    }

    /**
     * @param url      上传文件地址
     * @param mapParam 请求参数应该是 name1=value1&name2=value2 的形式。
     * @param file     文件
     * @return
     */
    public static String postFile(String url, Map<String, String> mapParam, File file) {
        try {
            return postFile(url, mapParam, file.getName(), new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error("错误-上传文件失败:" + e);
        }
        return null;
    }

    public static String postFile(String url, Map<String, String> mapParam, String fileName, InputStream inputStream) {
        DataOutputStream out = null;
        BufferedReader in = null;
        StringBuilder result = null;
        String boundary = "----" + UUID.randomUUID().toString();

        try {
            String urlNameString = url + mapToParam(mapParam);
            URL realUrl = new URL(urlNameString);
            System.out.println(realUrl.toString());
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            byte[] data = new byte[1024];

            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestProperty("Accept-Language", "en-US,en,zh,zh-CN");
            connection.setRequestProperty("Accept-Charset", "ISO-8859-1,*,utf-8");
            connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            connection.setRequestProperty("User-Agent", "Nutz.Robot");
            connection.setRequestProperty("Accept",
                    "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Length", Integer.toString(inputStream.available()));
            // 发送POST请求必须设置如下两行

            // 获取URLConnection对象对应的输出流
            out = new DataOutputStream(connection.getOutputStream());

            // 发送请求参数
            out.writeBytes("--" + boundary + "\r\n");
            out.writeBytes("Content-Disposition:    form-data;    name=\"myFile\";    filename=\"" + fileName + "\"\r\n");
            out.writeBytes("Content-Type:   application/octet-stream\r\n\r\n");

            while (true) {
                int amountRead = inputStream.read(data);
                //System.out.println(amountRead);
                if (amountRead == -1) {
                    break;
                }
                out.write(data, 0, amountRead);
            }
            out.writeBytes("\r\n");
            out.writeBytes("--" + boundary + "--" + "\r\n");

            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            result = new StringBuilder();
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("错误-发送POST请求上传文件失败:" + e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
}
