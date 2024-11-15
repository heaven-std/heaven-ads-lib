package com.heaven.android.heavenlib.config

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.heaven.android.heavenlib.R
import com.heaven.android.heavenlib.datas.models.AppLanguage

class ConfigLanguage private constructor(
    val bgItemLanguage: Int,
    val languages: List<AppLanguage>,
    val icDoneLanguage: Int,
    val colorTitleLanguage: Int,
    val icSelectedLanguage: Int,
    val icUnSelectedLanguage: Int
) {
    class Builder {
        private var bgItemLanguage: Int = R.drawable.heaven_bg_language
        private var languages: List<AppLanguage> = AppLanguage.appSupportedLanguages
        private var icDoneLanguage: Int = R.drawable.ic_check
        private var colorTitleLanguage: Int = Color.BLACK
        private var icSelectedLanguage: Int = R.drawable.ic_selected
        private var icUnSelectedLanguage: Int = R.drawable.ic_un_selected

        fun setBgItemLanguage(@DrawableRes bgItemLanguage: Int) = apply { this.bgItemLanguage = bgItemLanguage }
        fun setLanguages(languages: List<AppLanguage>) = apply { this.languages = languages }
        fun setIcDoneLanguage(@DrawableRes icDoneLanguage: Int) = apply { this.icDoneLanguage = icDoneLanguage }
        fun setColorTitleLanguage(@ColorInt colorTitleLanguage: Int) = apply { this.colorTitleLanguage = colorTitleLanguage }
        fun setIcSelectedLanguage(@DrawableRes icSelectedLanguage: Int) = apply { this.icSelectedLanguage = icSelectedLanguage }
        fun setIcUnSelectedLanguage(@DrawableRes icUnSelectedLanguage: Int) = apply { this.icUnSelectedLanguage = icUnSelectedLanguage }

        fun build(): ConfigLanguage {
            return ConfigLanguage(
                bgItemLanguage,
                languages,
                icDoneLanguage,
                colorTitleLanguage,
                icSelectedLanguage,
                icUnSelectedLanguage
            )
        }
    }
}