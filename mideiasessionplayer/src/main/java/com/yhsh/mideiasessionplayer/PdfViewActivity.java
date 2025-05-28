package com.yhsh.mideiasessionplayer;

import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfViewActivity extends AppCompatActivity {
    private static final String TAG = "PdfViewActivity";
    private PdfRenderer pdfRenderer;
    private int currentPage = 0;
    private int pageCount;
    private TextView tvCurrentPage;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        tvCurrentPage = findViewById(R.id.tv_current_page);
        vp = findViewById(R.id.vp);
//            File file = new File(getCacheDir(), "test.pdf");
        try {
            InputStream in = getAssets().open("one.pdf");
            File file = new File(getCacheDir(), "one.pdf");
            inputStreamToFile(in, file);
            ParcelFileDescriptor fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(fd);
            pageCount = pdfRenderer.getPageCount();
            vp.setAdapter(new PdfAdapter(getApplicationContext(), pageCount, pdfRenderer));
            vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    Log.d(TAG, "position:" + position);
                    currentPage = position;
                    tvCurrentPage.setText("第" + (currentPage + 1) + "页");
                }
            });
//            renderPage(currentPage);
            tvCurrentPage.setText("第1页");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void inputStreamToFile(InputStream inputStream, File file) throws IOException {
        // 建立输出流，写入目标文件
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096]; // 可以调整缓冲区大小
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.flush();
        } finally {
            // 关闭输入流
            inputStream.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
    }

    public void prePage(View view) {
        if (currentPage <= 0) {
            Toast.makeText(getApplicationContext(), "已经是首页了", Toast.LENGTH_LONG).show();
            return;
        }
        int page = --currentPage;
        tvCurrentPage.setText("第" + (page + 1) + "页");
        vp.setCurrentItem(page, true);
    }

    public void nextPage(View view) {
        if (currentPage >= pageCount) {
            Toast.makeText(getApplicationContext(), "已经是最后一页了", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d(TAG, "currentPage:" + currentPage);
        int page = ++currentPage;
        Log.d(TAG, "page:" + page);
        tvCurrentPage.setText("第" + (page + 1) + "页");
        vp.setCurrentItem(page, true);
    }
}
