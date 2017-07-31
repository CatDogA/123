package com.chumi.widget.http.cookie;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Tom on 2017/5/25.
 */

public class SimpleCookieJar implements CookieJar {
    private final List<Cookie> allCookies = new ArrayList<>();

    //保存
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        allCookies.addAll(cookies);

        Log.i("cookie", "saveFromResponse:   " + allCookies.size());

    }

    //取出
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> result = new ArrayList<>();
        if (allCookies.size() > 0) {
            for (Cookie cookie : allCookies) {
                if (cookie.matches(url)) {
                    result.add(cookie);
                }
            }
        }
        Log.i("cookie", "loadForRequest:  " + result.size());
        return result;
    }
}
