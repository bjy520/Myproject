package com.shuaiyu.myproject

import android.app.Application
import com.hjq.toast.ToastUtils

class MyApp :Application() {
    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this);
    }
}