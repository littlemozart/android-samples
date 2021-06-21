package com.lee.androidsamples.ui.drag

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.customview.widget.ViewDragHelper

class DragViewLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var viewDragHelper: ViewDragHelper? = null
    private var dragView: View? = null

    private var dragViewX = 0
    private var dragViewY = 0

    private var isFirst = true

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev ?: return super.onInterceptTouchEvent(ev)
        return viewDragHelper?.shouldInterceptTouchEvent(ev) ?: super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        try {
            event?.run { viewDragHelper?.processTouchEvent(this) }
        } catch (e: Exception) {
            Log.d(TAG, "onTouchEvent: ", e)
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (viewDragHelper?.continueSettling(true) == true) {
            postInvalidateOnAnimation()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        dragView?.run {
            if (isFirst) {
                isFirst = false
                dragViewX = this.left
                dragViewY = this.top
            } else {
                this.layout(
                    dragViewX,
                    dragViewY,
                    dragViewX + this.measuredWidth,
                    dragViewY + this.measuredHeight
                )
            }
        }
    }

    fun setDragView(view: View) {
        if (dragView != view) {
            dragView = view
            initViewDragHelper()
        }
    }

    private fun initViewDragHelper() {
        if (viewDragHelper == null) {
            viewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
                override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                    return child == dragView
                }

                override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                    if (child.measuredHeight + child.marginBottom + top > measuredHeight) {
                        return measuredHeight - child.marginBottom - child.measuredHeight
                    }
                    return maxOf(child.marginTop, top)
                }

                override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                    if (child.measuredWidth + child.marginEnd + left > measuredWidth) {
                        return measuredWidth - child.marginEnd - child.measuredWidth
                    }
                    return maxOf(child.marginStart, left)
                }

                override fun getViewHorizontalDragRange(child: View): Int {
                    return measuredWidth - child.measuredWidth - child.marginStart - child.marginEnd
                }

                override fun getViewVerticalDragRange(child: View): Int {
                    return measuredHeight - child.measuredHeight - child.marginTop - child.marginBottom
                }

                override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                    super.onViewReleased(releasedChild, xvel, yvel)
                    if (releasedChild == dragView) {
                        dragViewY = releasedChild.y.toInt()
                        dragViewX = if (releasedChild.x >= measuredWidth / 2) {
                            measuredWidth - releasedChild.measuredWidth - releasedChild.marginEnd
                        } else {
                            releasedChild.marginStart
                        }
                        viewDragHelper?.settleCapturedViewAt(dragViewX, dragViewY)
                        invalidate()
                    }
                }
            })
        }
    }

    companion object {
        private const val TAG = "DragViewLayout"
    }
}