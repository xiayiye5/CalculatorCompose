package com.yhsh.flowstudy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.yhsh.flowstudy.banner.BannerActivity
import com.yhsh.flowstudy.bean.PersonRoom
import com.yhsh.flowstudy.viewmodel.HomeModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

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
    private val viewModel2 by viewModels<HomeModel>()
    lateinit var etByName: EditText
    lateinit var etInsertName: EditText
    lateinit var etInsertAge: EditText
    lateinit var etInsertAddress: EditText
    lateinit var spDeletePerson: Spinner
    val ld = MutableLiveData<ArrayList<String>>()
    var list: ArrayList<String> = ArrayList<String>()
    var listPerson: List<PersonRoom> = ArrayList()
    var deletePerson: PersonRoom? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etByName = findViewById(R.id.et_by_name)
        etInsertName = findViewById(R.id.et_insert_name)
        etInsertAge = findViewById(R.id.et_insert_age)
        etInsertAddress = findViewById(R.id.et_insert_address)
        spDeletePerson = findViewById(R.id.sp_delete_person)
        lifecycleScope.launch(Dispatchers.Main) {
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
        val dynamicAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, list
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        ld.observe(this) {
            this.list = it
            //刷新了列表数据
            Log.d(TAG, "刷新了列表数据………………${it.size}")
            dynamicAdapter.notifyDataSetChanged()
        }
        // 设置适配器
        spDeletePerson.adapter = dynamicAdapter

        // 设置选择监听
        spDeletePerson.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(
                    this@MainActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT
                ).show()
                deletePerson = listPerson[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 无操作
            }
        }
        counter.observe(this) {
            Log.d(TAG, "查看获取到的值：${it}")
        }
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
                    Log.d(TAG, "通过名字查询 name:${it.name},age:${it.age},address:${it.address}")
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
                list.clear()
                person.forEach {
                    Log.d(TAG, "所有 id:${it.id},name:${it.name},age:${it.age},ads:${it.address}")
                    list.add(it.name)
                }
                listPerson = person
                ld.postValue(list)
            }
        }
    }

    fun insert(view: View) {
        var name = etInsertName.text.toString().trim()
        var age = etInsertAge.text.toString().trim()
        var address = etInsertAddress.text.toString().trim()
        name = if (TextUtils.isEmpty(name)) "名字未知" else name
        age = if (TextUtils.isEmpty(age)) "0" else age
        address = if (TextUtils.isEmpty(address)) "地址未知" else age
        val p = PersonRoom(name, null, age.toInt(), address)
        MainScope().launch(Dispatchers.IO) {
            MyDataBase.getDb(applicationContext)?.getPersonRoomDao()?.insertAccount(p)
        }
    }

    fun deletePerson(view: View) {
        deletePerson?.let {
            MainScope().launch(Dispatchers.IO) {
                //删除方式一
//                MyDataBase.getDb(applicationContext)?.getPersonRoomDao()?.deletePerson(it)
                it.id?.let { personId ->
                    Log.d(TAG, "删除了${it.name} ${it.id}")
                    //删除方式二
                    MyDataBase.getDb(applicationContext)?.getPersonRoomDao()?.deleteId(personId)
                }
            }
        }
    }

    fun flowLayout(view: View) {
        startActivity(Intent(this, FlowActivity::class.java))
    }

    fun lyricLayout(view: View) {
        startActivity(Intent(this, LyricActivity::class.java))
    }

    fun postValue(view: View) {
        incrementCounter()
        //跳转礼物页面
        startActivity(Intent(this, GiftActivity::class.java))
    }

    private val counter = MutableLiveData<Int>()

    fun getCounter(): Int? {
        return counter.value
    }

    fun incrementCounter() {
        // 快速连续多次调用 postValue
        Thread {
            for (i in 1..100) {
                counter.postValue(i)
                //Thread.sleep(10); // 如果加上短暂的停顿，可以显示更多不同的值
            }
        }.start()
    }

    fun banner(view: View) {
        startActivity(Intent(this, BannerActivity::class.java))
    }
}