package com.example.mytestgo.utils

import android.animation.ArgbEvaluator
import android.graphics.*
import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint1: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val defaltPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint2: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progressPercentage: Int = 0

    private val totalMarks = 120 // 总刻度数
    private val markLength = 80f // 刻度线长度
    private val strokeWidth = 4f // 刻度线宽度
    private val circleMargin = 90f // 刻度线与外部圆的距离
    private val circleWith = 100f // 外部圆宽度

    init {
        paint1.style = Paint.Style.STROKE
        paint1.strokeWidth = strokeWidth
        paint1.color = Color.parseColor("#F8F9FC")
        paint1.isAntiAlias = true // 设置抗锯齿效果

        defaltPaint.style = Paint.Style.STROKE
        defaltPaint.strokeWidth = circleWith
        defaltPaint.color = Color.GRAY // Default to gray
        defaltPaint.isAntiAlias = true // 设置抗锯齿效果

        paint2.style = Paint.Style.STROKE
        paint2.strokeWidth = circleWith
        paint2.color = Color.GRAY // Default to gray
        paint2.isAntiAlias = true // 设置抗锯齿效果

        textPaint.textSize = 120f
        textPaint.strokeWidth = 30f
        textPaint.color = Color.parseColor("#FFFF33")
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            val centerX = width / 2f
            val centerY = height / 2f
            val radius = 350 //Math.min(width, height) / 2f - radiusOffset

            // 绘制刻度线和外部圆
            for (i in 0 until totalMarks) {
                val angle = Math.toRadians(360 * i.toDouble() / totalMarks - 90)
                val startX = centerX + radius * cos(angle).toFloat()
                val startY = centerY + radius * sin(angle).toFloat()
                val endX = centerX + (radius - markLength) * cos(angle).toFloat()
                val endY = centerY + (radius - markLength) * sin(angle).toFloat()

                val markColor = if (i / totalMarks.toFloat() < progressPercentage / 100f) {
                    Color.parseColor("#FFFF33") // 这里是根据进度决定刻度的颜色
                } else {
                    Color.GRAY // 其他部分保持灰色
                }

                paint1.color = markColor
                canvas.drawLine(startX, startY, endX, endY, paint1)
            }

            // 绘制外部圆
            canvas.drawCircle(centerX, centerY, radius + circleMargin, defaltPaint)

            // 绘制进度圆弧
            val progressAngle = (progressPercentage / 100f) * 360
            val rectF = RectF(
                centerX - radius - circleMargin,
                centerY - radius - circleMargin,
                centerX + radius + circleMargin,
                centerY + radius + circleMargin
            )
            paint2.color = Color.parseColor("#FFCC00") // 进度圆弧的颜色为橙色
            canvas.drawArc(rectF, -90f, progressAngle, false, paint2)

            // 绘制百分比文本
            val text = "$progressPercentage%"
            val textBounds = Rect()
            textPaint.getTextBounds(text, 0, text.length, textBounds)
            val textX = centerX
            val textY = centerY + textBounds.height() / 2
            canvas.drawText(text, textX, textY, textPaint)


        }
    }


    fun setProgress(percentage: Int) {
        progressPercentage = percentage
        invalidate()
    }

}
