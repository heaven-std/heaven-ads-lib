package com.heaven.android.heavenlib

import android.app.Application
import com.heaven.android.heavenlib.ads.AppOpenManager
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.datas.HeavenSharePref
import com.heaven.android.heavenlib.views.splash.SplashActivity
import java.lang.RuntimeException

abstract class HeavenApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //
        initData()
        initOtherFunc()
        initFireBase()
        initOpenAd()
    }

    abstract fun initOtherFunc()

    private fun initData() {
        HeavenSharePref.init(applicationContext)
    }

    private fun initFireBase() {
        FBConfig.config()
    }

    private fun initOpenAd() {
        val id = HeavenEnv.configAdUnitID.resumeAll
        if (id.isEmpty()) {
            throw RuntimeException("HeavenApplication: ResumeAll ad unit ID not found!")
        }
        AppOpenManager.instance?.init(this, id)
        AppOpenManager.instance?.disableAppResumeWithActivity(SplashActivity::class.java)
    }

}