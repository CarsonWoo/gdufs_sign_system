package com.carson.gdufs_sign_system.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.carson.gdufs_sign_system.R

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask
import kotlin.math.pow

/**
 * 滚动选择器
 */
class PickerScrollView : View {

    private var mDataList: MutableList<String>? = null
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private var mCurrentSelected: Int = 0
    private var mPaint: Paint? = null

    private var mOtherTextPaint: Paint? = null

    private var mMaxTextSize = 20f
    private var mMinTextSize = 10f

    private val mMaxTextAlpha = 255f
    private val mMinTextAlpha = 120f

    private var mViewHeight: Int = 0
    private var mViewWidth: Int = 0

    private var mLastDownY: Float = 0.toFloat()
    /**
     * 滑动的距离
     */
    private var mMoveLen = 0f
    private var isInit = false
    private var mSelectListener: onSelectListener? = null
    private var timer: Timer? = null
    private var mTask: MyTimerTask? = null

    private val updateHandler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0f
                if (mTask != null) {
                    mTask!!.cancel()
                    mTask = null
                    performSelect()
                }
            } else
            // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED
            invalidate()
        }

    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    fun setOnSelectListener(listener: onSelectListener) {
        mSelectListener = listener
    }

    private fun performSelect() {
        if (mSelectListener != null)
            mSelectListener!!.onSelect(mDataList!![mCurrentSelected])
    }

    fun setData(datas: MutableList<String>) {
        mDataList = datas
        mCurrentSelected = datas.size / 2
        invalidate()
    }

    /**
     * 选择选中的item的index
     *
     * @param selected
     */
    fun setSelected(selected: Int) {
        mCurrentSelected = selected
        val distance = mDataList!!.size / 2 - mCurrentSelected
        if (distance < 0)
            for (i in 0 until -distance) {
                moveHeadToTail()
                mCurrentSelected--
            }
        else if (distance > 0)
            for (i in 0 until distance) {
                moveTailToHead()
                mCurrentSelected++
            }
        invalidate()
    }

    /**
     * 选择选中的内容
     *
     * @param mSelectItem
     */
    fun setSelected(mSelectItem: String) {
        for (i in mDataList!!.indices)
            if (mDataList!![i] == mSelectItem) {
                setSelected(i)
                break
            }
    }

    private fun moveHeadToTail() {
        val datasBean = mDataList!![0]
        mDataList!!.removeAt(0)
        mDataList!!.add(datasBean)
    }

    private fun moveTailToHead() {
        val datasBean = mDataList!![mDataList!!.size - 1]
        mDataList!!.removeAt(mDataList!!.size - 1)
        mDataList!!.add(0, datasBean)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewHeight = measuredHeight
        mViewWidth = measuredWidth
        // 按照View的高度计算字体大小
        mMaxTextSize = mViewHeight / 8.0f
        mMinTextSize = mMaxTextSize / 2f
        isInit = true
        invalidate()
    }

    private fun init() {
        timer = Timer()
        mDataList = ArrayList()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.textAlign = Paint.Align.CENTER
        val mColorText = 0x333333
        mPaint!!.color = mColorText

        mOtherTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mOtherTextPaint!!.style = Paint.Style.FILL
        mOtherTextPaint!!.textAlign = Paint.Align.CENTER
        mOtherTextPaint!!.color = resources.getColor(R.color.colorGray)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 根据index绘制view
        if (isInit)
            drawData(canvas)
    }

    private fun drawData(canvas: Canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        val scale = parabola(mViewHeight / 4.0f, mMoveLen)
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        mPaint!!.textSize = size
        mPaint!!.alpha = ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt()
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        val x = (mViewWidth / 2.0).toFloat()
        val y = (mViewHeight / 2.0 + mMoveLen).toFloat()
        val fmi = mPaint!!.fontMetricsInt
        val baseline = (y - (fmi.bottom / 2.0 + fmi.top / 2.0)).toFloat()

        val indexs = mCurrentSelected
        val textData = mDataList!![indexs]
        canvas.drawText(textData, x, baseline, mPaint!!)

        // 绘制上方data
        run {
            var i = 1
            while (mCurrentSelected - i >= 0) {
                drawOtherText(canvas, i, -1)
                i++
            }
        }
        // 绘制下方data
        var i = 1
        while (mCurrentSelected + i < mDataList!!.size) {
            drawOtherText(canvas, i, 1)
            i++
        }
    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private fun drawOtherText(canvas: Canvas, position: Int, type: Int) {
        val d = MARGIN_ALPHA * mMinTextSize * position.toFloat() + type * mMoveLen
        val scale = parabola(mViewHeight / 4.0f, d)
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        mOtherTextPaint!!.textSize = size
        mOtherTextPaint!!.alpha = ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt()
        val y = (mViewHeight / 2.0 + type * d).toFloat()
        val fmi = mPaint!!.fontMetricsInt
        val baseline = (y - (fmi.bottom / 2.0 + fmi.top / 2.0)).toFloat()

        val indexs = mCurrentSelected + type * position
        val textData = mDataList!![indexs]
        canvas.drawText(textData, (mViewWidth / 2.0).toFloat(), baseline, mOtherTextPaint!!)
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private fun parabola(zero: Float, x: Float): Float {
        val f = (1 - (x / zero).toDouble().pow(2.0)).toFloat()
        return if (f < 0) 0F else f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> doDown(event)
            MotionEvent.ACTION_MOVE -> doMove(event)
            MotionEvent.ACTION_UP -> doUp(event)
        }
        return true
    }

    private fun doDown(event: MotionEvent) {
        if (mTask != null) {
            mTask!!.cancel()
            mTask = null
        }
        mLastDownY = event.y
    }

    private fun doMove(event: MotionEvent) {

        mMoveLen += event.y - mLastDownY

        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            moveTailToHead()
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            moveHeadToTail()
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize
        }

        mLastDownY = event.y
        invalidate()
    }

    private fun doUp(event: MotionEvent) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0f
            return
        }
        if (mTask != null) {
            mTask!!.cancel()
            mTask = null
        }
        mTask = MyTimerTask(updateHandler)
        timer!!.schedule(mTask, 0, 10)
    }

    private inner class MyTimerTask internal constructor(internal var handler: Handler) :
        TimerTask() {

        override fun run() {
            handler.sendMessage(handler.obtainMessage())
        }

    }

    interface onSelectListener {
        fun onSelect(item: String)
    }

    companion object {

        val TAG = "PickerScrollView:"
        /**
         * text之间间距和minTextSize之比
         */
        val MARGIN_ALPHA = 2.8f
        /**
         * 自动回滚到中间的速度
         */
        val SPEED = 2f
    }
}