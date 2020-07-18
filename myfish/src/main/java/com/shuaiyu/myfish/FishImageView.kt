package com.shuaiyu.myfish

import android.graphics.*
import android.graphics.drawable.Drawable

class FishImageView : Drawable() {
    lateinit var paint: Paint
    lateinit var path: Path
    private val OTHER_ALPHA = 110


    // 鱼的重心
    private var middlePoint: PointF? = null

    // 鱼的主要朝向角度
    private val fishMainAngle = 90f

    // 绘制鱼头的半径
    private val HEAD_RADIUS = 100f

    // 鱼身长度
    private val BODY_LENGTH = HEAD_RADIUS * 3.2f

    // 鱼鳍的长度
    private val FINS_LENGTH = 1.3f * HEAD_RADIUS

    // 寻找鱼鳍起始点坐标的线长
    private val FIND_FINS_LENGTH = 0.9f * HEAD_RADIUS

    // 大圆的半径
    private val BIG_CIRCLE_RADIUS = 0.7f * HEAD_RADIUS

    // 中圆的半径
    private val MIDDLE_CIRCLE_RADIUS = 0.6f * BIG_CIRCLE_RADIUS

    // 小圆半径
    private val SMALL_CIRCLE_RADIUS = 0.4f * MIDDLE_CIRCLE_RADIUS

    // --寻找尾部中圆圆心的线长
    private val FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS * (0.6f + 1)

    // --寻找尾部小圆圆心的线长
    private val FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f)

    // --寻找大三角形底边中心点的线长
    private val FIND_TRIANGLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 2.7f

    init {
        paint = Paint()
        path = Path()
        paint.setStyle(Paint.Style.FILL)
        paint.setAntiAlias(true)
        paint.setDither(true)
        paint.setARGB(OTHER_ALPHA, 244, 92, 71)
        middlePoint = PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS)
    }


    /**
     * @param startPoint 起始点坐标
     * @param length     要求的点到起始点的直线距离 -- 线长
     * @param angle      鱼当前的朝向角度
     * @return
     */
    fun calculatePoint(
        startPoint: PointF, length: Float, angle: Float
    ): PointF? {
        // x坐标
        val deltaX =
            (Math.cos(Math.toRadians(angle.toDouble())) * length).toFloat()
        // y坐标
        val deltaY =
            (Math.sin(Math.toRadians(angle - 180.toDouble())) * length).toFloat()
        return PointF(startPoint.x + deltaX, startPoint.y + deltaY)
    }

    override fun draw(canvas: Canvas) {
        val fishAngle = fishMainAngle

        /**
         * 画鱼头
         */
        val headPoint: PointF? = calculatePoint(middlePoint!!, BODY_LENGTH / 2, fishAngle)
        canvas.drawCircle(headPoint!!.x, headPoint.y, HEAD_RADIUS, paint);


        // 画右鱼鳍
        val rightFinsPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle - 110)
        makeFins(canvas, rightFinsPoint!!, fishMainAngle, true)

        // 画做鱼鳍
        val leftFinsPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle + 110)
        makeFins(canvas, leftFinsPoint!!, fishMainAngle, false)
        val bodyBottomCenterPoint = calculatePoint(headPoint, BODY_LENGTH, fishAngle - 180)
        val mpaint = makeSegment(
            canvas,
            bodyBottomCenterPoint,
            BIG_CIRCLE_RADIUS, MIDDLE_CIRCLE_RADIUS,
            FIND_MIDDLE_CIRCLE_LENGTH,
            fishAngle - 180,
            true
        )

        makeSegment(
            canvas,
            mpaint,
            MIDDLE_CIRCLE_RADIUS, SMALL_CIRCLE_RADIUS,
            FIND_SMALL_CIRCLE_LENGTH,
            fishAngle - 180,
            false
        )

    }


    private fun makeTriangel(
        canvas: Canvas, startPoint: PointF, findCenterLength: Float,
        findEdgeLength: Float, fishAngle: Float
    ) {
        // 三角形底边的中心坐标
        val centerPoint = calculatePoint(startPoint, findCenterLength, fishAngle - 180)
        // 三角形底边两点
        val leftPoint = calculatePoint(centerPoint!!, findEdgeLength, fishAngle + 90)
        val rightPoint = calculatePoint(centerPoint, findEdgeLength, fishAngle - 90)
        path.reset()
        path.moveTo(startPoint.x, startPoint.y)
        path.lineTo(leftPoint!!.x, leftPoint.y)
        path.lineTo(rightPoint!!.x, rightPoint.y)
        canvas.drawPath(path, paint)
    }

    private fun makeSegment(
        canvas: Canvas,
        bottomCenterPoint: PointF?,
        bigRadius: Float,
        smallRadius: Float,
        findSmallCircleLength: Float,
        fishAngle: Float,
        hasBigCircle: Boolean
    ): PointF? {

        var buttompointF: PointF? =
            calculatePoint(bottomCenterPoint!!, findSmallCircleLength, fishAngle);
        // 画梯形

        var topLeftPoint = calculatePoint(bottomCenterPoint!!, bigRadius, fishAngle + 90);

        var topRightPoint = calculatePoint(bottomCenterPoint!!, bigRadius, fishAngle - 90);

        var bottomLeftPoint = calculatePoint(buttompointF!!, smallRadius, fishAngle + 90);

        var bottomRightPoint = calculatePoint(buttompointF!!, smallRadius, fishAngle - 90);

        if (hasBigCircle) {
            canvas.drawCircle(bottomCenterPoint!!.x, bottomCenterPoint.y, bigRadius, paint);
        }

        canvas.drawCircle(buttompointF!!.x, buttompointF.y, smallRadius, paint);
        // 画梯形
        path.reset()
        path.rMoveTo(topLeftPoint!!.x, topLeftPoint!!.y)
        path.lineTo(topRightPoint!!.x, topRightPoint!!.y)
        path.lineTo(bottomRightPoint!!.x, bottomRightPoint!!.y)
        path.lineTo(bottomLeftPoint!!.x, bottomLeftPoint!!.y)
        canvas.drawPath(path, paint)
        return buttompointF;
    }


    /**
     * 画鱼鳍
     *
     * @param startPoint 起始坐标
     * @param isRight    是否是右鱼鳍
     */
    private fun makeFins(
        canvas: Canvas,
        startPoint: PointF,
        fishAngle: Float,
        isRight: Boolean
    ) {
        val controlAngle = 115f

        // 鱼鳍的终点 --- 二阶贝塞尔曲线的终点
        val endPoint = calculatePoint(startPoint, FINS_LENGTH, fishAngle - 180)
        // 控制点
        val controlPoint = calculatePoint(
            startPoint, FINS_LENGTH * 1.8f,
            if (isRight) fishAngle - controlAngle else fishAngle + controlAngle
        )
        // 绘制
        path.reset()
        // 将画笔移动到起始点
        path.moveTo(startPoint.x, startPoint.y)
        // 二阶贝塞尔曲线
        path.quadTo(controlPoint!!.x, controlPoint.y, endPoint!!.x, endPoint.y)
        canvas.drawPath(path, paint)
    }


    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.setColorFilter(colorFilter)
    }

    override fun getIntrinsicWidth(): Int {
        return (8.38f * HEAD_RADIUS).toInt()
    }

    override fun getIntrinsicHeight(): Int {
        return (8.38f * HEAD_RADIUS).toInt()
    }
}