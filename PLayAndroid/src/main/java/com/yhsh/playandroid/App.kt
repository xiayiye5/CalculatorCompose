package com.yhsh.playandroid

import android.app.Application
import android.util.Log
import com.tencent.mmkv.MMKV
import com.yhsh.playandroid.util.AppUtils

class App : Application() {
    companion object {
        const val TAG = "App"
    }

    override fun onCreate() {
        super.onCreate()
        val rootDir = MMKV.initialize(this)
        //添加mmkv初始化
        Log.d(TAG, "初始化成功:$rootDir")
        AppUtils.setContext(this)
    }
}