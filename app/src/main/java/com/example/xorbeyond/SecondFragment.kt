
package com.example.xorbeyond

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.xorbeyond.databinding.FragmentSecondBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var constraintLayoutRoot: ConstraintLayout
    private lateinit var exoPlayerView: StyledPlayerView

    private lateinit var mediaSource: MediaSource
    private lateinit var simpleExoPlayer: ExoPlayer



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        simpleExoPlayer = ExoPlayer.Builder(this.requireContext()).build()

        simpleExoPlayer.addListener(playerListener)

        binding.exoPlayerView.player = simpleExoPlayer

        val url : String = "https://live.kanal7.com/live/kanal7LiveMobile/index.m3u8"

        simpleExoPlayer.seekTo(0)

        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()

        mediaSource = HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(
            MediaItem.fromUri(url)
        )

        simpleExoPlayer.setMediaSource(mediaSource)
        simpleExoPlayer.prepare()
    }

    override fun onResume() {
        super.onResume()
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.play()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady = false
    }


    override fun onDestroy() {
        super.onDestroy()

        simpleExoPlayer.removeListener(playerListener)
        simpleExoPlayer.stop()
        simpleExoPlayer.clearMediaItems()


        _binding = null
    }

    private var playerListener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()
            // exoPlayerView.useController = false

        }


        /*
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Toast.makeText(this@SecondFragment.requireContext(), "$(error.message)",
                    Toast.LENGTH_SHORT).show()
            }
    */

    }

}



