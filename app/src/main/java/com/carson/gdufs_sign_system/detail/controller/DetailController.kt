package com.carson.gdufs_sign_system.detail.controller

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.detail.DetailActivity
import com.carson.gdufs_sign_system.detail.DetailFragment
import com.carson.gdufs_sign_system.detail.IViewCallback
import com.carson.gdufs_sign_system.sign.SignActivity

class DetailController(detailFragment: DetailFragment, private val mIView: IViewCallback): BaseController<DetailFragment>(detailFragment),
    NestedScrollView.OnScrollChangeListener, View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.detail_back -> {
                (mFragment?.activity as DetailActivity?)?.onBackPressed()
            }
            R.id.detail_sign_fab -> {
                (mFragment?.activity as DetailActivity?)?.apply {
                    val toSign = Intent(this, SignActivity::class.java)
                    startActivity(toSign)
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
                }
            }
        }
    }

    companion object {
        private const val TAG = "DetailController"
        private const val SCROLL_BASE = 30F
    }

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        val scrollMax = v?.getChildAt(0)?.height?.minus(v.measuredHeight)
        if (scrollY >= SCROLL_BASE) {
            scrollMax?.apply {
                Log.i(TAG , "${(scrollY - SCROLL_BASE) / (this.toFloat() - SCROLL_BASE)}")
                mIView.onFabShow((scrollY - SCROLL_BASE) / (this.toFloat() - SCROLL_BASE))
            }
        }
    }

}