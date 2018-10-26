package com.garden.gts.ma.util;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import com.garden.gts.ma.R;

/**
 * Created by GTS on 2018/10/20.
 */
public class KongFu extends Thread {
    private final Object lock = new Object();
    private boolean pause = false;
    private boolean stop = false;
    private int time1;
    private int time2;
    private int times;
    private int time;
    private int realTimes;
    private SoundPool soundPool;

    public boolean isPause() {
        return pause;
    }

    public void pauseThread() {
        pause = true;
    }

    public void resumeThread() {
        pause = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void stopThread() {
        stop = true;
    }

    void onPause() {
        Log.e("1", "被打断");
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public KongFu(int time1, int time2, int times, Context context) {
        this.time1 = time1;
        this.time2 = time2;
        this.times = times;
        this.time = 0;
        this.realTimes = 0;
        soundPool = new SoundPool.Builder().build();
        soundPool.load(context, R.raw.cymbals, 1);
    }

    @Override
    public void run() {
        int totalTime = time1 + time2;
        while (true) {
            while (pause) {
                onPause();
            }
            try {
                if (stop) {
                    time = 0;
                    realTimes = 0;
                    stop = false;
                    return;
                }
                if (realTimes == 0 && time == 0) {
                    Thread.sleep(Utils.waitTime);
                }
                if (time == time1 && time1 != 0) {
                    soundPool.play(1, 1, 1, 1, 0, 1);
                    Thread.sleep(Utils.restTime);
                }
                if (time == totalTime) {
                    soundPool.play(1, 1, 1, 1, 0, 1);
                    time = 0;
                    realTimes++;
                    if (realTimes != times) {
                        Thread.sleep(Utils.restTime);
                    }
                }
                if (realTimes == times) {
                    realTimes = 0;
                    stop = false;
                    break;
                }
                Thread.sleep(1000);
                time++;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
