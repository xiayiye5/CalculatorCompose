package com.yhsh.flowstudy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SlideMenu extends ViewGroup {
    private List<ViewGroup> viewGroups;
    private float startX;
    private static final String TAG = "SlideMenu";

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int leftViewWidth = getChildAt(0).getMeasuredWidth();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取手指在当前View的X坐标
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动过程中，根据手指移动的距离滑动第一个子View
                float moveX = event.getX();
                scrollBy((int) (startX - moveX), 0);
                //判断左右两边超出边界的情况
                int currentScrollX = getScrollX();
                Log.d(TAG, "currentScrollX = " + currentScrollX + ", leftViewWidth = " + leftViewWidth);
                if (currentScrollX < -leftViewWidth) {
                    //只能滑动到最左边后不能继续滑动
                    scrollTo(-leftViewWidth, 0);
                } else if (currentScrollX > 0) {
                    //不能超出右边界
                    scrollTo(0, 0);
                } else {
                    //滑动过程中，更新手指的X坐标
                    startX = moveX;
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起手指时，判断滑动距离是否超过一半
                int scrollX = getScrollX();
                if (Math.abs(scrollX) > leftViewWidth / 2) {
                    //滑动距离超过一半，则滑动到最左边或最右边
                    scrollTo(-leftViewWidth, 0);
                } else {
                    //否则滑动回原来的位置
                    scrollTo(0, 0);
                }
                break;
        }
        return true;
    }
}
