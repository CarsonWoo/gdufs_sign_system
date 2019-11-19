package com.carson.gdufs_sign_system.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity

class LoginActivity : BaseActivity() {
    override fun getContentViewResId(): Int {
        return R.layout.activity_login
    }

    private lateinit var mContainer: ConstraintLayout
    private val mLoginController = LoginController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContainer = findViewById(R.id.container)
        // 默认一进来先加载登录页
        initTab()
    }

    private fun initTab() {
        mLoginController.initTab()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoginController.onDestroy()
    }

    override fun onBackPressed() {
        if (!mLoginController.onBackPressed()) {
            super.onBackPressed()
        }
    }


}
