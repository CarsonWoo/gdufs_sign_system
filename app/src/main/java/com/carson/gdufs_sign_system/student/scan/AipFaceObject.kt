package com.carson.gdufs_sign_system.student.scan

import com.baidu.aip.face.AipFace
import com.baidu.aip.util.Base64Util
import com.carson.gdufs_sign_system.utils.Const

class AipFaceObject private constructor() {

    companion object {
        private var mClient: AipFace? = null

        @Synchronized
        fun getClient(): AipFace {
            if (mClient == null) {
                mClient = AipFace(Const.BAIDU_APP_ID, Const.BAIDU_API_KEY, Const.BAIDU_SECRET_KEY)
                mClient?.setConnectionTimeoutInMillis(2000)
            }
            return mClient!!
        }
    }
}