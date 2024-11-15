package com.heaven.android.heavenlib.config

class ConfigBuildInfo private constructor(
    val applicationId: String,
    val isDebug: Boolean,
    val versionCode: Int,
    val versionName: String,
) {
    class Builder {
        private var applicationId: String = ""
        private var isDebug: Boolean = false
        private var versionCode: Int = 0
        private var versionName: String = ""

        fun setApplicationId(applicationId: String) = apply { this.applicationId = applicationId }
        fun setIsDebug(isDebug: Boolean) = apply { this.isDebug = isDebug }
        fun setVersionCode(versionCode: Int) = apply { this.versionCode = versionCode }
        fun setVersionName(versionName: String) = apply { this.versionName = versionName }

        fun build(): ConfigBuildInfo {
            return ConfigBuildInfo(applicationId, isDebug, versionCode, versionName)
        }
    }
}