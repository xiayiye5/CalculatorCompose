package com.yhsh.flowstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.yhsh.flowstudy.view.LyricsView;

import java.util.ArrayList;
import java.util.List;

public class LyricActivity extends AppCompatActivity {
    public static final String TAG = "LyricActivity";
    int n = 0;
    Handler h = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            lyricsView.updateCurrentTime(1000L * (n++)); // 单位毫秒
            sendEmptyMessageDelayed(0, 1000);
        }
    };
    private LyricsView lyricsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);
        List<LyricsView.LyricLine> lines = new ArrayList<>();
        lines.add(new LyricsView.LyricLine(1000, "可能南方的陽光照著北方的風"));
        lines.add(new LyricsView.LyricLine(5000, "可能時光被吹走 從此無影無蹤"));
        lines.add(new LyricsView.LyricLine(10000, "可能故事只剩下一個難忘的人"));
        lines.add(new LyricsView.LyricLine(13000, "可能在昨夜夢裡 依然笑得純真"));
        lines.add(new LyricsView.LyricLine(16000, "可能北京的後海 許多漂泊的魂"));
        lines.add(new LyricsView.LyricLine(19000, "可能成都小酒館有群孤獨的人"));
        lines.add(new LyricsView.LyricLine(22000, "可能枕邊有微笑才能暖你清晨"));
        lines.add(new LyricsView.LyricLine(25000, "可能夜空有流星才能照你前行"));
        lines.add(new LyricsView.LyricLine(28000, "可能西安城牆上有人誓言不分"));
        lines.add(new LyricsView.LyricLine(31000, "可能要去到大理才算愛得認真"));
        lines.add(new LyricsView.LyricLine(34000, "可能誰說要陪你牽手走完一生"));
        lines.add(new LyricsView.LyricLine(37000, "可能笑著流出淚 某天在某時辰"));
        lines.add(new LyricsView.LyricLine(40000, "可能桂林有漁船為你迷茫點燈"));
        lines.add(new LyricsView.LyricLine(45000, "可能在呼倫草原 牛羊流成風景"));
        lines.add(new LyricsView.LyricLine(48000, "可能再也找不到願意相信的人"));
        lines.add(new LyricsView.LyricLine(50000, "可能穿越了彷徨腳步才能堅定"));
        lines.add(new LyricsView.LyricLine(53000, "可能武當山道上有人虔誠攀登"));
        lines.add(new LyricsView.LyricLine(57000, "可能周莊小巷裡忽然忘掉年輪"));
        lines.add(new LyricsView.LyricLine(60000, "可能要多年以後才能看清曾經"));
        lines.add(new LyricsView.LyricLine(63000, "可能在當時身邊有雙溫柔眼晴"));
        lines.add(new LyricsView.LyricLine(65000, "可能西安城牆上有人誓言不分"));
        lines.add(new LyricsView.LyricLine(68000, "可能要去到大理才算愛得認真"));
        lines.add(new LyricsView.LyricLine(70000, "可能誰說要陪你牽手走完一生"));
        lines.add(new LyricsView.LyricLine(73000, "可能笑著流出淚"));
        lines.add(new LyricsView.LyricLine(75000, "可能終於有一天剛好遇見愛情"));
        lines.add(new LyricsView.LyricLine(76000, "可能永遠在路上有人奮鬥前行"));
        lines.add(new LyricsView.LyricLine(79000, "可能一切的可能 相信才有可能"));
        lines.add(new LyricsView.LyricLine(82000, "可能擁有過夢想才能叫做青春"));
        lyricsView = findViewById(R.id.lyricsView);
        lyricsView.setLyrics(lines);
        h.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "歌词页面退出了！！！");
        h.removeCallbacksAndMessages(null);
        h = null;
    }
}