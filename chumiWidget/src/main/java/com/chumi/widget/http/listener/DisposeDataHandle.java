package com.chumi.widget.http.listener;

/**
 * Created by Administrator on 2017/5/25.
 */

public class DisposeDataHandle {
    public DisposeDataListener mListener = null;
    public Class<?> mClass = null;
    public String mSource = null;

    public DisposeDataHandle(DisposeDataListener listener)
    {
        this.mListener = listener;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz)
    {
        this.mListener = listener;
        this.mClass = clazz;
    }

    public DisposeDataHandle(DisposeDataListener listener, String source)
    {
        this.mListener = listener;
        this.mSource = source;
    }
}
