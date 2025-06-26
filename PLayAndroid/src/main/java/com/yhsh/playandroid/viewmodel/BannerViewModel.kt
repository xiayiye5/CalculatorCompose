package com.yhsh.playandroid.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.yhsh.playandroid.bean.BannerBean
import com.yhsh.playandroid.net.API
import com.yhsh.playandroid.view.HomeViewPager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BannerViewModel : BaseViewModel() {
    private val TAG = "BannerViewModel"
    private val bannerState = MutableStateFlow<List<BannerBean>?>(null)
    val _bannerState = bannerState.asStateFlow()
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val viewPager = msg.obj as HomeViewPager
            val index = (viewPager.currentItem + 1) % 3
            Log.d(TAG, "当前轮播图位置:$index")
            viewPager.currentItem = index
            sendMessageDelayed(Message.obtain().apply {
                obj = msg.obj
            }, 3000)
        }
    }

    fun banner() {
        viewModelScope.launch {
            API.banner().catch {
                Log.d(TAG, "首页轮播图请求异常$it")
            }.collect {
                bannerState.value = it
                Log.d(TAG, "首页轮播图请求成功：$it")
            }
        }
    }

    fun startLoop(homeViewPager: ViewPager) {
        handler.sendMessageDelayed(Message.obtain().apply {
            obj = homeViewPager
        }, 3000)
    }

    /**
     * 停止轮播图轮播
     */
    fun stopLoop() {
        handler.removeCallbacksAndMessages(null)
        Log.d(TAG, "停止了轮播图滚动")
    }
}