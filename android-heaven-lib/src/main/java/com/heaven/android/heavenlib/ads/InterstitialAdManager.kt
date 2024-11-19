package com.heaven.android.heavenlib.ads

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnPaidEventListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.datas.HeavenSharePref
import com.heaven.android.heavenlib.utils.Logger
import java.util.Date
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


object HeavenInterstitialAD {
    private var mInterstitialAd: InterstitialAd? = null
    private var durationShowInterAds: Date? = null
    private var isShowingInterAds = AtomicBoolean(false)

    private val adRequest = AdRequest.Builder().build()
    private var fbConfig = FBConfig.getAdsConfig()

    private const val TAG = "HeavenInterstitialAD"


    fun loadAndDisplay(
        activity: Activity,
        adUnitId: String,
        isSplashAd: Boolean = false,
        enable: Boolean,
        onDone: (String) -> Unit
    ) {
        if (HeavenSharePref.isShowFullAds) {
            makeRequestLoadAd(activity, adUnitId, isSplashAd, onDone)
            return
        }

        if (!enable || !fbConfig.enable_all_ads) {
            mInterstitialAd = null
            onDone("Disable all ad from RMCF")
            return
        }

        if (!checkDurationShowAds()) {
            mInterstitialAd = null
            onDone("Time show ad < 30s")
            return
        }

        makeRequestLoadAd(activity, adUnitId, isSplashAd, onDone)
    }

    private fun displayInterAD(activity: Activity) {
        if (mInterstitialAd == null) {
            return
        }

        val callback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                Logger.log(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                Logger.log(TAG, "Ad dismissed fullscreen content.")
                isShowingInterAds.getAndSet(false)
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Logger.log(TAG, "Ad failed to show fullscreen content.")
                isShowingInterAds.getAndSet(false)
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                Logger.log(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                isShowingInterAds.getAndSet(true)
                Logger.log(TAG, "Ad showed fullscreen content.")
            }
        }

        mInterstitialAd?.fullScreenContentCallback = callback
        mInterstitialAd!!.show(activity)
    }

    private fun makeRequestLoadAd(
        activity: Activity,
        adUnitId: String,
        isSplashAd: Boolean = false,
        onDone: (String) -> Unit
    ) {
        val callback = object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                Logger.log(TAG, "AD load failure! ${adError.message}")
                onDone.invoke("Ad failed to load")
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Logger.log(TAG, "Ad loaded success!")

                interstitialAd.onPaidEventListener = OnPaidEventListener { adValue ->
                    val valueMicros = adValue.valueMicros
                    Log.d(HeavenNativeAd.TAG, "$valueMicros")

                }
                mInterstitialAd = interstitialAd
                durationShowInterAds = Date()

                onDone.invoke("")
                displayInterAD(activity)
            }
        }
        InterstitialAd.load(activity, adUnitId, adRequest, callback)
    }


    fun loadAndDisplayV2(
        activity: Activity,
        adUnitId: String,
        isSplashAd: Boolean = false,
        enable: Boolean,
        onDone: (String) -> Unit
    ) {

    }

    private fun checkDurationShowAds(): Boolean {
        return if (durationShowInterAds == null) {
            true
        } else {
            val time = if (!HeavenSharePref.isShowFullAds) 30 else 0
            calculateDifferenceInMinutes(durationShowInterAds!!, Date()) > time
        }
    }

    private fun calculateDifferenceInMinutes(startDate: Date, endDate: Date): Long {
        val diffInMillis: Long = endDate.time - startDate.time
        return TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
    }
}
