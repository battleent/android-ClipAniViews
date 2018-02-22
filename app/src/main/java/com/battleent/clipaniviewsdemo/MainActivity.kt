package com.battleent.clipaniviewsdemo

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.battleent.clipaniview.ClipAniLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clip_ani_layout.run {
            setClipText("Buy")
            setClipTextSize(15f)
            setClipTextStyle(Typeface.BOLD)
            setClipTextColor(ContextCompat.getColor(context, android.R.color.white))
            setClipBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            setClipAniDuration(100)
            setClipStayDuration(2000)
            setDelegate(object : ClipAniLayout.Delegate {
                override fun onClickFillStatues() {
                    Toast.makeText(this@MainActivity, "Fill Click", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


}
