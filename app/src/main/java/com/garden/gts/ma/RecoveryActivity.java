package com.garden.gts.ma;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.garden.gts.ma.util.ImmersedStatusbarUtils;
import com.garden.gts.ma.util.KongFu;
import com.garden.gts.ma.util.MusicServices;
import com.garden.gts.ma.util.Utils;

public class RecoveryActivity extends AppCompatActivity implements View.OnClickListener {

    private MusicServices musc;
    private Button start;
    private TextView words, remind;
    private boolean jump;
    private KongFu kongFu;
    private RelativeLayout r;
    private int marginTop;
    private int remindSeconds;
    private Handler handler = new Handler();
    private boolean startState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        r = (RelativeLayout) findViewById(R.id.recoveryLayout);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        words = (TextView) findViewById(R.id.words);
        remind = (TextView) findViewById(R.id.remind);
        remind.setText(Utils.waitTime / 1000 + "秒后继续开始");
        findViewById(R.id.back).setOnClickListener(this);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        marginTop = Utils.getWindowHeight(this) / 4;
        ImmersedStatusbarUtils.setViewMargin(start, false, 0, 0, marginTop, 0);
        musc = MusicServices.getMusc();
        musc.playBackgroundMusic(this, R.raw.alwayswithme);
        switch (Utils.kongFuStyle) {
            case 1:
                kongFu = new KongFu(120, 60, 3, this);
                break;
            case 2:
                kongFu = new KongFu(0, Utils.r2Time * 60, 1, this);
                r.setBackgroundResource(R.mipmap.pict2);
                break;
            case 3:
                kongFu = new KongFu(0, Utils.r3Time * 60, 1, this);
                r.setBackgroundResource(R.mipmap.pict3);
//                start.setBackgroundResource(R.drawable.button5);
//                start.setTextColor(0xFF0E60AC);
                start.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;

            case 4:
                kongFu = new KongFu(0, Utils.r4Time * 60, 1, this);
                r.setBackgroundResource(R.mipmap.pict4);
                break;
        }
        jump = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.onBackPressed();
                break;
            case R.id.start:
                startState = true;
                kongFu.start();
                start.setVisibility(View.INVISIBLE);
                r.removeView(start);
                ImmersedStatusbarUtils.setViewMargin(words, false, 0, 0, marginTop - 10, 0);
                words.setVisibility(View.VISIBLE);
        }
    }

    public void refresh() {
        remind.setVisibility(View.VISIBLE);
        remindSeconds = 0;
        handler.postDelayed(runnable, 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int times = Utils.waitTime / 1000;
            if (remindSeconds < times) {
                remind.setText(times - remindSeconds + "秒后继续开始");
                remindSeconds++;
                handler.postDelayed(runnable, 1000);
            } else {
                remind.setVisibility(View.INVISIBLE);
                handler.removeCallbacks(runnable);
                kongFu.resumeThread();
            }
        }
    };

    @Override
    protected void onRestart() {
        if (!jump) {
            musc.resumeBackgroundMusic(this);
            if (kongFu != null && kongFu.isPause() && startState) {
                refresh();
                Log.e("onRestart", "执行唤醒");
            }
        } else {
            jump = false;
        }
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (!jump) {
            musc.pauseBackgroundMusic();
            if (!kongFu.isPause() && startState) {
                Log.e("Activity", "onPaused");
                kongFu.pauseThread();
            }
        } else {
            jump = false;
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        jump = true;
        musc.stopBackgroundMusic();
        kongFu.stopThread();
        startState = false;
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.anim_fenter, R.anim.anim_fexit);
        finish();
    }
}
