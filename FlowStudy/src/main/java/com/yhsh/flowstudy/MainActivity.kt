package com.yhsh.flowstudy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yhsh.flowstudy.viewmodel.HomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * 检索未使用的资源文件 inspect code
 */
class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private val viewModel: HomeModel by lazy { HomeModel() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch(Dispatchers.Main) {
            val a = flow<Int> {
                for (i in 0..10) {
                    emit(i)
                }
            }
            a.collect {
                Log.d(TAG, "${Thread.currentThread().name}:循环获取数据:${it}")
            }
            launch {
                viewModel.event.collect {
                    Log.d(TAG, "${Thread.currentThread().name}:热流获取的数据: $it")
                }
            }
            launch {
                viewModel.data.collect { flow ->
                    Log.d(TAG, "${Thread.currentThread().name}:请求获取到的数据：$flow")
                }
            }
            viewModel.request()
            viewModel.send()
            viewModel.sendList()
            viewModel.sharedIn()
        }
    }
}