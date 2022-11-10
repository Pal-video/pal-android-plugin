package video.pal.sdk.expanded

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import video.pal.R
import video.pal.sdk.common.CropVideoView

class TalkLayout:ConstraintLayout, VideoOverlayLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init()
    }

    private var descriptionLayout: View? = null

    private var titleTextView: TextView? = null

    private var subtitleTextView: TextView? = null

    private var poweredByLayout: ConstraintLayout? = null

    private var time: TextView? = null

    private var closeBtn: ImageButton? = null

    private var mVideoPlayer: CropVideoView? = null

    private var onCloseTapListener: (() -> Unit)? = null

    private var exitFn: (() -> Unit)? = null

    private fun init() {
        inflate(context, R.layout.talk_video_layout, this)
        descriptionLayout = findViewById(R.id.description_layout)
        titleTextView = findViewById(R.id.title)
        subtitleTextView = findViewById(R.id.subtitle)
        time = findViewById(R.id.timerTextView)
        closeBtn = findViewById(R.id.close_btn)
        poweredByLayout = findViewById(R.id.powered_by_layout)

        time!!.text = "00:00"
    }

    fun setDescription(
        title: String,
        subtitle: String,
    ) {
        titleTextView!!.text = title
        subtitleTextView!!.text = subtitle
    }

    fun setOnCloseTapListener(onCloseTapListener: () -> Unit) {
        this.onCloseTapListener = onCloseTapListener
        closeBtn!!.setOnClickListener {
            assert(exitFn != null) { "exit function must be set" }
            onCloseTapListener()
            exitFn!!()
        }
    }

    override fun setVideoPlayer(player: CropVideoView) {
        mVideoPlayer = player
        mVideoPlayer!!.setTimeListener {
            videoTime -> time!!.text = videoTime.toString()
        }
    }

    override fun setClose(exitFn: () -> Unit) {
        this.exitFn = exitFn
    }


}