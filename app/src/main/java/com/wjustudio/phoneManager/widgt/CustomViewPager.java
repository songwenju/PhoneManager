package com.wjustudio.phoneManager.widgt;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：songwenju on 2016/3/13 10:21
 * 邮箱：songwenju01@163.com
 */
public class CustomViewPager extends ViewPager {
    private boolean isCanScroll = true;
    public CustomViewPager(Context context) {
        this(context,null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }
    public void setScanScroll(boolean isCanScroll){
        this.isCanScroll = isCanScroll;
    }
}
