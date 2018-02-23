# android-ClipAniViews
![license](https://img.shields.io/badge/license-MIT%20License-blue.svg)

This is an android view library "ClipAniViews" by [Battle Entertainment](https://www.battleent.com/)

![device-new](https://user-images.githubusercontent.com/13096491/36575045-f2c2d5be-188b-11e8-9dfd-4ea20b384e3c.gif)




## Usage
### ClipAniLayout
```xml
<com.battleent.clipaniview.ClipAniLayout
        android:layout_width="300dp"
        android:layout_height="150dp"
        app:clip_aniDuration="3"
        app:clip_aniStayDuration="2000"
        app:clip_backgroundColor="@color/colorAccent"
        app:clip_text="Infomation"
        app:clip_textColor="@android:color/white"
        app:clip_textSize="20">

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:src="@drawable/image_webtoon" />
    </com.battleent.clipaniview.ClipAniLayout>
```
OR
```kotlin
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
```
