package com.carson.gdufs_sign_system.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.carson.gdufs_sign_system.R

class CircleImageView: AppCompatImageView {

    private val mPaint: Paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private var mRadius = 0
    private var mRealSize = 0
    private var mBorderColor: Int = 0
    private var mBorderWidth: Float = 1F
    private val mBorderPaint: Paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private lateinit var mMatrix: Matrix

    constructor(context: Context?): this(context, null)

    constructor(context: Context?, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
        typedArray?.apply {
            if (hasValue(R.styleable.CircleImageView_borderColor)) {
                mBorderColor = getColor(R.styleable.CircleImageView_borderColor, Color.WHITE)
            }
            if (hasValue(R.styleable.CircleImageView_borderWidth)) {
                mBorderWidth = getDimension(R.styleable.CircleImageView_borderWidth, 1F)
                Log.e(TAG, "width = $mBorderWidth")
            }
            recycle()
        }
        initParams()
    }

    private fun initParams() {
        mPaint.isAntiAlias = true
        mMatrix = Matrix()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mRealSize = (measuredWidth - paddingLeft - paddingRight).
            coerceAtMost(measuredHeight - paddingTop - paddingBottom)
        mRadius = mRealSize / 2
    }

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable
        if (drawable == null) {
            super.onDraw(canvas)
            return
        }
        if (drawable is ColorDrawable) {
            Log.e(TAG, "colorDrawable")
            mPaint.color = drawable.color
            canvas.drawCircle(paddingLeft + mRealSize.toFloat() / 2, paddingTop + mRealSize.toFloat() / 2,
                mRadius.toFloat(), mPaint)
            if (mBorderColor != 0) {
                setBorderColor(mBorderColor)
                mBorderPaint.style = Paint.Style.STROKE
                mBorderPaint.strokeWidth = mBorderWidth
                canvas.drawCircle(paddingLeft + mRealSize.toFloat() / 2, paddingTop + mRealSize.toFloat() / 2,
                    mRadius.toFloat() - mBorderWidth, mBorderPaint)
            }
            return
        }
        if (drawable is BitmapDrawable) {
            Log.e(TAG, "BitmapDrawable")
            mPaint.shader = initBitmapShader(drawable)
            canvas.drawCircle(paddingLeft + mRealSize.toFloat() / 2, paddingTop + mRealSize.toFloat() / 2,
                mRadius.toFloat(), mPaint)
            if (mBorderColor != 0) {
                setBorderColor(mBorderColor)
                mBorderPaint.style = Paint.Style.STROKE
                mBorderPaint.strokeWidth = mBorderWidth
                canvas.drawCircle(paddingLeft + mRealSize.toFloat() / 2, paddingTop + mRealSize.toFloat() / 2,
                    mRadius.toFloat() - mBorderWidth, mBorderPaint)
            }
            return
        }
        super.onDraw(canvas)
    }

    private fun initBitmapShader(bitmapDrawable: BitmapDrawable): BitmapShader {
        val bitmap = bitmapDrawable.bitmap
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        // 缩放比
        val scale = (width / bitmap.width).coerceAtLeast(height / bitmap.height)
        mMatrix.setScale(scale.toFloat(), scale.toFloat())
        bitmapShader.setLocalMatrix(mMatrix)
        return bitmapShader
    }

    /**
     * 设置外框颜色
     */
    fun setBorderColor(color: Int) {
        mBorderPaint.color = color
        postInvalidate()
    }

    companion object {
        private const val TAG = "CircleImageView"
    }

}