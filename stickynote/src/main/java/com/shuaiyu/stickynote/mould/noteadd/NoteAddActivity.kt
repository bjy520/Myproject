package com.shuaiyu.stickynote.mould.noteadd

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shuaiyu.stickynote.R
import com.shuaiyu.stickynote.view.AddView
import kotlinx.android.synthetic.main.activity_note_add.*

class NoteAddActivity : AppCompatActivity() {

    companion object fun start(context: Context) {
        val starter = Intent(context, NoteAddActivity::class.java)
        context.startActivity(starter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add)
        lin_me.removeAllViews()
        var addView: AddView = AddView(this)
        lin_me.addView(addView)
        btn_me.setOnClickListener {
            var addView: AddView = AddView(this)
            lin_me.addView(addView)
        }

    }
}