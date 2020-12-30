package com.shuaiyu.turntablecricle

import android.graphics.Color.red
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
            color= resources.getColor(R.color.asd)
        }
        val viewData1 = ViewData()
        viewData1.apply {
            value=20
            name="30%"
            color= resources.getColor(R.color.yellow)
        }
        val listOf = listOf(viewData, viewData1)
        val mutableListOf = mutableListOf<ViewData>()
        mutableListOf.add(viewData1)
        mutableListOf.add(viewData)
        val turntableView = TurntableView(this)
        turntableView.setData(listOf)
        contentView.me.addView(turntableView)
    }
}