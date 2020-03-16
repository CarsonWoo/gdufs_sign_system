package com.carson.gdufs_sign_system.manager.lookup.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.manager.lookup.model.SingleItemModel
import com.carson.gdufs_sign_system.widget.CircleImageView
import com.carson.gdufs_sign_system.widget.RoundImageView

class LookupSingleItemAdapter(private val mItemList: MutableList<SingleItemModel>) : RecyclerView.Adapter<LookupSingleItemAdapter.LookupSingleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LookupSingleHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_lookup_single, parent, false)
        return LookupSingleHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun onBindViewHolder(holder: LookupSingleHolder, position: Int) {
        val model = mItemList[position]
        holder.mClazz.text = model.clazz
        holder.mUsername.text = model.username
//        holder.mPortrait.setImageResource(R.drawable.icon_student)
        if (model.isSigned) {
            holder.mStatus.text = "已签到"
            val drawable = holder.mRoot.context.resources.getDrawable(R.drawable.status_pass)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            holder.mStatus.setCompoundDrawables(drawable,
                null, null, null)
            holder.mStatus.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            holder.mStatus.text = "未签到"
            val drawable = holder.mRoot.context.resources.getDrawable(R.drawable.status_unpass)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            holder.mStatus.setCompoundDrawables(drawable,
                null, null, null)
            holder.mStatus.setTextColor(Color.parseColor("#F44336"))
        }
    }

    inner class LookupSingleHolder(mItemView: View): RecyclerView.ViewHolder(mItemView) {

        val mRoot: RelativeLayout = mItemView.findViewById(R.id.lookup_single_container)
        val mPortrait: RoundImageView = mItemView.findViewById(R.id.lookup_single_portrait)
        val mUsername: TextView = mItemView.findViewById(R.id.lookup_single_username)
        val mClazz: TextView = mItemView.findViewById(R.id.lookup_single_clazz)
        val mStatus: TextView = mItemView.findViewById(R.id.lookup_single_status)

    }
}