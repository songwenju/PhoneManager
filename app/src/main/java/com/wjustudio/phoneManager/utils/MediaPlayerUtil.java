package com.wjustudio.phoneManager.utils;

import android.media.MediaPlayer;

/**
 * 作者：songwenju on 2016/3/20 18:44
 * 邮箱：songwenju01@163.com
 */
public class MediaPlayerUtil {
    private static MediaPlayer mediaPlayer;
    private static MediaPlayer getInstanse(){
        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        return mediaPlayer;
    }
}
