package com.carson.gdufs_sign_system.manager.lookup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.model.MyActivityItemBean
import com.carson.gdufs_sign_system.widget.RoundImageView

class LookupTotalItemAdapter(private var mItemList: MutableList<MyActivityItemBean>,
                             private val mCallback: LookupItemClickCallback):
    RecyclerView.Adapter<LookupTotalItemAdapter.LookupTotalItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LookupTotalItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_lookup_total, parent, false)
        return LookupTotalItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    fun setData(itemList: MutableList<MyActivityItemBean>) {
        mItemList = itemList
    }

    override fun onBindViewHolder(holder: LookupTotalItemHolder, position: Int) {
        val itemModel = mItemList[position]
        holder.mStartTime.text = itemModel.startTime
        holder.mTitle.text = itemModel.name
        holder.mEndTime.text = itemModel.endTime
        holder.mImageView.setImageResource(R.drawable.gdufs_tmp)
        Glide.with(holder.itemView.context).load(itemModel.picUrl).into(holder.mImageView)
        holder.mCardView.setOnClickListener {
            mCallback.onItemClick(it, itemModel.signingId)
        }
    }

    inner class LookupTotalItemHolder(mItemView: View): RecyclerView.ViewHolder(mItemView) {
        val mStartTime: TextView = mItemView.findViewById(R.id.total_item_time)
        val mTitle: TextView = mItemView.findViewById(R.id.lookup_total_item_title)
        val mEndTime: TextView = mItemView.findViewById(R.id.lookup_total_item_end_time)
        val mCardView: CardView = mItemView.findViewById(R.id.lookup_item_view)
        val mImageView: RoundImageView = mItemView.findViewById(R.id.lookup_total_item_image)
    }

    interface LookupItemClickCallback {
        fun onItemClick(view: View, id: Long)
    }

}