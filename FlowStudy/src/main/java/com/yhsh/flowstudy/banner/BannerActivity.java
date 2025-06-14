package com.yhsh.flowstudy.banner;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.yhsh.flowstudy.R;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity {
    private static final String TAG = "BannerActivity";
    int[] imgList = new int[]{R.drawable.one, R.drawable.two, R.drawable.three};
    List<ImageView> listImageView = new ArrayList<>();
    private int pointLeftMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        ViewPager vp = findViewById(R.id.view_pager);
        LinearLayout pointGroup = findViewById(R.id.point_group);
        ImageView pointSelect = findViewById(R.id.point_select);
        for (int i = 0; i < imgList.length; i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(imgList[i]);
            listImageView.add(imageView);
            //添加3个点
            ImageView point = new ImageView(getApplicationContext());
            point.setImageResource(R.drawable.shpe_point_normal);
            //从第二个小店开始设置小点之间的间距
            if (i > 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 10;
                point.setLayoutParams(params);
            }
            pointGroup.addView(point);
        }
        vp.setAdapter(new BannerAdapter(listImageView));
        pointSelect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //回调此方法后布局才绘制完成，此时方可拿到当前页面的大小、间距等信息
                //获取滑动一页小圆点需要滑动的距离，第二个小圆点的左边距减去第一个小圆点的左边距
                if (pointGroup.getChildCount() > 1) {
                    pointLeftMargin = pointGroup.getChildAt(1).getLeft() - pointGroup.getChildAt(0).getLeft();
                }
                //会执行多次回调需要第一次收到后进行移除
                pointSelect.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled position:" + position + "滑动的百分比:" + positionOffset);
                //滑动百分比乘以滑动距离
                int margin = (int) ((positionOffset + position) * pointLeftMargin);
                Log.d(TAG, "onPageScrolled margin:" + margin);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pointSelect.getLayoutParams();
                //根据当前滑动百分比设置小圆点的位置
                layoutParams.leftMargin = margin;
                pointSelect.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {

            }
        });
    }
}