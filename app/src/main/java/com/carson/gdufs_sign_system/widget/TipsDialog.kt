package com.carson.gdufs_sign_system.widget

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.carson.gdufs_sign_system.R
import java.lang.ref.WeakReference

class TipsDialog(private val contextRef: WeakReference<Context>) {
    private val mDialog: AlertDialog

    private val mTips: TextView
    private val mConfirmButton: TextView
    private val mCancelButton: TextView

    private var mListener: OnTipsDialogClickListener? = null

    init {
        val contentView = LayoutInflater.from(contextRef.get()).inflate(R.layout.layout_tips_dialog,
            null, false)
        mTips = contentView.findViewById(R.id.tips_dialog_text)
        mConfirmButton = contentView.findViewById(R.id.tips_dialog_confirm)
        mCancelButton = contentView.findViewById(R.id.tips_dialog_cancel)

        mConfirmButton.setOnClickListener {
            mListener?.onConfirm(this)
        }

        mCancelButton.setOnClickListener {
            mListener?.onCancel(this)
        }

        mDialog = AlertDialog.Builder(contextRef.get()!!)
            .setView(contentView)
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

    fun show() = mDialog.show()

    interface OnTipsDialogClickListener {
        fun onConfirm(dialog: TipsDialog)

        fun onCancel(dialog: TipsDialog)
    }
}