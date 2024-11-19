package com.heaven.android.heavenlib.ads

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnPaidEventListener
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.heaven.android.heavenlib.R
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.datas.HeavenSharePref
import com.heaven.android.heavenlib.utils.Logger
import com.heaven.android.heavenlib.utils.isNotEmptyChild
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

enum class TypeNativeAd {
    FULL_AD, NO_MEDIA
}

object HeavenNativeAd {
    private val configAd = FBConfig.getAdsConfig()
    private val optional = NativeAdOptions.Builder().build()
    const val TAG = "NativeAdsManager"

    var nativeAds = mutableMapOf<String, NativeAd?>()

    //
    fun preLoadAdNative(
        context: Context,
        adUnitID: String,
        enable: Boolean,
    ) {
        if (HeavenSharePref.isShowFullAds) {
            makeRequestAd(context, adUnitID) { nativeAd, error ->
                nativeAds[adUnitID] = nativeAd
                Logger.log("preLoadAdNative", "ERR: $error")
            }
            return
        }

        if (!configAd.enable_all_ads || !enable) {
            Logger.log(TAG, "enable all ads: FALSE || enable this ad: FALSE")
            return
        }

        makeRequestAd(context, adUnitID) { nativeAd, error ->
            nativeAds[adUnitID] = nativeAd
            Logger.log("preLoadAdNative", "ERR: $error")
        }
    }

    fun displayFromPreLoadAd(
        context: Context,
        adUnitId: String,
        parentView: FrameLayout,
        type: TypeNativeAd,
    ) {
        val nativeAd = nativeAds.getOrDefault(adUnitId, null) ?: run {
            parentView.visibility = View.GONE
            return
        }
        displayFromInstanceAd(context, nativeAd, parentView, type)
    }

    // ----------------------------------------------------------------
    fun loadAndDisplay(
        context: Context,
        adUnitID: String,
        enable: Boolean,
        parentView: FrameLayout,
        type: TypeNativeAd = TypeNativeAd.FULL_AD,
    ) {
        if (HeavenSharePref.isShowFullAds) {
            Logger.log(TAG, "Load ad from is show full ad")
            makeRequestAd(
                context = context,
                adUnitId = adUnitID
            ) { nativeAd, error ->
                if (nativeAd != null) {
                    displayFromInstanceAd(context, nativeAd, parentView, type)
                }
                if (!error.isNullOrEmpty()) {
                    parentView.visibility = View.GONE
                }
            }
            return
        }

        if (!configAd.enable_all_ads || !enable) {
            Logger.log(TAG, "enable all ads: FALSE")
            parentView.visibility = View.GONE
            return
        }
        makeRequestAd(
            context = context,
            adUnitId = adUnitID
        ) { nativeAd, error ->
            if (nativeAd != null) {
                displayFromInstanceAd(context, nativeAd, parentView, type)
            }
            if (!error.isNullOrEmpty()) {
                parentView.visibility = View.GONE
            }
        }
    }

    fun displayFromInstanceAd(
        context: Context,
        ad: NativeAd?,
        parentView: FrameLayout,
        type: TypeNativeAd
    ) {
        if (ad == null) {
            parentView.visibility = View.GONE
            return
        }
        parentView.visibility = View.VISIBLE
        val adView = if (type == TypeNativeAd.FULL_AD) getViewNativeNormalAd(context)
        else getViewNativeNoMediaAd(context)

        if (parentView.isNotEmptyChild()) {
            parentView.removeAllViews()
        }

        parentView.addView(adView)
        displayAd(ad, adView)
    }

    private fun displayAd(
        ad: NativeAd,
        adView: NativeAdView,
    ) {
        val headlineView = adView.findViewById<TextView>(R.id.tvHeaderAds)
        val bodyView = adView.findViewById<TextView>(R.id.tvContentAds)
        val mediaView = adView.findViewById<MediaView>(R.id.mediaViewAds)
        val vCallToAction = adView.findViewById<View>(R.id.vCallToActionView)
        val btnCallToAction = adView.findViewById<Button>(R.id.btnCallToActionView)
        val iconView = adView.findViewById<ImageView>(R.id.imgAds)
        val tvAdChoice = adView.findViewById<TextView>(R.id.tvAdChoice)

        if (ad.headline == null) {
            headlineView.visibility = View.GONE
        } else {
            headlineView.visibility = View.VISIBLE
            headlineView.text = ad.headline
            headlineView.setTextColor(HeavenEnv.configStyleApp.textColorHeaderAD)
            adView.headlineView = headlineView
        }

        if (ad.body == null) {
            bodyView.visibility = View.GONE
        } else {
            bodyView.visibility = View.VISIBLE
            bodyView.text = ad.body
            bodyView.setTextColor(HeavenEnv.configStyleApp.textColorContentAD)
            adView.bodyView = bodyView
        }

        if (mediaView != null) {
            mediaView.visibility = View.VISIBLE
            mediaView.mediaContent = ad.mediaContent
            adView.mediaView = mediaView
        }

        if (ad.callToAction == null) {
            btnCallToAction.visibility = View.GONE
        } else {
            btnCallToAction.setBackgroundResource(HeavenEnv.configStyleApp.backgroundButtonAD)
            btnCallToAction.setTextColor(HeavenEnv.configStyleApp.textColorButtonAD)
            btnCallToAction.visibility = View.VISIBLE
            btnCallToAction.text = ad.callToAction
            adView.callToActionView = vCallToAction
        }

        if (ad.icon == null) {
            iconView.visibility = View.GONE
        } else {
            iconView.visibility = View.VISIBLE
            iconView.setImageDrawable(
                ad.icon!!.drawable
            )
            adView.iconView = iconView
        }

        tvAdChoice.setBackgroundResource(HeavenEnv.configStyleApp.backgroundIconChoiceAD)
        tvAdChoice.setTextColor(HeavenEnv.configStyleApp.textColorAdChoiceAD)

        adView.setBackgroundColor(if (HeavenSharePref.isShowFullAds) Color.TRANSPARENT else HeavenEnv.configStyleApp.backgroundAdNormalAD)
        adView.setNativeAd(ad)
    }

    @SuppressLint("InflateParams")
    private fun getViewLoading(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.view_loading_ad, null)
    }

    @SuppressLint("InflateParams")
    private fun getViewNativeNormalAd(context: Context): NativeAdView {
        val isFullAd = HeavenSharePref.isShowFullAds
        if (isFullAd) {
            return LayoutInflater.from(context)
                .inflate(R.layout.native_ads_media_fullad, null) as NativeAdView
        }
        return LayoutInflater.from(context)
            .inflate(R.layout.native_ads_media_normal, null) as NativeAdView
    }

    @SuppressLint("InflateParams")
    private fun getViewNativeNoMediaAd(context: Context): NativeAdView {
        val isFullAd = HeavenSharePref.isShowFullAds

        if (isFullAd) {
            return LayoutInflater.from(context)
                .inflate(R.layout.native_ads_no_media_fullad, null) as NativeAdView
        }
        return LayoutInflater.from(context)
            .inflate(R.layout.native_ads_no_media, null) as NativeAdView
    }

    private fun makeRequestAd(
        context: Context,
        adUnitId: String,
        onDone: (NativeAd?, String?) -> Unit
    ) {
        AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                Log.d(TAG, "Ad loaded successfully")
                val onPaid = OnPaidEventListener { adValue ->
                    val valueMicros = adValue.valueMicros
                    Log.d(TAG, "$valueMicros")
                }
                ad.setOnPaidEventListener(onPaid)
                onDone(ad, null)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(errorCode: LoadAdError) {
                    Logger.log(
                        TAG,
                        "onAdFailedToLoad: Ad failed to load with error code $errorCode"
                    )
                    onDone(null, errorCode.message)
                }
            })
            .withNativeAdOptions(optional)
            .build()
            .loadAd(AdRequest.Builder().build())
    }

    suspend fun makeRequestAd(
        context: Context,
        adUnitId: String
    ): NativeAd? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            AdLoader.Builder(context, adUnitId)
                .forNativeAd { ad ->
                    Log.d(TAG, "Ad loaded successfully")
                    val onPaid = OnPaidEventListener { adValue ->
                        val valueMicros = adValue.valueMicros
                        Log.d(TAG, "$valueMicros")
                    }
                    ad.setOnPaidEventListener(onPaid)
                    continuation.resume(ad)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: LoadAdError) {
                        Logger.log(
                            TAG,
                            "onAdFailedToLoad: Ad failed to load with error code $errorCode"
                        )
                        continuation.resume(null)
                    }
                })
                .withNativeAdOptions(optional)
                .build()
                .loadAd(AdRequest.Builder().build())
        }
    }

}