package com.plugin.pal

import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import com.plugin.pal.sdk.miniature.CircleVideoView
import androidx.appcompat.app.AppCompatActivity
import com.plugin.pal.sdk.miniature.CropVideoTextureView

class MainActivity : AppCompatActivity() {

    val videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            this.createPopup3()
            //Toast.makeText(this@MainActivity, "Starting Pal video", Toast.LENGTH_SHORT).show()
        }

        Handler(mainLooper).post {
            val cropVideo = findViewById<CropVideoTextureView>(R.id.texture_test)
            cropVideo.setScaleType(CropVideoTextureView.ScaleType.CENTER_CROP);
            cropVideo.setDataSource(videoUrl);
            cropVideo.play()
        }

    }

    private fun createPopup3() {
        val minVideoView = CircleVideoView(this)
        minVideoView.show(this, videoUrl)
    }

}