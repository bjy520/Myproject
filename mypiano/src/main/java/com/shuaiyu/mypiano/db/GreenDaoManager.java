package com.shuaiyu.mypiano.db;

import android.database.sqlite.SQLiteDatabase;

import com.shuaiyu.mypiano.MyApp;


public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static GreenDaoManager mInstance; //单例

    private GreenDaoManager(){
        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(MyApp.getContext(), "key.db");//此处为自己需要处理的表
//            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDb());
//            mDaoSession = mDaoMaster.newSession();
            SQLiteDatabase db = devOpenHelper.getWritableDatabase();

            // encrypted SQLCipher database
            // note: you need to add SQLCipher to your dependencies, check the build.gradle file
            // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
            // Database db = helper.getEncryptedWritableDb("encryption-key");

            mDaoSession = new DaoMaster(db).newSession();
        }
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {//保证异步处理安全操作

                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }
    public DaoSession getSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
