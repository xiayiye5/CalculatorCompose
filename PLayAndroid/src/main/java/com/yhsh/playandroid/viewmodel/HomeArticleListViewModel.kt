package com.yhsh.playandroid.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.scwang.smart.refresh.layout.SmartRefreshLayout
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
    var currentPage = 0
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

    fun initRefresh(refreshLayout: SmartRefreshLayout) {
        //禁止下拉刷新
        refreshLayout.setEnableRefresh(false)
        //设置加载更多监听
        refreshLayout.setOnLoadMoreListener {
            currentPage++
            Log.d(TAG, "上拉了，开始请求下一页数据${currentPage}")
            getArticleList(currentPage)
        }
    }
}