
# Hướng dẫn cài đặt, sử dụng thư viện Heaven-lib

## I, Cài đặt thư viện:

1, Thêm đoạn sau vào file `setting.gradle`:
``` 
    dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
2, Add dependency:
```
	dependencies {
	        implementation 'com.github.lehaidanggg:heaven-lib:$version'
	}
```

3, Thêm Firebase crashlytic:
- `build.gradle`(app):
```
	plugins {
    		...
    		id 'com.google.gms.google-services'
    		id 'com.google.firebase.crashlytics'
	}
```
- `build.gradle`(project):
```
	plugins {
		...
		id 'com.google.gms.google-services' version your_version apply false
    		id 'com.google.firebase.crashlytics' version your_version apply false
	}
```


### II, Setup thư viện:

1, Xoá `Laucher` của activity có sẵn trong `Mainifest.xml`:
```
    <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
```
2, Thêm các ID ADS, Meta SDK,... trong `Mainifest.xml`:
```
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="@string/app_id" />
    <meta-data
        android:name="com.google.android.gms.ads.flag.NATIVE_AD_DEBUGGER_ENABLED"
        android:value="false" />

    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="@string/meta_app_id" />
    <meta-data
        android:name="com.facebook.sdk.ClientToken"
        android:value="@string/meta_client_token" />
```
3, Tạo class `MainApplication` extends từ `HeavenApplication`:
* Lưu ý: phải enable buildConfig trong file `build.gralde`(app)
```
	buildFeatures {
		buildConfig true
    	}
```

MainApplication
```
class MainApplication : HeavenApplication() {

    override fun initOtherFunc() {
        CONTEXT = applicationContext
        HeavenEnv.init(
            buildConfig = configEnv(),
            configSplash = configSplash(),
            configLanguage = configLanguage(),
            configIntro = configIntro(),
            configAdUnitID = AdUnitID,
            configStyleApp = configStyleApp()
        )
        HeavenEnv.enableTestFullAd() // enable full ad 
        // các config khác ...
		
    }

    // ------------------------------CONFIG AD LIB----------------------------------
	// các biến môi trường của dự án
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
            .setImageSplash(R.drawable.ic_hand)
            .setTitleSplash(R.string.app_name)
            .setTextSizeTitleSplash(30)
            .setTextColorTitleSplash(Color.parseColor("#273172"))
            .build()
        return configSplash
    }

    private fun configIntro(): ConfigIntro {
        val configIntro = ConfigIntro.Builder(
            getListContentIntro(),
            nextScreen = HomeActivity::class.java
        )
            .setTextSizeTitleIntro(30)
            .setTextSizeSubTitleIntro(20)
            .setTextColorTitleIntro(Color.parseColor("#273172"))
            .setTextColorSubTitleIntro(Color.parseColor("#273172"))
            .setTextSizeBtnNext(18)
            .build()

        return configIntro
    }

    private fun getListContentIntro(): List<AppIntro> {
        val lsPages = ArrayList<AppIntro>()
        lsPages.add(
            AppIntro(
                R.string.clapping,
                R.string.to_find_the_lost_phone,
                R.drawable.ic_intro1,
            )
        )
        lsPages.add(
            AppIntro(
                R.string.moving_alarm,
                R.string.turn_on_alert_when,
                R.drawable.ic_intro2,
            )
        )
        lsPages.add(
            AppIntro(
                R.string.intruder_alarm,
                R.string.take_a_picture_of_some_one,
                R.drawable.ic_intro3,
            )
        )
        lsPages.add(
            AppIntro(
                R.string.pocket_alarm,
                R.string.alarm_ringing,
                R.drawable.ic_pocket_alarm,
            )
        )

        return lsPages
    }

    private fun configLanguage(): ConfigLanguage {
        val config = ConfigLanguage.Builder().apply {
            setBgItemLanguage(R.drawable.bg_language)
            setLanguages(AppLanguage.appSupportedLanguages)
            setIcDoneLanguage(R.drawable.ic_baseline_check)
            setColorTitleLanguage(Color.BLACK)
            setIcSelectedLanguage(R.drawable.ic_radio_selected)
            setIcUnSelectedLanguage(R.drawable.ic_radio_uncheck)
        }.build()

        return config
    }

    private fun configStyleApp(): ConfigStyleApp {
        val config = ConfigStyleApp.Builder()
            .setBackgroundApp(R.drawable.bg)
            .setBackgroundButtonAD(R.drawable.bg_btn_ads)
            .setBackgroundIconChoiceAD(R.drawable.ads_icon2)
            .setBackgroundAdNormalAD(Color.WHITE)
            .build()

        return config
    }
}

object AdUnitID : IAdUnitID {
    override val interSplash: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeLanguage: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeLanguageSelected: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeLanguageFromSetting: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeIntro1: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeIntro2: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeIntro3: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeIntro4: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeIntro1_2nd: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeIntro2_2nd: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeIntro3_2nd: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val nativeIntro4_2nd: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val interIntroCompleted: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"
    override val resumeAll: String
        get() = "ca-app-pub-2857599586325936/aaaaaaaaaa"

    val otherIDAD = "ca-app-pub-2857599586325936/aaaaaaaaaa"
	// thêm các id ad khác...

}

object KeyRMCF {
	// ngoài các key mặc định từ remote config thì khai báo các key khác ở đây
    val enable_native_home_01 = "enable_native_home_01"
    val enable_native_home_02 = "enable_native_home_02"
    val enable_native_clapfinder_turnon = "enable_native_clapfinder_turnon"
    val enable_native_clapfinder_turnoff = "enable_native_clapfinder_turnoff"
    val enable_reward_clapfinder = "enable_reward_clapfinder"
    val enable_native_movingalarm_turnon = "enable_native_movingalarm_turnon"
    val enable_native_movingalarm_turnoff = "enable_native_movingalarm_turnoff"
    val enable_reward_movingalarm = "enable_reward_movingalarm"
    val enable_native_chargingalarm_turnon = "enable_native_chargingalarm_turnon"
    val enable_native_chargingalarm_turnoff = "enable_native_chargingalarm_turnoff"
    val enable_reward_chargingalarm = "enable_reward_chargingalarm"
    val enable_native_intruderalarm_turnon = "enable_native_intruderalarm_turnon"
    val enable_native_intruderalarm_turnoff = "enable_native_intruderalarm_turnoff"
    val enable_native_intruderalarm_historyreport = "enable_native_intruderalarm_historyreport"
    val enable_reward_intruderalarm = "enable_reward_intruderalarm"
    val enable_native_setting_alarmsound = "enable_native_setting_alarmsound"
    val enable_native_pocketalarm_turnon = "enable_native_pocketalarm_turnon"
    val enable_reward_pocketalarm = "enable_reward_pocketalarm"
    val enable_native_earphonealarm_turnon = "enable_native_earphonealarm_turnon"
    val enable_reward_earphonealarm = "enable_reward_earphonealarm"
    val enable_native_home_03 = "enable_native_home_03"
    val enable_native_fullbatteryalarm_turnon = "enable_native_fullbatteryalarm_turnon"
    val enable_reward_fullbatteryalarm = "enable_reward_fullbatteryalarm"
	...
}
```

* Sau khi extends từ `HeavenApplication` phải khai báo trong tag <application> ở file `Mainifest.xml`
```
<application
        android:name=".application.MainApplication" // khai báo class MainApplication
        android:allowBackup="true"
        android:hardwareAccelerated="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:requestLegacyExternalStorage="true"
        android:supportsRtl="true"
        android:theme="@style/Theme.BaseAndroid"
		... các nội dung khác
```


### III, Sử dụng thư viện:

1, AD NATIVE:
`layout.xml`
```
<FrameLayout
    android:id="@+id/fr_ads"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">
```

### Load and display:
```
HeavenNativeAd.loadAndDisplay(
    context,
    adUnitID,
    FBConfig.getAdsConfig().getCustomField(KeyRMCF.customKey) as Boolean,
    binding.frAds,
    TypeNativeAd.FULL_AD // hoặc TypeNativeAd.NO_MEDIA
)
```

- Pre load:
```
- Load:
HeavenNativeAd.preLoadAdNative(
	context, 
	adUnitID,
	FBConfig.getAdsConfig().getCustomField(KeyRMCF.customKey) as Boolean
)
- Display:
HeavenNativeAd.displayFromPreLoadAd(
    context,
    adUnitID,
    binding.frAds,
    TypeNativeAd.FULL_AD // hoặc TypeNativeAd.NO_MEDIA
)
```

2, AD Inter:

```
showLoading(true)
HeavenInterstitialAD.loadInterAd(
    activity = context,
    adUnitId = adUnitId,
    isSplashAd = false, // isSplash
    enable = FBConfig.getAdsConfig().getCustomField(KeyRMCF.customKey) as Boolean,
    onDone = {
        showLoading(false)
		...
    }
)
```

3, AD Reward:

```
showDialogLoadingFullScreen(true)
HeavenRewardAD.loadAd(
	context = context,
    adUnitID = adUnitID,
    enable = FBConfig.getAdsConfig().getCustomField(KeyRMCF.customKey) as Boolean,
    onDone = {
		showDialogLoadingFullScreen(false)
		...
    }
)

```
4, AD Banner:

```
- Layout: 
<FrameLayout
    android:id="@+id/frAd"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentStart="true"
    android:layout_alignParentEnd="true"
    android:layout_alignParentBottom="true" />

-------------------------------------------------------------------------------------
- Load banner:
override fun onResume() {
    super.onResume()
    val vAd = findViewById<FrameLayout>(R.id.frAd)
    HeavenBannerAD.loopLoadBanner(
		context,
		bannerUnitID,
		FBConfig.getAdsConfig().getCustomField(KeyRMCF.customKey) as Boolean,
		vAd)
}

override fun onPause() {
    super.onPause()
    HeavenBannerAD.stopLoopLoadBanner()
}

```


