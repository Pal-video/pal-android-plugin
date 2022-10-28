package com.plugin.pal

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            this.createPopup3()
            //Toast.makeText(this@MainActivity, "Starting Pal video", Toast.LENGTH_SHORT).show()
        }

    }

    private fun createPopup3() {
        val minVideoView = CircleVideoView(this)
        minVideoView.load("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")

        val mParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        //val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //wm.addView(view, mParams)
        this.addContentView(minVideoView, mParams)
    }

}