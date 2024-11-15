package com.heaven.sampleheavenlib

import android.graphics.Color
import com.heaven.android.heavenlib.HeavenApplication
import com.heaven.android.heavenlib.config.ConfigBuildInfo
import com.heaven.android.heavenlib.config.ConfigIntro
import com.heaven.android.heavenlib.config.ConfigLanguage
import com.heaven.android.heavenlib.config.ConfigSplash
import com.heaven.android.heavenlib.config.ConfigStyleApp
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.config.IAdUnitID
import com.heaven.android.heavenlib.datas.HeavenSharePref
import com.heaven.android.heavenlib.datas.models.AppIntro
import com.heaven.android.heavenlib.datas.models.AppLanguage

class MainApplication : HeavenApplication() {

    override fun initOtherFunc() {
        HeavenEnv.init(
            buildConfig = configEnv(),
            configSplash = configSplash(),
            configLanguage = configLanguage(),
            configIntro = configIntro(),
            configAdUnitID = AdUnitID,
            configStyleApp = configStyleApp()
        )
        HeavenEnv.enableTestFullAd()
    }

    private fun configEnv(): ConfigBuildInfo {
        val configBuildInfo = ConfigBuildInfo.Builder()
            .setApplicationId(BuildConfig.APPLICATION_ID)
            .setIsDebug(BuildConfig.DEBUG)
            .setVersionCode(BuildConfig.VERSION_CODE)
            .setVersionName(BuildConfig.VERSION_NAME)
            .build()

        return configBuildInfo
    }

    private fun configSplash(): ConfigSplash {
        val configSplash = ConfigSplash.Builder()
            .setImageSplash(R.drawable.img_splash)
            .setTitleSplash(R.string.app_name)
            .setTextSizeTitleSplash(30)
            .build()
        return configSplash
    }

    private fun configIntro(): ConfigIntro {
        val configIntro = ConfigIntro.Builder(
            getListContentIntro(),
            nextScreen = MainActivity::class.java
        )
            .setTextSizeTitleIntro(30)
            .setTextSizeSubTitleIntro(16)
            .build()

        return configIntro
    }

    private fun getListContentIntro(): List<AppIntro> {
        val lsPages = ArrayList<AppIntro>()
        lsPages.add(
            AppIntro(
                R.string.title_intro1,
                R.string.des_intro1,
                R.drawable.img_intro1,
            )
        )
        lsPages.add(
            AppIntro(
                R.string.title_intro2,
                R.string.des_intro2,
                R.drawable.img_intro2,
            )
        )
        lsPages.add(
            AppIntro(
                R.string.title_intro3,
                R.string.des_intro3,
                R.drawable.img_intro3,
            )
        )
        lsPages.add(
            AppIntro(
                R.string.title_intro4,
                R.string.des_intro4,
                R.drawable.img_intro4,
            )
        )

        return lsPages
    }

    private fun configLanguage(): ConfigLanguage {
        val config = ConfigLanguage.Builder().apply {
            setBgItemLanguage(R.drawable.bg_language)
            setLanguages(AppLanguage.appSupportedLanguages)
            setIcDoneLanguage(R.drawable.ic_done)
            setColorTitleLanguage(Color.BLACK)
            setIcSelectedLanguage(R.drawable.ic_selected)
            setIcUnSelectedLanguage(R.drawable.ic_un_selected)
        }.build()

        return config
    }

    private fun configStyleApp(): ConfigStyleApp {
        val config = ConfigStyleApp.Builder()
            .setBackgroundApp(R.drawable.img_bg_app)
            .setBackgroundButtonAD(R.drawable.bg_btn_ad)
            .setBackgroundIconChoiceAD(R.drawable.bg_ad_choice)
            .build()

        return config
    }
}

object AdUnitID : IAdUnitID {
    override val interSplash: String
        get() = "ca-app-pub-3940256099942544/1033173712"
    override val nativeLanguage: String
        get() = "ca-app-pub-2857599586325936/7036609922"
    override val nativeLanguageSelected: String
        get() = "ca-app-pub-3940256099942544/1044960115"
    override val nativeLanguageFromSetting: String
        get() = "ca-app-pub-3940256099942544/1044960115"
    override val nativeIntro1: String
        get() = "ca-app-pub-2857599586325936/2004609242"
    override val nativeIntro2: String
        get() = "ca-app-pub-2857599586325936/9691527570"
    override val nativeIntro3: String
        get() = "ca-app-pub-2857599586325936/2521292607"
    override val nativeIntro4: String
        get() = "ca-app-pub-2857599586325936/4439200898"
    override val nativeIntro1_2nd: String
        get() = "ca-app-pub-2857599586325936/9691527570"
    override val nativeIntro2_2nd: String
        get() = "ca-app-pub-3940256099942544/1044960115"
    override val nativeIntro3_2nd: String
        get() = "ca-app-pub-2857599586325936/2521292607"
    override val nativeIntro4_2nd: String
        get() = "ca-app-pub-2857599586325936/4439200898"
    override val interIntroCompleted: String
        get() = "ca-app-pub-3940256099942544/1033173712"
    override val resumeAll: String
        get() = "ca-app-pub-3940256099942544/9257395921"

    val otherAdID: String = "ca-app-pub-3940256099942544/2247696110"
    // ...
}

object KeyRMCF {
    val enable_inter_scan_home = "enable_inter_scan_home"
    // ...
}













