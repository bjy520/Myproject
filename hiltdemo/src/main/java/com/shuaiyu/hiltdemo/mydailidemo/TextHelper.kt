package com.shuaiyu.hiltdemo.mydailidemo

object  TextHelper : IProcessor {
    private lateinit var iProcessor: IProcessor

    fun init(iProcessor: IProcessor){
        TextHelper.iProcessor =iProcessor
    }
     
    fun getIProcessor(): IProcessor = iProcessor
    override fun setShowText() {
        iProcessor.setShowText()
    }
}