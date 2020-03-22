package com.carson.gdufs_sign_system.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.utils.ScreenUtils

class BannerDot : LinearLayout {

    companion object {
        const val TAG = "BannerDot"
        private const val SCALE = 1.2F
    }
    private var mSize: Int = 0
    private var mPosisition: Int = 0
    private val mDotList = ArrayList<CircleImageView>()

    private var mSupportInfinite = false

    constructor(context: Context?, size: Int): this(context, null)

    constructor(context: Context?, attributeSet: AttributeSet?): super(context, attributeSet) {
        gravity = Gravity.END
        orientation = HORIZONTAL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (widthMeasureSpec == MeasureSpec.AT_MOST && heightMeasureSpec == MeasureSpec.AT_MOST) {
            // 都为wrap_content
            val tHeight = ScreenUtils.dip2px_10(context)
            val tWidth = if (mSize == 0) {
                mSize * ScreenUtils.dip2px_10(context)
            } else {
                ScreenUtils.dip2px(context, 100F)
            }
            setMeasuredDimension(tWidth, tHeight)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    //
    fun initParam(size: Int) {
        this.mSize = size
        context?.let {
            if (mSize > 0) {
                initViews(it)
            }
        }
    }

    fun getPosition() = mPosisition

    private fun initViews(context: Context) {
        for (i in 1..mSize) {
            val imgView = CircleImageView(context)
            val layoutParams = LayoutParams(ScreenUtils.dip2px_5(context), ScreenUtils.dip2px_5(context))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.marginStart = ScreenUtils.dip2px_2(context)
                layoutParams.marginEnd = ScreenUtils.dip2px_2(context)
            } else {
                layoutParams.leftMargin = ScreenUtils.dip2px_2(context)
                layoutParams.rightMargin = ScreenUtils.dip2px_2(context)
            }
            imgView.layoutParams = layoutParams
            imgView.setImageResource(R.color.colorGray)
            mDotList.add(imgView)
            addView(imgView)
        }
        selectDot(0)
    }

    fun selectDot(pos: Int) {
        val imgView: CircleImageView = if (mSupportInfinite) {
            when (pos) {
                0 -> {
                    mPosisition = mSize - 1
                    mDotList[mSize - 1]
                }
                mSize + 1 -> {
                    mPosisition = 0
                    mDotList[0]
                }
                else -> {
                    mPosisition = pos - 1
                    mDotList[pos - 1]
                }
            }
        } else {
            mPosisition = pos
            mDotList[pos]
        }
        postDelayed({
            resetAllDots()
            imgView.setImageResource(R.color.colorCyan)
        }, 500)
    }

    fun isSupportInfinite(support: Boolean) {
        this.mSupportInfinite = support
    }

    private fun resetAllDots() {
        mDotList.forEach {
            it.setImageResource(R.color.colorGray)
        }
    }

}