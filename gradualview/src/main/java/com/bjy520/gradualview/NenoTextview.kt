package com.bjy520.gradualview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class NenoTextview : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun initPaint() {
        mPaint = Paint()
        mPaint?.textSize = mTextSize
    }

    var mViewWidth //TextView的宽度
            = 0f
    var mViewHeight //TextView的高度
            = 0f
    private var mLinearGradient //渲染器
            : LinearGradient? = null
    private var mMatrix //图片变换处理器
            : Matrix? = null
    private var mPaint //字体的笔
            : Paint? = null
    var mTranslate = 0 //表示平移的速度

    var mTranslateH = 0 //表示平移的速度
    private var mSlide: Boolean = false   //设置是否移动
    private var mColor: Boolean = false     // 设置是否有颜色

    private var mColors: IntArray? = intArrayOf(Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN)
    fun setColor(colors: IntArray?) {
        mColors = colors
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val bounds = Rect()
        mPaint?.getTextBounds(mText.toCharArray(), 0, mText.length, bounds)
        val width = bounds.left + bounds.width()
        setMeasuredDimension(width, heightMeasureSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        //先让父类方法执行，由于上面我们给父类的 Paint 套上了渲染器，所以这里出现的文字已经是彩色的了
        super.onDraw(canvas)
        if (mColor) {
            mViewHeight = measuredHeight.toFloat()
            mViewWidth = measuredWidth.toFloat()
            if (mViewWidth > 0) {
                mPaint?.isAntiAlias = true
                mPaint?.textSize = mTextSize
                //得到 父类 TextView 中写字的那支笔.，注意是继承Textview
                mLinearGradient = mColors?.let {
                    LinearGradient(
                        0f,
                        0f,
                        0f,
                        mViewHeight,
                        it,
                        null,
                        Shader.TileMode.CLAMP
                    )
                }
                val baseY =
                    (canvas!!.height / 2 - (mPaint!!.descent() + mPaint!!.ascent()) / 2)
                mPaint?.shader = mLinearGradient
                mMatrix = Matrix()
                mPaint?.let { canvas?.drawText(mText, 0f, baseY, it) }
            }
        } else {
            mPaint?.reset()
            mPaint?.isAntiAlias = true
            mPaint?.textSize = mTextSize
            val baseY =
                (canvas!!.height / 2 - (mPaint!!.descent() + mPaint!!.ascent()) / 2)
            mPaint?.let {
                it.color = mTextColor
                // 需要对齐其基准线  后面修改
                canvas?.drawText(mText, 0f, baseY, it)
            }
        }
        if (mMatrix != null && mSlide) {
            //利用 Matrix 的平移动作实现霓虹灯的效果，这里是每次滚动1/10
            mTranslate += (mViewWidth / 10).toInt()
            mTranslateH += (mViewHeight / 10).toInt()
            //如果滚出了控件边界，就要拉回来重置开头，这里重置到了屏幕左边的空间
            if (mTranslate > mViewWidth) {
                mTranslate = (-mViewWidth / 2).toInt()
            }
            if (mTranslateH > mViewHeight) {
                mTranslateH = (-mViewHeight / 2).toInt()
            }
            //设置平移距离
            mMatrix!!.setTranslate(mTranslate.toFloat(), mTranslateH.toFloat())
            //平移效果生效
            mLinearGradient!!.setLocalMatrix(mMatrix)
            //延迟 100 毫秒再次刷新 View 也就是再次执行本 onDraw 方法
            postInvalidateDelayed(50)
        }
        requestLayout()
    }

    fun setSlide(slide: Boolean) {
        mSlide = slide
        mColor = slide
    }

    private var mText: String = ""
    fun setText(text: String) {
        mText = text
        invalidate()
    }

    fun setHasColors(Color: Boolean, colors: IntArray? = intArrayOf()) {
        mColor = Color
        mColors = colors
        invalidate()
    }

    private var mTextColor: Int = Color.BLUE
    fun setTextColor(text: Int) {
        mTextColor = text
    }

    private var mTextSize: Float = dip(18).toFloat()
    fun setTextSize(text: Float) {
        mTextSize = dip(text).toFloat()
    }
}

fun View.dip(value: Int) =
    (value * resources.displayMetrics.density).toInt()


fun View.dip(value: Float) =
    (value * resources.displayMetrics.density).toInt()