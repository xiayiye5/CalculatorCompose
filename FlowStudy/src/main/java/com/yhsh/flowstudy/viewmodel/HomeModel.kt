package com.yhsh.flowstudy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.yhsh.flowstudy.bean.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeModel : ViewModel() {
    val data = MutableStateFlow("123")
    val event = MutableSharedFlow<Event>(replay = 1)

    @OptIn(DelicateCoroutinesApi::class)
    fun request() {
        GlobalScope.launch(Dispatchers.Main) {
            data.value = "996"
            val peopleOne = Event("张三", 18, "男")
            //只支持挂起函数中调用
            event.emit(peopleOne)
        }
        //可以在非挂起函数中调用
        val success = event.tryEmit(Event("李四", 28, "男"))
        Log.d("MainActivity", "热流发送成功：$success")
    }

    /**
     * 集合创建flow方式
     */
    suspend fun send() {
        listOf<String>("香蕉", "西瓜", "西红柿", "柿子", "哈密瓜", "红枣", "橘子").asFlow()
            .collect {
                Log.d("MainActivity", "验证集合创建flow的方式：$it")
            }
    }

    /**
     * 自建集合发射流方式
     */
    suspend fun sendList() {
        flowOf(
            "张三" to 18, "张璐" to "男", "李俊" to "女", "李军" to 20, "韩露露" to "女"
        ).collect {
            Log.d("MainActivity", "自建集合发射流方式创建flow的方式：$it")
        }
    }

    suspend fun sharedIn() {
        //下面协程作用域表示各个子协程相互独立，任何一个子协程异常后并不会影响其它子协程和父协程取消或异常
        val main = CoroutineScope(Dispatchers.Main + SupervisorJob())
        val sharedInFlow = flow<String> {
            for (i in 0..5) {
                val timeString = SimpleDateFormat(
                    "HH:mm:ss:SSS", Locale.getDefault()
                ).format(System.currentTimeMillis())
                Log.e(
                    "MainActivity",
                    "${Thread.currentThread().name}:触发发送数据 index = $i 时间 = $timeString"
                )/*
                如果发送单单的时间戳字符串，时间戳字符串很多是一样的时间字符串，这会导致数据去重。
                为了避免这种情况这里添加了序号数值。
                */
                emit("序号 = $i 时间 = $timeString")
            }/*
                  解释shareIn的参数：
                    scope 是创建热流的协程域
                    started 为热流的启动模式
                    replay为缓存数据的数量，不能设置为0，否则在没有订阅者时会立刻丢弃缓存的数据
                 */
        }.shareIn(scope = main, started = SharingStarted.Eagerly, replay = 5)
        main.launch(Dispatchers.Main) {
            launch {
                sharedInFlow.collect {
                    Log.d(
                        "MainActivity", "${Thread.currentThread().name}:sharedIn创建flow的方式：$it"
                    )
                }
            }
            sharedInFlow.collect {
                Log.d("MainActivity", "${Thread.currentThread().name}:sharedIn创建flow的方式二：$it")
            }
        }
    }
}