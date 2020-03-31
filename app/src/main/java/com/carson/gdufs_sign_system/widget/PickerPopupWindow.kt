package com.carson.gdufs_sign_system.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.utils.ScreenUtils
import java.lang.ref.WeakReference

class PickerPopupWindow(
    private val contextRef: WeakReference<Context>,
    private val anchorView: ViewGroup,
    private val dataList: MutableList<String>,
    private val mListener: OnConfirmPickListener): View.OnClickListener, PickerScrollView.OnSelectListener {

    private lateinit var mContentView: View
    private lateinit var mPopupWindow: PopupWindow
    private lateinit var mConfirmBtn: TextView
    private lateinit var mCancelBtn: TextView
    private lateinit var mPickerView: PickerScrollView

    private var mPickItem = if (dataList.size > 0) dataList[0] else ""

    init {
        mContentView = LayoutInflater.from(contextRef.get()).inflate(R.layout.layout_popup_picker_window,
            anchorView, false)
        initViews()
        initEvents()
    }

    private fun initViews() {
        mPopupWindow = PopupWindow(mContentView)
        mConfirmBtn = mContentView.findViewById(R.id.popup_confirm)
        mCancelBtn = mContentView.findViewById(R.id.popup_cancel)
        mPickerView = mContentView.findViewById(R.id.popup_picker)

        // init PopupWindow
        contextRef.get()?.apply {
            mPopupWindow.width = ScreenUtils.getScreenWidth(this)
            mPopupWindow.height = ScreenUtils.dip2px(this, 200F)
            mPopupWindow.elevation = ScreenUtils.dip2px_5(this).toFloat()
        }
        Log.e("Picker", "width = ${mPopupWindow.width} height = ${mPopupWindow.height}")
        mPopupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        mPopupWindow.animationStyle = R.style.PopupAnimation
        mPopupWindow.isOutsideTouchable = true

        // init Picker
        mPickerView.setData(dataList)
        mPickerView.setSelected(0)
        mPickerView.setOnSelectListener(this)
    }

    private fun initEvents() {
        mConfirmBtn.setOnClickListener(this)
        mCancelBtn.setOnClickListener(this)
    }

    fun show() {
        Log.e("Picker", "show")
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        mPopupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0)
    }

    fun dismiss() {
        mPopupWindow.dismiss()
    }

    fun isShowing(): Boolean {
        return mPopupWindow.isShowing
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.popup_confirm -> {
                mListener.onConfirmPick(mPickItem)
                mPopupWindow.dismiss()
            }
            R.id.popup_cancel -> {
                mPopupWindow.dismiss()
            }
        }
    }

    override fun onSelect(item: String) {
        mPickItem = item
    }

    interface OnConfirmPickListener {
        fun onConfirmPick(value: String)
    }
}