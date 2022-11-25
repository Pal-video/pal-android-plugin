package video.pal.sdk.common

import android.media.MediaPlayer
import android.os.Handler


class VideoTimer(
    private var mediaPlayer: MediaPlayer,
    private val taskHandler: Handler
) {

    private var running = false

    private var timeListenr: ((VideoTime) -> Unit)? = null

    class VideoTime(
        val elapsedTime: Int,
        val totalTime: Int) {

        override fun toString(): String {
            return String.format("%s/%s", formattedTime(elapsedTime), formattedTime(totalTime))
        }

        private fun formattedTime(millis: Int): String {
            val buf = StringBuffer()
            val hours = (millis / (1000 * 60 * 60))
            val minutes = (millis % (1000 * 60 * 60) / (1000 * 60))
            val seconds = (millis % (1000 * 60 * 60) % (1000 * 60) / 1000)

            if(hours > 0) {
                buf.append(String.format("%02d", hours))
                    .append(":")
            }
            buf.append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds))
            return buf.toString()
        }
    }

    fun setSecondsListener(listener : (VideoTime) -> Unit) {
        this.timeListenr = listener
    }

    fun start() {
        if(running || timeListenr == null) {
            return;
        }
        running = true
        val task = object: Runnable {
            override fun run() {
                try {
                    if(running) {
                        val result = VideoTime(
                            mediaPlayer.currentPosition,
                            mediaPlayer.duration)
                        timeListenr!!(result)
                        taskHandler.postDelayed(this, 1000)
                    }
                } catch (_: Exception) {}
            }
        }
        taskHandler.post(task)
    }

    fun stop() {
        running = false
    }

}