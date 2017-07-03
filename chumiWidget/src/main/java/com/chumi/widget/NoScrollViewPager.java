package com.chumi.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能滑动的ViewPager
 *
 * Created by CHUMI on 2016/6/14.
 */
public class NoScrollViewPager extends ViewPager {

	private boolean noScroll = false;

	public NoScrollViewPager(Context context) {
		super(context);
	}

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置是否可以左右滑动
	 *
	 * @param noScroll
	 * true 不能滑动
	 * false 能滑动
	 */
	public void setNoScroll(boolean noScroll) {
		this.noScroll = noScroll;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return !noScroll && super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return !noScroll && super.onInterceptTouchEvent(ev);
	}
}
