package com.garden.gts.ma.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.garden.gts.ma.R;

/**
 * This class is used for controlling background music
 */
public class MusicServices {

    private static final String TAG = "Bg_Music";
    private float mLeftVolume;
    private float mRightVolume;
    private Context mContext;
    private MediaPlayer mBackgroundMediaPlayer;
    private boolean isPaused;
    private int root;

    private MusicServices() {
        initData();
    }

    private static class MusicFactory {
        private static MusicServices musics = new MusicServices();
    }

    public static MusicServices getMusc() {
        return MusicFactory.musics;
    }

    //初始化一些数据
    private void initData() {
        mLeftVolume = 0.5f;
        mRightVolume = 0.5f;
        isPaused = false;

    }

    /**
     * 根据path路径播放背景音乐
     */
    public void playBackgroundMusic(Context cont, int root) {
        if (!Utils.soundTurn) {
            return;
        }
        if (root != 0) {
            this.root = root;
        }
        if (this.root == 0) {
            this.root = R.raw.xiaozhiche;
        }
        if (cont != null) {
            mContext = cont;
        } else {
            Log.e(TAG, "Context is null");
            return;
        }
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.release();
        }
        mBackgroundMediaPlayer = MediaPlayer.create(mContext, this.root);
        if (mBackgroundMediaPlayer == null) {
            Log.e(TAG, "playBackgroundMusic: background media player is null");
        } else {
            try {
                setBackgroundVolume(0.5f);
                mBackgroundMediaPlayer.setLooping(true);
                mBackgroundMediaPlayer.start();
            } catch (Exception e) {
                Log.e(TAG, "playBackgroundMusic: error state");
            }
        }
        isPaused = false;
    }

    /**
     * 停止播放背景音乐
     */
    public void stopBackgroundMusic() {
        if (!Utils.soundTurn) {
            return;
        }
        if (mBackgroundMediaPlayer != null && !isPaused) {
            mBackgroundMediaPlayer.stop();
            mBackgroundMediaPlayer.release();
            // should set the state, if not , the following sequence will be error
            // play -> pause -> stop -> resume
            this.isPaused = true;
        }
    }

    /**
     * 暂停播放背景音乐
     */
    public void pauseBackgroundMusic() {
        if (!Utils.soundTurn) {
            return;
        }
        if (mBackgroundMediaPlayer != null && !isPaused) {
            mBackgroundMediaPlayer.pause();
            this.isPaused = true;
        }
    }

    /**
     * 继续播放背景音乐
     */
    public void resumeBackgroundMusic() {
        if (!Utils.soundTurn) {
            return;
        }
        if(mBackgroundMediaPlayer==null){
            this.playBackgroundMusic(mContext, 0);
        }else{
            if(isPaused){
                try {
                    mBackgroundMediaPlayer.start();
                    isPaused=false;
                }catch (Exception e){
                    mBackgroundMediaPlayer.release();
                    this.playBackgroundMusic(mContext, 0);
                }
            }
        }
    }

    /**
     * 判断背景音乐是否正在播放
     */
    public boolean isBackgroundMusicPlaying() {
        return mBackgroundMediaPlayer != null && mBackgroundMediaPlayer.isPlaying();
    }

    /**
     * 得到背景音乐的“音量”
     *
     * @return float
     */
    public float getBackgroundVolume() {
        if (this.mBackgroundMediaPlayer != null) {
            return (this.mLeftVolume + this.mRightVolume) / 2;
        } else {
            return 0.0f;
        }
    }

    /**
     * 设置背景音乐的音量
     *
     * @param volume：设置播放的音量，float类型
     */
    public void setBackgroundVolume(float volume) {
        this.mLeftVolume = this.mRightVolume = volume;
        if (this.mBackgroundMediaPlayer != null) {
            this.mBackgroundMediaPlayer.setVolume(this.mLeftVolume, this.mRightVolume);
        }
    }
}