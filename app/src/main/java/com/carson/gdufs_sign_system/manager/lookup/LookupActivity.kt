package com.carson.gdufs_sign_system.manager.lookup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity

class LookupActivity : BaseActivity() {
    override fun getContentViewResId(): Int {
        return R.layout.activity_lookup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
    }
}
