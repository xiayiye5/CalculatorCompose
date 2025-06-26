package com.yhsh.playandroid.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue

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

    /**
     * dp转px（支持正/负值）
     * @param dp 要转换的dp值
     * @param context 上下文用于获取屏幕密度
     */
    fun dp2px(dp: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, metrics
        )
    }

    fun dp2pxInt(dp: Float): Int {
        val metrics = context.resources.displayMetrics
        return (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, metrics
        ) + 0.5f).toInt()
    }

    /**
     * px转dp（支持正/负值）
     * @param px 要转换的像素值
     * @param context 上下文用于获取屏幕密度
     */
    fun px2dp(px: Float): Float {
        val density = context.resources.displayMetrics.density
        return px / density
    }

    fun px2dpInt(px: Float): Int {
        val density = context.resources.displayMetrics.density
        return (px / density + 0.5f).toInt()
    }

    /**
     * sp转px（用于字体大小）
     * @param sp 要转换的sp值
     * @param context 上下文用于获取缩放密度
     */
    fun sp2px(sp: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, sp, metrics
        )
    }

    /**
     * px转sp（用于字体大小）
     * @param px 要转换的像素值
     * @param context 上下文用于获取缩放密度
     */
    fun px2sp(px: Float): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return px / scaledDensity
    }
}