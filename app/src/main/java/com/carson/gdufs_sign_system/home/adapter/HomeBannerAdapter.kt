package com.carson.gdufs_sign_system.home.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.carson.gdufs_sign_system.utils.ScreenUtils
import com.carson.gdufs_sign_system.widget.BannerDot
import com.carson.gdufs_sign_system.widget.RoundImageView
import kotlin.math.abs

class HomeBannerAdapter(bannerList: MutableList<String>) : PagerAdapter(),
    ViewPager.OnPageChangeListener {

    companion object {
        const val TAG = "HomeBannerAdapter"
    }

    private val mBannerList: MutableList<String> = bannerList
    private val mImgList = ArrayList<RoundImageView>()
    private var mSize: Int = 0
    private var mBannerDot: BannerDot? = null
    private lateinit var mViewPager: ViewPager

    private var mCurrentPosition = 0

    init {
        mSize = mBannerList.size
        mBannerList.add(0, bannerList[mSize - 1])
        mBannerList.add(bannerList[0])
    }

    fun setUpWithBannerDot(bannerDot: BannerDot) {
        this.mBannerDot = bannerDot
        mBannerDot?.initParam(mSize)
        mBannerDot?.isSupportInfinite(true)
    }

    fun setUpWithViewPager(viewPager: ViewPager) {
        this.mViewPager = viewPager
        mViewPager.pageMargin = ScreenUtils.dip2px_10(viewPager.context)
        mViewPager.offscreenPageLimit = mSize
        mViewPager.adapter = this@HomeBannerAdapter
        mViewPager.setPageTransformer(true, AlphaTransformer())
        mViewPager.addOnPageChangeListener(this@HomeBannerAdapter)
        mViewPager.setCurrentItem(1, false)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imgView = RoundImageView(container.context)
//        if (mImgList.size < position) {
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imgView.layoutParams = params
        imgView.scaleType = ImageView.ScaleType.CENTER_CROP
        imgView.setRadius(ScreenUtils.dip2px_10(container.context))
//            mImgList.add(imgView)
//        }/* else {
//            imgView = mImgList[position]
//        }*/
        container.addView(imgView)
        Glide.with(container.context).load(mBannerList[position]).into(imgView)
        return imgView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return mBannerList.size
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (mCurrentPosition == 0) {
                mViewPager.setCurrentItem(mBannerList.size - 2, false)
            } else if (mCurrentPosition == mBannerList.size - 1) {
                mViewPager.setCurrentItem(1, false)
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        Log.i(TAG, "onPageScrolled: position = $position")
    }

    override fun onPageSelected(position: Int) {
        Log.i(TAG, "position = $position")
        mCurrentPosition = position
        mBannerDot?.selectDot(position)
    }

    class AlphaTransformer: ViewPager.PageTransformer {

        companion object {
            private const val MIN_ALPHA = 0.5F
        }

        override fun transformPage(page: View, position: Float) {
            val alphaFactor = MIN_ALPHA + (1 - MIN_ALPHA) * (1 - abs(position))
            page.alpha = alphaFactor
        }
    }
}