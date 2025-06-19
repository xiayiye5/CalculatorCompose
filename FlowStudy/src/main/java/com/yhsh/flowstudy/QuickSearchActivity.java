package com.yhsh.flowstudy;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yhsh.flowstudy.view.QuickIndexBar;

public class QuickSearchActivity extends AppCompatActivity {
    private static final String TAG = "QuickSearchActivity";
    //通讯录好友数据集合
    private final String[][] contacts = new String[][]{{"A", "阿德"}, {"A", "阿三"}, {"A", "阿明"}, {"A", "阿狗"}, {"B", "比尔"}, {"B", "比利"}, {"B", "毕设"}, {"C", "查理"}, {"C", "查单"}, {"C", "茶叶蛋"}, {"C", "擦布"}, {"D", "丹尼"}, {"D", "大户"}, {"D", "单子"}, {"D", "担子"}, {"E", "艾米"}, {"E", "恩施"}, {"E", "恩达"}, {"E", "恩惠"}, {"E", "嗯把"}, {"F", "弗兰克"}, {"F", "佛家"}, {"F", "浮沉"}, {"F", "附和"}, {"G", "格雷戈里"}, {"G", "疙瘩"}, {"G", "格格"}, {"H", "汉克"}, {"H", "河流"}, {"H", "湖大"}, {"I", "伊恩"}, {"J", "杰克"}, {"J", "佳作"}, {"J", "嘉禾"}, {"K", "卡尔"}, {"K", "卡车"}, {"K", "卡巴斯基"}, {"K", "看门狗"}, {"L", "劳伦斯"}, {"L", "劳作"}, {"L", "老奴"}, {"L", "老女"}, {"M", "马克"}, {"M", "妈妈"}, {"M", "麻将"}, {"M", "麻烦"}, {"N", "诺曼"}, {"N", "诺珀"}, {"N", "懦弱"}, {"O", "奥利弗"}, {"O", "懊悔"}, {"O", "傲慢"}, {"P", "帕特里克"}, {"P", "爬坡"}, {"P", "爬山"}, {"Q", "奎因"}, {"Q", "秦始皇"}, {"Q", "秦明"}, {"Q", "气焊"}, {"R", "罗伯特"}, {"R", "容易"}, {"R", "容若"}, {"R", "融合"}, {"S", "萨姆"}, {"S", "三山"}, {"S", "萨达姆"}, {"S", "撒泼"}, {"T", "泰德"}, {"T", "塔山"}, {"T", "塔河"}, {"T", "塌胡"}, {"U", "乌尔里希"}, {"U", "乌克兰"}, {"U", "物理"}, {"U", "无户口"}, {"V", "瓦尔特"}, {"W", "威廉"}, {"W", "威武"}, {"W", "维和"}, {"W", "威吓"}, {"X", "希拉里"}, {"X", "稀里糊涂"}, {"X", "希望"}, {"X", "洗碗"}, {"Y", "伊莱恩"}, {"Y", "义务"}, {"Y", "衣服"}, {"Y", "衣衫"}, {"Y", "义乌"}, {"Y", "遗物"}, {"Y", "异物"}, {"Z", "扎克"}, {"Z", "扎到"}, {"Z", "炸弹"}, {"Z", "诈唬"}, {"Z", "炸死"}, {"Z", "扎丝"}, {"Z", "扎人"}, {"#", "其他"}, {"#", "@大将军"}, {"#", "#胡萝卜"}, {"#", "@大山"}, {"#", "#九死一生"}, {"#", "@万物生活"}, {"#", "@996"}, {"#", "#瞎名字"}, {"#", "@臭鸡蛋"}, {"#", "#劳动法"}, {"#", "@地方很多"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_search);
        QuickIndexBar quickIndexBar = findViewById(R.id.quick_index_bar);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new QuickSearchAdapter(contacts));
        quickIndexBar.setOnLetterChangeListener(letter -> {
            Toast.makeText(getApplicationContext(), letter, Toast.LENGTH_SHORT).show();
            //根据点击的position跳转到对应的联系人位置
            int index = 0;
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i][0].equals(letter)) {
                    index = i;
                    break;
                }
            }
            //根据position回到指定位置，置顶
            linearLayoutManager.scrollToPositionWithOffset(index, 0);
        });
    }
}