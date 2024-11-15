package com.heaven.android.heavenlib.config

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class ConfigSplash private constructor(
    val imageSplash: Int,
    val titleSplash: Int,
    val textSizeTitleSplash: Int,
    val textColorTitleSplash: Int
) {
    class Builder {
        private var imageSplash: Int = 0
        private var titleSplash: Int = 0
        private var textSizeTitleSplash: Int = 20
        private var textColorTitleSplash: Int = Color.BLACK

        fun setImageSplash(@DrawableRes imageSplash: Int) = apply { this.imageSplash = imageSplash }
        fun setTitleSplash(@StringRes titleSplash: Int) = apply { this.titleSplash = titleSplash }
        fun setTextSizeTitleSplash(textSizeTitleSplash: Int) = apply { this.textSizeTitleSplash = textSizeTitleSplash }
        fun setTextColorTitleSplash(@ColorInt textColorTitleSplash: Int) = apply { this.textColorTitleSplash = textColorTitleSplash }

        fun build(): ConfigSplash {
            return ConfigSplash(
                imageSplash,
                titleSplash,
                textSizeTitleSplash,
                textColorTitleSplash
            )
        }
    }
}