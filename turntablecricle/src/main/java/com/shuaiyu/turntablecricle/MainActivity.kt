package com.shuaiyu.turntablecricle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.shuaiyu.turntablecricle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val contentView =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewData = ViewData()
        viewData.apply {
            value=30
            name="30%"
            color= resources.getColor(R.color.black)
        }
        val viewData1 = ViewData()
        viewData1.apply {
            value=20
            name="30%"
            color= resources.getColor(R.color.white)
        }
        val mutableListOf = mutableListOf<ViewData>()
        mutableListOf.add(viewData1)
        mutableListOf.add(viewData)
        val turntableView = TurntableView(this)
        turntableView.setData(mutableListOf)
        contentView.me.addView(turntableView)
    }
}