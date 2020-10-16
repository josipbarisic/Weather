package com.barisic.weather.activities

import android.os.Bundle
import android.widget.Toast
import com.barisic.weather.R
import com.barisic.weather.util.GoogleApiConfig
import com.barisic.weather.util.VIDEO_ID
import com.barisic.weather.util.showToast
import com.barisic.weather.viewmodels.MainViewModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_you_tube.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class YouTubeActivity : YouTubeBaseActivity() {
    private val mainViewModel: MainViewModel by inject()
    private val youTubeInitListener = object : YouTubePlayer.OnInitializedListener {
        override fun onInitializationSuccess(
            p0: YouTubePlayer.Provider?,
            youTubePlayer: YouTubePlayer?,
            p2: Boolean
        ) {
            val id = intent.getStringExtra(VIDEO_ID)
            if (!id.isNullOrEmpty()) youTubePlayer?.loadVideo(id)
        }

        override fun onInitializationFailure(
            p0: YouTubePlayer.Provider?,
            p1: YouTubeInitializationResult?
        ) {
            showToast(R.string.video_loading_error, Toast.LENGTH_SHORT)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_you_tube)
        Timber.d("${mainViewModel.youTubeSearchResult.value} you_tube")

        youtube_player.initialize(GoogleApiConfig.getApiKey(), youTubeInitListener)
    }
}