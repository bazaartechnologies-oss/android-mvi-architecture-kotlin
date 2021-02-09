package com.tech.bazaar.template.recyclerview

import android.os.Parcelable
import com.tech.bazaar.template.R
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
open class ViewType(
    var type: Type
) : Serializable, Parcelable {

    var success: Boolean = false
    var message: String = ""

    enum class Type {
        ERROR,
        CATEGORIES,
        EMPTY,
        ON_ITEM_FRONT_LOADED,
    }

    companion object {

        fun viewHolderLayout(viewType: Int): Int {

            return when (viewType) {
//                Type.CATEGORIES.ordinal -> {
//                    R.layout.vh_category
//                }
                else -> 0
            }

        }
    }
}