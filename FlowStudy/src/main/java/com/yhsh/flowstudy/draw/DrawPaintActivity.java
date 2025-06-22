package com.yhsh.flowstudy.draw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.yhsh.flowstudy.R;

public class DrawPaintActivity extends AppCompatActivity {

    private ImageView ivPaint;
    private final String TAG = "DrawPaintActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_paint);
        ivPaint = findViewById(R.id.iv_paint);
        //创建画画板背景
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //禁止图片缩放处理
        opts.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.paint_bg, opts);
        Log.d(TAG, "bitmap宽高: " + bitmap.getHeight() + "," + bitmap.getWidth());
        //创建画画板模板
        Bitmap bitmapCopy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        //创建画布
        Canvas canvas = new Canvas(bitmapCopy);
        //创建画笔
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        //开始作画
        canvas.drawBitmap(bitmapCopy, new Matrix(), paint);
//        canvas.drawLine(100, 100, 200, 200, paint);
        ivPaint.setImageBitmap(bitmapCopy);
        //设置画布的触摸事件
        ivPaint.setOnTouchListener(new View.OnTouchListener() {

            private float startY;
            private float startX;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        startY = motionEvent.getY();
                        Log.d(TAG, "ACTION_DOWN: " + startX + "," + startY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float endX = motionEvent.getX();
                        float endY = motionEvent.getY();
                        Log.d(TAG, "ACTION_MOVE: " + endX + "," + endY);
                        canvas.drawLine(startX, startY, endX, endY, paint);
                        startX = endX;
                        startY = endY;
                        //最后显示到图片上面
                        ivPaint.setImageBitmap(bitmapCopy);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void saveImage(View view) {
    }

    public void paintColor(View view) {
    }

    public void paintWidth(View view) {
    }
}