package com.tech.bazaar.template.extention

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tech.bazaar.template.AppApplication
import com.tech.bazaar.template.R
import com.tech.bazaar.template.base.view.BaseFragment
import com.tech.bazaar.template.dialog.AlertMessage
import com.tech.bazaar.template.dialog.BazaarToast

fun View.showMessageInToast(
    context: Context?,
    message: String,
    gravity: Int = Gravity.FILL_HORIZONTAL or Gravity.BOTTOM,
    xOffset: Int = 0,
    yOffset: Int = dpToPixel(80), // bottom nav bar height
    messageIdentifier: String? = null,
    onClick: ((messageIdentifier: String?, alertMessageInstance: AlertMessage) -> Unit?)? = null
): BazaarToast? {
    var toast: BazaarToast? = null
    context?.let {
        toast = BazaarToast.getInstance(it, root = this as ViewGroup)
        toast?.show(message, gravity, xOffset, yOffset)
    }

    return toast
}

fun View.showMessageInToastCenter(
    context: Context?,
    message: String,
    gravity: Int = Gravity.CENTER,
    messageIdentifier: String? = null,
    onClick: ((messageIdentifier: String?, alertMessageInstance: AlertMessage) -> Unit?)? = null
): BazaarToast? {
    var toast: BazaarToast? = null
    context?.let {
        toast = BazaarToast.getInstance(it, root = this as ViewGroup)
        toast?.show(message, gravity, 0, 0)
    }

    return toast
}

/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @return A int value to represent px equivalent to dp depending on device density
 */
fun dpToPixel(dp: Int): Int {
    return dp * (AppApplication.appContext.resources
        .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.show(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun View.setBackgroundTintColor(color: Int) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        this.backgroundTintList =
            ContextCompat.getColorStateList(AppApplication.appContext, color)
    }
}

fun View.focusAndShowKeyboard() {

    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {

        showTheKeyboardNow()

    } else {
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun Fragment.hideKeyboardFromCurrentFocus() {
    requireActivity().currentFocus?.hideKeyboard()
}

fun Fragment.onBackPressAction(action: () -> Unit) {
    requireActivity().onBackPressedDispatcher
        .addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                action()
            }
        })
}

fun View.showKeyboard() {
    if (requestFocus()) {
        this.post {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

fun getHtmlFormattedText(inputAsString: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(inputAsString, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(inputAsString)
    }

}

fun EditText.addOnTextChangeUpdate(action: (text: String, size: Int) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            action(s.toString().trim(), s.toString().trim().length)
        }

    })
}

@SuppressLint("ResourceType")
fun BaseFragment.getColorHashCode(resId: Int): String {
    return resources.getString(resId).replace("#ff", "#")
}

fun BaseFragment.onBoardingHeadingText(
    phone: String?,
    message: String,
    timer: String,
    endMessage: String,
    focusTextColorResource: Int = R.color.colorAccent
): String {
    return String.format(
        "%s %s<b><font color=%s> %s</font></b> %s",
        phone,
        message,
        getColorHashCode(focusTextColorResource),
        timer,
        endMessage
    )
}
