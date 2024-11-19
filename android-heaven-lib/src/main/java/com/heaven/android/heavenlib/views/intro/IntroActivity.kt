package com.heaven.android.heavenlib.views.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.heaven.android.heavenlib.R
import com.heaven.android.heavenlib.ads.HeavenInterstitialAD
import com.heaven.android.heavenlib.ads.HeavenNativeAd
import com.heaven.android.heavenlib.ads.TypeNativeAd
import com.heaven.android.heavenlib.base.activity.BaseActivity
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.databinding.ActivityIntroBinding
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.datas.HeavenSharePref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroActivity : BaseActivity<ActivityIntroBinding>() {
    private val configIntro = HeavenEnv.configIntro
    private val configAdID = HeavenEnv.configAdUnitID
    private val fbConfig = FBConfig.getAdsConfig()

    private val viewmodel by viewModels<IntroVM> {
        IntroVM.Factory
    }

    private var indexIntro: Int = 0
    private lateinit var adapter: IntroAdapter
    private var job: Job? = null

    private val onChangePage = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            indexIntro = position
            binding.tvStart.text =
                if (indexIntro == configIntro.intros.size - 1) getString(R.string.start)
                else getString(R.string.next)


            if (indexIntro == configIntro.intros.size - 1) {
                startDelay()
            } else {
                cancelDelay()
            }

            val adNative = viewmodel.getAdByID(viewmodel.getAdUnitID()[indexIntro])
            HeavenNativeAd.displayFromInstanceAd(
                this@IntroActivity, adNative, binding.frAd, TypeNativeAd.FULL_AD
            )
        }
    }

    override fun makeBinding(layoutInflater: LayoutInflater): ActivityIntroBinding {
        return ActivityIntroBinding.inflate(layoutInflater)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        //
        binding.imgBgApp.setImageResource(HeavenEnv.configStyleApp.backgroundApp)
        binding.tvStart.setTextColor(configIntro.textColorEnableBtnNextIntro)
        binding.tvStart.textSize = configIntro.textSizeBtnNext.toFloat()

        val firstPageIntro = configIntro.intros.first()
        adapter = IntroAdapter(arrayListOf(firstPageIntro))
        binding.vPager.adapter = adapter
        binding.vPager.registerOnPageChangeCallback(onChangePage)
        TabLayoutMediator(binding.tabLayout, binding.vPager) { _, _ -> }.attach()
        //
        viewmodel.loadPages(applicationContext)
    }

    override fun observeUI() {
        super.observeUI()
        viewmodel.nativeAds.observe(this) { ads ->
            if (ads.size != configIntro.intros.size) {
                return@observe
            }
            adapter.addDataSet(configIntro.intros.toMutableList())
            binding.tvStart.setOnClickListener { onClickNext() }
            // display ad first
            HeavenNativeAd.displayFromInstanceAd(
                this,
                viewmodel.getAdByID(viewmodel.getAdUnitID()[0]),
                binding.frAd,
                TypeNativeAd.FULL_AD
            )
        }
    }

    private fun onClickNext() {
        if (indexIntro == configIntro.intros.size - 1) {
            showLoading(true)
            HeavenSharePref.isFirstInstall = false
            HeavenInterstitialAD.loadAndDisplay(
                this,
                configAdID.interIntroCompleted,
                isSplashAd = true,
                enable = false,
                onDone = { error ->
                    showLoading(false)
                    val intent = Intent(this, configIntro.nextScreen)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                }
            )
            return
        }
        indexIntro++
        binding.vPager.setCurrentItem(indexIntro, true)
    }

    private fun startDelay() {
        cancelDelay()
        job = CoroutineScope(Dispatchers.Main).launch {
            with(binding.tvStart) {
                setTextColor(configIntro.textColorDisableBtnNextIntro)
                setOnClickListener {}
                delay(fbConfig.time_allow_next * 1000L)
                setTextColor(configIntro.textColorEnableBtnNextIntro)
                setOnClickListener { onClickNext() }
            }
        }
    }

    private fun cancelDelay() {
        if (job != null) {
            job?.cancel()
            job = null
            with(binding.tvStart) {
                setTextColor(configIntro.textColorEnableBtnNextIntro)
                setOnClickListener { onClickNext() }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vPager.unregisterOnPageChangeCallback(onChangePage)
        cancelDelay()
    }
}