package com.carson.gdufs_sign_system.widget

import android.content.Context
import android.graphics.Outline
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import android.view.View
import android.view.ViewOutlineProvider
import com.carson.gdufs_sign_system.R
import kotlin.math.min

class RoundTextureView: TextureView {

    private var mRadius = 0F

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RoundTextureView)
        a.apply {
            mRadius = if (hasValue(R.styleable.RoundTextureView_textureRadius)) {
                // 默认为圆形
                a.getFloat(R.styleable.RoundTextureView_textureRadius, measuredWidth.toFloat() / 2)
            } else {
                measuredWidth.toFloat() / 2
            }
        }
        a.recycle()

        outlineProvider = object: ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                outline?.setOval(0, 0, measuredWidth, measuredWidth)
            }
        }
        clipToOutline = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mRadius = min(measuredWidth, measuredHeight).toFloat() / 2
    }

    fun setRadius(radius: Float) {
        this.mRadius = radius

    }

    fun turnRound() {
        invalidateOutline()
    }

}