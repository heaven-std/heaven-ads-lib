package com.heaven.android.heavenlib.views.splash

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.gms.ads.MobileAds
import com.heaven.android.heavenlib.ads.HeavenInterstitialAD
import com.heaven.android.heavenlib.ads.HeavenNativeAd
import com.heaven.android.heavenlib.base.activity.BaseActivity
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.databinding.ActivitySplashBinding
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.datas.HeavenSharePref
import com.heaven.android.heavenlib.datas.IRemoteCFListener
import com.heaven.android.heavenlib.ump.IUmpListener
import com.heaven.android.heavenlib.ump.UMPHelper
import com.heaven.android.heavenlib.ump.UmpHelperImpl
import com.heaven.android.heavenlib.update_app.AppUpdateHelper
import com.heaven.android.heavenlib.update_app.AppUpdateHelperImpl
import com.heaven.android.heavenlib.update_app.IAppUpdateListener
import com.heaven.android.heavenlib.utils.Logger
import com.heaven.android.heavenlib.views.intro.IntroActivity
import com.heaven.android.heavenlib.views.language.LanguageActivity
import kotlinx.coroutines.async
import java.util.concurrent.atomic.AtomicBoolean

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
    private val adUnitInter = HeavenEnv.configAdUnitID.interSplash
    private val configApp = HeavenEnv.configStyleApp

    private lateinit var umpHelper: UMPHelper
    private lateinit var appUpdateHelper: AppUpdateHelper

    override fun makeBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        //
        umpHelper = UmpHelperImpl(this)
        appUpdateHelper = AppUpdateHelperImpl(this)
        //setup view
        with(binding) {
            imgBg.setImageResource(configApp.backgroundApp)
            imgSplash.setImageResource(HeavenEnv.configSplash.imageSplash)
            tvSplash.text = getString(HeavenEnv.configSplash.titleSplash)
            tvSplash.setTextColor(HeavenEnv.configSplash.textColorTitleSplash)
            tvSplash.textSize = HeavenEnv.configSplash.textSizeTitleSplash.toFloat()
            tvSubtitle.setTextColor(HeavenEnv.configSplash.textColorTitleSplash)
        }
        // request consent
        requestConsent()
    }

    private fun requestConsent() {
        umpHelper.requestConsent(object : IUmpListener {
            override fun requestConsentCompleted(err: String?) {
                Logger.log("requestConsent", "$err")
                initMobileAds()
            }
        })
    }

    private fun fetchRMCF() {
        FBConfig.getFirebaseConfig(object : IRemoteCFListener {
            override fun onRMCFLoadCompleted(error: String?) {
                Logger.log("fetchRMCF", "Error RMCF: $error")
                checkUpdate()
            }
        })
    }

    private fun initMobileAds() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }
        activityScope.launch {
            MobileAds.initialize(this@SplashActivity) {
                runOnUiThread {
                    fetchRMCF()
                }
            }
        }
    }

    private fun checkUpdate() {
        appUpdateHelper.checkUpdate(object : IAppUpdateListener {
            override fun onDismiss() {
                showAdsInter()
            }
        })
    }

    private fun showAdsInter() {
        HeavenInterstitialAD.loadAndDisplay(
            this,
            adUnitInter,
            isSplashAd = true,
            enable = FBConfig.getAdsConfig().enable_inter_splash,
            onDone = { error ->
                Logger.log("showAdsInter", error)
                preLoadAdLanguage()
            }
        )
    }

    private fun checkScreen() {
        if (HeavenSharePref.isFirstInstall) {
            val bundle = Bundle()
            bundle.putBoolean(LanguageActivity.IS_FROM_SPLASH, true)
            val intent = Intent(this@SplashActivity, LanguageActivity::class.java)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this@SplashActivity, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun preLoadAdLanguage() = activityScope.launch {
        Logger.log("preLoadAdLanguage", "IS FIRST INSTALL: ${HeavenSharePref.isFirstInstall}")
        if (!HeavenSharePref.isFirstInstall) {
            checkScreen()
            return@launch
        }
        showLoading(true)
        val fbConfig = FBConfig.getAdsConfig()
        val configAdID = HeavenEnv.configAdUnitID

        if (HeavenSharePref.isShowFullAds) {
            val nativeLanguage = async {
                HeavenNativeAd.makeRequestAd(applicationContext, configAdID.nativeLanguage)
            }.await()
            val nativeLanguageSelected = async {
                HeavenNativeAd.makeRequestAd(applicationContext, configAdID.nativeLanguageSelected)
            }.await()

            HeavenNativeAd.nativeAds[configAdID.nativeLanguage] = nativeLanguage
            HeavenNativeAd.nativeAds[configAdID.nativeLanguageSelected] = nativeLanguageSelected
            showLoading(false)
            checkScreen()
            return@launch
        }

        if (!fbConfig.enable_all_ads) {
            showLoading(false)
            checkScreen()
            return@launch
        }

        if (fbConfig.enable_native_language) {
            val nativeLanguage = async {
                HeavenNativeAd.makeRequestAd(applicationContext, configAdID.nativeLanguage)
            }.await()
            HeavenNativeAd.nativeAds[configAdID.nativeLanguage] = nativeLanguage
        }

        if (fbConfig.enable_native_language_selected) {
            val nativeLanguage = async {
                HeavenNativeAd.makeRequestAd(applicationContext, configAdID.nativeLanguageSelected)
            }.await()
            HeavenNativeAd.nativeAds[configAdID.nativeLanguageSelected] = nativeLanguage
        }

        showLoading(false)
        checkScreen()
    }

}