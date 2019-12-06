package com.carson.gdufs_sign_system.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.carson.gdufs_sign_system.R

class TabSelector: LinearLayout, View.OnClickListener {

    enum class TAB {
        HOME, USER
    }

    private lateinit var mRoot: View
    private lateinit var mHomeLayout: LinearLayout
    private lateinit var mUserLayout: LinearLayout
    private lateinit var mHomeIcon: ImageView
    private lateinit var mUserIcon: ImageView

    private var mIconList: ArrayList<ImageView>? = null

    private var mSelectListener: OnTabSelectListener? = null

    private var mCurTab = TAB.HOME

    constructor(context: Context?): this(context, null)

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {
        initParams(context)
    }

    fun setSelectListener(listener: OnTabSelectListener) {
        this.mSelectListener = listener
    }

    private fun initParams(ctx: Context?) {
        mRoot = LayoutInflater.from(ctx).inflate(R.layout.tab_selector, this, true)
        mHomeLayout = mRoot.findViewById(R.id.home_selector)
        mUserLayout = mRoot.findViewById(R.id.user_selector)
        mHomeIcon = mRoot.findViewById(R.id.iv_home)
        mUserIcon = mRoot.findViewById(R.id.iv_user)
        mIconList = arrayListOf(mHomeIcon, mUserIcon)
        mHomeLayout.setOnClickListener(this)
        mUserLayout.setOnClickListener(this)
        gravity = Gravity.BOTTOM
//        background = ColorDrawable(Color.WHITE)
//        background.alpha = 128
//        mRoot.background.alpha = 128
        setSelected(TAB.HOME)
    }

    private fun setSelected(tab: TAB) {
        resetIcon()
        when (tab) {
            TAB.HOME -> {
                mHomeIcon.isSelected = true
            }
            TAB.USER -> {
                mUserIcon.isSelected = true
            }
        }
    }

    private fun resetIcon() {
        mIconList?.forEach {
            it.isSelected = false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.home_selector -> {
                if (mCurTab == TAB.HOME) {
                    mSelectListener?.onTabReselected(TAB.HOME.ordinal)
                } else {
                    mCurTab = TAB.HOME
                    setSelected(TAB.HOME)
                    mSelectListener?.onTabSelected(TAB.HOME.ordinal)
                }
            }
            R.id.user_selector -> {
                if (mCurTab == TAB.USER) {
                    mSelectListener?.onTabReselected(TAB.USER.ordinal)
                } else {
                    mCurTab = TAB.USER
                    setSelected(TAB.USER)
                    mSelectListener?.onTabSelected(TAB.USER.ordinal)
                }
            }
        }
    }

    interface OnTabSelectListener {
        fun onTabSelected(position: Int)
        fun onTabReselected(position: Int)
    }
}