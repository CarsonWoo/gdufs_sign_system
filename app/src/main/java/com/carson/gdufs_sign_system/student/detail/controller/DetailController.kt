package com.carson.gdufs_sign_system.student.detail.controller

import android.Manifest
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.student.detail.DetailActivity
import com.carson.gdufs_sign_system.student.detail.DetailFragment
import com.carson.gdufs_sign_system.student.detail.IViewCallback
import com.carson.gdufs_sign_system.student.sign.SignActivity
import com.carson.gdufs_sign_system.utils.PermissionUtils

class DetailController(detailFragment: DetailFragment, private val mIView: IViewCallback): BaseController<DetailFragment>(detailFragment),
    NestedScrollView.OnScrollChangeListener, View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.detail_back -> {
                (mFragment?.activity as DetailActivity?)?.onBackPressed()
            }
            R.id.detail_sign_fab -> {
                mFragment?.let {
                    PermissionUtils.getInstance().with(it).requestCode(PermissionUtils.CODE_MULTI)
                        .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        .request(object : PermissionUtils.PermissionCallback {
                            override fun denied() {
                                PermissionUtils.getInstance().showDialog()
                            }

                            override fun granted() {
                                (mFragment?.activity as DetailActivity?)?.apply {
                                    val toSign = Intent(this, SignActivity::class.java)
                                    startActivity(toSign)
                                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
                                }
                            }
                        })
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