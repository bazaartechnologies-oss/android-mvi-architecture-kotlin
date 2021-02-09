package com.tech.bazaar.template.dialog

import android.animation.Animator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.tech.bazaar.template.R

class AlertMessage(private val rootView: ViewGroup, private val context: Context) {

    companion object {
        const val CENTER = "center"
        const val BOTTOM = "bottom"
    }

    private var onClick: ((messageIdentifier: String?, alertMessageInstance: AlertMessage) -> Unit?)? =
        null
    var messageIdentifier: String? = null

    private var handler = Handler()
    private var runnable: Runnable? = null

    fun showMessage(message: String, gravity: String, cornerRadius: Int, onClick: ((messageIdentifier: String?, alertMessageInstance: AlertMessage) -> Unit?)? = null) {

        // remove existing alert view
        removeExistingAlertView(rootView, rootView.childCount)

        // add Alert message view to root view
        val messageView = addAlertMessageView(message, gravity, cornerRadius)

        messageView.setOnClickListener {
            onClick?.let { it1 -> it1(messageIdentifier, this) }
        }

        // animate view
        animateAlertMessageView(messageView)
    }

    private fun alertVisibility(messageView: RelativeLayout, visible: Boolean) {
        messageView.apply {

            // Set the content view to 0% opacity but visible, so that it is visible
            alpha = if (visible) 0f else 1f
            visibility = if (visible) View.VISIBLE else View.GONE

            // Animate the content view to 100% opacity, and clear any animation
            animate()
                .alpha(if (visible) 1f else 0f)
                .setDuration(500)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        clearAnimation()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {

                    }

                })
        }
    }


    private fun animateAlertMessageView(messageView: RelativeLayout) {

        messageView.apply {

            alertVisibility(this, true)

            runnable = Runnable {
                val refresh1 = Handler(Looper.getMainLooper())
                refresh1.post {
                    alertVisibility(this, false)
                }
            }

            runnable?.let {
                handler.postDelayed(it, 2000)
            }
        }
    }

    private fun addAlertMessageView(
        message: String,
        gravity: String,
        cornerRadius: Int
    ): RelativeLayout {
        val messageLayout: RelativeLayout =
            LayoutInflater.from(context).inflate(
                R.layout.view_show_message,
                rootView,
                false
            ) as RelativeLayout

        val alertMessage = messageLayout.findViewById<TextView>(R.id.alertMessage)
        val cornerLayout = messageLayout.findViewById<LinearLayout>(R.id.cornerLayout)

        if (cornerRadius == 8)
            cornerLayout.background =
                ContextCompat.getDrawable(context, R.drawable.corner_radius8_message_layout)
        else
            cornerLayout.background =
                ContextCompat.getDrawable(context, R.drawable.corner_radius10_message_layout)

        alertMessage.text = message
        val messageLayoutParam = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (gravity == CENTER) {
            messageLayoutParam.addRule(RelativeLayout.CENTER_IN_PARENT)
        } else {
            messageLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        }
        messageLayout.layoutParams = messageLayoutParam

        rootView.addView(messageLayout)
        return messageLayout
    }

    private fun removeExistingAlertView(rootView: ViewGroup, childCount: Int) {

        try {
            // remove runnable callback from handler
            runnable?.let {
                handler.removeCallbacks(it)
            }

            // remove alert view if exist
            for (i in 0 until childCount) {

                val currentView: View? = rootView[i]

                if (currentView != null && currentView.tag == "MESSAGE") {
                    rootView.removeViewAt(i)
                }
            }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }
}