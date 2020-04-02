package com.carson.gdufs_sign_system.widget

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carson.gdufs_sign_system.R

class EmptyViewHolder(val mItemView: View): RecyclerView.ViewHolder(mItemView) {
    val emptyText: TextView = mItemView.findViewById(R.id.empty_text)
}