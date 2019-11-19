package com.carson.gdufs_sign_system.login


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.LifeCallbackManager

class LoginFragment private constructor(private var mLoginController: LoginController?): BaseFragment(), View.OnClickListener {

    private lateinit var mContainer: View
    private lateinit var mEditUserName: EditText
    private lateinit var mEditPassword: EditText
    private lateinit var mBtnLogin: Button
    private lateinit var mBtnRegister: Button
    private lateinit var mTvForgetPassword: TextView

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainer = inflater.inflate(R.layout.fragment_login, container, false)
        initViews()
        initEvents()
        return mContainer
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                mLoginController?.login(mEditUserName.text.toString(), mEditPassword.text.toString())
            }
            R.id.btn_register -> {
                if (activity is LoginActivity) {
                    mLoginController?.goToRegister()
                }
            }
            R.id.tv_forget_password -> {
                // find password dialog
            }
        }
    }

    private fun initViews() {
        mEditUserName = mContainer.findViewById(R.id.et_username)
        mEditPassword = mContainer.findViewById(R.id.et_password)
        mBtnLogin = mContainer.findViewById(R.id.btn_login)
        mBtnRegister = mContainer.findViewById(R.id.btn_register)
        mTvForgetPassword = mContainer.findViewById(R.id.tv_forget_password)
    }

    private fun initEvents() {
        mBtnLogin.setOnClickListener(this)
        mBtnRegister.setOnClickListener(this)
        mTvForgetPassword.setOnClickListener(this)
    }

    companion object {
        private val TAG = "LoginFragment"
        @SuppressLint("StaticFieldLeak")
        private var instance: LoginFragment? = null
        fun newInstance(loginController: LoginController?): LoginFragment {
            if (instance == null) {
                instance = LoginFragment(loginController)
            }
            return instance as LoginFragment
        }
    }
}
