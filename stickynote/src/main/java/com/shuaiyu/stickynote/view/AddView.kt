package com.shuaiyu.stickynote.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.shuaiyu.stickynote.Bean.AddViewBean
import com.shuaiyu.stickynote.R

class AddView : View {
    private lateinit var edtMe: EditText
    private lateinit var switchMe: EditText
    private lateinit var mAddViewBean: AddViewBean

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context?) {
        val inflate = LayoutInflater.from(context).inflate(R.layout.item_addview, null)
        edtMe = inflate.findViewById<EditText>(R.id.edt_me)
        switchMe = inflate.findViewById<EditText>(R.id.switch_me)
    }


    fun getViewData(): AddViewBean {
        mAddViewBean = AddViewBean(null, false);
        mAddViewBean.isSuccess = switchMe.isActivated
        mAddViewBean.tag = edtMe.text.toString()
        return mAddViewBean
    }
}