package com.carson.gdufs_sign_system.manager.post.adapter

import android.graphics.Color
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carson.gdufs_sign_system.R

class PopupMultiItemAdapter(private val mDataList: MutableList<PopupMultiItem>)
    : RecyclerView.Adapter<PopupMultiItemAdapter.PopupMultiItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopupMultiItemHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_multi_select, parent, false)
        return PopupMultiItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: PopupMultiItemHolder, position: Int) {
        // 默认还原
        holder.mItemView.isSelected = false
        holder.mTextView.setTextColor(holder.mItemView.context.resources.getColor(R.color.alphaColorCyan))
        // 开始填数据
        holder.mTextView.text = mDataList[position].text
        var isSelected = mDataList[position].isSelected
        holder.mItemView.isSelected = isSelected
        holder.mTextView.setTextColor(
            if (isSelected) Color.WHITE
            else holder.mItemView.context.resources.getColor(R.color.alphaColorCyan)
        )
        holder.mItemView.setOnTouchListener { v, event ->
//            Log.e("PopupMultiItemAdapter", "onTouch")
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    isSelected = !isSelected
                    mDataList[position].isSelected = isSelected
                    holder.mItemView.isSelected = isSelected
//                    Log.e("PopupMultiItemAdapter", "position = $position isSelected = $isSelected mItemView.isSelected = ${holder.mItemView.isSelected}")
                    holder.mTextView.setTextColor(
                        if (isSelected) Color.WHITE
                        else holder.mItemView.context.resources.getColor(R.color.alphaColorCyan)
                    )
                    return@setOnTouchListener true
                }
                else -> {
                    return@setOnTouchListener false
                }
            }
        }
//        holder.mItemView.setOnClickListener {
//            isSelected = !isSelected
//            mDataList[position].isSelected = isSelected
//            holder.mItemView.isSelected = isSelected
//            Log.e("PopupMultiItemAdapter", "position = $position isSelected = $isSelected mItemView.isSelected = ${holder.mItemView.isSelected}")
//            holder.mTextView.setTextColor(
//                if (isSelected) Color.WHITE
//                else holder.mItemView.context.resources.getColor(R.color.alphaColorCyan)
//            )
//        }
    }

    fun getResult(): String {
        val buffer = StringBuffer()
        mDataList.forEach {
            if (it.isSelected) {
                buffer.append("${it.text} ")
            }
        }
        Log.e("PopupMultiItemAdapter", buffer.toString())
        return buffer.toString()
    }

    inner class PopupMultiItemHolder(val mItemView: View) : RecyclerView.ViewHolder(mItemView) {
        val mTextView: TextView = mItemView.findViewById(R.id.item_multi_select_text)
    }
}

data class PopupMultiItem(
    val text: String,
    var isSelected: Boolean
)