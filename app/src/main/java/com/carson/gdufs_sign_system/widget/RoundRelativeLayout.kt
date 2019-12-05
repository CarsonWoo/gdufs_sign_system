package com.carson.gdufs_sign_system.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

class RoundRelativeLayout: RelativeLayout {

    private val mRids = floatArrayOf(26F, 26F, 26F, 26F, 26F, 26F, 26F, 26F)

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private lateinit var mPath: Path
    private lateinit var mRectF: RectF
    private val mPaint: Paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    constructor(context: Context?): this(context, null)

    constructor(context: Context?, attributeSet: AttributeSet?): super(context, attributeSet) {
        initParams()
    }

    private fun initParams() {
//        setBackgroundColor(Color.WHITE)
        clipChildren = true
        mPath = Path()
        mPath.fillType = Path.FillType.EVEN_ODD
    }

    private fun checkPathChanged() {
        if (width == mWidth && mHeight == height) {
            return
        }

        mWidth = width
        mHeight = height

        mPath.reset()
        mRectF = RectF(0F, 0F, mWidth.toFloat(), mHeight.toFloat())

        mPath.addRoundRect(mRectF, mRids, Path.Direction.CW)
    }

    override fun draw(canvas: Canvas?) {
        val saveCount = canvas?.save()
        checkPathChanged()
        canvas?.clipPath(mPath)
        super.draw(canvas)
        saveCount?.let { canvas.restoreToCount(it) }
    }

}