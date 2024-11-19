package com.heaven.android.heavenlib.config

class ConfigBuildInfo private constructor(
    val applicationId: String,
    val isDebug: Boolean,
    val versionCode: Int,
    val versionName: String,
    // id debug
    val deviceIdUMPDebug: String,
    val deviceIdAdDebug: String
) {
    class Builder {
        private var applicationId: String = ""
        private var isDebug: Boolean = false
        private var versionCode: Int = 0
        private var versionName: String = ""
        private var deviceIdUMPDebug: String = ""
        private var deviceIdAdDebug: String = ""

        fun setApplicationId(applicationId: String) = apply { this.applicationId = applicationId }
        fun setIsDebug(isDebug: Boolean) = apply { this.isDebug = isDebug }
        fun setVersionCode(versionCode: Int) = apply { this.versionCode = versionCode }
        fun setVersionName(versionName: String) = apply { this.versionName = versionName }
        fun setDeviceIdUMPDebug(deviceID: String) = apply { this.deviceIdUMPDebug = deviceID }
        fun setDeviceIdAdDebug(deviceID: String) = apply { this.deviceIdAdDebug = deviceID }

        fun build(): ConfigBuildInfo {
            return ConfigBuildInfo(
                applicationId,
                isDebug,
                versionCode,
                versionName,
                deviceIdUMPDebug,
                deviceIdAdDebug
            )
        }
    }
}