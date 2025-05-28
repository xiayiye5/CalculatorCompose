package com.yhsh.mideiasessionplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * HomeActivity 是播放器应用的主界面，负责展示当前播放的歌曲信息、歌词、专辑封面，并提供播放控制功能。
 * 包括播放/暂停、上一曲、下一曲、进度调节等操作。
 */
public class HomeActivity extends AppCompatActivity {
    /**
     * 专辑封面图片控件
     */
    private ImageView ivAlbumCover;
    /**
     * 歌曲标题文本控件
     */
    private TextView tvSongTitle;
    /**
     * 歌手名文本控件
     */
    private TextView tvArtist;
    /**
     * 歌词文本控件
     */
    private TextView tvLyrics;
    /**
     * 播放进度条控件
     */
    private SeekBar seekbarProgress;
    /**
     * 上一曲按钮
     */
    private ImageButton btnPrevious;
    /**
     * 播放/暂停按钮
     */
    private ImageButton btnPlayPause;
    /**
     * 下一曲按钮
     */
    private ImageButton btnNext;

    /**
     * 音乐播放服务实例
     */
    private MusicService musicService;
    /**
     * 当前是否正在播放
     */
    private boolean isPlaying = false;

    /**
     * ServiceConnection 用于管理与 MusicService 的连接和断开。
     * 连接成功后获取服务实例，初始化播放器。
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 绑定服务后获取 MusicService 实例
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            initializePlayer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 服务断开时清空引用
            musicService = null;
        }
    };

    /**
     * Activity 创建时初始化界面和绑定音乐服务。
     *
     * @param savedInstanceState Activity 状态保存数据
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        bindMusicService();
    }

    /**
     * 初始化界面控件并设置点击事件。
     */
    private void initViews() {
        ivAlbumCover = findViewById(R.id.iv_album_cover);
        tvSongTitle = findViewById(R.id.tv_song_title);
        tvArtist = findViewById(R.id.tv_artist);
        tvLyrics = findViewById(R.id.tv_lyrics);
        seekbarProgress = findViewById(R.id.seekbar_progress);
        btnPrevious = findViewById(R.id.btn_previous);
        btnPlayPause = findViewById(R.id.btn_play_pause);
        btnNext = findViewById(R.id.btn_next);

        setupClickListeners();
    }

    /**
     * 设置各个按钮和进度条的点击事件监听。
     */
    private void setupClickListeners() {
        btnPlayPause.setOnClickListener(v -> {
            if (musicService != null) {
                if (isPlaying) {
                    musicService.pause();
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    musicService.play();
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                }
                isPlaying = !isPlaying;
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (musicService != null) {
                musicService.skipToPrevious();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (musicService != null) {
                musicService.skipToNext();
            }
        });

        seekbarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && musicService != null) {
                    musicService.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * 绑定音乐播放服务，确保播放器功能可用。
     */
    private void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 初始化播放器，加载音乐列表等操作。
     */
    private void initializePlayer() {
        // 创建示例播放列表（实际应用中应该从文件系统或数据库加载）
        List<MusicItem> playlist = new ArrayList<>();

        // 添加示例音乐（实际开发中应该从存储中读取）
        playlist.add(new MusicItem(
                "示例音乐1",
                "演唱者1",
                "file:///android_asset/covers/cover1.jpg",
                "file:///android_asset/music/song1.mp3",
                "file:///android_asset/lyrics/lyric1.lrc"
        ));

        playlist.add(new MusicItem(
                "示例音乐2",
                "演唱者2",
                "file:///android_asset/covers/cover2.jpg",
                "file:///android_asset/music/song2.mp3",
                "file:///android_asset/lyrics/lyric2.lrc"
        ));

        // 设置播放列表到服务中
        if (musicService != null) {
            musicService.setPlaylist(playlist);

            // 更新界面显示第一首歌的信息
            updateMusicInfo(playlist.get(0));

            // 设置进度条最大值（假设音乐时长为3分钟）
            seekbarProgress.setMax(180000);

            // 启动进度更新任务
            startProgressUpdate();
        }
    }

    /**
     * 更新音乐信息显示
     *
     * @param musicItem 当前播放的音乐项
     */
    private void updateMusicInfo(MusicItem musicItem) {
        tvSongTitle.setText(musicItem.getTitle());
        tvArtist.setText(musicItem.getArtist());

        // 加载专辑封面
        try {
            InputStream is = getAssets().open(musicItem.getAlbumArtPath().replace("file:///android_asset/", ""));
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            ivAlbumCover.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            // 设置默认封面
            ivAlbumCover.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // 加载歌词（简单实现，实际应该使用LRC解析器）
        try {
            InputStream is = getAssets().open(musicItem.getLyricsPath().replace("file:///android_asset/", ""));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder lyrics = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                lyrics.append(line).append("\n");
            }
            tvLyrics.setText(lyrics.toString());
        } catch (IOException e) {
            e.printStackTrace();
            tvLyrics.setText("暂无歌词");
        }
    }

    /**
     * 启动进度更新任务
     */
    private void startProgressUpdate() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (musicService != null && isPlaying) {
                    // 更新进度条
                    int currentPosition = musicService.getCurrentPosition();
                    seekbarProgress.setProgress(currentPosition);
                }
                handler.postDelayed(this, 1000); // 每秒更新一次
            }
        }, 1000);
    }

    /**
     * Activity 销毁时解绑音乐服务，防止内存泄漏。
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}