package com.yhsh.playandroid.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 创建可以缓存的banner对象
 */
@Parcelize
data class BannerList(val bannerBean: List<BannerBean>) : Parcelable
