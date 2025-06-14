package com.yhsh.flowstudy

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class GiftActivity : AppCompatActivity() {
    private val viewModel: GiftWallViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gift)
        val giftWallView = findViewById<GiftWallView>(R.id.viewPager2_gift)
        val btnList1 = findViewById<Button>(R.id.btnList1)
        val btnList2 = findViewById<Button>(R.id.btnList2)

        // 初始化礼物墙
        giftWallView.initialize(viewModel)

        // 准备数据
        val giftList1 = listOf(
            Gift("id0", "小星星"),
            Gift("id1", "甜筒"),
            Gift("id2", "粉钻")
        )

        val giftList2 = listOf(
            Gift("id0", "小星星"),
            Gift("id1", "甜筒"),
            Gift("id2", "粉钻"),
            Gift("id3", "捧在手心"),
            Gift("id4", "白头偕老"),
            Gift("id5", "太阳神"),
            Gift("id6", "花车巡游"),
            Gift("id7", "你好"),
            Gift("id8", "惊喜咖啡"),
            Gift("id9", "520气球"),
            Gift("id10", "有才"),
            Gift("id11", "魔法奇缘"),
            Gift("id12", "四叶草"),
            Gift("id13", "浪漫星星")
        )

        // 按钮切换数据
        btnList1.setOnClickListener {
            giftWallView.showGift(giftList1)
        }

        btnList2.setOnClickListener {
            giftWallView.showGift(giftList2)
        }

        // 初始展示第一个列表
        giftWallView.showGift(giftList2)
    }
}