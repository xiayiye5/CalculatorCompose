package com.yhsh.playandroid.bean

data class ResponseBaseBean<T> constructor(val `data`: T?, val errorCode: Int, val errorMsg: String)
