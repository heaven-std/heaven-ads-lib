package com.heaven.android.heavenlib.config

import com.heaven.android.heavenlib.datas.HeavenSharePref
import java.lang.RuntimeException

object HeavenEnv {
    // BUILD CONFIG
    private var _buildConfig: ConfigBuildInfo? = null
    val buildConfig: ConfigBuildInfo
        get() = _buildConfig ?: throw RuntimeException("HeavenEnv: Build config not initialized!")

    // SPLASH
    private var _configSplash: ConfigSplash? = null
    val configSplash: ConfigSplash
        get() = _configSplash ?: throw RuntimeException("HeavenEnv: Splash config not initialized!")


    // INTRO
    private var _configIntro: ConfigIntro? = null
    val configIntro: ConfigIntro
        get() = _configIntro ?: throw RuntimeException("HeavenEnv: Intro config not initialized!")

    // LANGUAGE
    private var _configLanguage: ConfigLanguage? = null
    val configLanguage: ConfigLanguage
        get() = _configLanguage
            ?: throw RuntimeException("HeavenEnv: Language config not initialized!")

    // AD UNIT ID
    private var _configAdUnitID: IAdUnitID? = null
    val configAdUnitID: IAdUnitID
        get() = _configAdUnitID
            ?: throw RuntimeException("HeavenEnv: AD Unit config not initialized!")

    // Config style app
    private var _configStyleApp: ConfigStyleAD? = null
    val configStyleApp: ConfigStyleAD
        get() = _configStyleApp
            ?: throw RuntimeException("HeavenEnv: Config Style App not initialized!")


    //REMOTE CONFIG
    // AD: id, layout
    fun init(
        buildConfig: ConfigBuildInfo,
        configSplash: ConfigSplash,
        configIntro: ConfigIntro,
        configLanguage: ConfigLanguage,
        configAdUnitID: IAdUnitID,
        configStyleApp: ConfigStyleAD
    ) {
        _buildConfig = buildConfig
        _configSplash = configSplash
        _configIntro = configIntro
        _configLanguage = configLanguage
        _configAdUnitID = configAdUnitID
        _configStyleApp = configStyleApp
    }

    fun enableTestFullAd() {
        if (buildConfig.isDebug){
            HeavenSharePref.isTestFullAd = true
        }
    }
}