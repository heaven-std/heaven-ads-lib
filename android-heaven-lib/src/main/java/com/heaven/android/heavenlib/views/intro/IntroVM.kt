package com.heaven.android.heavenlib.views.intro

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.android.gms.ads.nativead.NativeAd
import com.heaven.android.heavenlib.ads.HeavenNativeAd
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.datas.HeavenSharePref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class IntroVM : ViewModel() {
    private val intros = HeavenEnv.configIntro.intros
    private val configAdId = HeavenEnv.configAdUnitID
    private val fbConfig = FBConfig.getAdsConfig()

    private var _nativeAds = MutableLiveData<MutableMap<String, NativeAd?>>(mutableMapOf())
    val nativeAds: LiveData<MutableMap<String, NativeAd?>> = _nativeAds

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun loadPages(context: Context) = coroutineScope.launch {
        val ids = getAdUnitID()
        val deferredResults = ids.mapIndexed { index, id ->
            async {
                loadAdIntro(context, id, getEnabledByIndex(index))
            }
        }

        deferredResults.forEach { it.await() }
    }

    private suspend fun loadAdIntro(context: Context, adID: String, enableAd: Boolean) {
        if (HeavenSharePref.isShowFullAds) {
            val nativeAd = HeavenNativeAd.makeRequestAd(context, adID)
            addValueToMap(adID, nativeAd)
            return
        }

        if (!enableAd || !fbConfig.enable_all_ads) {
            addValueToMap(adID, null)
            return
        }

        val nativeAd = HeavenNativeAd.makeRequestAd(context, adID)
        addValueToMap(adID, nativeAd)
    }

    private fun addValueToMap(id: String, nativeAd: NativeAd?) {
        val currentAds = _nativeAds.value ?: mutableMapOf()
        currentAds[id] = nativeAd
        _nativeAds.postValue(currentAds)
    }

    private fun getEnabledByIndex(index: Int): Boolean {
        return when (index) {
            0 -> if (HeavenSharePref.isFirstInstall) fbConfig.enable_native_intro1 else fbConfig.enable_native_intro1_2nd
            1 -> if (HeavenSharePref.isFirstInstall) fbConfig.enable_native_intro2 else fbConfig.enable_native_intro2_2nd
            2 -> if (HeavenSharePref.isFirstInstall) fbConfig.enable_native_intro3 else fbConfig.enable_native_intro3_2nd
            3 -> if (HeavenSharePref.isFirstInstall) fbConfig.enable_native_intro4 else fbConfig.enable_native_intro4_2nd
            else -> true
        }
    }

    fun getAdUnitID(): List<String> {
        return arrayListOf(
            if (HeavenSharePref.isFirstInstall) configAdId.nativeIntro1 else configAdId.nativeIntro1_2nd,
            if (HeavenSharePref.isFirstInstall) configAdId.nativeIntro2 else configAdId.nativeIntro2_2nd,
            if (HeavenSharePref.isFirstInstall) configAdId.nativeIntro3 else configAdId.nativeIntro3_2nd,
            if (HeavenSharePref.isFirstInstall) configAdId.nativeIntro4 else configAdId.nativeIntro4_2nd
        )
    }

    fun getAdByID(adID: String): NativeAd? {
        val currentAds = _nativeAds.value ?: mutableMapOf()
        return currentAds.getOrDefault(adID, null)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    companion object {
        const val TAG = "IntroVM"
        //
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return IntroVM() as T
            }
        }
    }
}