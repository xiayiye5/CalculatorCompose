package com.yhsh.playandroid.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yhsh.playandroid.bean.ArticleBean
import com.yhsh.playandroid.net.API
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeArticleListViewModel : BaseViewModel() {
    val TAG = "HomeArticleListViewModel"
    private val articleStateFlow = MutableStateFlow<ArticleBean?>(null)
    var _articleStateFlow = articleStateFlow.asStateFlow()
    fun getArticleList(page: Int = 0) {
        viewModelScope.launch {
            API.homeArticleList(page).catch {
                Log.d(TAG, "获取文章列表失败：${it.message}")
            }.collect {
                if (it.datas.isNotEmpty()) {
                    Log.d(TAG, "获取文章列表成功:${it.total}")
                    articleStateFlow.value = it
                } else {
                    Log.d(TAG, "获取文章列表失败：文章条数为空")
                }
            }
        }
    }
}