package com.shuaiyu.turntablecricle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

class TurntableView(context: Context) : View(context) {
    private val mColors = intArrayOf(
        Color.BLUE, Color.DKGRAY, Color.CYAN, Color.RED, Color.GREEN
    )
    var mPaint: Paint = Paint()
    var rectF: RectF = RectF()
    var w = 0
    var h = 0
    var currentStartAngle = 0f  //开始角度
    var r = 0f  //半径
    private var viewDatas //数据集
            : List<ViewData>? = null
    init {
        mPaint.run {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize = 30f
            isAntiAlias = true
        }
    }

    public fun setData(viewDatas: List<ViewData>){
        this.viewDatas=viewDatas
        initData()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        r= (w.coerceAtMost(h) / 2).toFloat()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //移动坐标到中间
        canvas?.translate((w / 2).toFloat(), (h / 2).toFloat())
        //设置将要用来画扇形的矩形的轮廓
        rectF.set(-r, -r, r, r)

        viewDatas?.forEach {
            mPaint.color = it.color;
            canvas?.drawArc(rectF, currentStartAngle, it.angle, true, mPaint);
            //绘制扇形上文字
            val textAngle: Float = currentStartAngle + it.angle / 2 //计算文字位置角度

            mPaint.setColor(Color.BLACK)
            val x =
                (r / 2 * Math.cos(textAngle * Math.PI / 180)).toFloat() //计算文字位置坐标

            val y = (r / 2 * Math.sin(textAngle * Math.PI / 180)).toFloat()
            mPaint.setColor(Color.BLACK) //文字颜色
            if(it.name!=null) {
                canvas?.drawText(it.name!!, x, y, mPaint);    //绘制文字
            }
            currentStartAngle += it.angle;


        }

    }

    private fun initData() {
        if (null == viewDatas || viewDatas!!.size == 0) {
            return
        }
        var sumValue = 0f //数值和
        for (i in viewDatas!!.indices) {
            val viewData = viewDatas!![i]
            sumValue += viewData.value
        }
        for (data in viewDatas!!) {
            val percentage = data.value / sumValue //计算百分比
            val angle = percentage * 360 //对应的角度
            data.percentage = percentage
            data.angle = angle
        }
    }
}