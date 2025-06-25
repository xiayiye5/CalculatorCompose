package com.yhsh.playandroid.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yhsh.playandroid.bean.BannerBean
import com.yhsh.playandroid.net.API
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BannerViewModel : BaseViewModel() {
    private val TAG = "BannerViewModel"
    private val bannerState = MutableStateFlow<BannerBean?>(null)
    val _bannerState = bannerState.asStateFlow()
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
}