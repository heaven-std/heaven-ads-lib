package com.heaven.android.heavenlib.config

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.heaven.android.heavenlib.R
import com.heaven.android.heavenlib.datas.models.AppIntro

class ConfigIntro private constructor(
    val intros: List<AppIntro>,
    val nextScreen: Class<out AppCompatActivity>,
    val textColorTitleIntro: Int,
    val textSizeTitleIntro: Int,
    val textColorSubTitleIntro: Int,
    val textSizeSubTitleIntro: Int,
    val textColorEnableBtnNextIntro: Int,
    val textColorDisableBtnNextIntro: Int,
    val textSizeBtnNext: Int,
) {
    class Builder(
        private val intros: List<AppIntro>,
        private val nextScreen: Class<out AppCompatActivity>
    ) {
        private var textColorTitleIntro: Int = Color.BLACK
        private var textSizeTitleIntro: Int = 18
        private var textColorSubTitleIntro: Int = Color.BLACK
        private var textSizeSubTitleIntro: Int = 14
        private var textColorEnableBtnNextIntro: Int = Color.BLACK
        private var textColorDisableBtnNextIntro: Int = Color.GRAY
        private var textSizeBtnNext: Int = 16
        private var tabPagerIndicator: Int = R.drawable.tab_pager_selector

        fun setTextColorTitleIntro(@ColorInt textColorTitleIntro: Int) = apply { this.textColorTitleIntro = textColorTitleIntro }
        fun setTextSizeTitleIntro(textSizeTitleIntro: Int) = apply { this.textSizeTitleIntro = textSizeTitleIntro }
        fun setTextColorSubTitleIntro(@ColorInt textColorSubTitleIntro: Int) = apply { this.textColorSubTitleIntro = textColorSubTitleIntro }
        fun setTextSizeSubTitleIntro(textSizeSubTitleIntro: Int) = apply { this.textSizeSubTitleIntro = textSizeSubTitleIntro }
        fun setTextColorEnableBtnNextIntro(@ColorInt textColorEnableBtnNextIntro: Int) = apply { this.textColorEnableBtnNextIntro = textColorEnableBtnNextIntro }
        fun setTextColorDisableBtnNextIntro(@ColorInt textColorDisableBtnNextIntro: Int) = apply { this.textColorDisableBtnNextIntro = textColorDisableBtnNextIntro }
        fun setTextSizeBtnNext(textSizeBtnNext: Int) = apply { this.textSizeBtnNext = textSizeBtnNext }

        fun build(): ConfigIntro {
            return ConfigIntro(
                intros,
                nextScreen,
                textColorTitleIntro,
                textSizeTitleIntro,
                textColorSubTitleIntro,
                textSizeSubTitleIntro,
                textColorEnableBtnNextIntro,
                textColorDisableBtnNextIntro,
                textSizeBtnNext,
            )
        }
    }
}
