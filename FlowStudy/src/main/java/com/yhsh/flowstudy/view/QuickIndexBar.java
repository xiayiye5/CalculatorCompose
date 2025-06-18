package com.yhsh.flowstudy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 快速索引工具
 */
public class QuickIndexBar extends View {

    private Paint mPaint;
    private int layoutHeight;
    private int layoutWidth;
    //记录上次点击的字母
    int lastIndex = -1;
    private static final String TAG = "QuickIndexBar";
    //26个英文字母集合
    private final String[] alphabetData = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private float cellHeight;


    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public QuickIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(28);
        mPaint.setColor(Color.RED);
        //设置文字剧中绘制
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取当前View的宽高
        layoutHeight = getMeasuredHeight();
        layoutWidth = getMeasuredWidth();
    }

    /**
     * 绘制文字的方法
     *
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cellHeight = layoutHeight * 1.0f / alphabetData.length;
        float xPosition = layoutWidth * 1.0f / 2;
        for (int i = 0; i < alphabetData.length; i++) {
            //Y坐标的位置是26个格子每个格子的高度一半+文字高度的一半
            canvas.drawText(alphabetData[i], xPosition, cellHeight / 2 + getTextHeight(alphabetData[i]) / 2 + cellHeight * i, mPaint);
            //每个格子之间画一条线
            canvas.drawLine(0, cellHeight * (i + 1), layoutWidth, cellHeight * (i + 1), mPaint);
        }
    }

    private float getTextHeight(String text) {
        Rect mRect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        return mRect.height();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //按下和移动的时候都需要回调点击的字母，只需要判断当前的Y坐标除以每个格子的高度就是当前的字母索引
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int index = (int) (y / cellHeight);
                if (lastIndex != index) {
                    if (listener != null && index < alphabetData.length) {
                        listener.onLetterChange(alphabetData[index]);
                    }
                    Log.d(TAG, "当前选中的文字: " + alphabetData[index]);
                    lastIndex = index;
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起时候需要重置
                lastIndex = -1;
                break;
            default:
                break;
        }
        return true;
    }

    //添加字母的点击事件回调
    public interface OnLetterChangeListener {
        void onLetterChange(String letter);
    }

    OnLetterChangeListener listener;

    public void setOnLetterChangeListener(OnLetterChangeListener listener) {
        this.listener = listener;
    }
}
