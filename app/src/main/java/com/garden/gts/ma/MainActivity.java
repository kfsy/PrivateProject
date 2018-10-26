package com.garden.gts.ma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.garden.gts.ma.util.ImmersedStatusbarUtils;
import com.garden.gts.ma.util.MusicServices;
import com.garden.gts.ma.util.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Context mContext;
    Button recovery1, recovery2, recovery3, recovery4, soundSwitch, options, assist, exit;
    private long exitTime = 0;
    private MusicServices music;
    private SharedPreferences sp;
    //是否为跳转
    private boolean jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("meditationAssistants", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        TextView title = (TextView) findViewById(R.id.Top);
        int marginTop = Utils.getWindowHeight(this) / 12;
        ImmersedStatusbarUtils.setViewMargin(title, false, 0, 0, marginTop, 0);

        recovery1 = (Button)findViewById(R.id.recovery1);
        recovery2 = (Button)findViewById(R.id.recovery2);
        recovery3 = (Button)findViewById(R.id.recovery3);
        recovery4 = (Button)findViewById(R.id.recovery4);
        soundSwitch = (Button)findViewById(R.id.soundSwitch);
        options = (Button)findViewById(R.id.options);
        assist = (Button)findViewById(R.id.assist);
        exit = (Button)findViewById(R.id.exit);
        recovery1.setOnClickListener(this);
        recovery2.setOnClickListener(this);
        recovery3.setOnClickListener(this);
        recovery4.setOnClickListener(this);
        soundSwitch.setOnClickListener(this);
        options.setOnClickListener(this);
        assist.setOnClickListener(this);
        exit.setOnClickListener(this);
        Utils.checkData(sp);
        if (Utils.soundTurn) {
            soundSwitch.setText("背景音乐：开");
        } else {
            soundSwitch.setText("背景音乐：关");
        }
        jump = false;
        Log.e("音乐开关",Utils.soundTurn+"");
        music = MusicServices.getMusc();
        music.playBackgroundMusic(this, R.raw.xiaozhiche);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recovery1:
                jump = true;
                music.stopBackgroundMusic();
                Utils.kongFuStyle = 1;
                startActivity(new Intent(this, RecoveryActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                finish();
                break;
            case R.id.recovery2:
                jump = true;
                music.stopBackgroundMusic();
                Utils.kongFuStyle = 2;
                startActivity(new Intent(this, RecoveryActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                finish();
                break;
            case R.id.recovery3:
                jump = true;
                music.stopBackgroundMusic();
                Utils.kongFuStyle = 3;
                startActivity(new Intent(this, RecoveryActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                finish();
                break;
            case R.id.recovery4:
                jump = true;
                music.stopBackgroundMusic();
                Utils.kongFuStyle = 4;
                startActivity(new Intent(this, RecoveryActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                finish();
                break;
            case R.id.soundSwitch:
                try {
                    soundSwitch.setEnabled(false);
                    if (Utils.soundTurn) {
                        soundSwitch.setText("背景音乐：关");
                        music.stopBackgroundMusic();
                        Utils.soundTurn = false;
                    } else {
                        Utils.soundTurn = true;
                        soundSwitch.setText("背景音乐：开");
                        music.resumeBackgroundMusic(this);
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("sound", Utils.soundTurn);
                    editor.apply();
                    Thread.sleep(500);
                    soundSwitch.setEnabled(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.options:
                jump = true;
                startActivity(new Intent(this, Options.class));
                overridePendingTransition(R.anim.anim_tenter, R.anim.anim_texit);
                break;
            case R.id.assist:
                jump = true;
                startActivity(new Intent(this, Assist.class));
                break;
            case R.id.exit:
                exitProcess();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                exitProcess();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exitProcess(){
        music.stopBackgroundMusic();
        finish();
        System.exit(0);
    }

    @Override
    protected void onRestart() {
        if (!jump) {
            music.resumeBackgroundMusic(this);
        } else {
            jump = false;
        }
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (!jump) {
            music.pauseBackgroundMusic();
        } else {
            jump = false;
        }
        super.onStop();
    }

}
