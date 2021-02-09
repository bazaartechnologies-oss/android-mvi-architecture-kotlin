package com.tech.bazaar.template.dialog

import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tech.bazaar.template.R

class BazaarGeneralDialogFactory constructor(
    private val res: Resources,
    private val context: Context
) {

    fun buildLogoutDialog(
        positiveAction: (() -> Unit)?,
        negativeAction: (() -> Unit)?
    ): BazaarGeneralDialog {
        val builder = BazaarGeneralDialog.Builder()
        builder.title = res.getString(R.string.profile_log_out)
        builder.message = res.getString(R.string.dialog_message)
        builder.positiveButtonText = res.getString(R.string.profile_log_out_button_text)
        builder.negativeButtonText = res.getString(R.string.dialog_go_back)
        builder.buttonDrawable = ContextCompat.getDrawable(context, R.drawable.red_round_corner)
        builder.positiveAction = positiveAction
        builder.negativeAction = negativeAction
        builder.isCancelable = true
        return BazaarGeneralDialog(builder)
    }

    fun buildAndroidForceUpdateDialog(
        positiveAction: (() -> Unit)?
    ): BazaarGeneralDialog {
        val builder = BazaarGeneralDialog.Builder()
        builder.title = context.getString(R.string.naye_features)
        builder.message = context.getString(R.string.update_message)
        builder.positiveButtonText = context.getString(R.string.update_button)
        builder.buttonDrawable = ContextCompat.getDrawable(context, R.drawable.black_round_corner)
        builder.positiveAction = positiveAction
        builder.isCancelable = false
        return BazaarGeneralDialog(builder)
    }

    fun showDialog(
        bazaarGeneralDialog: BazaarGeneralDialog,
        fragmentManager: FragmentManager
    ) {
        if (!bazaarGeneralDialog.isAdded) {
            bazaarGeneralDialog.show(fragmentManager, BazaarGeneralDialog::class.java.name)
        }
    }

    fun getPermissionSettingDialog(
        positiveAction: (() -> Unit)?,
        negativeAction: (() -> Unit)?
    ): BazaarGeneralDialog {
        val builder = BazaarGeneralDialog.Builder()
        builder.title = res.getString(R.string.setting_dialog_title)
        builder.message = res.getString(R.string.setting_dialog_message)
        builder.positiveButtonText = res.getString(R.string.setting_dialog_button_tittle)
        builder.negativeButtonText = res.getString(R.string.helpline_go_back)
        builder.buttonDrawable = ContextCompat.getDrawable(context, R.drawable.black_round_corner)
        builder.positiveAction = positiveAction
        builder.negativeAction = negativeAction
        builder.isCancelable = false
        return BazaarGeneralDialog(builder)
    }

    fun buildNewAccountDialog(
        dialogMessage: String,
        positiveAction: (() -> Unit)?
    ): BazaarGeneralDialog {
        val builder = BazaarGeneralDialog.Builder()
        builder.title = context.getString(R.string.new_account_dialog_title)
        builder.message = dialogMessage
        builder.positiveButtonText = context.getString(R.string.new_account_dialog_button)
        builder.buttonDrawable = ContextCompat.getDrawable(context, R.drawable.black_round_corner)
        builder.positiveAction = positiveAction
        builder.isCancelable = true
        return BazaarGeneralDialog(builder)
    }

    fun buildForceLogoutDialog(
        positiveAction: (() -> Unit)?
    ): BazaarGeneralDialog {
        val builder = BazaarGeneralDialog.Builder()
        builder.title = context.getString(R.string.logout_title)
        builder.message = context.getString(R.string.logout_message)
        builder.positiveButtonText = context.getString(R.string.logout_button)
        builder.buttonDrawable = ContextCompat.getDrawable(context, R.drawable.black_round_corner)
        builder.positiveAction = positiveAction
        builder.isCancelable = false
        return BazaarGeneralDialog(builder)
    }

}