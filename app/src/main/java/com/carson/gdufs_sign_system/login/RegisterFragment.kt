package com.carson.gdufs_sign_system.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.login.controller.RegisterController

class RegisterFragment : BaseFragment(), View.OnClickListener, IRegisterCallback {

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
    private lateinit var mBtnRegister: Button
    private lateinit var mEditStudentId: EditText
    private lateinit var mTextClazz: TextView

    private lateinit var mRoot: ConstraintLayout

    private val mRegisterController =
        RegisterController(this, this)

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
            R.id.btn_register -> {
                hideSoftInput()
                if (validPassword()) {
                    mRegisterController.register(mEditUsername.text.toString(), mEditPassword.text.toString(),
                        mTextClazz.text.toString(), mEditStudentId.text.toString())
                } else {
                    Toast.makeText(context, "invalid password confirm.", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.et_clazz -> {
                hideSoftInput()
                mRegisterController.initPopupWindow(mRoot)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return mRegisterController.onBackPressed()
    }

    override fun onDestroy() {
        mRegisterController.onDestroy()
        super.onDestroy()
    }

    private fun initViews() {
        mRoot = mContainer.findViewById(R.id.register_container)
        mEditUsername = mContainer.findViewById(R.id.et_username)
        mEditPassword = mContainer.findViewById(R.id.et_password)
        mEditConfirmPwd = mContainer.findViewById(R.id.et_confirm_password)
        mEditStudentId = mContainer.findViewById(R.id.et_student_id)
        mTextClazz = mContainer.findViewById(R.id.et_clazz)
        mBtnRegister = mContainer.findViewById(R.id.btn_register)
    }

    private fun initEvents() {
        mBtnRegister.setOnClickListener(this)
        mTextClazz.setOnClickListener(this)
    }

    private fun validPassword(): Boolean {
        return mEditPassword.text.toString() == mEditConfirmPwd.text.toString()
    }

    private fun hideSoftInput() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(mContainer.windowToken, 0)
    }

    override fun onShowClazz(clazz: String?) {
        mTextClazz.text = clazz
    }

    override fun onRegisterSuccess() {
        mEditUsername.text = null
        mEditStudentId.text = null
        mEditConfirmPwd.text = null
        mEditPassword.text = null
        mTextClazz.text = null
    }

}