package com.yhsh.mideiasessionplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


public class PdfAdapter extends PagerAdapter {

    private final Context context;
    private final int count;
    private final PdfRenderer pdfRenderer;

    public PdfAdapter(Context context, int count, PdfRenderer pdfRenderer) {
        this.context = context;
        this.count = count;
        this.pdfRenderer = pdfRenderer;
    }

    @Override
    public int getCount() {
        return count;  // 图片总数
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

        // 设置 ImageView 的 LayoutParams (根据你的需要调整宽高)
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(layoutParams);
        renderPage(imageView, position);

        // 将 ImageView 添加到 ViewPager 容器
        container.addView(imageView);

        return imageView; // 返回的是 View
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 从容器中移除 View
        container.removeView((ImageView) object);  // object 就是 instantiateItem 返回的 View
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object; // View 和 Object 是否是同一个对象
    }

    private void renderPage(ImageView pdfImageView, int index) {
        PdfRenderer.Page page = pdfRenderer.openPage(index);
        Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        pdfImageView.setImageBitmap(bitmap);
        page.close();
    }
}
