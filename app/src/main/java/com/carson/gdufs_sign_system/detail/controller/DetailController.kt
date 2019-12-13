package com.carson.gdufs_sign_system.detail.controller

import androidx.core.widget.NestedScrollView
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.detail.DetailFragment
import com.carson.gdufs_sign_system.detail.IViewCallback

class DetailController(detailFragment: DetailFragment, private val mIView: IViewCallback): BaseController<DetailFragment>(detailFragment), NestedScrollView.OnScrollChangeListener {
    companion object {
        private const val TAG = "DetailController"
        private const val SCROLL_BASE = 100F
    }

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        val scrollMax = v?.getChildAt(0)?.height?.minus(v.measuredHeight)
        if (scrollY > SCROLL_BASE) {
            scrollMax?.apply {
                mIView.onFabShow((scrollY - SCROLL_BASE) / (this.toFloat() - SCROLL_BASE))
            }
        }
    }

}