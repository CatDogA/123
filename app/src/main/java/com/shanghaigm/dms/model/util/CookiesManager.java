package com.shanghaigm.dms.model.util;

import com.chumi.widget.http.cookie.PersistentCookieStore;
import com.shanghaigm.dms.DmsApplication;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/7/31.
 */

public class CookiesManager implements CookieJar {
    DmsApplication app = DmsApplication.getInstance();
    private final PersistentCookieStore cookieStore = new PersistentCookieStore(app.getApplicationContext());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }
}
