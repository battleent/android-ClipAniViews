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

# License
```xml
MIT License

Copyright (c) 2017 Battle Entertainment

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
