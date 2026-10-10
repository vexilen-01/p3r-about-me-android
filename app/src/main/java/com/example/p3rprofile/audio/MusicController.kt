package com.example.p3rprofile.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
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

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: Boolean get() = _isPlaying.value

    val currentTrack: Track
        get() = playlist[currentTrackIndex]

    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared = false

    fun initPlayer(context: Context, autoPlay: Boolean = true) {
        if (mediaPlayer == null) {
            prepareTrack(currentTrackIndex, autoPlay = autoPlay)
        }
    }

    private fun prepareTrack(index: Int, autoPlay: Boolean) {
        _currentTrackIndex.intValue = index
        isPrepared = false

        mediaPlayer?.let {
            try {
                if (it.isPlaying) it.stop()
            } catch (_: Exception) {}
            it.reset()
            it.release()
        }

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            try {
                setDataSource(playlist[index].audioUrl)
                setOnPreparedListener { mp ->
                    isPrepared = true
                    if (autoPlay) {
                        mp.start()
                        _isPlaying.value = true
                    }
                }
                setOnCompletionListener {
                    nextTrack()
                }
                setOnErrorListener { _, _, _ ->
                    _isPlaying.value = false
                    isPrepared = false
                    true
                }
                prepareAsync()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun togglePlay() {
        val player = mediaPlayer ?: return
        if (isPrepared) {
            if (player.isPlaying) {
                player.pause()
                _isPlaying.value = false
            } else {
                player.start()
                _isPlaying.value = true
            }
        } else {
            prepareTrack(currentTrackIndex, autoPlay = true)
        }
    }

    fun nextTrack() {
        val next = (currentTrackIndex + 1) % playlist.size
        prepareTrack(next, autoPlay = true)
    }

    fun prevTrack() {
        val prev = (currentTrackIndex - 1 + playlist.size) % playlist.size
        prepareTrack(prev, autoPlay = true)
    }

    fun release() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) it.stop()
            } catch (_: Exception) {}
            it.release()
        }
        mediaPlayer = null
        isPrepared = false
        _isPlaying.value = false
    }
}
