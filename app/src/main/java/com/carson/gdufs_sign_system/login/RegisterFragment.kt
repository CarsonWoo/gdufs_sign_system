package com.carson.gdufs_sign_system.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.login.controller.RegisterController

class RegisterFragment : BaseFragment(), View.OnClickListener {

    companion object {
        private const val FRAGMENT_TAG = "Register"
        private val TAG = "RegisterFragment"
        @JvmStatic
        fun newInstance() = RegisterFragment().apply {
        }
    }

    private lateinit var mContainer: View
    private lateinit var mEditUsername: EditText
    private lateinit var mEditPassword: EditText
    private lateinit var mEditConfirmPwd: EditText
    private lateinit var mEditCode: EditText
    private lateinit var mBtnGetCode: Button
    private lateinit var mBtnRegister: Button
    private lateinit var mEditStudentId: EditText

    private val mRegisterController =
        RegisterController(this)

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

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_get_code -> {

            }
            R.id.btn_register -> {
                if (validPassword()) {
                    mRegisterController.register(mEditUsername.text.toString(), mEditPassword.text.toString(), mEditCode.text.toString(), mEditStudentId.text.toString())
//                    mLoginController?.register(mEditUsername.text.toString(), mEditPassword.text.toString(), mEditCode.text.toString())
                } else {
                    Toast.makeText(context, "invalid password confirm.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return mRegisterController.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRegisterController.onDestroy()
    }

    private fun initViews() {
        mEditUsername = mContainer.findViewById(R.id.et_username)
        mEditPassword = mContainer.findViewById(R.id.et_password)
        mEditConfirmPwd = mContainer.findViewById(R.id.et_confirm_password)
        mEditStudentId = mContainer.findViewById(R.id.et_student_id)
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