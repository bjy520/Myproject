package com.shuaiyu.mymoveimage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
// 做一个毛玻璃效果
class MainActivity : AppCompatActivity() {
    var myMoveView: MyMoveView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Glide.with(this).load(R.drawable.preview).into(img_bottom)
        myMoveView= MyMoveView(this)
        lin_top.removeAllViews()
//        lin_top.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        lin_top.addView(myMoveView)
//        myMoveView?.setViewData(0f,0f,30f)
    }
}
