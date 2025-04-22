package com.yhsh.flowstudy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yhsh.flowstudy.viewmodel.HomeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 检索未使用的资源文件 inspect code
 *  GlobalScope.launch(){}默认Dispatchers.Default子线程
 *  viewModelScope.launch(){}默认Dispatchers.Main主线程
 *  lifecycleScope.launch(){}默认Dispatchers.Main主线程
 *  MainScope().launch(){}默认Dispatchers.Main主线程
 */
class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private val viewModel: HomeModel by lazy { HomeModel() }

    @OptIn(DelicateCoroutinesApi::class)
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
            viewModel.collectLeastData()
            viewModel.test()
        }
        //自定义线程池方式设置协程调度器
        val threads = ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            6,
            10,
            TimeUnit.SECONDS,
            LinkedBlockingDeque(10)
        ).asCoroutineDispatcher()
        CoroutineScope(threads).launch {

        }
    }
}