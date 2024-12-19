package ru.aston.polechudes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DrumResult @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs) {

    private val textView: TextView
    private val imageView: ImageView

    init {
        inflate(context, R.layout.drum_result, this)
        textView = findViewById(R.id.textViewCustom)
        imageView = findViewById(R.id.imageViewCustom)
    }

    fun resetContent(){
        textView.visibility = View.GONE
        imageView.visibility = View.GONE
    }

    fun setContent(color: Int, isEven: Boolean) {
        if (isEven) {
            textView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            textView.text = "Selected Color"
            textView.setTextColor(color)
        } else {
            textView.visibility = View.GONE
            imageView.visibility = View.VISIBLE
            Glide.with(context)
                .load("https://loremflickr.com/320/240")
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
        }
        invalidate()
    }
}