package com.garden.gts.ma.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by GTS on 2016/9/24.
 */
public class Utils {

    public static boolean soundTurn = true;
    public static int waitTime = 10 * 1000;

    public static int restTime = (int) (5.6 * 1000);
    public static int kongFuStyle = 1;

    public static List<String> waitTimeList = new ArrayList<>(32);
    public static List<String> restTimeList = new ArrayList<>(16);
    public static List<String> minuteList = new ArrayList<>(16);
    public static int r2Time = 5;
    public static int r3Time = 5;
    public static int r4Time = 5;

    static{
        //初始化可选参数
        for (int i = 1; i <= 30; i++) {
            waitTimeList.add(String.valueOf(i));
            if (i <= 12) {
                restTimeList.add(String.valueOf(i));
            }
            if (i >= 5 && i <= 10) {
                minuteList.add(String.valueOf(i));
            }
        }
    }

    public static void checkData(SharedPreferences sp) {
        int waitTime = sp.getInt("waitTime", 0);
        if(waitTime!=0){
            Utils.waitTime =waitTime;
        }
        int restTime=sp.getInt("restTime",0);
        if(restTime!=0){
            Utils.restTime=restTime;
        }
        Utils.r2Time=sp.getInt("r2Time",5);
        Utils.r3Time=sp.getInt("r2Time",5);
        Utils.r4Time=sp.getInt("r2Time",5);
        soundTurn=sp.getBoolean("sound",true);
    }

    public static int getWindowWidth(Context cont) {

        DisplayMetrics dm = cont.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getWindowHeight(Context cont) {
        DisplayMetrics dm = cont.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static boolean prompt = true;
}
