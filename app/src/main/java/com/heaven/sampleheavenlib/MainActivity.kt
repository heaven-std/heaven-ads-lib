package com.heaven.sampleheavenlib

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.heaven.android.heavenlib.ads.HeavenBannerAD
import com.heaven.android.heavenlib.ads.HeavenInterstitialAD
import com.heaven.android.heavenlib.ads.HeavenNativeAd
import com.heaven.android.heavenlib.ads.HeavenRewardAD
import com.heaven.android.heavenlib.ads.TypeNativeAd
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.views.language.LanguageActivity

class MainActivity : AppCompatActivity() {
    private val adUnitID = AdUnitID
    private val fbConfig = FBConfig.getAdsConfig()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //
        findViewById<Button>(R.id.btnLoadReward).setOnClickListener{
            HeavenRewardAD.loadAd(
                this,
                "ca-app-pub-3940256099942544/5224354917",
                true,
                onDone = { err ->
                    Toast.makeText(this, "Collected reward!", Toast.LENGTH_SHORT).show()
                }
            )
        }
        findViewById<Button>(R.id.btnLoadInter).setOnClickListener {
            HeavenInterstitialAD.loadInterAd(
                this,
                "ca-app-pub-3940256099942544/1033173712",
                isSplashAd = true,
                enable = FBConfig.getAdsConfig().enable_inter_splash,
                onDone = { error ->
                    Toast.makeText(this, "Collected inter", Toast.LENGTH_SHORT).show()
                }
            )
        }

        loadNativeFullAd()
        loadNativeNoMedia()
    }

    private fun loadNativeFullAd() {
        val parentView = findViewById<FrameLayout>(R.id.frAdNativeFull)
        val enableAd = fbConfig.getCustomField(KeyRMCF.enable_inter_scan_home) as Boolean
        HeavenNativeAd.loadAndDisplay(
            applicationContext,
            adUnitID.otherAdID,
            enableAd,
            parentView,
            type = TypeNativeAd.FULL_AD
        )
    }

    private fun loadNativeNoMedia() {
        val parentView = findViewById<FrameLayout>(R.id.frAdNativeNoMedia)
        HeavenNativeAd.loadAndDisplay(
            applicationContext,
            "ca-app-pub-3940256099942544/2247696110",
            true,
            parentView,
            type = TypeNativeAd.NO_MEDIA
        )
    }

    override fun onResume() {
        super.onResume()
        val vAd = findViewById<FrameLayout>(R.id.frAd)
        HeavenBannerAD.loopLoadBanner(this,"ca-app-pub-3940256099942544/9214589741", true, vAd)
    }

    override fun onPause() {
        super.onPause()
        HeavenBannerAD.stopLoopLoadBanner()
    }
}