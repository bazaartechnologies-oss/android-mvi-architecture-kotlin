package com.tech.bazaar.template.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import java.util.ArrayList

/**
 * EnhancedRadioGroup is created to detect RadioButtons with-in container/nested view or where
 * RadioButtons are not direct child of RadioGroup, as normal RadioGroup can't detect these.
 */
class EnhancedRadioGroup : RadioGroup, View.OnClickListener {

    interface OnSelectionChangedListener {
        fun onSelectionChanged(radioButton: RadioButton?, index: Int)
    }

    private var selectionChangedListener: OnSelectionChangedListener? = null
    var radioButtons = ArrayList<RadioButton>()

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            getRadioButtons()
        }
    }

    private fun getRadioButtons() {
        radioButtons.clear()
        checkForRadioButtons(this)
    }

    private fun checkForRadioButtons(viewGroup: ViewGroup?) {
        if (viewGroup == null) {
            return
        }
        for (i in 0 until viewGroup.childCount) {
            val v = viewGroup.getChildAt(i)
            if (v is RadioButton) {
                v.setOnClickListener(this)
                // store index of item
                v.setTag(radioButtons.size)
                radioButtons.add(v)
            } else if (v is ViewGroup) {
                checkForRadioButtons(v)
            }
        }
    }

    val selectedItem: RadioButton?
        get() {
            if (radioButtons.isEmpty()) {
                getRadioButtons()
            }
            for (radioButton in radioButtons) {
                if (radioButton.isChecked) {
                    return radioButton
                }
            }
            return null
        }

    fun setOnSelectionChanged(selectionChangedListener: OnSelectionChangedListener?) {
        this.selectionChangedListener = selectionChangedListener
    }

    fun setSelectedById(id: Int) {
        if (radioButtons.isEmpty()) {
            getRadioButtons()
        }
        for (radioButton in radioButtons) {
            val isSelectedRadioButton = radioButton.id == id
            radioButton.isChecked = isSelectedRadioButton
            if (isSelectedRadioButton && selectionChangedListener != null) {
                selectionChangedListener!!.onSelectionChanged(radioButton, radioButton.tag as Int)
            }
        }
    }

    fun setSelectedByIndex(index: Int) {
        if (radioButtons.isEmpty()) {
            getRadioButtons()
        }
        if (radioButtons.size > index) {
            setSelectedRadioButton(radioButtons[index])
        }
    }

    override fun onClick(v: View) {
        setSelectedRadioButton(v as RadioButton)
    }

    override fun clearCheck() {
        super.clearCheck()
        clearAll()
    }

    private fun clearAll() {
        for (radioButton in radioButtons) {
            if (radioButton.isChecked) {
                radioButton.isChecked = false
            }
        }
    }

    private fun setSelectedRadioButton(rb: RadioButton) {
        if (radioButtons.isEmpty()) {
            getRadioButtons()
        }
        for (radioButton in radioButtons) {
            radioButton.isChecked = rb === radioButton
        }
        if (selectionChangedListener != null) {
            selectionChangedListener!!.onSelectionChanged(rb, rb.tag as Int)
        }
    }
}