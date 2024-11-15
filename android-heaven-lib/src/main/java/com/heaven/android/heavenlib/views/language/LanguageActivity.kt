package com.heaven.android.heavenlib.views.language

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.heaven.android.heavenlib.R
import com.heaven.android.heavenlib.ads.HeavenNativeAd
import com.heaven.android.heavenlib.ads.TypeNativeAd
import com.heaven.android.heavenlib.base.activity.BaseActivity
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.databinding.ActivityLanguageBinding
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.datas.HeavenSharePref
import com.heaven.android.heavenlib.datas.models.AppLanguage
import com.heaven.android.heavenlib.views.intro.IntroActivity

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private val configLanguage = HeavenEnv.configLanguage
    private val configAdUnitIDs = HeavenEnv.configAdUnitID
    private val configStyleApp = HeavenEnv.configStyleApp
    private lateinit var adapter: LanguageAdapter
    private var languageCode = ""
    private var isFromSplash: Boolean = false

    companion object {
        const val IS_FROM_SPLASH = "is_from_splash"
        const val TAG = "LanguageActivity"
    }

    override fun makeBinding(layoutInflater: LayoutInflater): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        //
        isFromSplash = intent.extras?.getBoolean(IS_FROM_SPLASH) ?: false
        with(binding) {
            binding.imgBgApp.setImageResource(configStyleApp.backgroundApp)
            tvHeader.setTextColor(configLanguage.colorTitleLanguage)
            tvSubTitle.setTextColor(configLanguage.colorTitleLanguage)
            btnDone.setImageResource(configLanguage.icDoneLanguage)
            btnDone.alpha = 0.5F
            btnDone.setOnClickListener {
                onClickDone()
            }
        }

        //
        adapter = LanguageAdapter(
            configLanguage.languages.toMutableList(),
            object : IClickLanguage {
                override fun onClickLanguage(language: AppLanguage, position: Int) {
                    languageCode = language.code
                    binding.btnDone.alpha = 1F
                    displayAd(configAdUnitIDs.nativeLanguageSelected)
                }
            }
        )

        binding.rcv.layoutManager = LinearLayoutManager(this)
        binding.rcv.adapter = adapter
        //
        if (isFromSplash) {
            displayAd(configAdUnitIDs.nativeLanguage)
        } else {
            HeavenNativeAd.loadAndDisplay(
                this,
                HeavenEnv.configAdUnitID.nativeLanguageFromSetting,
                FBConfig.getAdsConfig().enable_native_language_setting,
                binding.frAd,
                TypeNativeAd.FULL_AD
            )
        }
    }

    private fun onClickDone() {
        if (languageCode.isEmpty()) {
            return
        }
        HeavenSharePref.languageCode = languageCode
        if (isFromSplash) {
            Intent(this, IntroActivity::class.java).also {
                startActivity(it)
                finish()
            }
        } else {
            finish()
        }
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun displayAd(adID: String) {
        HeavenNativeAd.displayFromPreLoadAd(
            this,
            adID,
            binding.frAd,
            TypeNativeAd.FULL_AD
        )
    }

}
