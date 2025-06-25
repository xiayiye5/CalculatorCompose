package com.yhsh.playandroid.net

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

/**
 * 自定义请求头请求头拦截器
 */
class NetInterceptor : Interceptor {
    val TAG = "NetInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // 打印请求信息
        val requestLog = StringBuilder().apply {
            append("\n========================= Request =======================================\n")
            append("Method: ${request.method()}\n")
            append("URL: ${request.url()}\n")
            append("Headers: ${request.headers()}\n")

            // 打印请求体（非GET请求）
            if (request.body() != null && !request.method().equals("GET", true)) {
                val buffer = Buffer()
                request.body()?.writeTo(buffer)
                append("Body: ${buffer.readUtf8()}\n")
            }
        }
        Log.e(TAG, "requestLog:$requestLog")

        val response = chain.proceed(request)

        // 打印响应信息
        val responseLog = StringBuilder().apply {
            append("\n============================= Response =====================================\n")
            append("Code: ${response.code()}\n")
            append("Headers: ${response.headers()}\n")

            // 打印响应体
            val responseBody = response.peekBody(Long.MAX_VALUE)
            append("Body: ${responseBody.string()}\n")
        }
        Log.e(TAG, "NETWORK:$responseLog")
        return chain.proceed(request)
    }
}