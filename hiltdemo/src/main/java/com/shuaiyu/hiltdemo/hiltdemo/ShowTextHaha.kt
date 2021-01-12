package com.shuaiyu.hiltdemo.hiltdemo

import android.util.Log
import javax.inject.Inject


class ShowTextHaha : IProcessor {
    companion object{
        private const val TAG = "shuaiyu"
    }
    @Inject
    constructor(){

    }
    override fun setShowText() {
        Log.e(TAG, "haha" )
    }
}