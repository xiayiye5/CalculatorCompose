package com.yhsh.playandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class HomeViewPager extends ViewPager {
    public HomeViewPager(@NonNull Context context) {
        super(context);
    }

    public HomeViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        //下面不能直接返回true,否则会导致ViewPager里面的子View拿不到点击事件
        return super.onInterceptTouchEvent(ev);
    }
}
