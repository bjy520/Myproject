package com.shuaiyu.businesscard.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(),View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( getContextView())
        initView()
        initListener()
        getExData()
        initData()
    }
    abstract fun  initListener()
    abstract fun initView()
    abstract  fun getContextView():Int
    abstract fun initData()
    abstract fun getExData()
    abstract fun setData()


}