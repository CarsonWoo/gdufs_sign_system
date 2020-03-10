package com.carson.gdufs_sign_system.manager.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity

class PostActivity : BaseActivity() {
    override fun getContentViewResId(): Int = R.layout.activity_post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
    }
}
