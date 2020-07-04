package com.shuaiyu.mypiano.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;


import com.shuaiyu.mypiano.R;
import com.shuaiyu.mypiano.db.Dao;
import com.shuaiyu.mypiano.db.DaoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SoudPoolsUtil {
   public static boolean stop = true;
    private volatile static SoudPoolsUtil mSingleInstance = null;

    public static SoudPoolsUtil getInstance(Context context) {
        if (mSingleInstance == null) {
            synchronized (SoudPoolsUtil.class) {
                if (mSingleInstance == null) {
                    mSingleInstance = new SoudPoolsUtil(context);
                }
            }
        }
        return mSingleInstance;
    }

    SoundPool soundPool;
    Context context;
    int size;
    List<Integer> sounds;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // 要做的事情

            if (size ==1) {
                playPianoSound();
            } else {
               List<Dao> daos= DaoUtils.queryAll();
                Random random2 = new Random();
                int ran2;
                ran2 = random2.nextInt(daos.size());
                play(context, 0, daos.get(ran2).getSoundPath(), 1);
            }

            super.handleMessage(msg);

        }
    };

    private void playPianoSound(){
        Random random2 = new Random();
        int ran2;
        ran2 = random2.nextInt(6);//0-size  不包括size   [0,size);
        if (sounds != null && sounds.size() == 0) {
            sounds.add(R.raw.a14);
            sounds.add(R.raw.a21);
            sounds.add(R.raw.a22);
            sounds.add(R.raw.a31);
            sounds.add(R.raw.a32);
            sounds.add(R.raw.a76);
        }
        play(context, sounds.get(ran2), null, 0);
    }
    public void closeTimer() {
        stop = false;
    }

    private SoudPoolsUtil(Context context) {
        this.context = context;
        sounds = new ArrayList<>();
    }

    //实例化SoundPool
    public void randomPlay(final int interval, final int time, List<String> paths, int size, final Context context) {
        this.size = size;
        final Random random2 = new Random();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sum = 0;
                while (stop) {
                    try {
                        int ran = random2.nextInt(1+interval);
                        sum = ran + sum;
                        if (sum >= time) {
                            stop = false;
                        }
                        Thread.sleep(ran * 1000);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void play(Context context, int res, String path, int playType) {
        //playType   0 通过一个资源ID  1通过指定的路径加载 2通过一个AssetFileDescriptor对象  3  通过FileDescriptor加载
        //sdk版本21是SoundPool 的一个分水岭
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入最多播放音频数量,
            builder.setMaxStreams(2);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            /**
             * 第一个参数：int maxStreams：SoundPool对象的最大并发流数
             * 第二个参数：int streamType：AudioManager中描述的音频流类型
             *第三个参数：int srcQuality：采样率转换器的质量。 目前没有效果。 使用0作为默认值。
             */
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        //可以通过四种途径来记载一个音频资源：
        //1.通过一个AssetFileDescriptor对象
        //int load(AssetFileDescriptor afd, int priority)
        //2.通过一个资源ID
        //int load(Context context, int resId, int priority)
        //3.通过指定的路径加载
        //int load(String path, int priority)
        //4.通过FileDescriptor加载
        //int load(FileDescriptor fd, long offset, long length, int priority)
        //声音ID 加载音频资源,这里用的是第二种，第三个参数为priority，声音的优先级*API中指出，priority参数目前没有效果，建议设置为1。
        switch (playType) {
            case 0:
                final int voiceId = soundPool.load(context, res, 1);
                //异步需要等待加载完成，音频才能播放成功
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        if (status == 0) {
                            //第一个参数soundID
                            //第二个参数leftVolume为左侧音量值（范围= 0.0到1.0）
                            //第三个参数rightVolume为右的音量值（范围= 0.0到1.0）
                            //第四个参数priority 为流的优先级，值越大优先级高，影响当同时播放数量超出了最大支持数时SoundPool对该流的处理
                            //第五个参数loop 为音频重复播放次数，0为值播放一次，-1为无限循环，其他值为播放loop+1次
                            //第六个参数 rate为播放的速率，范围0.5-2.0(0.5为一半速率，1.0为正常速率，2.0为两倍速率)
                            soundPool.play(voiceId, 1, 1, 1, 0, 1);
                        }
                    }
                });
                break;
            case 1:
                final int voiceId2 = soundPool.load(path, 1);
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        if (status == 0) {
                            //第一个参数soundID
                            //第二个参数leftVolume为左侧音量值（范围= 0.0到1.0）
                            //第三个参数rightVolume为右的音量值（范围= 0.0到1.0）
                            //第四个参数priority 为流的优先级，值越大优先级高，影响当同时播放数量超出了最大支持数时SoundPool对该流的处理
                            //第五个参数loop 为音频重复播放次数，0为值播放一次，-1为无限循环，其他值为播放loop+1次
                            //第六个参数 rate为播放的速率，范围0.5-2.0(0.5为一半速率，1.0为正常速率，2.0为两倍速率)
                            soundPool.play(voiceId2, 1, 1, 1, 0, 1);
                        }
                    }
                });
                break;
            default:


        }
    }
}
