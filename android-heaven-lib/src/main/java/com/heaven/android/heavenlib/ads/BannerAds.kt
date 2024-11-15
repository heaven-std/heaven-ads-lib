package com.heaven.android.heavenlib.ads

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.datas.HeavenSharePref
import com.heaven.android.heavenlib.utils.Logger
import java.util.concurrent.atomic.AtomicBoolean

class MyTaskRunner(
    val timeReload: Int,
    val task: () -> Unit
) {
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            task()
            handler.postDelayed(this, timeReload * 1000L)
        }
    }

    fun startRepeatingTask() {
        handler.post(runnable)
    }

    fun stopRepeatingTask() {
        handler.removeCallbacks(runnable)
    }
}

object HeavenBannerAD {
    private var adView: AdView? = null
    private const val KEY_BUNDLE_BANNER = "collapsible"
    private const val VALUE_BUNDLE_BANNER = "bottom"
    //
    private var isBannerClosed = AtomicBoolean(true)
    private val isShowFullAd = HeavenSharePref.isShowFullAds
    private val fbConfig = FBConfig.getAdsConfig()
    private val timeReloadBanner = if (isShowFullAd) fbConfig.time_reload_banner else 30
    private var loopClass: MyTaskRunner? = null

    fun loopLoadBanner(
        context: Context,
        idAd: String,
        enable: Boolean,
        parent: ViewGroup
    ) {
        loopClass = MyTaskRunner(timeReloadBanner) {
            if (isBannerClosed.get()) {
                Logger.log("loopLoadBanner", "Start loop load banner!")
                loadBanner(context = context, idAd = idAd, enable = enable, parent = parent)
            }
        }.also {
            it.startRepeatingTask()
        }
    }

    fun stopLoopLoadBanner() {
        Logger.log("stopLoopLoadBanner", "Stop loop load banner!")
        loopClass?.stopRepeatingTask()
        loopClass = null
    }

    private fun loadBanner(
        context: Context,
        idAd: String,
        enable: Boolean,
        parent: ViewGroup
    ) {
        if (HeavenSharePref.isShowFullAds) {
            makeRequestBanner(context = context, idAd = idAd, parent = parent)
            return
        }

        if (!enable || !fbConfig.enable_all_ads) {
            parent.visibility = View.GONE
            return
        }

        makeRequestBanner(context = context, idAd = idAd, parent = parent)
    }

    private fun makeRequestBanner(
        context: Context,
        idAd: String,
        parent: ViewGroup
    ) {
        val callBack = object : AdListener() {
            override fun onAdClicked() {
                Logger.log("makeRequestBanner", "Banner clicked!")
            }

            override fun onAdClosed() {
                Logger.log("makeRequestBanner", "Banner closed!")
                isBannerClosed.set(true)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Logger.log("makeRequestBanner", "Banner failed to load!")
                parent.visibility = View.GONE
                isBannerClosed.set(true)
            }

            override fun onAdImpression() {}

            override fun onAdLoaded() {
                Logger.log("makeRequestBanner", "Banner loaded!")
                parent.removeAllViews()
                parent.addView(adView)
            }

            override fun onAdOpened() {
                Logger.log("makeRequestBanner", "Banner opened!")
                isBannerClosed.set(false)
            }
        }

        adView = AdView(context)
        adView.let {
            it?.setAdSize(getAdsBanner(context, parent))
            it?.adUnitId = idAd
            it?.adListener = callBack
            val extras = Bundle()
            extras.putString(KEY_BUNDLE_BANNER, VALUE_BUNDLE_BANNER)
            val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()

            it?.loadAd(adRequest)
        }
    }

    private fun getAdsBanner(context: Context, parent: ViewGroup): AdSize {
        val outMetrics = context.resources.displayMetrics

        var adWidthPixels = parent.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / outMetrics.density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)

    }
}


