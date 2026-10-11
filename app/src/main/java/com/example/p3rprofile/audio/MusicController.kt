package com.example.p3rprofile.audio

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf

data class Track(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val audioUrl: String,
    val coverUrl: String
)

object MusicController {
    val playlist = listOf(
        Track(
            id = "m1",
            title = "Color Your Night",
            album = "Persona 3 Reload Original Soundtrack",
            artist = "Lotus Juice, Azumi Takahashi",
            audioUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/bg-music-1.mp3",
            coverUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/p3r.jpg"
        ),
        Track(
            id = "m2",
            title = "Full Moon Full Life",
            album = "Persona 3 Reload Original Soundtrack",
            artist = "Azumi Takahashi, Lotus Juice",
            audioUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/bg-music-2.mp3",
            coverUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/p3r.jpg"
        ),
        Track(
            id = "m3",
            title = "Mass Destruction (-Reload-)",
            album = "Persona 3 Reload Original Soundtrack",
            artist = "Lotus Juice, Azumi Takahashi",
            audioUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/bg-music-3.mp3",
            coverUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/p3r.jpg"
        ),
        Track(
            id = "m4",
            title = "It's Going Down Now",
            album = "Persona 3 Reload Original Soundtrack",
            artist = "Lotus Juice, Azumi Takahashi",
            audioUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/bg-music-4.mp3",
            coverUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/p3r.jpg"
        ),
        Track(
            id = "m5",
            title = "When The Moon's Reaching Out Stars",
            album = "Persona 3 Reload Original Soundtrack",
            artist = "Azumi Takahashi, Lotus Juice",
            audioUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/bg-music-5.mp3",
            coverUrl = "https://persona-3-reload-about-me-website-beta.vercel.app/p3r.jpg"
        )
    )

    private val _currentTrackIndex = mutableIntStateOf(0)
    val currentTrackIndex: Int get() = _currentTrackIndex.intValue

    // True only while sound is actually coming out.
    private val _isPlaying = mutableStateOf(false)
    val isPlaying: Boolean get() = _isPlaying.value

    // True while a track is being downloaded/buffered (show "LOADING" in the UI).
    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    // True when the last track failed to load (usually no internet).
    private val _hasError = mutableStateOf(false)
    val hasError: Boolean get() = _hasError.value

    val currentTrack: Track
        get() = playlist[currentTrackIndex]

    private var player: ExoPlayer? = null

    // Set when the app goes to the background while music was playing.
    private var resumeWhenForeground = false

    private val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> _isLoading.value = true
                Player.STATE_READY -> _isLoading.value = false
                Player.STATE_ENDED -> nextTrack()
                else -> Unit
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            _isLoading.value = false
            _isPlaying.value = false
            _hasError.value = true
        }
    }

    /** Creates the player on first call (safe to call again, e.g. after rotation). */
    fun initPlayer(context: Context, autoPlay: Boolean = true) {
        if (player != null) return
        player = ExoPlayer.Builder(context.applicationContext).build().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                /* handleAudioFocus = */ true
            )
            addListener(listener)
        }
        prepareTrack(currentTrackIndex, autoPlay)
    }

    private fun prepareTrack(index: Int, autoPlay: Boolean) {
        val exo = player ?: return
        _currentTrackIndex.intValue = index
        _isLoading.value = true
        _hasError.value = false
        exo.setMediaItem(MediaItem.fromUri(playlist[index].audioUrl))
        exo.playWhenReady = autoPlay
        exo.prepare()
    }

    fun togglePlay() {
        val exo = player
        if (exo == null) return
        when {
            // Last load failed: try the same track again.
            _hasError.value -> prepareTrack(currentTrackIndex, autoPlay = true)
            exo.playWhenReady -> exo.pause()
            else -> exo.play()
        }
    }

    fun nextTrack() {
        prepareTrack((currentTrackIndex + 1) % playlist.size, autoPlay = true)
    }

    fun prevTrack() {
        prepareTrack((currentTrackIndex - 1 + playlist.size) % playlist.size, autoPlay = true)
    }

    /** Current playback position in ms (0 if nothing is playing yet). */
    fun positionMs(): Int = player?.currentPosition?.coerceAtLeast(0)?.toInt() ?: 0

    /** Length of the current track in ms (0 until it is known). */
    fun durationMs(): Int {
        val d = player?.duration ?: return 0
        return if (d == C.TIME_UNSET) 0 else d.coerceAtLeast(0).toInt()
    }

    /** Call when the app is no longer visible. Pauses music and remembers to resume it. */
    fun onAppBackgrounded() {
        val exo = player ?: return
        resumeWhenForeground = exo.playWhenReady
        exo.pause()
    }

    /** Call when the app is visible again. Resumes music only if it was playing before. */
    fun onAppForegrounded() {
        if (!resumeWhenForeground) return
        resumeWhenForeground = false
        player?.play()
    }

    fun release() {
        player?.removeListener(listener)
        player?.release()
        player = null
        resumeWhenForeground = false
        _isPlaying.value = false
        _isLoading.value = false
    }
}
