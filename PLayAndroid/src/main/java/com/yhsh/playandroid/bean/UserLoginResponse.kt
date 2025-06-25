package com.yhsh.playandroid.bean

data class UserLoginResponse(
    val data: Data?,
    val errorCode: Int,
    val errorMsg: String
)

data class Data(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val coinCount: Int,
    val collectIds: List<Any>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String, // &zwnj;**注意**&zwnj;：这里添加了 publicName 字段， String,
    val type: Int,
    val username: String
)
