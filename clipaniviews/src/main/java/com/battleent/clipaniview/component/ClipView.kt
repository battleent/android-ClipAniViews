package com.battleent.clipaniview.component

import android.graphics.Canvas

/**
 * Created by byundongmyung on 2018. 2. 22..
 * Copyright (c) 2017 battleent rights reserved.
 */

interface ClipView {
    fun setProgress(progress: Int)
    fun getMaxProgress() : Int
    fun onDraw(canVas: Canvas)
    fun setBackgroundColor(color: Int)
}
