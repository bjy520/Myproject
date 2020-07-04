package com.shuaiyu.mypiano;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shuaiyu.mypiano.db.Dao;
import com.shuaiyu.mypiano.db.DaoUtils;
import com.shuaiyu.mypiano.main.KeyView;
import com.shuaiyu.mypiano.utils.SoudPoolsUtil;

import java.io.File;

import static com.shuaiyu.mypiano.utils.SoudPoolsUtil.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "MainActivity";
//    Gson gson = new Gson();
    boolean hasPermission;
    LinearLayout lin_keyView;
    KeyView mKeyView;
    EditText key_size, play_interval, play_time;
    Button btn_sure, btn_start_play, btn_key_set, btn_stop_play, btn_reset;
    TextView text_tp;
    int keysize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
        setDefaultDate();
        setAdapter();
        if (getPerssion()) {
//            Timer mTimer=new Timer();
//            mTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
////                    Main2Activity.startMain2Activity(MainActivity.this);
////                    finish();
//                }
//            },3000);
        }
        ;


    }

    private void setDefaultDate() {
        key_size.setText("6");
        play_interval.setText("1");
        play_time.setText("10");
        text_tp.setText("已储存声音" + DaoUtils.queryAll().size() + "条");
    }

    private void setAdapter() {
        btn_sure.setOnClickListener(this);
        btn_key_set.setOnClickListener(this);
        btn_start_play.setOnClickListener(this);
        btn_stop_play.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
    }

    int playtime = 20, interval = 1;
    int paly_state;//0  当前为播放状态  1设置状态

    private void setBtnText() {
        if (paly_state == 0) {
            paly_state = 1;
            btn_key_set.setText(R.string.set_state);
        } else {
            paly_state = 0;
            btn_key_set.setText(R.string.play_state);
            text_tp.setText("已储存声音" + DaoUtils.queryAll().size() + "条");
        }
        mKeyView.setState(paly_state);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                try {
                    keysize = Integer.valueOf(key_size.getText().toString().trim());
                } catch (Exception e) {
                    Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                }
                addView(keysize);
                break;
            case R.id.btn_key_set:
                if (mKeyView != null) {

                    setBtnText();
                }
                break;
            case R.id.btn_start_play:
                if (!TextUtils.isEmpty(play_interval.getText().toString().trim())) {
                    interval = Integer.valueOf(play_interval.getText().toString().trim());
                } else {
                    interval = 2;
                }
                try {
                    keysize = Integer.valueOf(key_size.getText().toString().trim());
                } catch (Exception e) {
                    Toast.makeText(this, "", Toast.LENGTH_LONG).show();
                }
                if (!TextUtils.isEmpty(play_time.getText().toString().trim())) {
                    playtime = Integer.valueOf(play_time.getText().toString().trim());
                } else {
                    interval = 2;
                }
                spu.stop = true;
                spu.randomPlay(interval, playtime, null, keysize, this);
                break;
            case R.id.btn_stop_play:
                spu.closeTimer();
                break;
            case R.id.btn_reset:
                for (Dao dao : DaoUtils.queryAll()) {
                    DaoUtils.deleteSound(dao.getId());
                }
                String file = Environment.getExternalStorageDirectory() + "/record/";
                File file2=new File(file);
                if(removeFile(file2)){
                    Toast.makeText(this,"删除成功",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this,"删除失败",Toast.LENGTH_LONG).show();
                }
                setDefaultDate();
                break;
        }

    }

        private boolean removeFile(File file){
            //如果是文件直接删除
            try{
                if(file.isFile()){
                    file.delete();
                    return true;
                }
                //如果是目录，递归判断，如果是空目录，直接删除，如果是文件，遍历删除
                if(file.isDirectory()){
                    File[] childFile = file.listFiles();
                    if(childFile == null || childFile.length == 0){
                        file.delete();
                        return true;
                    }
                    for(File f : childFile){
                        removeFile(f);
                    }
                    file.delete();
                }
                return true;
            }catch (Exception e){
                return false;
            }

        }

    SoudPoolsUtil spu;

    private void addView(int size) {
        lin_keyView.removeAllViews();
        mKeyView = new KeyView(getApplicationContext());
        mKeyView.setSize(keysize);
        lin_keyView.addView(mKeyView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (spu != null) {
            spu.closeTimer();
        }
    }

    private void init() {
        spu = getInstance(getApplicationContext());
    }

    private void initView() {
        lin_keyView = findViewById(R.id.lin_keyView);
        key_size = findViewById(R.id.key_size);
        play_interval = findViewById(R.id.play_interval);
        play_time = findViewById(R.id.play_time);
        btn_sure = findViewById(R.id.btn_sure);
        btn_start_play = findViewById(R.id.btn_start_play);
        btn_key_set = findViewById(R.id.btn_key_set);
        btn_stop_play = findViewById(R.id.btn_stop_play);
        btn_reset = findViewById(R.id.btn_reset);
        text_tp = findViewById(R.id.text_tp);
    }

    private boolean getPerssion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        ) {
//                ||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)  != PackageManager.PERMISSION_GRANTED//读取手机状态
//                ||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED//照相
//                ||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED //允许一个程序访问CellID或WiFi热点来获取粗略的位置
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)//允许一个程序访问精良位置(如GPS)

            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
//                    , Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            }, 1);
        } else {
            hasPermission = true;

        }
        return hasPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        hasPermission = true;
        for (int code : grantResults) {
            if (code != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
            }
        }
        if (checkPermissions()) {
//            Timer mTimer = new Timer();
//            mTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    Main2Activity.startMain2Activity(MainActivity.this);
//                    finish();
//                }
//            }, 3000);


//            try {
////                    String first= SharePreferenceUtils.getString(SplashActivity.this,SharePreferenceUtils.OLDVERSION,"0");
////                    PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
////                    if(!first.equals(packageInfo.versionName)){
////                        WelcomeActivity.startWelcomeActivity(SplashActivity.this);
////                        SharePreferenceUtils.putString(SplashActivity.this,SharePreferenceUtils.OLDVERSION,packageInfo.versionName);
////                    }else {
//////                    Intent intent=new Intent();
//////                    intent.setClass(SplashActivity.this,MainActivity.class);
//////                    startActivity(intent);
////                    }
//            } catch (Exception e) {
//            }
        }


    }

    AlertDialog mAlertDialog;

    public boolean checkPermissions() {
        if (!hasPermission) {
            Toast.makeText(this, "请开启应用权限", Toast.LENGTH_LONG).show();
            if (mAlertDialog == null) {
                mAlertDialog = new AlertDialog.Builder(this)
                        .setTitle("当前应用缺少必要权限")
                        .setMessage("请点击设置-权限-打开所需权限。")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAlertDialog.cancel();
                                finish();
                            }
                        })
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toSelfSetting(MainActivity.this);
                            }
                        })
                        .create();
            }
            if (!mAlertDialog.isShowing()) {
                mAlertDialog.show();
            }


            return false;
        }
        return true;
    }

    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }


    private String getChannel(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (isOnKeyBacking) {
            handler.removeCallbacks(onBackTimeRunnable);
            // 退出
            AppExit();
            finish();
            return true;
        } else {
            isOnKeyBacking = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            handler.postDelayed(onBackTimeRunnable, 2000);
            return true;
        }
    }

    private boolean isOnKeyBacking;
    private Handler handler = new Handler();
    private Runnable onBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            isOnKeyBacking = false;
        }
    };

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            // 杀死该应用进程
            Process.killProcess(Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
