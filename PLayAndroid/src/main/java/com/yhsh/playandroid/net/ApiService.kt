package com.yhsh.playandroid.net

import com.yhsh.playandroid.bean.BannerBean
import com.yhsh.playandroid.bean.UserLoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String, @Field("password") password: String
    ): UserLoginResponse


    @GET("/banner/json")
    suspend fun banner(): BannerBean
}
