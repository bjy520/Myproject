package com.shuaiyu.businesscard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.shuaiyu.businesscard.base.BaseActivity
import com.shuaiyu.businesscard.moudle.BiliBiliSettingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initListener() {
        btn_button.setOnClickListener (this)
    }

    override fun initView() {
    }


    override fun getContextView(): Int =R.layout.activity_main


    override fun initData() {
    }

    override fun getExData() {
    }

    override fun setData() {
    }

    override fun onClick(v: View?) {
        Log.e("Tag", "onClick:    $v " )
        when(v?.id){
            R.id.btn_button -> {
                startActivity(Intent(this,BiliBiliSettingActivity::class.java))
            }

        }
    }
}
