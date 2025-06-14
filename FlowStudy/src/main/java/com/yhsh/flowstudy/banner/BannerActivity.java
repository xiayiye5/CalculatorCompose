package com.yhsh.flowstudy.banner;

import android.os.Bundle;
import android.widget.ImageView;

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
        for (int i = 0; i < imgList.length; i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(imgList[i]);
            listImageView.add(imageView);
        }
        vp.setAdapter(new BannerAdapter(listImageView));
    }
}