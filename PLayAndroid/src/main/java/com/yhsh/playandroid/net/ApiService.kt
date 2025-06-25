package com.yhsh.playandroid.net

import com.yhsh.playandroid.bean.ArticleBean
import com.yhsh.playandroid.bean.BannerBean
import com.yhsh.playandroid.bean.UserLoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String, @Field("password") password: String
    ): UserLoginResponse


    @GET("/banner/json")
    suspend fun banner(): BannerBean

    @GET("/article/list/{page}/json")
    suspend fun homeArticleList(@Path("page") page: Int = 0): ArticleBean
}
