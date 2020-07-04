package com.shuaiyu.mypiano.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.shuaiyu.mypiano.R;
import com.shuaiyu.mypiano.db.Dao;
import com.shuaiyu.mypiano.db.DaoUtils;
import com.shuaiyu.mypiano.utils.RecordUtil;
import com.shuaiyu.mypiano.utils.SoudPoolsUtil;


public class KeyView extends View {
    Context  context;
    private int mwidth, mheight;
    Paint mKeyPaint;
    Paint mButtonPaint;
    Canvas mCanvas;
    int settingState;//0 播放状态  1设置状态  弹出录音或者选取录音
    int size=1;
    public void setSize(int size){
        this.size=size;
        invalidate();
    }
    public KeyView(Context context) {
        super(context);
        this.context=context;
        init(context,null);
    }
    public KeyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mwidth=MeasureSpec.getSize(widthMeasureSpec);
        mheight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
    SoudPoolsUtil spu;
    private void init(Context context,@Nullable AttributeSet attrs){
        mKeyPaint = new Paint();
        mKeyPaint.setAntiAlias(true);
        mKeyPaint.setStyle(Paint.Style.FILL);//实心矩形框
        mKeyPaint.setColor(Color.BLACK);
        mButtonPaint=new Paint();
        spu = SoudPoolsUtil.getInstance(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas=canvas;
        drawKeys();

    }

    private void drawKeys() {
        if (settingState == 0) {

            for (int i=0;i<size;i++){
                if(i%2==0){
                    mKeyPaint.setColor(Color.BLACK);
                }else {
                    mKeyPaint.setColor(Color.WHITE);
                }
                mCanvas.drawRect((mwidth/size)*i,0,(mwidth/size)*i+(mwidth/size),mheight,mKeyPaint);
            }
            mCanvas.save();
        }else {
            for (int i=0;i<size;i++){
                if(i%2==0){
                    mKeyPaint.setColor(Color.BLACK);
                }else {
                    mKeyPaint.setColor(Color.WHITE);
                }
                mCanvas.drawRect((mwidth/size)*i,0,(mwidth/size)*i+(mwidth/size),mheight,mKeyPaint);
            }
            mButtonPaint.setColor(Color.BLACK);
            mButtonPaint.setTextSize(50);
            for (int i=0;i<size;i++){
                if(i%2==0){
                    mButtonPaint.setColor(Color.WHITE);
                }else {
                    mButtonPaint.setColor(Color.BLACK);
                }
                mCanvas.drawText("录音",(mwidth/size)*(i+1)-((mwidth/size)/2)-50,mheight/2,mButtonPaint);
            }
        }
    }

    public void setState(int state){
        settingState=state;
        invalidate();
    }
    RecordUtil re;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX=0;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float downX=event.getRawX();
                float downY=event.getRawY();
                if(settingState==0){
                    //播放状态  点击会播放相对应的音乐
                    eventX=(int) downX/(mwidth / size);
                    if(downY>(mheight/2)) {
                        for (int i = 0; i < size; i++) {
                            if (i % 2 == 0) {
                                if ((mwidth / size) * i < downX && downX < ((mwidth / size) * i + (mwidth / size))) {
                                    if(DaoUtils.getInstance().querySound(eventX)!=null&&DaoUtils.getInstance().querySound(eventX).size()==1){
                                        spu.play(context,0, DaoUtils.getInstance().querySound(eventX).get(0).getSoundPath(), 1);
                                    }else {
                                        spu.play(context, R.raw.jizhongshenti, "", 0);
                                    }

                                }
                            }else {
                                if ((mwidth / size) * i < downX && downX < ((mwidth / size) * i + (mwidth / size))) {
                                    if(DaoUtils.getInstance().querySound(eventX)!=null&&DaoUtils.getInstance().querySound(eventX).size()==1){
                                        spu.play(context,0, DaoUtils.getInstance().querySound(eventX).get(0).getSoundPath(), 1);
                                    }else {
                                        spu.play(context, R.raw.shache, "", 0);
                                    }

                                }
                            }
                        }
                    }
                    Log.e("keyView", "onTouchEvent: "+downX+"  "+downY+"---"+mwidth+"  "+mheight);
                }else if(settingState==1){
                    //设置状态  点击会更改对应的音乐
//                    Toast.makeText(context,"setting",Toast.LENGTH_LONG).show();

                    re=new RecordUtil();
                    re.startRecord();
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                Toast.makeText(context,"您已取消此次录音",Toast.LENGTH_LONG).show();
//////
//////                re.cancelRecord();
                break;
            case MotionEvent.ACTION_UP:
                if(settingState==0){
                    return true;
                }
                float downXx=event.getRawX();
                final int eventXx= (int)downXx/(mwidth / size);
                re.setOnAudioStatusUpdateListener(new RecordUtil.OnAudioStatusUpdateListener() {
                    @Override
                    public void onUpdate(double db, long time) {
                        Log.e("hahaha", "onStop: "+ db+"-------"+time);
                    }

                    @Override
                    public void onStop(String filePath) {
                        Log.e("hahaha", "onStop: "+ filePath);
                        if(DaoUtils.getInstance().querySound(eventXx)!=null&&DaoUtils.getInstance().querySound(eventXx).size()>0){
                          int size=  DaoUtils.getInstance().querySound(eventXx).size();
                            for (int i=0;i<size;i++){
                                Dao dao=  DaoUtils.getInstance().querySound(eventXx).get(i);
                                DaoUtils.getInstance().deleteSound(dao.getId());
                            }
                        }
                        Dao dao=new Dao();
                        dao.setSoundPath(filePath);
                        dao.setTag(eventXx);
                        DaoUtils.getInstance().insertSound(dao);
                    }
                });
                re.stopRecord();
                break;
        }


        return true;
    }
}
