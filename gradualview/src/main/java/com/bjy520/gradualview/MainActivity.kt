package com.bjy520.gradualview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    val colors = arrayListOf<String>("#FFAAdd","#335657","#123567")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intArray = IntArray(colors.size)
        colors.forEachIndexed { index, s ->
            intArray[index] = Color.parseColor(s)
        }
        findViewById<NenoTextview>(R.id.n1).apply {
            setText("帅玉")
            setColor(intArray)
            setTextColor(Color.BLUE)
        }
        findViewById<NenoTextview>(R.id.n2).apply {
            setHasColors(false)
            setText("帅玉")
            setColor(intArray)
        }
        findViewById<NenoTextview>(R.id.n3).apply {
            setHasColors(false)
            setText("帅玉")
            setColor(intArray)
        }
        findViewById<NenoTextview>(R.id.n4).apply {
            setText("帅玉")
            setHasColors(true,intArray)
            setSlide(true)
            setColor(intArray)
        }
        findViewById<NenoTextview>(R.id.n5).apply {
            setText("帅玉")
            setHasColors(true,intArray)
            setSlide(true)
            setTextSize(20f)
            setColor(intArray)
        }
    }
}