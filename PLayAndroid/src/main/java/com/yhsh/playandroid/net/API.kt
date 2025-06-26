package com.yhsh.playandroid.net

import android.util.Log
import com.yhsh.playandroid.bean.ArticleBean
import com.yhsh.playandroid.bean.BannerBean
import com.yhsh.playandroid.bean.BannerList
import com.yhsh.playandroid.bean.UserLoginBean
import com.yhsh.playandroid.util.AppUtils
import com.yhsh.playandroid.util.CacheUtil
import com.yhsh.playandroid.util.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {
    private const val BASE_URL = "https://www.wanandroid.com/"
    private var apiService: ApiService
    private val TAG = "API"

    init {
        val client = OkHttpClient.Builder().addInterceptor(NetInterceptor()).build()
        val retrofit = Retrofit.Builder().client(client).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun login(userName: String, password: String): Flow<UserLoginBean> {
        return flow {
            //retrofit结合协程写法
            val response = apiService.login(userName, password)
            if (response.errorCode == 0 && response.data != null) {
                emit(response.data)
            } else {
                //登录失败抛出异常
                throw Exception(response.errorMsg)
            }
            //retrofit最原始的写法
            /*val call = apiService.login("铁路12306", "tielu12306")
                            call.enqueue(object : retrofit2.Callback<UserLoginResponse> {
                                override fun onFailure(call: retrofit2.Call<UserLoginResponse>, t: Throwable) {
                                    println("请求失败: ${t.message}")
                                }

                                override fun onResponse(
                                    call: retrofit2.Call<UserLoginResponse>,
                                    response: retrofit2.Response<UserLoginResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val responseBody = response.body()
                                        println("响应: $responseBody")
                                    } else {
                                        println("请求失败: ${response.errorBody()?.string()}")
                                    }
                                }
                            })*/
        }
    }

    fun banner(): Flow<List<BannerBean>> {
        return flow {
            if (!NetworkUtils.isNetworkConnected(AppUtils.context())) {
                val bannerList = CacheUtil.takeDataObj("banner", BannerList::class.java)
                if (null != bannerList && bannerList.bannerBean.isNotEmpty()) {
                    Log.d(TAG, "显示banner缓存数据:${bannerList.bannerBean[0].title}")
                    //优先显示缓存数据
                    emit(bannerList.bannerBean)
                }
            }
            val response = apiService.banner()
            if (response.errorCode == 0 && response.data != null) {
                emit(response.data)
                val bannerEmptyList = mutableListOf<BannerBean>()
                bannerEmptyList.addAll(response.data)
                val bannerData = BannerList(bannerEmptyList)
                //缓存banner对象
                CacheUtil.saveObj("banner", bannerData)
            } else {
                //登录失败抛出异常
                throw Exception(response.errorMsg)
            }
        }

    }

    fun homeArticleList(page: Int = 0): Flow<ArticleBean> {
        return flow {
            if (!NetworkUtils.isNetworkConnected(AppUtils.context())) {
                //未链接网络使用缓存
                val articleBean = CacheUtil.takeDataObj("articleList", ArticleBean::class.java)
                if (null != articleBean) {
                    //优先显示缓存，再更新服务器上面的最新数据
                    emit(articleBean)
                } else {
                    Log.d(TAG, "网络异常,无法展示文章列表")
                }
            }
            val response = apiService.homeArticleList(page)
            if (response.errorCode == 0 && response.data != null) {
                emit(response.data)
                //开始缓存数据
                if (page == 0) {
                    //仅缓存第一页数据
                    CacheUtil.saveObj("articleList", response.data)
                }
            } else {
                //文章列表获取失败抛出异常
                throw Exception(response.errorMsg)
            }
        }
    }
}
