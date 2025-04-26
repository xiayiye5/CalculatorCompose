package com.yhsh.flowstudy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.yhsh.flowstudy.bean.PersonRoom
import com.yhsh.flowstudy.viewmodel.HomeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
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
    lateinit var etByName: EditText
    lateinit var etInsertName: EditText
    lateinit var etInsertAge: EditText

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etByName = findViewById(R.id.et_by_name)
        etInsertName = findViewById(R.id.et_insert_name)
        etInsertAge = findViewById(R.id.et_insert_age)
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
//        val threads = ThreadPoolExecutor(
//            Runtime.getRuntime().availableProcessors(),
//            6,
//            10,
//            TimeUnit.SECONDS,
//            LinkedBlockingDeque(10)
//        ).asCoroutineDispatcher()
//        CoroutineScope(threads).launch {
//            Log.d(TAG, "${Thread.currentThread().name}:自定义线程池的协程线程")
//            callBack()
//            zip()
//            combine()
//        }
    }

    private suspend fun callBack() {
        val data = callbackFlow {
            trySend((1..10).toList()).onFailure {
                Log.d(TAG, "发送异常：$it")
            }
            //主动关闭流,下面的awaitClose方会被调用
            close()
            awaitClose {
                Log.d(TAG, "流被关闭了,可以执行关闭箭头动作了")
            }
        }
        data.collect {
            Log.d(TAG, "${Thread.currentThread().name}:callbackFlow获取到的数据：$it")
        }
    }

    //合并多个flow,以最短的哪个为准
    private suspend fun zip() {
        listOf<String>("胡萝卜", "西红柿").asFlow().zip((0..10).asFlow()) { a, b ->
            "$b-$a"
        }.collect {
            Log.d(TAG, "${Thread.currentThread().name}:打印组合后的数据：$it")
            //TODO 仅输入前2个 1-胡萝卜 2-西红柿
        }
    }

    // 动态组合，任一 Flow 发射新元素时触发 以最长的为准
    private suspend fun combine() {
        listOf<String>("胡萝卜", "西红柿").asFlow().combine((0..10).asFlow()) { a, b ->
            "$b-$a"
        }.collect {
            Log.d(TAG, "${Thread.currentThread().name}:打印组合后的数据：$it")
        }
    }

    fun byName(view: View) {
        MainScope().launch(Dispatchers.IO) {
            val queryName = MyDataBase.getDb(applicationContext)?.getPersonRoomDao()
                ?.getPersonByName(etByName.text.toString().trim())
            queryName?.collect { person ->
                person.forEach {
                    Log.d("MainActivity", "通过名字查询 name:${it.name},age:${it.age}")
                }
            }
//            queryName?.forEach {
//                Log.d("MainActivity", "name:${it.name},age:${it.age}")
//            }
        }
    }

    fun byAll(view: View) {
        MainScope().launch(Dispatchers.IO) {
            val allName = MyDataBase.getDb(applicationContext)?.getPersonRoomDao()?.getAll()
            allName?.collect { person ->
                person.forEach {
                    Log.d("MainActivity", "查询所有用户 name:${it.name},age:${it.age}")
                }
            }
        }
    }

    fun insert(view: View) {
        val name = etInsertName.text.toString().trim()
        val age = etInsertAge.text.toString().trim()
        val p = PersonRoom(name, null, age.toInt())
        MainScope().launch(Dispatchers.IO) {
            MyDataBase.getDb(applicationContext)?.getPersonRoomDao()?.insertAccount(p)
        }
    }
}