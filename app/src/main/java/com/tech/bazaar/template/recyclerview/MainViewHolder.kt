package com.tech.bazaar.template.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tech.bazaar.template.helper.ImageSequentialLoader

open class MainViewHolder(
    itemView: View,
    val imageSequentialLoader: ImageSequentialLoader? = null,
) : RecyclerView.ViewHolder(itemView) {

    fun onBind(
        data: ViewType?,
        position: Int = -1,
        onClick: ((ViewType, Int) -> Unit?)?,
        onLongPress: ((ViewType, Int) -> Unit?)?,
        isSearchProduct: Boolean = false
    ) {

        when (data) {

        }
    }
}