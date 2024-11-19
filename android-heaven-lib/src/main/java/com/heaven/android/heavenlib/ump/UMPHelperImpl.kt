package com.heaven.android.heavenlib.ump

import android.app.Activity
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.heaven.android.heavenlib.config.HeavenEnv

class UmpHelperImpl(val activity: Activity) : UMPHelper {
    private lateinit var consentInformation: ConsentInformation

    override fun requestConsent(listener: IUmpListener) {
        val debugSettings = ConsentDebugSettings.Builder(activity)

        if (HeavenEnv.buildConfig.isDebug) {
            debugSettings.setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            debugSettings.addTestDeviceHashedId(HeavenEnv.buildConfig.deviceIdUMPDebug)
        }

        val paramsBuilder = ConsentRequestParameters.Builder()
        if (HeavenEnv.buildConfig.isDebug) {
            paramsBuilder.setConsentDebugSettings(debugSettings.build())
        }
        val params = paramsBuilder.build()

        consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity
                ) { loadAndShowError ->
                    if (loadAndShowError != null) {
                        // Consent gathering failed.
                        listener.requestConsentCompleted(loadAndShowError.message)
                    }
                    // Consent has been gathered.
                    listener.requestConsentCompleted(null)
                }
            },
            { requestConsentError ->
                listener.requestConsentCompleted(requestConsentError.message)
            })
    }

}

