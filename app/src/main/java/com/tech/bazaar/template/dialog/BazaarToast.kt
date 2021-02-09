package com.tech.bazaar.template.dialog

import android.content.Context
import android.view.Gravity.FILL_HORIZONTAL
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.tech.bazaar.template.R

class BazaarToast constructor(
    private var context: Context,
    private var root: ViewGroup
) {
    private var toast: Toast? = null

    fun show(message: String, gravity: Int = FILL_HORIZONTAL, xOffset: Int, yOffset: Int) {
        val layout = LayoutInflater.from(context).inflate(R.layout.view_show_message, root, false)

        val text = layout.findViewById<TextView>(R.id.alertMessage)
        text.text = message

        toast = Toast(context)
        toast?.setGravity(gravity, xOffset, yOffset)
        toast?.duration = Toast.LENGTH_LONG
        toast?.view = layout
        toast?.show()
    }

    fun cancelToast() {
        toast?.cancel()
    }

    companion object {
        private var instance: BazaarToast? = null

        @Synchronized
        fun getInstance(
            context: Context,
            root: ViewGroup
        ): BazaarToast? {
            if (instance == null) {
                instance = BazaarToast(context, root)
            }
            instance!!.context = context
            instance!!.root = root
            return instance
        }
    }
}