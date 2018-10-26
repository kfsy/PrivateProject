package com.garden.gts.ma;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.garden.gts.ma.util.ImmersedStatusbarUtils;
import com.garden.gts.ma.util.MusicServices;
import com.garden.gts.ma.util.Utils;

public class Options extends AppCompatActivity implements View.OnClickListener {

    Button soundSwitch, r1Setting, r2Setting, r3Setting, r4Setting, waitSetting, back;
    private MusicServices music;
    private boolean jump;
    public PickerView pickerView;
    private String chooseNumber;
    private SharedPreferences sp;
    private boolean havechanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("meditationAssistants", MODE_PRIVATE);
        setContentView(R.layout.activity_options);
        soundSwitch = (Button) findViewById(R.id.soundSwitch);
        r1Setting = (Button) findViewById(R.id.r1Setting);
        r2Setting = (Button) findViewById(R.id.r2Setting);
        r3Setting = (Button) findViewById(R.id.r3Setting);
        r4Setting = (Button) findViewById(R.id.r4Setting);
        waitSetting = (Button) findViewById(R.id.waitTime);
        back = (Button) findViewById(R.id.back);
        soundSwitch.setOnClickListener(this);
        r1Setting.setOnClickListener(this);
        r2Setting.setOnClickListener(this);
        r3Setting.setOnClickListener(this);
        r4Setting.setOnClickListener(this);
        waitSetting.setOnClickListener(this);
        back.setOnClickListener(this);
        if (Utils.soundTurn) {
            soundSwitch.setText("背景音乐：开");
        } else {
            soundSwitch.setText("背景音乐：关");
        }
        int marginTop = Utils.getWindowHeight(this) / 4;
        ImmersedStatusbarUtils.setViewMargin(soundSwitch, false, 0, 0, marginTop - 10, 0);
        music = MusicServices.getMusc();
        jump = false;
        havechanged = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                        music.resumeBackgroundMusic();
                    }
                    Thread.sleep(500);
                    soundSwitch.setEnabled(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                havechanged=true;
                break;
            case R.id.back:
                this.onBackPressed();
                break;
            case R.id.r1Setting:
                dialog(1);
                break;
            case R.id.r2Setting:
                dialog(2);
                break;
            case R.id.r3Setting:
                dialog(3);
                break;
            case R.id.r4Setting:
                dialog(4);
                break;
            case R.id.waitTime:
                dialog(5);
                break;
        }

    }

    public void dialog(final int kongFuStyle) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //初始化自定义布局参数
        LayoutInflater layoutInflater = getLayoutInflater();
        final View customLayout = layoutInflater.inflate(R.layout.custom_dialog, (ViewGroup) findViewById(R.id.customDialog));
        // 为对话框设置视图
        builder.setView(customLayout);
        pickerView = (PickerView) customLayout.findViewById(R.id.picker);
        TextView title = (TextView) customLayout.findViewById(R.id.titleText);
        TextView units = (TextView) customLayout.findViewById(R.id.units);
        switch (kongFuStyle) {
            case 1:
                title.setText("起身/俯身中间动作时间");
                units.setText("秒");
                //为滚动选择器设置数据
                pickerView.setData(Utils.restTimeList);
                break;
            case 2:
                Log.e("text2Setting", "设置数据");
                title.setText("选择时间");
                units.setText("分钟");
                pickerView.setData(Utils.minuteList);
                break;
            case 3:
                title.setText("选择时间");
                units.setText("分钟");
                pickerView.setData(Utils.minuteList);
                break;
            case 4:
                title.setText("选择时间");
                units.setText("分钟");
                pickerView.setData(Utils.minuteList);
                break;
            case 5:
                title.setText("选择时间 ");
                units.setText("秒");
                pickerView.setData(Utils.waitTimeList);
                break;
        }

        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                Log.i("tag", "选择了" + text);
                chooseNumber = text;
            }
        });
        final int[] time = {0};
        //对话框的确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (kongFuStyle) {
                    case 1:
                        Utils.restTime = Integer.valueOf(chooseNumber) * 1000;
                        havechanged = true;
                        break;
                    case 2:
                        time[0] = Integer.valueOf(chooseNumber);
                        if (Utils.r2Time != time[0]) {
                            Utils.r2Time = Integer.valueOf(chooseNumber);
                            havechanged = true;
                        }
                        break;
                    case 3:
                        time[0] = Integer.valueOf(chooseNumber);
                        if (Utils.r3Time != time[0]) {
                            Utils.r3Time = Integer.valueOf(chooseNumber);
                            havechanged = true;
                        }
                        break;
                    case 4:
                        time[0] = Integer.valueOf(chooseNumber);
                        if (Utils.r4Time != time[0]) {
                            Utils.r4Time = Integer.valueOf(chooseNumber);
                            havechanged = true;
                        }
                        break;
                    case 5:
                        time[0]=Integer.valueOf(chooseNumber) * 1000;
                        if(Utils.waitTime!=time[0]) {
                            Utils.waitTime = time[0] ;
                            havechanged=true;
                        }
                        break;
                }
            }
        });
        //对话框的取消按钮
        builder.setNegativeButton("取消", null);
        builder.setCancelable(false);
        //显示对话框
        builder.show();
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
        Log.e("restTime", Utils.restTime + " ");
        Log.e("waitTimes", Utils.waitTime + "");
        Log.e("r2", Utils.r2Time + "");
        Log.e("r3", Utils.r3Time + "");
        Log.e("r4", Utils.r4Time + "");
        if(havechanged) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("sound",Utils.soundTurn);
            editor.putInt("waitTime",Utils.waitTime);
            editor.putInt("restTime",Utils.restTime);
            editor.putInt("r2Time",Utils.r2Time);
            editor.putInt("r3Time",Utils.r3Time);
            editor.putInt("r4Time",Utils.r4Time);
            editor.apply();
            Toast.makeText(Options.this, "设置已保存", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }
}
