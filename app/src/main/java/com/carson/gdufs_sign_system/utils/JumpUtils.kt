package com.carson.gdufs_sign_system.utils

import android.content.Intent

interface JumpCallback {
    fun startActivity(intent: Intent)

    fun startActivityForResult(intent: Intent, resultCode: Int)
}