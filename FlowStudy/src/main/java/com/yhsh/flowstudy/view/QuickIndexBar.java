package com.yhsh.flowstudy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 快速索引工具
 */
public class QuickIndexBar extends View {

    private Paint mPaint;
    private int layoutHeight;
    private int layoutWidth;
    private static final String TAG = "QuickIndexBar";
    //26个英文字母集合
    private final String[] alphabetData = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


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
        float cellHeight = layoutHeight * 1.0f / alphabetData.length;
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
}
