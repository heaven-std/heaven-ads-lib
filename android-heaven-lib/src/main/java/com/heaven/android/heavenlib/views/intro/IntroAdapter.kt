package com.heaven.android.heavenlib.views.intro

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heaven.android.heavenlib.R
import com.heaven.android.heavenlib.config.HeavenEnv
import com.heaven.android.heavenlib.datas.models.AppIntro

class IntroAdapter(
    private var items: MutableList<AppIntro>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun addDataSet(items: MutableList<AppIntro>) {
        this.items.clear()
        this.items = items
        notifyDataSetChanged()
    }

    inner class NormalIntroViewHolder(
        private val itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: AppIntro) {
            val vContext = itemView.context
            val configIntroPage = HeavenEnv.configIntro

            val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
            val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
            val imgIntro = itemView.findViewById<ImageView>(R.id.imgIntro)

            tvTitle.text = vContext.getString(data.title)
            tvTitle.setTextColor(configIntroPage.textColorTitleIntro)
            tvTitle.textSize = configIntroPage.textSizeTitleIntro.toFloat()

            tvDescription.text = vContext.getString(data.description)
            tvDescription.setTextColor(configIntroPage.textColorSubTitleIntro)
            tvDescription.textSize = configIntroPage.textSizeSubTitleIntro.toFloat()

            imgIntro.setImageResource(data.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_intro_normal, parent, false)
        return NormalIntroViewHolder(view)
    }

    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NormalIntroViewHolder).bind(items[position])
    }

}