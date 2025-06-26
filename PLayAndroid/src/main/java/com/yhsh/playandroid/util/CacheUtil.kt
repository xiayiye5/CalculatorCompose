package com.yhsh.playandroid.util

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type


object CacheUtil {
    private var cacheContain: MMKV = MMKV.defaultMMKV()
    private val gson = Gson()
    fun saveData(key: String, data: String) {
        cacheContain.encode(key, data)
    }

    fun takeData(key: String): String? {
        return cacheContain.decodeString(key)
    }

    fun <T : Parcelable> saveObj(key: String, data: T) {
        cacheContain.encode(key, data)
    }

    fun <T : Parcelable> takeDataObj(key: String, tClass: Class<T>): T? {
        return cacheContain.decodeParcelable(key, tClass)
    }

    //通过Gson将json字符串转换成List<Object>的形式
    fun <T> parseList(json: String?, clazz: Class<T>?): List<T>? {
        val type: Type = TypeToken.getParameterized(MutableList::class.java, clazz).type
        return gson.fromJson(json, type)
    }
}