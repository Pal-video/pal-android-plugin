package com.plugin.pal

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.plugin.pal.sdk.PalSdk

class MainActivity : AppCompatActivity() {

    val videoUrl_LQ = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    val videoUrl_HQ = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val palSdk = PalSdk()

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            palSdk.showVideo(
                this,
                videoUrl_LQ,
                videoUrl_HQ,
                "Lorem",
                "Lorem ipsum lorem",
                {  Log.d("MainActivity", "onSkip") },
                {  Log.d("MainActivity", "onExpand") },
                {  Log.d("MainActivity", "onVideoEnd") },
            )
            //Toast.makeText(this@MainActivity, "Starting Pal video", Toast.LENGTH_SHORT).show()
        }

//        Handler(mainLooper).post {
//            val cropVideo = findViewById<CropVideoTextureView>(R.id.texture_test)
//            cropVideo.setScaleType(CropVideoTextureView.ScaleType.CENTER_CROP);
//            cropVideo.setDataSource(videoUrl);
//            cropVideo.play()
//        }

    }

}