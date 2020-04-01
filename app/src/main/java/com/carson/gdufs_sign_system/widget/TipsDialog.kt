package com.carson.gdufs_sign_system.widget

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.utils.ScreenUtils
import java.lang.ref.WeakReference

class TipsDialog(private val contextRef: WeakReference<Context>) {
    private val mDialog: AlertDialog

    private val mContentView: View

    private val mTips: TextView
    private val mConfirmButton: TextView
    private val mCancelButton: TextView

    private var mListener: OnTipsDialogClickListener? = null

    init {
        mContentView = LayoutInflater.from(contextRef.get()).inflate(R.layout.layout_tips_dialog,
            null, false)
        mTips = mContentView.findViewById(R.id.tips_dialog_text)
        mConfirmButton = mContentView.findViewById(R.id.tips_dialog_confirm)
        mCancelButton = mContentView.findViewById(R.id.tips_dialog_cancel)

        mConfirmButton.setOnClickListener {
            mListener?.onConfirm(this)
        }

        mCancelButton.setOnClickListener {
            mListener?.onCancel(this)
        }

        mDialog = AlertDialog.Builder(contextRef.get()!!)
            .setView(mContentView)
            .setCancelable(true)
            .create()
    }

    fun setListener(listener: OnTipsDialogClickListener) {
        this.mListener = listener
    }

    fun setTips(tips: String?) {
        mTips.text = tips
    }

    fun isShowing(): Boolean = mDialog.isShowing

    fun dismiss() = mDialog.dismiss()

    fun show() {
        mDialog.show()

        val params = mDialog.window?.attributes
        params?.width = ScreenUtils.dip2px(contextRef.get()!!, 250F)
        params?.height = ScreenUtils.dip2px(contextRef.get()!!, 150F)
        mDialog.window?.attributes = params
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        mDialog.setContentView(mContentView)
    }

    interface OnTipsDialogClickListener {
        fun onConfirm(dialog: TipsDialog)

        fun onCancel(dialog: TipsDialog)
    }
}