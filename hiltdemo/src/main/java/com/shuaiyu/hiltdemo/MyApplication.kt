package com.shuaiyu.hiltdemo

import android.app.Application
import com.shuaiyu.hiltdemo.hiltdemo.IProcessor
import com.shuaiyu.hiltdemo.hiltdemo.annotation.BindCai
import com.shuaiyu.hiltdemo.mydailidemo.ShowTextHaha
import com.shuaiyu.hiltdemo.mydailidemo.TextHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    @BindCai
    lateinit var iProcessor: IProcessor
    override fun onCreate() {
        super.onCreate()
        TextHelper.init(ShowTextHaha())
    }

    fun getProccessor() = iProcessor
}