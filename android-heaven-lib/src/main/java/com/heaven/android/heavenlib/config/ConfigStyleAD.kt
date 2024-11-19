package com.heaven.android.heavenlib.config

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.heaven.android.heavenlib.R

class ConfigStyleAD private constructor(
    val backgroundApp: Int,
    val backgroundButtonAD: Int,
    val backgroundAdNormalAD: Int,
    val backgroundIconChoiceAD: Int,
    val textColorHeaderAD: Int,
    val textColorContentAD: Int,
    val textColorButtonAD: Int,
    val textColorAdChoiceAD: Int
) {
    @SuppressLint("ResourceType")
    class Builder {
        private var backgroundApp: Int = R.drawable.heaven_bg_app
        private var backgroundButtonAD: Int = R.drawable.bg_btn_ad
        private var backgroundAdNormalAD: Int = Color.GRAY
        private var backgroundIconChoiceAD: Int = R.drawable.bg_ad_icon
        private var textColorHeaderAD: Int = Color.BLACK
        private var textColorContentAD: Int = Color.BLACK
        private var textColorButtonAD: Int = Color.WHITE
        private var textColorAdChoiceAD: Int = Color.WHITE

        fun setBackgroundApp(@DrawableRes backgroundApp: Int) = apply { this.backgroundApp = backgroundApp }
        fun setBackgroundButtonAD(@DrawableRes backgroundButtonAD: Int) = apply { this.backgroundButtonAD = backgroundButtonAD }
        fun setBackgroundAdNormalAD(@ColorInt backgroundAdNormalAD: Int) = apply { this.backgroundAdNormalAD = backgroundAdNormalAD }
        fun setBackgroundIconChoiceAD(@DrawableRes backgroundIconChoiceAD: Int) = apply { this.backgroundIconChoiceAD = backgroundIconChoiceAD }
        fun setTextColorHeaderAD(@ColorInt textColorHeaderAD: Int) = apply { this.textColorHeaderAD = textColorHeaderAD }
        fun setTextColorContentAD(@ColorInt textColorContentAD: Int) = apply { this.textColorContentAD = textColorContentAD }
        fun setTextColorButtonAD(@ColorInt textColorButtonAD: Int) = apply { this.textColorButtonAD = textColorButtonAD }
        fun setTextColorAdChoiceAD(@ColorInt textColorAdChoiceAD: Int) = apply { this.textColorAdChoiceAD = textColorAdChoiceAD }

        fun build(): ConfigStyleAD {
            return ConfigStyleAD(
                backgroundApp,
                backgroundButtonAD,
                backgroundAdNormalAD,
                backgroundIconChoiceAD,
                textColorHeaderAD,
                textColorContentAD,
                textColorButtonAD,
                textColorAdChoiceAD
            )
        }
    }
}