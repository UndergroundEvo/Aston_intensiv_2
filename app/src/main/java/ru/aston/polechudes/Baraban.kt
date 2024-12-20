package ru.aston.polechudes

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd

class Baraban @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var radius = 200f
    private var currentRotation = 245f
    private val sectionCount = DrumColors.colors.size
    private var pointerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }
    private var onSpinCompleteListener: ((Int) -> Unit)? = null

    fun setSize(size: Float) {
        radius = size
        invalidate()
    }

    fun setOnSpinCompleteListener(listener: (Int) -> Unit) {
        onSpinCompleteListener = listener
    }

    fun spinTo(sectionsToSpin: Int) {
        currentRotation = 245f
        val sectionAngle = 360f / sectionCount
        val fullRotations = 2 * 360
        val targetRotation = sectionsToSpin * sectionAngle
        Log.d("spinTo","sectionAngle: $sectionAngle targetRotation: $targetRotation")

        val animator = ValueAnimator.ofFloat(currentRotation, targetRotation+currentRotation+fullRotations)
        animator.duration = 2000
        animator.interpolator = DecelerateInterpolator()

        animator.addUpdateListener { animation ->
            currentRotation = animation.animatedValue as Float
            Log.d("addUpdateListener","currentRotation: $currentRotation targetRotation: $targetRotation")
            invalidate()
        }

        animator.doOnEnd {
            val finalRotation = (targetRotation % 360)/(360f / sectionCount)
            val finalIndex = sectionCount - finalRotation
            Log.d("doOnEnd","finalRotation: $finalRotation sectionsToSpin: $sectionsToSpin")
            if ((finalIndex % 7).toInt() == 0){
                onSpinCompleteListener?.invoke(DrumColors.colors[0])
            }else{
                onSpinCompleteListener?.invoke(DrumColors.colors[finalIndex.toInt()])
            }
        }

        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f

        for (i in DrumColors.colors.indices) {
            paint.color = DrumColors.colors[i]
            canvas.drawArc(
                centerX - radius, centerY - radius,
                centerX + radius, centerY + radius,
                currentRotation + (360f / sectionCount) * i,
                360f / sectionCount,
                true,
                paint
            )
        }
        drawPointer(canvas, centerX, centerY)
    }

    private fun drawPointer(canvas: Canvas, centerX: Float, centerY: Float) {
        val pointerWidth = 40f
        val pointerHeight = -50f
        val pointerPath = android.graphics.Path().apply {
            moveTo(centerX, centerY - radius - pointerHeight)
            lineTo(centerX - pointerWidth / 2, centerY - radius)
            lineTo(centerX + pointerWidth / 2, centerY - radius)
            close()
        }
        canvas.drawPath(pointerPath, pointerPaint)
    }
}