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
import com.carson.gdufs_sign_system.student.main.model.SignItem
import com.carson.gdufs_sign_system.utils.ScreenUtils

class HomeSignItemAdapter: RecyclerView.Adapter<HomeSignItemAdapter.HomeItemViewHolder> {

    private val mItemList: MutableList<SignItem>

    private var mSignClickListener: OnSignClickListener? = null

    constructor(itemList: MutableList<SignItem>): super() {
        this.mItemList = itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_sign_item, parent, false)
        return HomeItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
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
        Glide.with(holder.mPic.context).load(item.picUrl).into(holder.mPic)
        holder.apply {
            mName.text = item.activityName
            mDate.text = item.date
            mPeopleNum.text = mPeopleNum.context.resources.getString(R.string.sign_people, item.signedPeople.toString())
            mBtnSign.setOnClickListener {
                mSignClickListener?.onSignClick(position)
            }
        }
    }

    fun setSignClickListener(listener: OnSignClickListener) {
        this.mSignClickListener = listener
    }

    inner class HomeItemViewHolder(val mItemView: View): RecyclerView.ViewHolder(mItemView) {

        val mPic = mItemView.findViewById<ImageView>(R.id.home_item_pic)
        val mName = mItemView.findViewById<TextView>(R.id.home_item_activity_name)
        val mDate = mItemView.findViewById<TextView>(R.id.home_item_sign_time)
        val mPeopleNum = mItemView.findViewById<TextView>(R.id.home_item_sign_people)
        val mBtnSign = mItemView.findViewById<Button>(R.id.btn_sign)
    }

    interface OnSignClickListener {
        fun onSignClick(position: Int)
    }
}