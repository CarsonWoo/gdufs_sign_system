package com.carson.gdufs_sign_system.login

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.transition.TransitionInflater
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.manager.manage.ManageActivity
import com.carson.gdufs_sign_system.student.main.MainActivity
import com.carson.gdufs_sign_system.utils.Const
import java.lang.ref.WeakReference

class SplashFragment : BaseFragment() {

    companion object {
        private const val FRAGMENT_TAG = "Splash"
        private const val TAG = "SplashFragment"

        @SuppressLint("StaticFieldLeak")
        private var mInstance: SplashFragment? = null

        fun newInstance(): SplashFragment {
            if (mInstance == null) {
                mInstance = SplashFragment()
            }
            return mInstance!!
        }
    }

    private lateinit var mRoot: View

    private lateinit var mLogoView: ImageView
    private lateinit var mLogoText: TextView

    private var mAnimatorSet: AnimatorSet? = null

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = inflater.inflate(R.layout.fragment_splash, container, false)
        initViews()
        initEvents()
        return mRoot
    }

    private fun initViews() {
        mLogoView = mRoot.findViewById(R.id.splash_logo_view)
        mLogoText = mRoot.findViewById(R.id.splash_logo_text)
    }

    private fun initEvents() {
        val alphaLogoAnimator = ObjectAnimator.ofFloat(mLogoView, "alpha",
            0F, 1F)
        val scaleXAnimator = ObjectAnimator.ofFloat(mLogoView, "scaleX",
            0F, 1F)
        val scaleYAnimator = ObjectAnimator.ofFloat(mLogoView, "scaleY",
            0F, 1F)
        val alphaTextAnimator = ObjectAnimator.ofFloat(mLogoText, "alpha",
            0F, 1F)
        mAnimatorSet = AnimatorSet().apply {
            this.playTogether(alphaLogoAnimator, scaleXAnimator, scaleYAnimator, alphaTextAnimator)
            duration = 2 * 1000L
        }
        mAnimatorSet?.start()
        mAnimatorSet?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                startToLogin()
            }

            override fun onAnimationCancel(animation: Animator?) {
                startToLogin()
            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }

    private fun startToLogin() {
        if (Const.getSharedPreference(WeakReference(context))
                ?.getString(Const.PreferenceKeys.USER_ID, "")?.isNotEmpty() == true) {
            Log.e(
                TAG, "identity = ${Const.getSharedPreference(WeakReference(context))
                    ?.getString(Const.PreferenceKeys.IDENTITY, "0")}")
            // 直接跳主页
            jumpToMain(Const.getSharedPreference(WeakReference(context))
                ?.getString(Const.PreferenceKeys.IDENTITY, "0"))
        } else {
            (activity as BaseFragmentActivity?)?.apply {
                setFragmentAnimation(R.anim.slide_right_in, R.anim.slide_left_out)
                hide("Splash")
                show("Login")
            }
        }
    }

    private fun jumpToMain(identity: String?) {
        if (identity == "0") {
            // 学生端
            // to student
            (activity as BaseActivity?)?.let {
                val toMain = Intent(it, MainActivity::class.java)
                // set data bundle
                it.startActivity(toMain)
                it.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
                it.finish(LoginActivity::class.java.name)
            }
        } else {
            // 管理端
            // to manager
            (activity as BaseActivity?)?.let {
                val toManage = Intent(it, ManageActivity::class.java)
                it.startActivity(toManage)
                it.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
                it.finish(LoginActivity::class.java.name)
            }
        }
    }

    override fun onDestroy() {
        mAnimatorSet?.cancel()
        super.onDestroy()
    }
}