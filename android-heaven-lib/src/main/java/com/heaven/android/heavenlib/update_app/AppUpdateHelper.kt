package com.heaven.android.heavenlib.update_app

interface AppUpdateHelper {
    fun checkUpdate(listener: IAppUpdateListener)
    fun openStore()
}