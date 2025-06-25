package com.yhsh.playandroid.net

import com.yhsh.playandroid.bean.BannerBean
import com.yhsh.playandroid.bean.UserLoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {
    private const val BASE_URL = "https://www.wanandroid.com/"
    private var apiService: ApiService

    init {
        val client = OkHttpClient.Builder().addInterceptor(NetInterceptor()).build()
        val retrofit = Retrofit.Builder().client(client).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun login(userName: String, password: String): Flow<UserLoginResponse> {
        return flow<UserLoginResponse> {
            //retrofit结合协程写法
            val response = apiService.login(userName, password)
            if (response.errorCode == 0 && response.data != null) {
                emit(response)
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

    fun banner(): Flow<BannerBean> {
        return flow {
            val response = apiService.banner()
            if (response.errorCode == 0 && response.data != null) {
                emit(response)
            } else {
                //登录失败抛出异常
                throw Exception(response.errorMsg)
            }
        }

    }
}
