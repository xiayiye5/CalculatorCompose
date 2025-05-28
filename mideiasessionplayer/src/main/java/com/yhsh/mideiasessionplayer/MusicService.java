package com.yhsh.mideiasessionplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐播放服务类
 * 负责管理音乐播放、媒体会话控制和播放状态更新
 * 通过MediaSession提供标准的媒体控制接口
 */
public class MusicService extends Service {
    /**
     * 媒体播放器实例，用于实际的音频播放控制
     */
    private MediaPlayer mediaPlayer;
    /**
     * 媒体会话实例，用于与系统和其他应用程序交互
     */
    private MediaSessionCompat mediaSession;
    /**
     * 播放状态构建器，用于更新媒体会话的播放状态
     */
    private PlaybackStateCompat.Builder stateBuilder;
    /**
     * 音乐服务绑定器实例，用于Activity与Service通信
     */
    private MusicBinder musicBinder = new MusicBinder();

    /**
     * 播放列表
     */
    private List<MusicItem> playlist = new ArrayList<>();
    /**
     * 当前播放的音乐索引
     */
    private int currentIndex = 0;

    /**
     * 服务创建时的初始化
     * 初始化媒体会话和媒体播放器
     */
    @Override
    public void onCreate() {
        super.onCreate();
        initMediaSession();
        initMediaPlayer();
    }

    /**
     * 初始化媒体会话
     * 设置媒体控制回调和播放状态
     */
    private void initMediaSession() {
        mediaSession = new MediaSessionCompat(this, "MusicService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SEEK_TO);

        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                play();
            }

            @Override
            public void onPause() {
                pause();
            }

            @Override
            public void onSkipToNext() {
                skipToNext();
            }

            @Override
            public void onSkipToPrevious() {
                skipToPrevious();
            }

            @Override
            public void onSeekTo(long pos) {
                seekTo((int) pos);
            }
        });

        mediaSession.setActive(true);
    }

    /**
     * 初始化媒体播放器
     * 设置播放完成监听器
     */
    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> skipToNext());
    }

    /**
     * 开始播放音乐
     * 如果媒体播放器已初始化且当前未播放，则开始播放
     */
    public void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
        }
    }

    /**
     * 暂停音乐播放
     * 如果媒体播放器正在播放，则暂停播放
     */
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            updatePlaybackState(PlaybackStateCompat.STATE_PAUSED);
        }
    }

    /**
     * 设置播放列表
     *
     * @param playlist 要播放的音乐列表
     */
    public void setPlaylist(List<MusicItem> playlist) {
        this.playlist = playlist;
        if (!playlist.isEmpty()) {
            prepareAndPlay(0);
        }
    }

    /**
     * 准备并播放指定索引的音乐
     *
     * @param index 要播放的音乐索引
     */
    private void prepareAndPlay(int index) {
        if (index >= 0 && index < playlist.size()) {
            currentIndex = index;
            MusicItem item = playlist.get(index);

            try {
                mediaPlayer.reset();
                String musicPath = item.getMusicPath().replace("file:///android_asset/", "");
                mediaPlayer.setDataSource(getAssets().openFd(musicPath));
                mediaPlayer.prepare();

                // 更新媒体信息
                updateMetadata(item.getTitle(), item.getArtist(), item.getAlbumArtPath());

                // 开始播放
                play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放下一首歌曲
     */
    public void skipToNext() {
        if (!playlist.isEmpty()) {
            int nextIndex = (currentIndex + 1) % playlist.size();
            prepareAndPlay(nextIndex);
        }
    }

    /**
     * 播放上一首歌曲
     */
    public void skipToPrevious() {
        if (!playlist.isEmpty()) {
            int previousIndex = (currentIndex - 1 + playlist.size()) % playlist.size();
            prepareAndPlay(previousIndex);
        }
    }

    /**
     * 跳转到指定播放位置
     *
     * @param position 目标播放位置（毫秒）
     */
    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    /**
     * 获取当前播放位置
     *
     * @return 当前播放位置（毫秒）
     */
    public int getCurrentPosition() {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
    }

    /**
     * 更新播放状态
     *
     * @param state 播放状态（播放中、暂停等）
     */
    private void updatePlaybackState(int state) {
        long position = mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;

        stateBuilder.setState(state, position, 1.0f);
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    /**
     * 更新媒体元数据
     *
     * @param title    歌曲标题
     * @param artist   艺术家
     * @param albumArt 专辑封面URI
     */
    public void updateMetadata(String title, String artist, String albumArt) {
        MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, albumArt);

        mediaSession.setMetadata(metadataBuilder.build());
    }

    /**
     * 绑定服务时返回绑定器实例
     *
     * @param intent 绑定服务的Intent
     * @return 音乐服务绑定器实例
     */
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    /**
     * 服务销毁时释放资源
     * 释放媒体播放器和媒体会话
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaSession != null) {
            mediaSession.release();
        }
    }

    /**
     * 音乐服务绑定器内部类
     * 用于Activity与Service之间的通信
     */
    public class MusicBinder extends Binder {
        /**
         * 获取音乐服务实例
         *
         * @return MusicService实例
         */
        public MusicService getService() {
            return MusicService.this;
        }
    }
}