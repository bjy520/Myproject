package com.shuaiyu.turntablecricle

import android.graphics.Color.red
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.shuaiyu.turntablecricle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var asd: MutableList<ViewData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val contentView =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)


        contentView.btnAdd.setOnClickListener {
            val viewData = ViewData()
            viewData.name = contentView.edtNum.text.toString()
            asd.add(viewData)
            contentView.turntable.setData(asd)
        }
        contentView.btnClean.setOnClickListener{
            asd.clear()
            contentView.turntable.setData(asd)
        }
    }
}