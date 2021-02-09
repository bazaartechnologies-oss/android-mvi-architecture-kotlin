package com.tech.bazaar.template.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.tech.bazaar.template.R

class StickyScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.scrollViewStyle
) : NestedScrollView(context, attrs, defStyle) {

    private var stickyViews: ArrayList<View>? = null
    private var currentlyStickingView: View? = null
    private var stickyViewTopOffset = 0f
    private var stickyViewLeftOffset = 0
    private var redirectTouchesToStickyView = false
    private var clippingToPadding = false
    private var clipToPaddingHasBeenSet = false
    private var shadowHeight: Int
    private var shadowDrawable: Drawable? = null

    private val invalidateRunnable: Runnable = object : Runnable {
        override fun run() {
            if (currentlyStickingView != null) {
                val l = getLeftForViewRelativeOnlyChild(currentlyStickingView)
                val t = getBottomForViewRelativeOnlyChild(currentlyStickingView!!)
                val r = getRightForViewRelativeOnlyChild(currentlyStickingView)
                val b =
                    (scrollY + (currentlyStickingView!!.height + stickyViewTopOffset)).toInt()
                invalidate(l, t, r, b)
            }
            postDelayed(this, 16)
        }
    }

    init {
        setup()
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.StickyScrollView, defStyle, 0
        )
        val density = context.resources.displayMetrics.density
        val defaultShadowHeightInPix =
            (DEFAULT_SHADOW_HEIGHT * density + 0.5f).toInt()
        shadowHeight = a.getDimensionPixelSize(
            R.styleable.StickyScrollView_stuckShadowHeight,
            defaultShadowHeightInPix
        )
        val shadowDrawableRes = a.getResourceId(
            R.styleable.StickyScrollView_stuckShadowDrawable, -1
        )
        if (shadowDrawableRes != -1) {
            shadowDrawable = context.resources.getDrawable(
                shadowDrawableRes
            )
        }
        a.recycle()
    }

    /**
     * Sets the height of the shadow drawable in pixels.
     *
     * @param height
     */
    fun setShadowHeight(height: Int) {
        shadowHeight = height
    }

    fun setup() {
        stickyViews = ArrayList()
    }

    private fun getLeftForViewRelativeOnlyChild(v: View?): Int {
        var v = v
        var left = v!!.left
        while (v!!.parent !== getChildAt(0)) {
            v = v!!.parent as View
            left += v.left
        }
        return left
    }

    private fun getTopForViewRelativeOnlyChild(v: View?): Int {
        var v = v
        var top = v!!.top
        while (v!!.parent !== getChildAt(0)) {
            v = v!!.parent as View
            top += v.top
        }
        return top
    }

    private fun getRightForViewRelativeOnlyChild(v: View?): Int {
        var v = v
        var right = v!!.right
        while (v!!.parent !== getChildAt(0)) {
            v = v!!.parent as View
            right += v.right
        }
        return right
    }

    private fun getBottomForViewRelativeOnlyChild(v: View): Int {
        var v = v
        var bottom = v.bottom
        while (v.parent !== getChildAt(0)) {
            v = v.parent as View
            bottom += v.bottom
        }
        return bottom
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        super.onLayout(changed, l, t, r, b)
        if (!clipToPaddingHasBeenSet) {
            clippingToPadding = true
        }
        notifyHierarchyChanged()
    }

    override fun setClipToPadding(clipToPadding: Boolean) {
        super.setClipToPadding(clipToPadding)
        clippingToPadding = clipToPadding
        clipToPaddingHasBeenSet = true
    }

    override fun addView(child: View) {
        super.addView(child)
        findStickyViews(child)
    }

    override fun addView(child: View, index: Int) {
        super.addView(child, index)
        findStickyViews(child)
    }

    override fun addView(
        child: View,
        index: Int,
        params: ViewGroup.LayoutParams
    ) {
        super.addView(child, index, params)
        findStickyViews(child)
    }

    override fun addView(child: View, width: Int, height: Int) {
        super.addView(child, width, height)
        findStickyViews(child)
    }

    override fun addView(
        child: View,
        params: ViewGroup.LayoutParams
    ) {
        super.addView(child, params)
        findStickyViews(child)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (currentlyStickingView != null) {
            canvas.save()
            canvas.translate(
                paddingLeft + stickyViewLeftOffset.toFloat(),
                scrollY + stickyViewTopOffset + if (clippingToPadding) paddingTop else 0
            )
            canvas.clipRect(
                0f, (if (clippingToPadding) -stickyViewTopOffset else 0f),
                width - stickyViewLeftOffset.toFloat(),
                currentlyStickingView!!.height + shadowHeight + 1.toFloat()
            )
            if (shadowDrawable != null) {
                val left = 0
                val right = currentlyStickingView!!.width
                val top = currentlyStickingView!!.height
                val bottom = currentlyStickingView!!.height + shadowHeight
                shadowDrawable!!.setBounds(left, top, right, bottom)
                shadowDrawable!!.draw(canvas)
            }
            canvas.clipRect(
                0f,
                (if (clippingToPadding) -stickyViewTopOffset else 0f),
                width.toFloat(),
                currentlyStickingView!!.height.toFloat()
            )
            if (getStringTagForView(currentlyStickingView!!).contains(FLAG_HASTRANSPARANCY)) {
                showView(currentlyStickingView!!)
                currentlyStickingView!!.draw(canvas)
                hideView(currentlyStickingView!!)
            } else {
                currentlyStickingView!!.draw(canvas)
            }
            currentlyStickingView!!.draw(canvas)
            canvas.restore()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            redirectTouchesToStickyView = true
        }
        if (redirectTouchesToStickyView) {
            redirectTouchesToStickyView = currentlyStickingView != null
            if (redirectTouchesToStickyView) {
                redirectTouchesToStickyView =
                    ev.y <= currentlyStickingView!!.height + stickyViewTopOffset && ev.x >= getLeftForViewRelativeOnlyChild(
                        currentlyStickingView
                    ) && ev.x <= getRightForViewRelativeOnlyChild(currentlyStickingView)
            }
        } else if (currentlyStickingView == null) {
            redirectTouchesToStickyView = false
        }
        if (redirectTouchesToStickyView) {
            ev.offsetLocation(
                0f,
                -1 * (scrollY + stickyViewTopOffset - getTopForViewRelativeOnlyChild(
                    currentlyStickingView
                ))
            )
        }
        return super.dispatchTouchEvent(ev)
    }

    private var hasNotDoneActionDown = true

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (redirectTouchesToStickyView) {
            ev.offsetLocation(
                0f,
                scrollY + stickyViewTopOffset - getTopForViewRelativeOnlyChild(
                    currentlyStickingView
                )
            )
        }
        if (ev.action == MotionEvent.ACTION_DOWN) {
            hasNotDoneActionDown = false
        }
        if (hasNotDoneActionDown) {
            val down = MotionEvent.obtain(ev)
            down.action = MotionEvent.ACTION_DOWN
            super.onTouchEvent(down)
            hasNotDoneActionDown = false
        }
        if (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_CANCEL) {
            hasNotDoneActionDown = true
        }
        return super.onTouchEvent(ev)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        doTheStickyThing()
    }

    private fun doTheStickyThing() {
        var viewThatShouldStick: View? = null
        var approachingView: View? = null
        for (v in stickyViews!!) {
            val viewTop =
                getTopForViewRelativeOnlyChild(v) - scrollY + if (clippingToPadding) 0 else paddingTop
            if (viewTop <= 0) {
                if (viewThatShouldStick == null || viewTop > getTopForViewRelativeOnlyChild(
                        viewThatShouldStick
                    ) - scrollY + if (clippingToPadding) 0 else paddingTop
                ) {
                    viewThatShouldStick = v
                }
            } else {
                if (approachingView == null || viewTop < getTopForViewRelativeOnlyChild(
                        approachingView
                    ) - scrollY + if (clippingToPadding) 0 else paddingTop
                ) {
                    approachingView = v
                }
            }
        }
        if (viewThatShouldStick != null) {
            stickyViewTopOffset = if (approachingView == null) 0f else Math.min(
                0,
                getTopForViewRelativeOnlyChild(approachingView) - scrollY + (if (clippingToPadding) 0 else paddingTop) - viewThatShouldStick.height
            ).toFloat()
            if (viewThatShouldStick !== currentlyStickingView) {
                if (currentlyStickingView != null) {
                    stopStickingCurrentlyStickingView()
                }
                // only compute the left offset when we start sticking.
                stickyViewLeftOffset = getLeftForViewRelativeOnlyChild(viewThatShouldStick)
                startStickingView(viewThatShouldStick)
            }
        } else if (currentlyStickingView != null) {
            stopStickingCurrentlyStickingView()
        }
    }

    private fun startStickingView(viewThatShouldStick: View) {
        currentlyStickingView = viewThatShouldStick
        if (getStringTagForView(currentlyStickingView!!).contains(FLAG_HASTRANSPARANCY)) {
            hideView(currentlyStickingView!!)
        }
        if ((currentlyStickingView!!.tag as String).contains(FLAG_NONCONSTANT)) {
            post(invalidateRunnable)
        }
    }

    private fun stopStickingCurrentlyStickingView() {
        if (getStringTagForView(currentlyStickingView!!).contains(FLAG_HASTRANSPARANCY)) {
            showView(currentlyStickingView!!)
        }
        currentlyStickingView = null
        removeCallbacks(invalidateRunnable)
    }

    private fun notifyHierarchyChanged() {
        if (currentlyStickingView != null) {
            stopStickingCurrentlyStickingView()
        }
        stickyViews!!.clear()
        findStickyViews(getChildAt(0))
        doTheStickyThing()
        invalidate()
    }

    private fun findStickyViews(v: View) {
        if (v is ViewGroup) {
            val vg = v
            for (i in 0 until vg.childCount) {
                val tag = getStringTagForView(vg.getChildAt(i))
                if (tag.isNotEmpty() && tag.contains(STICKY_TAG)) {
                    stickyViews!!.add(vg.getChildAt(i))
                } else if (vg.getChildAt(i) is ViewGroup) {
                    findStickyViews(vg.getChildAt(i))
                }
            }
        } else {
            val tag = v.tag as String
            if (tag.isNotEmpty() && tag.contains(STICKY_TAG)) {
                stickyViews!!.add(v)
            }
        }
    }

    private fun getStringTagForView(v: View): String {
        return v.tag?.toString().orEmpty()
    }

    private fun hideView(v: View) {
        v.alpha = 0f
    }

    private fun showView(v: View) {
        v.alpha = 1f
    }

    companion object {
        /**
         * Tag for views that should stick and have constant drawing. e.g. TextViews, ImageViews etc
         */
        const val STICKY_TAG = "sticky"

        /**
         * Default height of the shadow peeking out below the stuck view.
         */
        private const val DEFAULT_SHADOW_HEIGHT = 10 // dp;


        /**
         * Flag for views that should stick and have non-constant drawing. e.g. Buttons, ProgressBars etc
         */
        const val FLAG_NONCONSTANT = "-nonconstant"

        /**
         * Flag for views that have aren't fully opaque
         */
        const val FLAG_HASTRANSPARANCY = "-hastransparancy"
    }

}