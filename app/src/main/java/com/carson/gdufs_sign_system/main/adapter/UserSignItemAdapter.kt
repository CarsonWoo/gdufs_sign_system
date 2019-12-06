package com.carson.gdufs_sign_system.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.main.model.SignItem
import com.carson.gdufs_sign_system.utils.ScreenUtils

class UserSignItemAdapter(private val mList: MutableList<SignItem>):
    RecyclerView.Adapter<UserSignItemAdapter.UserItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_sign_item, parent, false)
        return UserItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.mItemView.apply {
            val params = layoutParams as RecyclerView.LayoutParams
            params.width = ScreenUtils.dip2px(context, 180F)
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
        Glide.with(holder.mPic.context).load(item.picUrl).into(holder.mPic)
        holder.apply {
            mName.text = item.activityName
            mDate.text = item.date
            mPeopleNum.text = mPeopleNum.context.resources.getString(R.string.sign_people, item.signedPeople.toString())
        }
    }

    inner class UserItemViewHolder(val mItemView: View): RecyclerView.ViewHolder(mItemView) {
        val mPic = mItemView.findViewById<ImageView>(R.id.home_item_pic)
        val mName = mItemView.findViewById<TextView>(R.id.home_item_activity_name)
        val mDate = mItemView.findViewById<TextView>(R.id.home_item_sign_time)
        val mPeopleNum = mItemView.findViewById<TextView>(R.id.home_item_sign_people)
        val mBtnSign = mItemView.findViewById<Button>(R.id.btn_sign)

        init {
            mBtnSign.visibility = View.GONE
        }
    }

}