package com.yhsh.playandroid

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.yhsh.playandroid.page.HomeFragment
import com.yhsh.playandroid.page.MineFragment
import com.yhsh.playandroid.page.StudyFragment
import com.yhsh.playandroid.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private val TAG = "MainActivity"
    private val listPage = listOf(
        HomeFragment.newInstance("home", "1"),
        StudyFragment.newInstance("study", "2"),
        MineFragment.newInstance("mine", "3")
    )
    private val tabList = listOf("首页", "学习", "我的")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = HomeAdapter(supportFragmentManager, listPage)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        // 正确初始化示例
        tabLayout.apply {
            setupWithViewPager(viewPager) // 确保viewPager非空
            tabList.forEachIndexed { index, title ->
                getTabAt(index)?.let { tab ->  // 安全调用操作符
                    tab.text = title
                    tab.setIcon(R.drawable.ic_launcher_background)
                }
            }
        }


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
                val name = "${it.username}登录成功"
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