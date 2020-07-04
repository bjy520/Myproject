package com.shuaiyu.myproject.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils
import com.shuaiyu.myproject.moudle.main.MainActivity

object MyUtils {
    //目前所使用的权限
    private val permission =
        arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR)


    fun getPermissions(activity: Activity) {
        XXPermissions.with(activity)
            .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
            //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES) //支持请求6.0悬浮窗权限8.0请求安装权限

            .permission(Permission.Group.CALENDAR) //不指定权限则自动获取清单中的危险权限
            .request(object : OnPermission {
                override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                    if (quick) {
                        ToastUtils.show("被永久拒绝授权，请手动授予权限");
                        //如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(activity);
                    } else {
                        ToastUtils.show("获取权限失败");
                    }
                }

                override fun hasPermission(granted: MutableList<String>?, all: Boolean) {
                    if (all) {
                    } else {
                        ToastUtils.show("获取权限成功，部分权限未正常授予");
                    }
                }
            })
    }
    // 还没试过多权限
    fun ishavePermissions(context: Context): Boolean {
        if (XXPermissions.hasPermission(context, this.permission)) {
            return true
        }
        return false
    }




}