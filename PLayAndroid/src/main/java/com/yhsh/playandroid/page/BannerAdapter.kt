package com.yhsh.playandroid.page

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.yhsh.playandroid.bean.Banner

class BannerAdapter : PagerAdapter() {
    private var imgList: List<ImageView> = mutableListOf()
    private var urlList: List<Banner> = mutableListOf()
    override fun getCount(): Int {
        return imgList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
        container.removeView(`object` as View?)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val img = imgList[position]
        Glide.with(img.context).load(urlList[position].imagePath).into(img)
        container.addView(img)
        return img
    }

    fun refreshBanner(imgList: List<ImageView>, urlList: List<Banner>) {
        this.imgList = imgList
        this.urlList = urlList
        Log.d("", "长度${imgList.size}-${urlList.size}")
        notifyDataSetChanged()
    }
}
