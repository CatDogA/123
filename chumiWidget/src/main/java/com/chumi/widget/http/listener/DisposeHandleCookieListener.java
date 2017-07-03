package com.chumi.widget.http.listener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/25.
 */

public interface DisposeHandleCookieListener extends DisposeDataListener{
    public void onCookie(ArrayList<String> cookieStrLists);
}
