package com.heaven.android.heavenlib.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.datas.HeavenSharePref
import java.util.concurrent.atomic.AtomicBoolean


object HeavenRewardAD {
    private var rewardedAd: RewardedAd? = null
    private var request = AdRequest.Builder().build()
    private var fbConfig = FBConfig.getAdsConfig()
    private var isShowFullAd = HeavenSharePref.isShowFullAds
    private var isADShowing = AtomicBoolean(false)

    fun loadAd(
        context: Context,
        adUnitID: String,
        enable: Boolean,
        onDone: (String?) -> Unit,
    ) {
        if (isShowFullAd) {
            makeRequestAd(
                context = context,
                adUnitID = adUnitID,
                onDone = onDone
            )
            return
        }

        if (!enable || !fbConfig.enable_all_ads) {
            onDone("Disable from RMCF")
            return
        }


        if (isADShowing.get()) return
        AppOpenManager.instance?.disableAppResume()
        makeRequestAd(
            context = context,
            adUnitID = adUnitID,
            onDone = onDone
        )
    }

    private fun makeRequestAd(
        context: Context,
        adUnitID: String,
        onDone: (String?) -> Unit,
    ) {
        val callback = object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(errorCode: LoadAdError) {
                isADShowing.set(false)
                AppOpenManager.instance?.enableAppResume()
                onDone(errorCode.message)
            }

            override fun onAdLoaded(ad: RewardedAd) {
                isADShowing.set(true)
                rewardedAd = ad
                showAd(context, onDone)
            }
        }

        RewardedAd.load(
            context,
            adUnitID,
            request,
            callback
        )
    }

    private fun showAd(context: Context, onDone: (String?) -> Unit) {
        val fullScreenCallBack = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                isADShowing.set(false)
                onDone(null)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                super.onAdFailedToShowFullScreenContent(adError)
                rewardedAd = null
                isADShowing.set(false)
                onDone(adError.message)
            }

            override fun onAdShowedFullScreenContent() {
                isADShowing.set(true)
                rewardedAd = null
            }
        }
        rewardedAd?.fullScreenContentCallback = fullScreenCallBack
        rewardedAd?.show(context as Activity) {}
    }

}