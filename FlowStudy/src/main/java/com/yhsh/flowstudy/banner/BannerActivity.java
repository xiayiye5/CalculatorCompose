package com.yhsh.flowstudy.banner;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.yhsh.flowstudy.R;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity {
    int[] imgList = new int[]{R.drawable.one, R.drawable.two, R.drawable.three};
    List<ImageView> listImageView = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        ViewPager vp = findViewById(R.id.view_pager);
        LinearLayout pointGroup = findViewById(R.id.point_group);
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
        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }
        });
    }
}