package com.tech.bazaar.template.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tech.bazaar.template.helper.ImageSequentialLoader

open class PagingRecyclerView(
    private val imageSequentialLoader: ImageSequentialLoader? = null,
    private val isSearchProduct: Boolean = false,
) :
    PagedListAdapter<ViewType, MainViewHolder>(DIFF_CALLBACK) {

    private var onClick: ((ViewType, Int) -> Unit?)? = null
    private var onLongPress: ((ViewType, Int) -> Unit?)? = null

    fun addListener(onClick: (data: ViewType, position: Int) -> Unit?) {
        this.onClick = onClick
    }

    fun addLongPressListener(onLongPress: (data: ViewType, position: Int) -> Unit?) {
        this.onLongPress = onLongPress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(ViewType.viewHolderLayout(viewType), parent, false)
        return MainViewHolder(view, imageSequentialLoader)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, position, onClick, onLongPress, isSearchProduct)
    }

    override fun getItemViewType(position: Int): Int {
        getItem(position)?.type?.let {
            return it.ordinal
        }
        return 0
    }

    companion object {
        val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<ViewType>() {
            override fun areItemsTheSame(
                oldConcert: ViewType,
                newConcert: ViewType
            ) = oldConcert.type == newConcert.type

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldConcert: ViewType,
                newConcert: ViewType
            ) = oldConcert == newConcert
        }
    }
}