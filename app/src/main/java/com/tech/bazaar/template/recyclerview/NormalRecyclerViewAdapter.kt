package com.tech.bazaar.template.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tech.bazaar.template.helper.ImageSequentialLoader

class NormalRecyclerViewAdapter(
    private val imageSequentialLoader: ImageSequentialLoader? = null,
) : RecyclerView.Adapter<MainViewHolder>() {

    private var viewTypeList = ArrayList<ViewType?>()
    private var onClick: ((ViewType, Int) -> Unit?)? = null
    private var onLongPress: ((ViewType, Int) -> Unit?)? = null

    fun addListener(onClick: (data: ViewType, position: Int) -> Unit?) {
        this.onClick = onClick
    }

    fun addLongPressListener(onLongPress: (data: ViewType, position: Int) -> Unit?) {
        this.onLongPress = onLongPress
    }

    fun submitList(viewTypeList: ArrayList<ViewType?>) {
        this.viewTypeList = viewTypeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(ViewType.viewHolderLayout(viewType), parent, false)
        return MainViewHolder(view, imageSequentialLoader)
    }

    override fun getItemCount(): Int {
        return viewTypeList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = viewTypeList[position]
        holder.onBind(item, position, onClick, onLongPress)
    }

    override fun getItemViewType(position: Int): Int {
        viewTypeList[position]?.type?.let {
            return it.ordinal
        }
        return 0
    }
}