package com.yhsh.flowstudy.draw;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.yhsh.flowstudy.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

public class DrawPaintActivity extends AppCompatActivity {

    private ImageView ivPaint;
    private final String TAG = "DrawPaintActivity";
    private Bitmap bitmapCopy;
    private Paint paint;
    private int paintWidth = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_paint);
        ivPaint = findViewById(R.id.iv_paint);
        Button btnSave = findViewById(R.id.btn_save);
        //创建画画板背景
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //禁止图片缩放处理
        opts.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.paint_bg, opts);
        Log.d(TAG, "bitmap宽高: " + bitmap.getHeight() + "," + bitmap.getWidth());
        //创建画画板模板
        bitmapCopy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        //创建画布
        Canvas canvas = new Canvas(bitmapCopy);
        //创建画笔
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(paintWidth);
        //开始作画,添加默认背景
        canvas.drawBitmap(bitmap, new Matrix(), paint);
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
                        //设置像素点，设置颜色和位置
                        bitmapCopy.setPixel(100, 100, Color.TRANSPARENT);
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
        btnSave.setOnLongClickListener(view -> {
            //清空画布
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            //清空后重新绘制背景
            canvas.drawBitmap(bitmap, new Matrix(), paint);
            ivPaint.setImageBitmap(bitmapCopy);
            return true;
        });
    }

    public void saveImage(View view) {
        //动态检查是否右读写SD卡权限
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // 拥有权限，执行相关操作
            saveImage29(this, bitmapCopy);
        } else {
            // 没有权限，请求权限
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public void paintColor(View view) {
        //自动生成随机颜色
        paint.setColor(Color.argb(255, (int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
    }

    public void paintWidth(View view) {
        paint.setStrokeWidth(paintWidth++);
    }

    public void paintWidthLess(View view) {
        if (paintWidth > 1) {
            paintWidth--;
            paint.setStrokeWidth(paintWidth);
        }
    }

    public void saveImage29(Context context, Bitmap toBitmap) {
        ContentValues contentValues = new ContentValues();
        //设置图片的标题
        contentValues.put(MediaStore.Images.Media.TITLE, "paint_" + System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.WIDTH, toBitmap.getWidth());
        contentValues.put(MediaStore.Images.Media.HEIGHT, toBitmap.getHeight());
        //计算压缩后的图片大小
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        toBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        int compressedSizeByte = stream.toByteArray().length;  // 转换为字节
        contentValues.put(MediaStore.Images.Media.SIZE, compressedSizeByte);
        contentValues.put(MediaStore.Images.Media.ORIENTATION, 0);
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        //开始一个新的进程执行保存图片的操作
        Uri insertUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        //使用use可以自动关闭流
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(insertUri, "rw");
            if (toBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)) {
                Log.e(TAG, "保存成功success");
            } else {
                Log.e(TAG, "保存失败fail");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 权限请求的回调方法
     *
     * @param requestCode  请求码
     * @param permissions  请求的权限列表
     * @param grantResults 权限授予结果列表
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，执行相关操作
                saveImage29(this, bitmapCopy);
            } else {
                // 权限被拒绝，向用户显示说明或处理逻辑
                Toast.makeText(this, "权限被拒绝，无法保存图片", Toast.LENGTH_SHORT).show();
            }
        }
    }
}