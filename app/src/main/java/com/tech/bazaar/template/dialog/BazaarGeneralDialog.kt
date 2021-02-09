package com.tech.bazaar.template.dialog

import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.tech.bazaar.template.R
import com.tech.bazaar.template.databinding.LayoutBazaarDialogBinding
import com.tech.bazaar.template.extention.hide

class BazaarGeneralDialog(
    private val builder: Builder
) : DialogFragment() {

    private lateinit var binding: LayoutBazaarDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            R.layout.layout_bazaar_dialog,
            null,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = builder.isCancelable
        setUI()
        setActionListener()
    }

    private fun setUI() {
        binding.dialogTitle.text = this.builder.title
        binding.dialogMessage.text = this.builder.message
        if (!this.builder.negativeButtonText.isNullOrEmpty()) {
            binding.dialogBtnNegative.text = this.builder.negativeButtonText
        } else {
            binding.dialogBtnNegative.hide()
        }
        binding.dialogBtnPositive.text = this.builder.positiveButtonText

        builder.buttonDrawable?.let {
            binding.dialogBtnPositive.background = it
        }
    }

    private fun setActionListener() {
        binding.dialogBtnNegative.setOnClickListener {
            dismiss()
            this.builder.negativeAction?.invoke()
        }

        binding.dialogBtnPositive.setOnClickListener {
            dismiss()
            this.builder.positiveAction?.invoke()
        }
    }

    override fun onStart() {
        super.onStart()
        val display: Display? = activity?.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        val width: Int = size.x
        dialog?.window?.setLayout(width - 60, WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bazaar_dialog_bg)
    }

    data class Builder(
        var title: String? = null,
        var message: String? = null,
        var negativeButtonText: String? = null,
        var positiveButtonText: String? = null,
        var negativeAction: (() -> Unit?)? = null,
        var positiveAction: (() -> Unit?)? = null,
        var buttonDrawable: Drawable? = null,
        var isCancelable: Boolean = false
    )
}