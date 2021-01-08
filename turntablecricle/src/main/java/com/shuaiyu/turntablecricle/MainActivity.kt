package com.shuaiyu.turntablecricle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.shuaiyu.turntablecricle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var asd: MutableList<ViewData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val contentView =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)


        contentView.apply {
            btnAdd.setOnClickListener {
                val viewData = ViewData()
                viewData.name = contentView.edtNum.text.toString()
                asd.add(viewData)
                contentView.turntable.setData(asd)
            }
            btnClean.setOnClickListener{
                asd.clear()
                contentView.turntable.setData(asd)

            }
            turntable.setListener(object :TurntableView.getPointText{
                override fun getText(pointText: String?) {
                    Toast.makeText(this@MainActivity,pointText,Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}