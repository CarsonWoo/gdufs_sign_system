package com.carson.gdufs_sign_system.student.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.model.SignBean
import com.carson.gdufs_sign_system.utils.ScreenUtils
import com.carson.gdufs_sign_system.widget.EmptyViewHolder

class HomeSignItemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    companion object {
        private const val EMPTY = 1
        private const val ITEM = 2
    }

    private var mItemList: MutableList<SignBean>

    private var mSignClickListener: OnSignClickListener? = null

    constructor(itemList: MutableList<SignBean>): super() {
        this.mItemList = itemList
    }

    fun setData(itemList: MutableList<SignBean>) {
        this.mItemList = itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            EMPTY -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_empty, parent, false)
                EmptyViewHolder(view)
            }
            ITEM -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.home_sign_item, parent, false)
                HomeItemViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_empty, parent, false)
                EmptyViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (mItemList.size == 0) 1 else mItemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HomeItemViewHolder) {
            holder.mItemView.apply {
                val params = layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = ScreenUtils.dip2px_20(context)
                if (position % 2 == 0) {
                    // 左侧item
                    params.leftMargin = ScreenUtils.dip2px_20(context)
                    params.rightMargin = ScreenUtils.dip2px_10(context)
                } else {
                    params.leftMargin = ScreenUtils.dip2px_10(context)
                    params.rightMargin = ScreenUtils.dip2px_20(context)
                }
                this.layoutParams = params
            }
            val item = mItemList[position]
            Glide.with(holder.mPic.context).load(item.picUrl).thumbnail(0.5F).into(holder.mPic)
            holder.apply {
                mName.text = item.name
                mDate.text = item.startTime
                mPeopleNum.text = mPeopleNum.context.resources.getString(R.string.sign_people, item.num.toString())
                if (item.status == "已签到") {
                    mBtnSign.text = "已签到"
                    mBtnSign.isEnabled = false
                } else {
                    mBtnSign.text = "签到"
                    mBtnSign.isEnabled = true
                }
                mBtnSign.setOnClickListener {
                    mSignClickListener?.onSignClick(item.id)
                }
                mItemView.setOnClickListener {
                    mSignClickListener?.onSignClick(item.id)
                }
            }
        } else if (holder is EmptyViewHolder) {
            // 空
            holder.emptyText.text = "还没有签到活动哦"
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (mItemList.size == 0) {
            return EMPTY
        } else {
            return ITEM
        }
    }

    fun setSignClickListener(listener: OnSignClickListener) {
        this.mSignClickListener = listener
    }

    inner class HomeItemViewHolder(val mItemView: View): RecyclerView.ViewHolder(mItemView) {

        val mPic: ImageView = mItemView.findViewById(R.id.home_item_pic)
        val mName: TextView = mItemView.findViewById(R.id.home_item_activity_name)
        val mDate: TextView = mItemView.findViewById(R.id.home_item_sign_time)
        val mPeopleNum: TextView = mItemView.findViewById(R.id.home_item_sign_people)
        val mBtnSign: Button = mItemView.findViewById(R.id.btn_sign)
    }

    interface OnSignClickListener {
        fun onSignClick(id: Long)
    }
}