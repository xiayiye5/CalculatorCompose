package com.yhsh.playandroid

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yhsh.playandroid.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val url = "https://www.wanandroid.com/user/login"
        val body = FormBody.Builder()
            .add("username", "铁路12306")
            .add("password", "tielu12306")
            .build()

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("MainActivity", "请求失败: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                if (body != null) { // 双重判空
                Log.d("MainActivity", "响应: $body")
                }
            }
        })*/
        loginViewModel.login("铁路12306", "tielu12306")
        lifecycleScope.launch {
            loginViewModel._loginState.filterNotNull().collect {
                val name = "${it.data?.username}登录成功"
                Toast.makeText(this@MainActivity, name, Toast.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                // 在此处执行需要在STARTED状态下运行的协程代码
                Log.d(TAG, "执行了生命周期STARTED")
            }
        }
    }
}