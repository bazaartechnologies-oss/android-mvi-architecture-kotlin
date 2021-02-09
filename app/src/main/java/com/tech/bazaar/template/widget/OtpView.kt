package com.tech.bazaar.template.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tech.bazaar.template.R
import com.tech.bazaar.template.extention.addOnTextChangeUpdate
import com.tech.bazaar.template.extention.hideKeyboard
import com.tech.bazaar.template.extention.showKeyboard
import kotlinx.android.synthetic.main.view_otp.view.*

class OtpView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    View.OnClickListener {

    init {
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.view_otp, this)

        applyAttributes(context, attrs)
        setActiveIndicator()
        editTextPin.post {
            editTextPin.showKeyboard()
        }
        editTextPin.addOnTextChangeUpdate { text, size ->
            setActiveIndicator()
        }
        linearLayoutPinView.setOnClickListener(this)
    }

    private fun resetIndicators() {
        textViewPinCharacter1.setBackgroundResource(R.drawable.text_line_inactive)
        textViewPinCharacter2.setBackgroundResource(R.drawable.text_line_inactive)
        textViewPinCharacter3.setBackgroundResource(R.drawable.text_line_inactive)
        textViewPinCharacter4.setBackgroundResource(R.drawable.text_line_inactive)
        textViewPinCharacter5.setBackgroundResource(R.drawable.text_line_inactive)
        textViewPinCharacter6.setBackgroundResource(R.drawable.text_line_inactive)
    }

    private fun setActiveIndicator() {
        resetIndicators()
        editTextPin.text.toString().let {
            when (it.length) {
                0 -> {
                    textViewPinCharacter1.setBackgroundResource(R.drawable.text_line_active)
                    textViewPinCharacter1.text = ""
                }
                1 -> {
                    textViewPinCharacter1.text = "${it[0]}"
                    textViewPinCharacter2.text = ""
                    textViewPinCharacter2.setBackgroundResource(R.drawable.text_line_active)
                }
                2 -> {
                    textViewPinCharacter1.text = "${it[0]}"
                    textViewPinCharacter2.text = "${it[1]}"
                    textViewPinCharacter3.text = ""
                    textViewPinCharacter3.setBackgroundResource(R.drawable.text_line_active)
                }
                3 -> {
                    textViewPinCharacter1.text = "${it[0]}"
                    textViewPinCharacter2.text = "${it[1]}"
                    textViewPinCharacter3.text = "${it[2]}"
                    textViewPinCharacter4.text = ""
                    textViewPinCharacter4.setBackgroundResource(R.drawable.text_line_active)
                }
                4 -> {
                    textViewPinCharacter1.text = "${it[0]}"
                    textViewPinCharacter2.text = "${it[1]}"
                    textViewPinCharacter3.text = "${it[2]}"
                    textViewPinCharacter4.text = "${it[3]}"
                    textViewPinCharacter5.text = ""
                    textViewPinCharacter5.setBackgroundResource(R.drawable.text_line_active)
                }
                5 -> {
                    textViewPinCharacter1.text = "${it[0]}"
                    textViewPinCharacter2.text = "${it[1]}"
                    textViewPinCharacter3.text = "${it[2]}"
                    textViewPinCharacter4.text = "${it[3]}"
                    textViewPinCharacter5.text = "${it[4]}"
                    textViewPinCharacter6.text = ""
                    textViewPinCharacter6.setBackgroundResource(R.drawable.text_line_active)
                }
                6 -> {
                    textViewPinCharacter1.text = "${it[0]}"
                    textViewPinCharacter2.text = "${it[1]}"
                    textViewPinCharacter3.text = "${it[2]}"
                    textViewPinCharacter4.text = "${it[3]}"
                    textViewPinCharacter5.text = "${it[4]}"
                    textViewPinCharacter6.text = "${it[5]}"
                    completeAction?.let { it() }
                }

                else -> {
                }
            }
        }

    }


    var completeAction: (() -> Unit)? = null


    private fun applyAttributes(context: Context?, attrs: AttributeSet?) {
        val viewProperties = context?.obtainStyledAttributes(attrs, R.styleable.OtpView)
        viewProperties?.recycle()
    }


    fun getText() = editTextPin.text.toString()

    fun clearText() {
        editTextPin.text.clear()
        textViewPinCharacter1.text = ""
        textViewPinCharacter2.text = ""
        textViewPinCharacter3.text = ""
        textViewPinCharacter4.text = ""
        textViewPinCharacter5.text = ""
        textViewPinCharacter6.text = ""
        setActiveIndicator()
    }

    fun setText(data: String) = editTextPin.setText(data)

    fun hideKeyboard() = editTextPin.hideKeyboard()

    override fun onClick(v: View?) {
        when (v?.id) {
            linearLayoutPinView.id -> {
                setActiveIndicator()
                editTextPin.showKeyboard()
            }
        }
    }


}