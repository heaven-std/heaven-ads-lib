package com.heaven.android.heavenlib.datas.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.utils.Logger
import java.lang.RuntimeException
import java.lang.reflect.Type

data class AdConfigModel(
    val version_reviewing: String = "",
    var enable_all_ads: Boolean = true,
    var enable_rating: Boolean = true,
    val enable_inter_splash: Boolean = true,
    val enable_open_resume: Boolean = true,
    val enable_native_language: Boolean = true,
    val enable_native_language_selected: Boolean = true,
    val enable_native_language_setting: Boolean = true,
    val enable_native_intro1: Boolean = true,
    val enable_native_intro2: Boolean = true,
    val enable_native_intro3: Boolean = true,
    val enable_native_intro4: Boolean = true,
    val enable_native_intro1_2nd: Boolean = true,
    val enable_native_intro2_2nd: Boolean = true,
    val enable_native_intro3_2nd: Boolean = true,
    val enable_native_intro4_2nd: Boolean = true,
    val time_allow_next: Int = 3,
    val time_load_inter: Int = 30,
    val time_reload_banner: Int = 30,
    val customFields: MutableMap<String, Any?> = mutableMapOf()
) {

    fun addField(field: Map<String, Any?>) {
        customFields.putAll(field)
    }

    fun addCustomField(fields: List<Map<String, Any?>>) {
        for (fieldMap in fields) {
            customFields.putAll(fieldMap)
        }
    }

    fun getCustomField(key: String): Any {
        return customFields[key] ?: throw RuntimeException("Unknown field AdConfigModel")
    }

    companion object {

        fun fromJson(json: String): AdConfigModel {
            val gson = GsonBuilder()
                .registerTypeAdapter(AdConfigModel::class.java, DynamicModelDeserializer())
                .create()

            val obj =  gson.fromJson(json, AdConfigModel::class.java)

            return if (checkVersionReviewing(obj.version_reviewing)){
                obj.copy(
                    enable_all_ads = false,
                    enable_rating = false,
                    time_allow_next = 0
                )
            } else {
                obj
            }

        }

        fun defaultEnableAds(): AdConfigModel {
            return AdConfigModel()
        }

        fun checkVersionReviewing(versionReviewing: String): Boolean {
            return convertStringToInt(HeavenEnv.buildConfig.versionName) == convertStringToInt(
                versionReviewing
            )
        }

        private fun convertStringToInt(str: String): Int {
            return try {
                val strVersion = str.split(".")
                if (strVersion.size < 2) "${strVersion[0]}0".toInt()
                else "${strVersion[0]}${strVersion[1]}".toInt()
            } catch (e: Exception) {
                Logger.log("convertStringToInt", "convertStringToInt: err convert string to int")
                convertStringToInt(HeavenEnv.buildConfig.versionName)
            }
        }

    }

}

class DynamicModelDeserializer : JsonDeserializer<AdConfigModel> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AdConfigModel {
        val jsonObject = json.asJsonObject

        val gson = Gson()
        val model = gson.fromJson(jsonObject, AdConfigModel::class.java)
        val knownFields = AdConfigModel::class.java.declaredFields.map { it.name }.toSet()

        for ((key, value) in jsonObject.entrySet()) {
            if (key !in knownFields) {
                model.customFields[key] = when {
                    value.isJsonPrimitive -> {
                        val primitive = value.asJsonPrimitive
                        when {
                            primitive.isBoolean -> primitive.asBoolean
                            primitive.isNumber -> primitive.asNumber
                            primitive.isString -> primitive.asString
                            else -> null
                        }
                    }

                    value.isJsonObject -> value.asJsonObject
                    value.isJsonArray -> value.asJsonArray
                    else -> null
                }
            }
        }
        return model
    }
}
