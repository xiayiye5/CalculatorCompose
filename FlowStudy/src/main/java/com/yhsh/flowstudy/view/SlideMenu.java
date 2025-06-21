package com.yhsh.flowstudy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SlideMenu extends ViewGroup {
    private List<ViewGroup> viewGroups;

    public SlideMenu(Context context) {
        this(context, null);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {

    }

    public void setMenuView(List<ViewGroup> viewGroups) {
        this.viewGroups = viewGroups;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //开始测量子View
        int childCount = getChildCount();
        if (childCount > 2) {
            throw new RuntimeException("onMeasure测量过程异常：SlideMenu最多只能有两个子View");
        }
        for (int i = 0; i < childCount; i++) {
            if (i == 0) {
                //测量第一个子View的宽度，高度为父容器的高度
                View childFirst = getChildAt(0);
                measureChild(getChildAt(i), childFirst.getMeasuredWidth(), heightMeasureSpec);
            }
            //测量其它子View的宽度和高度
            View childOther = getChildAt(i);
            measureChild(childOther, widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount > 2) {
            throw new RuntimeException("onLayout摆放过程异常：SlideMenu最多只能有两个子View");
        }
        for (int j = 0; j < childCount; j++) {
            if (j == 0) {
                //第一个子View布局在最左边
                View childFirst = getChildAt(0);
                int measuredWidth = childFirst.getMeasuredWidth();
                childFirst.layout(-measuredWidth, 0, 0, b);
            } else {
                //其它子View布局占满整个屏幕
                View childOther = getChildAt(j);
                childOther.layout(0, 0, childOther.getMeasuredWidth(), b);
            }
        }
    }
}
