package com.yhsh.flowstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.yhsh.flowstudy.bean.SearchKeyWorld;
import com.yhsh.flowstudy.dao.SearchKeyWorldDao;
import com.yhsh.flowstudy.view.FlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class FlowActivity extends AppCompatActivity {

    private EditText rtHistory;
    private SearchKeyWorldDao historyDao;
    public static final String TAG = "FlowActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        FlowLayout fw = findViewById(R.id.fw_layout);
        rtHistory = findViewById(R.id.et_history);
        //获取数据库对象
        historyDao = MyDataBase.Companion.getDb(getApplicationContext()).getHistoryDao();
        List<String> list = new ArrayList<>();
        list.add("男装新孕妇");
        list.add("阿迪达斯新美服");
        list.add("李宁新运动");
        list.add("篮球");
        list.add("最强男明星");
        list.add("星际大海洋之旅");
        list.add("葫芦兄弟之七大魔王");
        list.add("七龙珠之孙悟空");
        list.add("最新战舰");
        list.add("最强无战事之东兴刘龙");
        list.add("赵谦孙俪王二麻子");
        list.add("三角形");
        list.add("的身份和大家开始广泛但是");
        list.add("地方都是");
        list.add("而侮辱额外");
        list.add("a对方的");
        list.add("a芬兰的南方");
        list.add("法国进口");
        list.add("w如同家人看");
        list.add("少女你是");
        list.add("飞机爱德华");
        list.add("s东方国家的司空见惯");
        list.add("的攻击方式领导给老师的两个");
        list.add("d分公司的");
        fw.setTextList(list);
        //查询所有数据
        Executors.newFixedThreadPool(3).submit(() -> {
            List<SearchKeyWorld> allHistory = historyDao.getAllHistory();
            runOnUiThread(() -> {
                for (SearchKeyWorld keyWorld : allHistory) {
                    Log.d(TAG, "打印所有历史记录：id:" + keyWorld.getKey() + " " + keyWorld.getKeyWorld() + " " + keyWorld.getSearchTime());
                }
            });
        });
    }

    public void addHistory(View view) {
        String keyWorld = rtHistory.getText().toString().trim();
        if (TextUtils.isEmpty(keyWorld) || null == historyDao) return;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
        String time = simpleDateFormat.format(new Date());
        SearchKeyWorld key = new SearchKeyWorld(keyWorld, 0, time);
        Executors.newFixedThreadPool(3).submit(() -> {
            historyDao.insertHistory(key);
            Log.d(TAG, "新增历史记录：" + key.getKeyWorld());
        });
    }
}