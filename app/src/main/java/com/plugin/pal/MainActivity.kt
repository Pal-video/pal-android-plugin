package com.plugin.pal

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.*
import android.widget.*
import com.plugin.pal.sdk.miniature.CircleVideoView
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
        val videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        minVideoView.show(this, videoUrl)
    }

}