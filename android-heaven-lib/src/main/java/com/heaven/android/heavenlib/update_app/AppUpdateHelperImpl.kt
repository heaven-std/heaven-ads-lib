package com.heaven.android.heavenlib.update_app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.datas.FBConfig
import com.heaven.android.heavenlib.dialogs.DialogForceUpdate
import com.heaven.android.heavenlib.dialogs.IClickForeUpdate
import com.heaven.android.heavenlib.utils.Utils.isNeedUpdateAppVersions

class AppUpdateHelperImpl(val activity: AppCompatActivity) : AppUpdateHelper {
    private var forceUpdateDialog: DialogForceUpdate? = null

    private val uriStore1 = "market://details?id=${HeavenEnv.buildConfig.applicationId}"
    private val uriStore2 =
        "https://play.google.com/store/apps/details?id=${HeavenEnv.buildConfig.applicationId}"


    override fun checkUpdate(listener: IAppUpdateListener) {
        val appVersionConfig = FBConfig.getAppVersionConfig()
        val statusUpdate = isNeedUpdateAppVersions(
            HeavenEnv.buildConfig.versionName, appVersionConfig
        )
        when (statusUpdate) {
            StatusForceUpdate.NONE -> {
                listener.onDismiss()
            }

            StatusForceUpdate.HAS_NO_THANKS -> {
                showUpdateDialog(false) { listener.onDismiss() }
            }

            StatusForceUpdate.ONLY_UPDATE -> {
                showUpdateDialog(true)
            }
        }

    }

    override fun openStore() {
        try {
            activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(uriStore1))
            )
        } catch (e: ActivityNotFoundException) {
            activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(uriStore2))
            )
        }
        activity.finish()
    }

    private fun showUpdateDialog(
        onlyUpdate: Boolean,
        onClickDismiss: (() -> Unit)? = null
    ) {
        forceUpdateDialog = DialogForceUpdate(
            onlyUpdate = onlyUpdate,
            object : IClickForeUpdate {
                override fun onClickUpdate() {
                    openStore()
                }

                override fun onClickNoThanks() {
                    dismissDialog()
                    onClickDismiss?.invoke()
                }
            }
        )
        forceUpdateDialog?.show(activity.supportFragmentManager, forceUpdateDialog?.tag)
    }

    private fun dismissDialog() {
        forceUpdateDialog?.dismiss()
    }
}