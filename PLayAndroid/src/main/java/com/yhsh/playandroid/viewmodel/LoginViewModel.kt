package com.yhsh.playandroid.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yhsh.playandroid.bean.UserLoginBean
import com.yhsh.playandroid.net.API
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    val TAG = "LoginViewModel"
    private val loginState = MutableStateFlow<UserLoginBean?>(null)
    val _loginState = loginState.asStateFlow()
    fun login(userName: String, password: String) {
        viewModelScope.launch {
            API.login(userName, password).catch {
                Log.d(TAG, "请求登录失败:$it")
            }.collect {
                Log.d(TAG, "请求登录成功:$it")
                //使用stateflow将登录成功的数据抛出去
                loginState.emit(it)
            }
        }
    }
}