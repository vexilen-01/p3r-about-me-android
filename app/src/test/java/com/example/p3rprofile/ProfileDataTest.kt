package com.example.p3rprofile

import com.example.p3rprofile.audio.MusicController
import com.example.p3rprofile.ui.components.P3RTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileDataTest {

    @Test
    fun playlist_hasFiveTracksWithUniqueIds() {
        val ids = MusicController.playlist.map { it.id }
        assertEquals(5, ids.size)
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun playlist_urlsAreHttps() {
        MusicController.playlist.forEach { track ->
            assertTrue("${track.title} audio", track.audioUrl.startsWith("https://"))
            assertTrue("${track.title} cover", track.coverUrl.startsWith("https://"))
        }
    }

    @Test
    fun tabs_haveUniqueLabels() {
        val labels = P3RTab.entries.map { it.display }
        assertEquals(6, labels.size)
        assertEquals(labels.size, labels.toSet().size)
    }
}
