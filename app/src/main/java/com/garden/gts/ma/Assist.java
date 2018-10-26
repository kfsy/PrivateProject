package com.garden.gts.ma;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.garden.gts.ma.util.ImmersedStatusbarUtils;
import com.garden.gts.ma.util.MusicServices;
import com.garden.gts.ma.util.Utils;

public class Assist extends AppCompatActivity implements View.OnClickListener {

    private MusicServices music;
    private boolean jump;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("meditationAssistants", MODE_PRIVATE);
        setContentView(R.layout.activity_assist);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        int marginTop = Utils.getWindowHeight(this) / 4;
        ImmersedStatusbarUtils.setViewMargin(reset, false, 0, 0, marginTop - 10, 0);
        music = MusicServices.getMusc();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.onBackPressed();
                break;
            case R.id.reset:
                Dialog alertDialog = new AlertDialog.Builder(Assist.this).
                        setTitle("确定要恢复所有设置吗？").
                        setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.apply();
                                Toast.makeText(Assist.this, "设置数据清除完毕", Toast.LENGTH_SHORT).show();
                            }
                        }).
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).
                        create();
                alertDialog.show();
                break;
        }
    }

    @Override
    protected void onRestart() {
        if (!jump) {
            music.resumeBackgroundMusic();
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

    @Override
    public void onBackPressed() {
        jump = true;
        super.onBackPressed();
    }
}
