package com.yhsh.flowstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.yhsh.flowstudy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义流式布局
 */
public class FlowLayout extends ViewGroup {
    private static final int DEFAULT_LINES = 3;
    private static final String TAG = "FlowLayout";
    private static final int DEFAULT_HORIZONTAL_MARGIN = 5;
    private static final int DEFAULT_VERTICAL_MARGIN = 5;
    private static final int DEFAULT_BORDER_RADIUS = 5;
    private static final int DEFAULT_TEXT_MAX_LENGTH = 20;
    //行数的集合
    private final List<List<View>> mLines = new ArrayList<>();
    //每行里面存放的对象数据
    private int maxLines;
    private float itemHorizontalMargin;
    private float itemVerticalMargin;
    private float textMaxLength;
    private float textColor;
    private float borderColor;
    private float borderRadius;
    private final List<String> mData = new ArrayList<>();

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public float getItemHorizontalMargin() {
        return itemHorizontalMargin;
    }

    public void setItemHorizontalMargin(float itemHorizontalMargin) {
        this.itemHorizontalMargin = itemHorizontalMargin;
    }

    public float getItemVerticalMargin() {
        return itemVerticalMargin;
    }

    public void setItemVerticalMargin(float itemVerticalMargin) {
        this.itemVerticalMargin = itemVerticalMargin;
    }

    public float getTextMaxLength() {
        return textMaxLength;
    }

    public void setTextMaxLength(float textMaxLength) {
        this.textMaxLength = textMaxLength;
    }

    public float getTextColor() {
        return textColor;
    }

    public void setTextColor(float textColor) {
        this.textColor = textColor;
    }

    public float getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(float borderColor) {
        this.borderColor = borderColor;
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        maxLines = type.getInt(R.styleable.FlowLayout_maxLines, DEFAULT_LINES);
        itemHorizontalMargin = type.getDimension(R.styleable.FlowLayout_itemHorizontalMargin, DEFAULT_HORIZONTAL_MARGIN);
        itemVerticalMargin = type.getDimension(R.styleable.FlowLayout_itemVerticalMargin, DEFAULT_VERTICAL_MARGIN);
        textMaxLength = type.getInt(R.styleable.FlowLayout_textMaxLength, DEFAULT_TEXT_MAX_LENGTH);
        textColor = type.getColor(R.styleable.FlowLayout_textColor, ContextCompat.getColor(getContext(), R.color.black));
        borderColor = type.getColor(R.styleable.FlowLayout_borderColor, ContextCompat.getColor(getContext(), R.color.black));
        borderRadius = type.getDimension(R.styleable.FlowLayout_borderRadius, DEFAULT_BORDER_RADIUS);
        Log.d(TAG, "maxLines:" + maxLines);
        Log.d(TAG, "itemHorizontalMargin:" + itemHorizontalMargin);
        Log.d(TAG, "itemVerticalMargin:" + itemVerticalMargin);
        Log.d(TAG, "textMaxLength:" + textMaxLength);
        Log.d(TAG, "textColor:" + textColor);
        Log.d(TAG, "borderColor:" + borderColor);
        Log.d(TAG, "borderRadius:" + borderRadius);
        //释放资源
        type.recycle();
    }

    //设置当前数据源
    public void setTextList(List<String> data) {
        this.mData.clear();
        this.mData.addAll(data);
        //创建子布局
        setUpChildren();
    }

    private void setUpChildren() {
        //设置数据之前先移除
        removeAllViews();
        for (String mDatum : mData) {
//            TextView view = new TextView(getContext());
            TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_text, this, false);
            view.setText(mDatum);
            //添加元素到父布局
            addView(view);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int parentSizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentSizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "mode:" + mode);
        Log.d(TAG, "parentSizeWidth:" + parentSizeWidth);
        Log.d(TAG, "EXACTLY:" + MeasureSpec.EXACTLY);
        Log.d(TAG, "AT_MOST:" + MeasureSpec.AT_MOST);
        Log.d(TAG, "UNSPECIFIED:" + MeasureSpec.UNSPECIFIED);
        //测量孩子
        int childCount = getChildCount();
        Log.d(TAG, "getChildCount:" + childCount);
        //未添加任何子控件直接不测了
        if (childCount <= 0) return;
        //清空集合
        mLines.clear();
        List<View> line = new ArrayList<>();
        //添加默认第一行
        mLines.add(line);
        //创建子布局的测量模式:可以无限大，但是不能超过父类的宽高
        int childrenWidthMeasureSpec = MeasureSpec.makeMeasureSpec(parentSizeWidth, MeasureSpec.AT_MOST);
        int childrenHeightMeasureSpec = MeasureSpec.makeMeasureSpec(parentSizeHeight, MeasureSpec.AT_MOST);
        //循环获取每个children并且测量
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.VISIBLE) {
                //child隐藏的话就不计算位置
                continue;
            }
            measureChild(child, childrenWidthMeasureSpec, childrenHeightMeasureSpec);
            //测量后可以拿到children宽高
            if (line.size() == 0) {
                line.add(child);
            } else {
                //判断是否可以添加
                boolean canBeAdd = checkChildrenCanBeAdd(line, child, parentSizeWidth);
                if (canBeAdd) {
                    //继续添加children
                    line.add(child);
                } else {
                    //TODO 未知原因，待研究
                    line = new ArrayList<>();
                    //末尾无法添加，换行后需要添加到line中
                    line.add(child);
                    //换行，添加一行
                    mLines.add(line);
                }
            }
            Log.d(TAG, "打印行高和每行个数：" + mLines.size() + "--" + line.size());
        }
        //根据所有尺寸计算行高,每个view的高度是一样的，获取第一个view的高度即可
        View child = getChildAt(0);
        int measuredChildHeight = child.getMeasuredHeight();
        Log.d(TAG, "measuredChildHeight:" + measuredChildHeight);
        //父布局的高度 = child高度 x 行高
        int parentMeasuredHeight = measuredChildHeight * mLines.size();
        //测量自己，设置父布局的宽度和高度
        setMeasuredDimension(parentSizeWidth, parentMeasuredHeight);
    }

    private boolean checkChildrenCanBeAdd(List<View> line, View child, int parentSizeWidth) {
        //判断当前行的宽度是否大于父布局宽度
        int childrenWidth = child.getMeasuredWidth();
        int totalWidth = 0;
        for (View view : line) {
            //计算当前行已添加View的宽度
            totalWidth += view.getMeasuredWidth();
        }
        //再加上需要添加的view的宽度来判断是否大于父布局的宽度
        totalWidth += childrenWidth;
        return totalWidth <= parentSizeWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() <= 0) {
            //未添加子item直接return
            return;
        }
        View firstChild = getChildAt(0);
        int currentLeft = 0;
        int currentTop = 0;
        int currentRight = 0;
        int currentBottom = firstChild.getMeasuredHeight();
        Log.d(TAG, "打印行数:" + mLines.size());
        for (List<View> mLine : mLines) {
            for (View view : mLine) {
                //每一行
                int childWidth = view.getMeasuredWidth();
//                int childHeight = view.getMeasuredHeight();
                currentRight += childWidth;
//                currentBottom = childHeight;
                view.layout(currentLeft, currentTop, currentRight, currentBottom);
                //从第二个开始left坐标需要变化
                currentLeft = currentRight;
            }
            //清零
            currentLeft = 0;
            currentRight = 0;
            currentBottom += firstChild.getMeasuredHeight();
            //从第二行开始top坐标需要改变,每增加一行需要累加之前的top
            currentTop += firstChild.getMeasuredHeight();
            Log.d(TAG, "currentTop:" + currentTop);
        }
    }
}
