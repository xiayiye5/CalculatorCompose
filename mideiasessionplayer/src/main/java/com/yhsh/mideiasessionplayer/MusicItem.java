package com.yhsh.mideiasessionplayer;

/**
 * 音乐项实体类
 * 用于存储和管理单个音乐项的相关信息，包括标题、艺术家、专辑封面、音乐文件路径和歌词文件路径
 */
public class MusicItem {
    /**
     * 音乐标题
     */
    private String title;
    /**
     * 艺术家名称
     */
    private String artist;
    /**
     * 专辑封面图片路径
     */
    private String albumArtPath;
    /**
     * 音乐文件路径
     */
    private String musicPath;
    /**
     * 歌词文件路径
     */
    private String lyricsPath;

    /**
     * 创建音乐项对象
     *
     * @param title        音乐标题
     * @param artist       艺术家名称
     * @param albumArtPath 专辑封面图片路径
     * @param musicPath    音乐文件路径
     * @param lyricsPath   歌词文件路径
     */
    public MusicItem(String title, String artist, String albumArtPath, String musicPath, String lyricsPath) {
        this.title = title;
        this.artist = artist;
        this.albumArtPath = albumArtPath;
        this.musicPath = musicPath;
        this.lyricsPath = lyricsPath;
    }

    /**
     * 获取音乐标题
     *
     * @return 音乐标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置音乐标题
     *
     * @param title 要设置的音乐标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取艺术家名称
     *
     * @return 艺术家名称
     */
    public String getArtist() {
        return artist;
    }

    /**
     * 设置艺术家名称
     *
     * @param artist 要设置的艺术家名称
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * 获取专辑封面图片路径
     *
     * @return 专辑封面图片路径
     */
    public String getAlbumArtPath() {
        return albumArtPath;
    }

    /**
     * 设置专辑封面图片路径
     *
     * @param albumArtPath 要设置的专辑封面图片路径
     */
    public void setAlbumArtPath(String albumArtPath) {
        this.albumArtPath = albumArtPath;
    }

    /**
     * 获取音乐文件路径
     *
     * @return 音乐文件路径
     */
    public String getMusicPath() {
        return musicPath;
    }

    /**
     * 设置音乐文件路径
     *
     * @param musicPath 要设置的音乐文件路径
     */
    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    /**
     * 获取歌词文件路径
     *
     * @return 歌词文件路径
     */
    public String getLyricsPath() {
        return lyricsPath;
    }

    /**
     * 设置歌词文件路径
     *
     * @param lyricsPath 要设置的歌词文件路径
     */
    public void setLyricsPath(String lyricsPath) {
        this.lyricsPath = lyricsPath;
    }
}