package com.example.imfine.util

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.example.imfine.R


class EllipsizeTextView : AppCompatTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private val ellipsis = "⋯" // 사용자 정의 말줄임표
    override fun onDraw(canvas: Canvas) {
        val layout = layout
        var text = text
        val width = width
        val paint = paint

        if (layout != null && text != null && width > 0) {
            val ellipsizeCount = layout.getEllipsisCount(maxLines - 1)

            if (ellipsizeCount > 0) {
                val ellipsize = text.substring(
                    0,
                    this.text.length - ellipsizeCount - 1
                )
                text = ellipsize + ellipsis

                paint.apply {
                    color = resources.getColor(R.color.textColorPrimary)
                }

                // 말줄임표를 추가한 텍스트를 다시 그림
                canvas.drawText(
                    text,
                    paddingLeft.toFloat(),
                    paddingTop + layout.height - paint.descent(),
                    paint
                )
            } else {
                super.onDraw(canvas)
            }
        } else {
            super.onDraw(canvas)
        }

    }
}

fun TextView.doOnTextChanged(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}