package com.plugin.pal

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.plugin.pal.sdk.PalSdk

class MainActivity : AppCompatActivity() {

//    val videoUrl_LQ = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
//    val videoUrl_HQ = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    // 406 / 720 => 0.56
    val videoUrl_LQ = "https://cellar-c2.services.clever-cloud.com/pal-production/projects/2eb9913b-3ad1-403d-8bb2-3ede72eede07/videos/d4cdcade-b0b1-43bf-9666-b3fbb28137bb_thumb.mp4"
    val videoUrl_HQ = "https://cellar-c2.services.clever-cloud.com/pal-production/projects/2eb9913b-3ad1-403d-8bb2-3ede72eede07/videos/d4cdcade-b0b1-43bf-9666-b3fbb28137bb.mp4"

    val palSdk = PalSdk()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            showVideo()
        }
        Handler(mainLooper).postDelayed({
            showVideo()
        }, 100)
    }

    private fun showVideo() {
        palSdk.showTalkVideo(
            this,
            videoUrl_LQ,
            videoUrl_HQ,
            "David",
            "CEO | Pal",
            {  Log.d("MainActivity", "onSkip") },
            {  Log.d("MainActivity", "onExpand") },
            {  Log.d("MainActivity", "onVideoEnd") },
        )
    }

}