package com.yhsh.flowstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import com.yhsh.flowstudy.R;

import java.util.ArrayList;
import java.util.List;

public class LyricsView extends View {
    private static final String TAG = "LyricsView";

    // 歌词行列表（时间戳和歌词内容）
    private List<LyricLine> mLyricLines = new ArrayList<>();
    // 当前播放时间（毫秒）
    private long mCurrentTime;
    // 文本绘制相关
    private Paint mNormalPaint;    // 普通歌词画笔
    private Paint mHighlightPaint; // 高亮歌词画笔
    private int mLineHeight;       // 行高
    private int mCurrentLineIndex = -1; // 当前高亮行索引
    // 滚动控制
    private Scroller mScroller;
    private int mOffsetY;          // 垂直偏移量

    // 歌词行数据结构
    public static class LyricLine {
        long time;   // 时间戳（毫秒）
        String text; // 歌词内容

        public LyricLine(long time, String text) {
            this.time = time;
            this.text = text;
        }
    }

    public LyricsView(Context context) {
        this(context, null);
    }

    public LyricsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 初始化画笔
        mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 读取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LyricsView);
        int normalColor = ta.getColor(R.styleable.LyricsView_normalColor, Color.GRAY);
        int highlightColor = ta.getColor(R.styleable.LyricsView_highlightColor, Color.WHITE);
        float normalTextSize = ta.getDimension(R.styleable.LyricsView_normalTextSize, 36);
        float highlightTextSize = ta.getDimension(R.styleable.LyricsView_highlightTextSize, 48);
        ta.recycle();

        // 配置画笔
        mNormalPaint.setColor(normalColor);
        mNormalPaint.setTextSize(normalTextSize);
        mNormalPaint.setTextAlign(Paint.Align.CENTER);

        mHighlightPaint.setColor(highlightColor);
        mHighlightPaint.setTextSize(highlightTextSize);
        mHighlightPaint.setTextAlign(Paint.Align.CENTER);

        // 计算行高（以高亮行高度为准）
        Paint.FontMetrics fm = mHighlightPaint.getFontMetrics();
        mLineHeight = (int) (fm.descent - fm.ascent + 20); // 增加间距

        // 初始化滚动器
        mScroller = new Scroller(context);
    }

    // 设置歌词数据
    public void setLyrics(List<LyricLine> lines) {
        mLyricLines.clear();
        mLyricLines.addAll(lines);
        requestLayout();
        invalidate();
    }

    // 更新当前播放时间
    public void updateCurrentTime(long time) {
        mCurrentTime = time;
        updateHighlightLine();
        invalidate();
    }

    // 更新高亮行
    private void updateHighlightLine() {
        if (mLyricLines.isEmpty()) return;

        int newIndex = 0;
        // 查找当前时间对应的行
        for (int i = 0; i < mLyricLines.size(); i++) {
            if (mCurrentTime >= mLyricLines.get(i).time) {
                newIndex = i;
            } else {
                break;
            }
        }

        // 如果行发生变化，触发滚动
        if (newIndex != mCurrentLineIndex) {
            mCurrentLineIndex = newIndex;
            smoothScrollToLine(mCurrentLineIndex);
        }
    }

    // 平滑滚动到指定行
    private void smoothScrollToLine(int lineIndex) {
        if (lineIndex < 0 || lineIndex >= mLyricLines.size()) return;

        int targetY = calculateScrollYForLine(lineIndex);
        int currentY = mOffsetY;
        int deltaY = targetY - currentY;

        mScroller.startScroll(0, currentY, 0, deltaY, 500);
        invalidate();
    }

    // 计算指定行应该滚动到的Y坐标
    private int calculateScrollYForLine(int lineIndex) {
        // 让目标行位于视图中央
        int viewHeight = getHeight();
        int lineCenterY = lineIndex * mLineHeight + mLineHeight / 2;
        return lineCenterY - viewHeight / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = mLyricLines.size() * mLineHeight;
        setMeasuredDimension(width, height);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mOffsetY = mScroller.getCurrY();
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLyricLines.isEmpty()) return;

        int viewWidth = getWidth();
        int x = viewWidth / 2;
        int y = mLineHeight - mOffsetY; // 初始Y坐标

        // 绘制所有歌词行
        for (int i = 0; i < mLyricLines.size(); i++) {
            LyricLine line = mLyricLines.get(i);
            Paint paint = (i == mCurrentLineIndex) ? mHighlightPaint : mNormalPaint;
            canvas.drawText(line.text, x, y, paint);
            y += mLineHeight;
        }
    }
}

