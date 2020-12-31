package com.shuaiyu.turntablecricle

import android.animation.ValueAnimator
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class TurntableView : View {

    private val mColors = intArrayOf(
        Color.BLUE, Color.DKGRAY, Color.CYAN, Color.RED, Color.GREEN
    )
    var mPaint: Paint = Paint()
    var rectF: RectF = RectF()
    var w = 0
    var h = 0
    var currentStartAngle = 270f  //开始角度
    var r = 0f  //半径
    var sr = 0f //里面按钮的半径   且是指尖的高度
    var showText: String = "开始旋转"
    private val path = Path()
    private var viewDatas: List<ViewData>? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        mPaint.run {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize = 30f
            isAntiAlias = true
            strokeWidth = 4f;
        }
        path.fillType = Path.FillType.EVEN_ODD
    }

    fun setData(viewDatas: List<ViewData>?) {
        this.viewDatas=null
        this.viewDatas = viewDatas
        initData()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        r = (w.coerceAtMost(h) / 2).toFloat()
        sr = r / 4
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //移动坐标到中间
        canvas?.translate((w / 2).toFloat(), (h / 2).toFloat())
        rectF.set(-r, -r, r, r)

        viewDatas?.forEach {
            mPaint.color = it.color;
            canvas?.drawArc(rectF, currentStartAngle, it.angle, true, mPaint);
            //绘制扇形上文字
            val textAngle: Float = currentStartAngle + it.angle / 2 //计算文字位置角度

            mPaint.color = Color.BLACK
            val x =
                (r / 2 * cos(textAngle * Math.PI / 180)).toFloat() //计算文字位置坐标

            val y = (r / 2 * sin(textAngle * Math.PI / 180)).toFloat()
            mPaint.color = Color.BLACK //文字颜色
            if (it.name != null) {
                canvas?.drawText(it.name!!, x, y, mPaint);    //绘制文字s
                currentStartAngle += it.angle;


            }
        }
        mPaint.color = Color.WHITE
        canvas?.drawCircle(0f, 0f, sr, mPaint)
        mPaint.color = Color.BLACK
        canvas?.drawText(showText, x - 50f, y, mPaint);    //绘制文字
        drawTriangle(canvas)
    }

    /**
     * 画指针
     *///70 135
    private fun drawTriangle(canvas: Canvas?) {
        var a = 70.0
        val pointa = getPoint(a)
        path.reset()
        path.moveTo(pointa.x, -pointa.y)
        path.lineTo(0f, -2 * sr)
        path.lineTo(-pointa.x, -pointa.y)
        path.lineTo(pointa.x, -pointa.y)
        path.close()
        mPaint.color = Color.WHITE
        canvas?.drawPath(path, mPaint)
    }

    private fun getPoint(x: Double): PointF {
        var xy = cos(Math.toRadians(x)) * sr//根据余弦计算X坐标
        var y = sin(Math.toRadians(x)) * sr;//根据正弦dao计算y坐标
        return PointF(xy.toFloat(), y.toFloat())
    }

    private fun initData() {
        if (null == viewDatas || viewDatas!!.isEmpty()) {
            return
        }
        for (i in viewDatas!!.indices) {
            val angle = 360 / (viewDatas!!.size) //对应的角度
            viewDatas!![i].angle = angle.toFloat()

            viewDatas!![i].color = if (i % 2 == 0) mColors[0] else mColors[1]
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            ACTION_DOWN -> {
                var index = 0f
                // event.getY  不带状态栏   getrawY 有状态栏
                val let = sqrt(((event.y - (h / 2)).pow(2)) + ((event.x - (w / 2))).pow(2))
                if (let < sr) {
                    Log.e(TAG, "--------------点击了开始")
                    index = currentStartAngle
                    val ofFloat =
                        ValueAnimator.ofFloat(currentStartAngle, currentStartAngle + 360f * 5)
                    ofFloat.run {
                        duration = 5000
                        addUpdateListener {
                            val animatedValue = it.animatedValue
                            currentStartAngle = animatedValue as Float
                            if (currentStartAngle - (360f * 5) < index) {
                                showText = "旋转中"
                            } else {
                                currentStartAngle %= 360f
                                showText = "开始旋转"
                            }

                            invalidate()
                        }
                    }
                    ofFloat.start();
                } else {
                    Log.e(TAG, "--------------点击了其他位置")
                }

            }

        }
        return super.onTouchEvent(event)
    }
}