package com.shuaiyu.mymoveimage

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View


public class MyMoveView : View {
    lateinit var paint: Paint
    lateinit var backgroundpaint: Paint
    lateinit var mcontext:Context
    constructor(context: Context) : super(context) {
        init()
        this.mcontext=context
    }

    var cx: Float = Float.MIN_VALUE
    var cy: Float = Float.MIN_VALUE
    var radius: Float = 100f

    constructor(
        context: Context,
        attributeSet: AttributeSet,
        paint: Paint
    ) : super(context, attributeSet) {
        init()
    }

    public fun setViewData(cx: Float, cy: Float, radois: Float) {
        this.cx = cx
        this.cy = cy
        this.radius = radius
        invalidate()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init()
    }

    var mywidth: Int = 0
    var myHight: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mywidth = MeasureSpec.getSize(widthMeasureSpec);
        myHight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("TAG", "onMeasure: $myHight    $mywidth")
    }

    @SuppressLint("ResourceAsColor")
    private fun init() {
        paint = Paint()
        paint.color = R.color.touming
        paint.alpha = 0
        paint.isAntiAlias = true;//抗锯齿
        paint.style = Paint.Style.FILL;//实心矩形框
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);


        backgroundpaint = Paint()
        backgroundpaint.color = R.color.gray

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawCircle(mywidth / 2.toFloat(), myHight / 2.toFloat(), radius, paint)
        canvas?.drawARGB(360, 0, 0, 0)

    }
//    private fun createImage(): Bitmap? {
//        val paint = Paint()
//        paint.isAntiAlias = true
//        val finalBmp = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(finalBmp)
//        canvas.drawBitmap(backgroundBitmap, 0, 0, paint)
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//        canvas.drawBitmap(mBitmap, 0, 0, paint)
//        return finalBmp
//    }
    //    fun T.asd (nm:T{}):R{
//        return nm(this)
//    }
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }
}