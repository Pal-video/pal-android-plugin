package com.plugin.pal

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.plugin.pal.sdk.PalSdk
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    val videoUrl_LQ = "https://cellar-c2.services.clever-cloud.com/pal-production/projects/2eb9913b-3ad1-403d-8bb2-3ede72eede07/videos/d4cdcade-b0b1-43bf-9666-b3fbb28137bb_thumb.mp4"
    val videoUrl_HQ = "https://cellar-c2.services.clever-cloud.com/pal-production/projects/2eb9913b-3ad1-403d-8bb2-3ede72eede07/videos/d4cdcade-b0b1-43bf-9666-b3fbb28137bb.mp4"

    val productionToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2V4YW1wbGUuY29tL2lzc3VlciIsInVwbiI6IjJlYjk5MTNiLTNhZDEtNDAzZC04YmIyLTNlZGU3MmVlZGUwN0BwYWwuaW8iLCJzdWIiOiIyZWI5OTEzYi0zYWQxLTQwM2QtOGJiMi0zZWRlNzJlZWRlMDciLCJpYXQiOjE2NjIxMzIwNDYsImdyb3VwcyI6WyJQcm9qZWN0Il0sImVudiI6IlBST0RVQ1RJT04iLCJleHAiOjEwMzAyMTMyMDQ2LCJqdGkiOiJlMTZhOTA2Yy01OTYwLTRiZGItYTlhZS0yZGJhOTkxOWIxYjIifQ.Y-y9IUnfBHMB4x9xWVnNjGdnLucdKatDQCrnz57rvdSKjzY5Ty4NPznExNtTr1vm_iwUroxullnz8okFc6-R7N4YCCDU23Zysd6lTWcrYFvypYZcEMUL1a3Dz-LbbHOz7_2sotPFqPzoJzYUG_2A1_3-So0duM78UNyCEvh_30t0PbWbmNFM8ma5qrAuX2JVwJv4ZoFa_Cya9JG84g3WbB5zuoHmVsan2bkln1mnVN8zHpwJDUWmgTo-qOVIYBMZOgNUv7_6xFM1_MUQJsuJw3JdycfpQR1-m76rRzvepv7Vv6aZIfULdsy0M1DmZyDMxX3kvBwgG8AYOReNUpEUcA"
    val developmentToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2V4YW1wbGUuY29tL2lzc3VlciIsInVwbiI6IjJlYjk5MTNiLTNhZDEtNDAzZC04YmIyLTNlZGU3MmVlZGUwN0BwYWwuaW8iLCJzdWIiOiIyZWI5OTEzYi0zYWQxLTQwM2QtOGJiMi0zZWRlNzJlZWRlMDciLCJpYXQiOjE2NjIxMzIwNDYsImdyb3VwcyI6WyJQcm9qZWN0Il0sImVudiI6IkRFVkVMT1BNRU5UIiwiZXhwIjoxMDMwMjEzMjA0NiwianRpIjoiYzk3NzQyMjYtMmUzOS00NjU0LWIwZjYtNWE0NDM1MGU0NWE2In0.e1j2pC8Sr9gaMyNE5J6mmfm6Tr8R8DvBrOLh4kSxj-SgRlj8WZziP8pixzdHxnzpVbP_QHNbG16jP-IM1fbxy628YPF5O6hH3h2V0jUyxGDyb_dvOqR6ODRSVzBHZBR4lgfrQLDu0ypWuNI3Rps3lxZeQbdZ220Cz0HjunRT1ITwaVjLzU_N3Gt1t7k0Dr8X7QXFTLWGzNtHsE7G71igdNDI3r_uKgk4PTDwk6ZjqpUrz0BZfWFgGE4rhekjVKZZOVVlbk5nhpCjdhmhfGc5Mp5PZ4P4CX-Rx8aqObPjn67bVhl7GMjJnsQZHhuQlt2xeX7i7PEiin__5ejBky9WQw"

    val palSdk = PalSdk()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // test SDK
        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            showVideo()
        }
//        Handler(mainLooper).postDelayed({
//            showVideo()
//        }, 100)

        // test plugin
        PalPlugin.setup(this, productionToken)
        PalPlugin.instance.clearSession()

        PalPlugin.instance.logCurrentScreen(this@MainActivity, "/")
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