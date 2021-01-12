package com.shuaiyu.hiltdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.shuaiyu.hiltdemo.mydailidemo.TextHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    fun click(view: View) = when (view.id) {
        R.id.btn_top ->
            TextHelper.getIProcessor().setShowText()
        else -> {
            (application as MyApplication).getProccessor().setShowText()
        }
    }
}