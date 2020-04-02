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

class UserSignItemAdapter(private var mList: MutableList<SignBean>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val EMPTY = 0
        private const val ITEM = 1
    }

    fun setData(list: MutableList<SignBean>) {
        mList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            ITEM -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.home_sign_item, parent, false)
                UserItemViewHolder(view)
            }
            EMPTY -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_empty, parent, false)
                EmptyViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_empty, parent, false)
                EmptyViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (mList.size == 0) 1 else mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mList.size == 0) EMPTY else ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserItemViewHolder) {
            holder.mItemView.apply {
                val params = layoutParams as RecyclerView.LayoutParams
                params.width = ScreenUtils.dip2px(context, 160F)
                when (position) {
                    0 -> {
                        // 最左侧item
                        params.leftMargin = ScreenUtils.dip2px_20(context)
                        params.rightMargin = ScreenUtils.dip2px_10(context)
                    }
                    mList.size - 1 -> {
                        // 最右侧item
                        params.leftMargin = ScreenUtils.dip2px_10(context)
                        params.rightMargin = ScreenUtils.dip2px_20(context)
                    }
                    else -> {
                        params.leftMargin = ScreenUtils.dip2px_10(context)
                        params.rightMargin = ScreenUtils.dip2px_10(context)
                    }
                }
                this.layoutParams = params
            }
            val item = mList[position]
            Glide.with(holder.mPic.context).load(item.picUrl).thumbnail(0.5F).into(holder.mPic)
            holder.apply {
                mName.text = item.name
                mDate.text = item.signingTime
                mPeopleNum.text = mPeopleNum.context.resources.getString(R.string.sign_people, item.num.toString())
            }
        } else if (holder is EmptyViewHolder) {
            holder.emptyText.text = "还没有已签到的活动哦~"
            val params = holder.mItemView.layoutParams
            params.width = ScreenUtils.getScreenWidth(holder.mItemView.context) - ScreenUtils.dip2px_20(holder.mItemView.context)
            params.height = ScreenUtils.dip2px(holder.mItemView.context, 230F)
            holder.mItemView.layoutParams = params
        }
    }

    inner class UserItemViewHolder(val mItemView: View): RecyclerView.ViewHolder(mItemView) {
        val mPic: ImageView = mItemView.findViewById(R.id.home_item_pic)
        val mName: TextView = mItemView.findViewById(R.id.home_item_activity_name)
        val mDate: TextView = mItemView.findViewById(R.id.home_item_sign_time)
        val mPeopleNum: TextView = mItemView.findViewById(R.id.home_item_sign_people)
        private val mBtnSign: Button = mItemView.findViewById(R.id.btn_sign)

        init {
            mBtnSign.visibility = View.GONE
        }
    }

}