package com.yhsh.playandroid.util

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object AppUtils {
    private lateinit var context: Context
    fun context(): Context {
        return context
    }

    @JvmStatic
    fun setContext(mContext: Context) {
        context = mContext
    }
}