/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 battleent
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.battleent.clipaniviews

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.battleent.clipaniviews.component.ClipTextView
import java.lang.ref.WeakReference
import java.util.*


class ClipAniLayout : FrameLayout, View.OnClickListener {

    companion object {
        const val MSG_PROGRESS = 0
        const val MSG_CLICK_DISABLE = 1
    }

    enum class ClipStatue {
        STATE_CLIP,
        STATE_FILL
    }

    private var delegate: Delegate? = null

    interface Delegate {
        fun onClickFillStatues()
    }

    private lateinit var clipTextView: ClipTextView
    var progressNumber = 0

    private var clipStatus = ClipStatue.STATE_CLIP

    private var handler: ClipAniLayoutHandler? = null
    private var aniThread: Thread? = null
    private var timer: Timer? = null

    private var backGroundColor: Int = ContextCompat.getColor(context, android.R.color.black)
    private var text: String = ""
    private var textColor: Int = ContextCompat.getColor(context, android.R.color.white)
    private var textSize: Int = 13
    private var aniDuration: Long = 3
    private var aniStayDuration: Long = 3000

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { setAttrs(attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setAttrs(attrs, defStyleAttr)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        clipViewCreate(context)
        updateAttr()
    }

    fun reset() {
        aniThread?.interrupt()
        timer?.cancel()
        timer?.purge()

        val msg = Message.obtain()
        msg.what = MSG_PROGRESS
        msg.arg1 = 0
        handler?.sendMessage(msg)

        clipStatus = ClipStatue.STATE_CLIP
    }

    private fun clipViewCreate(context: Context) {
        setOnClickListener(this)
        clipTextView = ClipTextView(context)
        clipTextView.layoutParams = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        clipTextView.gravity = Gravity.CENTER
        clipTextView.setTypeface(clipTextView.typeface, Typeface.NORMAL)
        addView(clipTextView)
    }

    private fun setAttrs(attrs: AttributeSet, defStyleAttr: Int? = null) {
        val typedArray = defStyleAttr?.let {
            context.obtainStyledAttributes(attrs, R.styleable.ClipView, defStyleAttr, 0)
        } ?: let {
            context.obtainStyledAttributes(attrs, R.styleable.ClipView)
        }

        try {
            typedArray.run {
                backGroundColor = getColor(R.styleable.ClipView_clip_backgroundColor, backGroundColor)
                getString(R.styleable.ClipView_clip_text)?.let {
                    text = it
                } ?: let {
                    text = ""
                }
                textColor = getColor(R.styleable.ClipView_clip_textColor, textColor)
                textSize = getInt(R.styleable.ClipView_clip_textSize, textSize)
                aniDuration = getInt(R.styleable.ClipView_clip_aniDuration, aniDuration.toInt()).toLong()
                aniStayDuration = getInt(R.styleable.ClipView_clip_aniStayDuration, aniStayDuration.toInt()).toLong()

            }
        } finally {
            typedArray.recycle()
        }
    }

    fun updateAttr() {
        clipTextView.setBackgroundColor(backGroundColor)
        clipTextView.text = text
        clipTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        clipTextView.setTextColor(textColor)
    }

    fun setClipText(text: CharSequence) {
        this.text = text.toString()
        clipTextView.text = text
    }

    fun setClipTextStyle(typeface: Int) {
        clipTextView.setTypeface(clipTextView.typeface, typeface)
    }

    fun setClipTextSize(sp: Float) {
        this.textSize = sp.toInt()
        clipTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    fun setClipTextColor(color: Int) {
        textColor = color
        clipTextView.setTextColor(color)
    }

    fun setClipBackgroundColor(color: Int) {
        backGroundColor = color
        clipTextView.setBackgroundColor(color)
    }

    fun setClipAniDuration(milliSec: Long) {
        aniDuration = milliSec / 100
    }

    fun setClipStayDuration(milliSec: Long) {
        aniStayDuration = milliSec
    }

    fun setOnAnimationEnable(animationEnable: Boolean) {
        when(animationEnable) {
            true -> this.clipStatus = ClipStatue.STATE_CLIP
            false -> this.clipStatus = ClipStatue.STATE_FILL
        }
    }

    fun setDelegate(delegate: Delegate) {
        this.delegate = delegate
    }

    override fun onClick(view: View) {
        handler = ClipAniLayoutHandler(WeakReference(this))

        when (clipStatus) {
            ClipStatue.STATE_CLIP -> {
                val task = Runnable {
                    handler?.sendEmptyMessage(MSG_CLICK_DISABLE)
                    clipStatus = ClipStatue.STATE_FILL
                    progressNumber = 0
                    aniThread?.let {
                        while (progressNumber < clipTextView.getMaxProgress() && !it.isInterrupted) {
                            try {
                                Thread.sleep(aniDuration)

                                ++progressNumber

                                val msg = Message.obtain()
                                msg.what = MSG_PROGRESS
                                msg.arg1 = progressNumber

                                handler?.sendMessage(msg)
                            } catch (e: InterruptedException) {
                                return@Runnable
                            }
                        }
                    }


                    timer = Timer()
                    timer?.schedule(object : TimerTask() {
                        override fun run() {
                            handler?.sendEmptyMessage(MSG_CLICK_DISABLE)
                            progressNumber = clipTextView.getMaxProgress()
                            aniThread?.let {
                                while (progressNumber > 0 && !it.isInterrupted) {
                                    try {
                                        Thread.sleep(aniDuration)

                                        --progressNumber

                                        val msg = Message.obtain()
                                        msg.what = MSG_PROGRESS
                                        msg.arg1 = progressNumber

                                        handler?.sendMessage(msg)
                                    } catch (e: InterruptedException) {
                                        break
                                    }
                                }
                            }
                            clipStatus = ClipStatue.STATE_CLIP
                        }
                    }, aniStayDuration)
                }
                aniThread = Thread(task)
                aniThread?.start()
            }
            ClipStatue.STATE_FILL -> {
                delegate?.onClickFillStatues()
            }
        }
    }

    private class ClipAniLayoutHandler(var clipAniLayout: WeakReference<ClipAniLayout>) : Handler() {

        override fun handleMessage(msg: Message) {
            when(msg.what) {
                MSG_PROGRESS -> {
                    clipAniLayout.get()?.let {
                        it.clipTextView.setProgress(msg.arg1)
                        it.isEnabled = msg.arg1 >= it.clipTextView.getMaxProgress() || msg.arg1 == 0
                    }
                }
                MSG_CLICK_DISABLE -> {
                    clipAniLayout.get()?.isEnabled = false
                }
            }
        }
    }
}