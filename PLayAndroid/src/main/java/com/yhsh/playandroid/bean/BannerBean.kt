package com.yhsh.playandroid.bean

data class BannerBean(
    val `data`: List<Banner>?,
    val errorCode: Int,
    val errorMsg: String
)

data class Banner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)
