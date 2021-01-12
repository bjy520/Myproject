package com.shuaiyu.hiltdemo.mydailidemo

import android.util.Log

class ShowTextHaha : IProcessor {
    companion object{
        private const val TAG = "shuaiyu"
    }
    override fun setShowText() {
        Log.e(TAG, "haha" )
    }
}