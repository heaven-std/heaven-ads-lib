package com.heaven.android.heavenlib.base.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.internal.ContextUtils
import com.google.firebase.analytics.FirebaseAnalytics
import com.heaven.android.heavenlib.R
import com.heaven.android.heavenlib.base.CoroutineLauncher
import com.heaven.android.heavenlib.datas.HeavenSharePref
import com.heaven.android.heavenlib.datas.models.AppLanguage
import com.heaven.android.heavenlib.datas.models.AppLanguage.Companion.appSupportedLanguages
import com.heaven.android.heavenlib.dialogs.DialogForceUpdate
import com.heaven.android.heavenlib.dialogs.IClickForeUpdate
import com.heaven.android.heavenlib.dialogs.LoadingDialog
import com.heaven.android.heavenlib.utils.MyContextWrapper
import java.util.Locale

typealias MyActivity = BaseActivity<*>

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    val activityScope: CoroutineLauncher by lazy {
        return@lazy CoroutineLauncher()
    }
    open val binding: B by lazy { makeBinding(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        prepareData()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val nameScreen = binding.toString()
        Log.e("BaseActivity", "onCreate: name: $nameScreen")
        val bundle = Bundle()
        bundle.putString(
            FirebaseAnalytics.Param.SCREEN_NAME,
            nameScreen
        )
        bundle.putString(
            FirebaseAnalytics.Param.SCREEN_CLASS,
            "class: $nameScreen"
        )
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
        setupView(savedInstanceState)
        observeUI()
    }

    abstract fun makeBinding(layoutInflater: LayoutInflater): B

    open fun setupView(savedInstanceState: Bundle?) {}

    open fun observeUI() {}

    open fun prepareData() {}

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancelCoroutines()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            fullScreenImmersive(window)
//            window.hideNavBar()
//            hideNavigationBar()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val localeToSwitchTo = HeavenSharePref.languageCode
        val localeUpdatedContext: ContextWrapper = MyContextWrapper.wrap(
            newBase,
            localeToSwitchTo
        )
        super.attachBaseContext(localeUpdatedContext)
    }

    open fun fullScreenImmersive(window: Window?) {
        if (window != null) {
            fullScreenImmersive(window.decorView)
        }
    }

    open fun fullScreenImmersive(view: View) {
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        view.systemUiVisibility = uiOptions
    }

    private var forceUpdateDialog: DialogForceUpdate? = null
    open fun showDialogForceUpdate(
        onlyUpdate: Boolean,
        onClickUpdate: () -> Unit,
        onClickNoThanks: () -> Unit
    ) {
        dismissDialogForceUpdate()
        forceUpdateDialog = DialogForceUpdate(
            onlyUpdate = onlyUpdate,
            object : IClickForeUpdate {
                override fun onClickUpdate() {
                    onClickUpdate.invoke()
                }

                override fun onClickNoThanks() {
                    onClickNoThanks.invoke()
                }
            }
        ).apply {
            show(supportFragmentManager, tag)
        }
    }

    open fun dismissDialogForceUpdate() {
        forceUpdateDialog?.dismiss()
    }

    private var loadingDialog: LoadingDialog? = null
    fun showLoading(isShow: Boolean) {
        if (isShow) {
            loadingDialog = LoadingDialog().apply {
                show(supportFragmentManager, tag)
            }
        } else {
            loadingDialog?.dismiss()
        }
    }

}