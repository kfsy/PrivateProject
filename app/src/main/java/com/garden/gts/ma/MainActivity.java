package com.garden.gts.ma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    Button recovery1, recovery2, recovery3, recovery4, options, assist, exit;
    private long exitTime = 0;
    private MusicServices musc;
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
        int[] id = {R.id.recovery1, R.id.recovery2, R.id.recovery3, R.id.recovery4, R.id.options, R.id.assist, R.id.exit};
        Button[] buttons = {recovery1, recovery2, recovery3, recovery4, options, assist, exit};
        for (int i = 0; i < 7; i++) {
            buttons[i] = (Button) findViewById(id[i]);
            buttons[i].setOnClickListener(this);
        }
        jump = false;
        Utils.checkData(sp);
        musc = MusicServices.getMusc();
        musc.playBackgroundMusic(this, R.raw.xiaozhiche);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recovery1:
                jump = true;
                musc.stopBackgroundMusic();
                Utils.kongFuStyle = 1;
                startActivity(new Intent(this, RecoveryActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                finish();
                break;
            case R.id.recovery2:
                jump = true;
                musc.stopBackgroundMusic();
                Utils.kongFuStyle = 2;
                startActivity(new Intent(this, RecoveryActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                finish();
                break;
            case R.id.recovery3:
                jump = true;
                musc.stopBackgroundMusic();
                Utils.kongFuStyle = 3;
                startActivity(new Intent(this, RecoveryActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                finish();
                break;
            case R.id.recovery4:
                jump = true;
                musc.stopBackgroundMusic();
                Utils.kongFuStyle = 4;
                startActivity(new Intent(this, RecoveryActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                finish();
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
                musc.stopBackgroundMusic();
                finish();
                System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                musc.stopBackgroundMusic();
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onRestart() {
        if (!jump) {
            musc.resumeBackgroundMusic();
        } else {
            jump = false;
        }
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (!jump) {
            musc.pauseBackgroundMusic();
        } else {
            jump = false;
        }
        super.onStop();
    }

}
