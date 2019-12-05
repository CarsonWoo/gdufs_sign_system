package com.carson.gdufs_sign_system.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView
import com.carson.gdufs_sign_system.R

class RoundImageView : ImageView {

    private var mBorderRadius: Int = 0
    private var mLeftTopRadius: Int = 0
    private var mLeftBottomRadius: Int = 0
    private var mRightTopRadius: Int = 0
    private var mRightBottomRadius: Int = 0

    private lateinit var mRectF: RectF

    private val mPaint: Paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private lateinit var mMatrix: Matrix

    constructor(context: Context?) : super(context) {
        initParams(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {
        initParams(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        initParams(context, attrs)
    }

    private fun initParams(context: Context?, attrs: AttributeSet?) {
        val a = context?.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
        a?.apply {
            if (hasValue(R.styleable.RoundImageView_radius)) {
                // 存在 则其他都为0
                mBorderRadius = getDimensionPixelOffset(R.styleable.RoundImageView_radius, 0)
            } else {
                mLeftTopRadius = this.getDimensionPixelOffset(R.styleable.RoundImageView_leftTopRadius, 0)
                mLeftBottomRadius = this.getDimensionPixelOffset(R.styleable.RoundImageView_leftBottomRadius, 0)
                mRightTopRadius = this.getDimensionPixelOffset(R.styleable.RoundImageView_rightTopRadius, 0)
                mRightBottomRadius = this.getDimensionPixelOffset(R.styleable.RoundImageView_rightBottomRadius, 0)
            }
        }
        a?.recycle()
        mMatrix = Matrix()
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mRectF = RectF(paddingLeft.toFloat(), paddingTop.toFloat(),
            width.toFloat() - paddingRight, height.toFloat() - paddingBottom)
    }

    /**
     * 提供给外部设置radius的方法
     */
    fun setRadius(leftTop: Int, rightTop: Int, rightBottom: Int, leftBottom: Int) {
        mLeftTopRadius = leftTop
        mRightTopRadius = rightTop
        mRightBottomRadius = rightBottom
        mLeftBottomRadius = leftBottom
    }

    /**
     * 重载
     */
    fun setRadius(radius: Int) {
        mBorderRadius = radius
    }

    override fun onDraw(canvas: Canvas?) {
        val drawable = drawable
        if (drawable == null) {
            super.onDraw(canvas)
            return
        }
        if (drawable is ColorDrawable) {
            mPaint.color = drawable.color
            if (mBorderRadius != 0) {
                // 直接画圆角
                canvas?.drawRoundRect(mRectF, mBorderRadius.toFloat(), mBorderRadius.toFloat(), mPaint)
            } else {
                // 明天再整
                super.onDraw(canvas)
            }
            return
        }
        if (drawable is BitmapDrawable) {
            mPaint.shader = getBitmapShader(drawable)
            if (mBorderRadius != 0) {
                canvas?.drawRoundRect(mRectF, mBorderRadius.toFloat(), mBorderRadius.toFloat(), mPaint)
            } else {
                super.onDraw(canvas)
            }
            return
        }
        super.onDraw(canvas)
    }

    private fun getBitmapShader(drawable: BitmapDrawable): Shader {
        val bitmap = drawable.bitmap
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val scale = (width / bitmap.width).coerceAtLeast(height / bitmap.height)
        mMatrix.setScale(scale.toFloat(), scale.toFloat())
        bitmapShader.setLocalMatrix(mMatrix)
        return bitmapShader
    }

}