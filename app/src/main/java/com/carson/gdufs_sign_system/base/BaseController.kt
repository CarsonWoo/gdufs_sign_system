package com.carson.gdufs_sign_system.base

abstract class BaseController<T: BaseFragment?>(protected var mFragment: T?) {
    open fun onDestroy() {
        mFragment = null
    }
}