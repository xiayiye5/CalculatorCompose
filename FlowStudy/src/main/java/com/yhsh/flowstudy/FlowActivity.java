package com.yhsh.flowstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yhsh.flowstudy.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class FlowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        FlowLayout fw = findViewById(R.id.fw_layout);
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
    }
}