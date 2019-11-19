package com.carson.gdufs_sign_system.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment

class RegisterFragment private constructor(private var mLoginController: LoginController?) : BaseFragment(), View.OnClickListener {

    companion object {
        private val TAG = "RegisterFragment"
        @SuppressLint("StaticFieldLeak")
        private var mInstance: RegisterFragment? = null
        fun newInstance(loginController: LoginController?): RegisterFragment {
            if (mInstance == null) mInstance = RegisterFragment(loginController)
            return mInstance as RegisterFragment
        }
    }

    private lateinit var mContainer: View
    private lateinit var mEditUsername: EditText
    private lateinit var mEditPassword: EditText
    private lateinit var mEditConfirmPwd: EditText
    private lateinit var mEditCode: EditText
    private lateinit var mBtnGetCode: Button
    private lateinit var mBtnRegister: Button

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContainer = inflater.inflate(R.layout.fragment_register, container, false)
        initViews()
        initEvents()
        return mContainer
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_get_code -> {

            }
            R.id.btn_register -> {
                if (validPassword()) {
                    mLoginController?.register(mEditUsername.text.toString(), mEditPassword.text.toString(), mEditCode.text.toString())
                } else {
                    Toast.makeText(context, "invalid password confirm.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {

    }

    private fun initViews() {
        mEditUsername = mContainer.findViewById(R.id.et_username)
        mEditPassword = mContainer.findViewById(R.id.et_password)
        mEditConfirmPwd = mContainer.findViewById(R.id.et_confirm_password)
        mEditCode = mContainer.findViewById(R.id.et_code)
        mBtnGetCode = mContainer.findViewById(R.id.btn_get_code)
        mBtnRegister = mContainer.findViewById(R.id.btn_register)
    }

    private fun initEvents() {
        mBtnGetCode.setOnClickListener(this)
        mBtnRegister.setOnClickListener(this)
    }

    private fun validPassword(): Boolean {
        return mEditPassword.text.toString() == mEditConfirmPwd.text.toString()
    }

}