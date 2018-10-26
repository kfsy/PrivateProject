package com.garden.gts.ma;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.garden.gts.ma.util.ImmersedStatusbarUtils;
import com.garden.gts.ma.util.MusicServices;
import com.garden.gts.ma.util.Utils;

public class Options extends AppCompatActivity implements View.OnClickListener {

    Button back;
    private MusicServices music;
    private boolean jump;
    private SharedPreferences sp;
    private EditText editText0, editText1, editText2, editText3, editText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("meditationAssistants", MODE_PRIVATE);
        setContentView(R.layout.activity_options);
        int marginTop = Utils.getWindowHeight(this) / 5;
        ImmersedStatusbarUtils.setViewMargin(findViewById(R.id.optionsLL), false, 0, 0, marginTop, 0);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        editText0= (EditText) findViewById(R.id.editText0);
        editText1= (EditText) findViewById(R.id.editText1);
        editText2= (EditText) findViewById(R.id.editText2);
        editText3= (EditText) findViewById(R.id.editText3);
        editText4= (EditText) findViewById(R.id.editText4);
        editText0.setOnClickListener(this);
        editText1.setOnClickListener(this);
        editText2.setOnClickListener(this);
        editText3.setOnClickListener(this);
        editText4.setOnClickListener(this);
        editText0.setText(String.valueOf(Utils.waitTime/1000));
        editText1.setText(String.valueOf(Utils.restTime/1000));
        editText2.setText(String.valueOf(Utils.r2Time));
        editText3.setText(String.valueOf(Utils.r3Time));
        editText4.setText(String.valueOf(Utils.r4Time));
        music = MusicServices.getMusc();
        jump = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.onBackPressed();
                break;
        }
    }

    public void saveData() {
        Utils.waitTime = Integer.parseInt(String.valueOf(editText0.getText()))*1000;
        Utils.restTime = Integer.parseInt(String.valueOf(editText1.getText()))*1000;
        Utils.r2Time = Integer.parseInt(String.valueOf(editText2.getText()));
        Utils.r3Time = Integer.parseInt(String.valueOf(editText3.getText()));
        Utils.r4Time = Integer.parseInt(String.valueOf(editText4.getText()));
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("waitTime", Utils.waitTime);
        editor.putInt("restTime", Utils.restTime);
        editor.putInt("r2Time", Utils.r2Time);
        editor.putInt("r3Time", Utils.r3Time);
        editor.putInt("r4Time", Utils.r4Time);
        editor.apply();
        Toast.makeText(Options.this, "设置已保存", Toast.LENGTH_SHORT).show();
    }

    private boolean isChanged() {
        return !String.valueOf(editText0.getText()).equals(String.valueOf(Utils.waitTime / 1000))
                || (!String.valueOf(editText1.getText()).equals(String.valueOf(Utils.restTime / 1000)))
                || (!String.valueOf(editText2.getText()).equals(String.valueOf(Utils.r2Time)))
                || (!String.valueOf(editText3.getText()).equals(String.valueOf(Utils.r3Time)))
                || (!String.valueOf(editText4.getText()).equals(String.valueOf(Utils.r4Time)));

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

    @Override
    public void onBackPressed() {
        if (isChanged()) {
            new AlertDialog.Builder(Options.this).
                    setTitle("尚有未保存的设置，是否保存？").
                    setMessage("点击对话框外返回修改\n点击取消不保存返回主界面").
                    setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //保存设置
                            saveData();
                            jump = true;
                            Options.this.finish();
                        }
                    }).
                    setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            jump = true;
                            Options.this.finish();
                        }
                    }).create().show();
        } else {
            jump = true;
            Log.e("restTime", Utils.restTime + " ");
            Log.e("waitTimes", Utils.waitTime + "");
            Log.e("r2", Utils.r2Time + "");
            Log.e("r3", Utils.r3Time + "");
            Log.e("r4", Utils.r4Time + "");
            super.onBackPressed();
        }
    }
}
