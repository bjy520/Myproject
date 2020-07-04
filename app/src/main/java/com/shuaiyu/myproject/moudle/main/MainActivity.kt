package com.shuaiyu.myproject.moudle.main

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.shuaiyu.myproject.R
import com.shuaiyu.myproject.utils.CalendarUtil
import com.shuaiyu.myproject.utils.MyUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //目前所使用的权限

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyUtils.getPermissions(this)
        button_me.setOnClickListener {
            val checkAndAddCalendarAccount = CalendarUtil.checkAndAddCalendarAccount(this)
            Log.e("TAG", "onCreate: $checkAndAddCalendarAccount" )
            var result=CalendarUtil. insertCalendarEvent(this,"标题","asdasdasdasd",1593262800000,1593263460000)
//            Log.e("TAG", "onCreate: $result" )
//            CalendarUtil.deleteCalendarEvent(this,"标题")
        }
    }
}