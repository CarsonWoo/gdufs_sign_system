package com.carson.gdufs_sign_system.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.utils.Const
import java.util.*

class TimePickerView : RelativeLayout, View.OnClickListener {

    constructor(context: Context): this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)

    private enum class ACTION {PREV, NEXT}

    private lateinit var mPrevDateButton: Button
    private lateinit var mNextDateButton: Button
    private lateinit var mSelectedDate: TextView
    private lateinit var mPickerHour: PickerScrollView
    private lateinit var mPickerMinute: PickerScrollView
    private lateinit var mPickerSecond: PickerScrollView

    private var mDateStr: String = Const.getCurrentDate()
    private var mHourStr: String = "09"
    private var mMinuteStr: String = "00"
    private var mSecondsStr: String = "00"

    private var mCurrentDate = Date()

    private val mBaseCurrentDate = mCurrentDate

    init {
        View.inflate(context, R.layout.layout_time_picker, this)
        initViews()
        initEvents()
    }

    private fun initViews() {
        mPrevDateButton = findViewById(R.id.btn_time_picker_previous_date)
        mNextDateButton = findViewById(R.id.btn_time_picker_next_date)
        mSelectedDate = findViewById(R.id.time_picker_date)
        mPickerHour = findViewById(R.id.time_picker_hour)
        mPickerMinute = findViewById(R.id.time_picker_minute)
        mPickerSecond = findViewById(R.id.time_picker_second)

        mSelectedDate.text = mDateStr
    }

    private fun initEvents() {
        mPrevDateButton.setOnClickListener(this)
        mNextDateButton.setOnClickListener(this)

        initPicker()
    }

    private fun initPicker() {
        mPickerHour.setDrawSelectedRect(true)
        mPickerMinute.setDrawSelectedRect(true)
        mPickerSecond.setDrawSelectedRect(true)
        val hours = mutableListOf<String>()
        (0..23).forEach {
            hours.add(if (it < 10) "0$it" else it.toString())
        }
        val minutes = mutableListOf<String>()
        val seconds = mutableListOf<String>()
        (0..59).forEach {
            minutes.add(if (it < 10) "0$it" else it.toString())
            seconds.add(if (it < 10) "0$it" else it.toString())
        }
        mPickerHour.setData(hours)
        mPickerMinute.setData(minutes)
        mPickerSecond.setData(seconds)

        mPickerHour.setSelected(9)
        mPickerMinute.setSelected("00")
        mPickerSecond.setSelected("00")

        mPickerHour.setOnSelectListener(object : PickerScrollView.OnSelectListener {
            override fun onSelect(item: String) {
                mHourStr = item
            }
        })

        mPickerMinute.setOnSelectListener(object : PickerScrollView.OnSelectListener {
            override fun onSelect(item: String) {
                mMinuteStr = item
            }
        })

        mPickerSecond.setOnSelectListener(object : PickerScrollView.OnSelectListener {
            override fun onSelect(item: String) {
                mSecondsStr = item
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_time_picker_previous_date -> {
                onDateClick(ACTION.PREV)
            }
            R.id.btn_time_picker_next_date -> {
                onDateClick(ACTION.NEXT)
            }
        }
    }

    private fun onDateClick(value: ACTION) {
        mCurrentDate = if (value == ACTION.PREV) {
            if (Date(mCurrentDate.time - 24 * 60 * 60 * 1000L) < mBaseCurrentDate) {
                // 比今天还要之前
                mCurrentDate
            } else {
                Date(mCurrentDate.time - 24 * 60 * 60 * 1000L)
            }
        } else {
            Date(mCurrentDate.time + 24 * 60 * 60 * 1000L)
        }
        mDateStr = Const.getSpecDate(mCurrentDate)
        mSelectedDate.text = mDateStr
    }

    fun getResult(): String {
        return "$mDateStr $mHourStr:$mMinuteStr:$mSecondsStr"
    }

}