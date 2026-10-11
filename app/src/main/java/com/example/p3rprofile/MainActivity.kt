package com.example.p3rprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.p3rprofile.audio.MusicController
import com.example.p3rprofile.ui.components.P3RNavigationBar
import com.example.p3rprofile.ui.components.P3RTab
import com.example.p3rprofile.ui.screens.*
import com.example.p3rprofile.ui.theme.P3RColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize background music playback across the app (matches webpage behavior)
        MusicController.initPlayer(this, autoPlay = true)

        setContent {
            var currentTab by rememberSaveable { mutableStateOf(P3RTab.ABOUT) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(P3RColors.SiteDeepBlack)
                    .safeDrawingPadding()
            ) {
                // Looping Persona 3 Reload Background Video (Direct from your website)
                com.example.p3rprofile.ui.components.P3RBackgroundVideo()

                Column(modifier = Modifier.fillMaxSize()) {
                    P3RNavigationBar(
                        currentTab = currentTab,
                        onTabSelected = { currentTab = it }
                    )

                    AnimatedContent(
                        targetState = currentTab,
                        transitionSpec = {
                            if (targetState.ordinal > initialState.ordinal) {
                                (slideInHorizontally(animationSpec = tween(300)) { it } + fadeIn()).togetherWith(
                                    slideOutHorizontally(animationSpec = tween(300)) { -it } + fadeOut()
                                )
                            } else {
                                (slideInHorizontally(animationSpec = tween(300)) { -it } + fadeIn()).togetherWith(
                                    slideOutHorizontally(animationSpec = tween(300)) { it } + fadeOut()
                                )
                            }
                        },
                        label = "tabTransition"
                    ) { target ->
                        when (target) {
                            P3RTab.ABOUT -> AboutMeScreen()
                            P3RTab.STATS -> PersonalityScreen()
                            P3RTab.PROJECTS -> ProjectsScreen()
                            P3RTab.GAMES -> GamesScreen()
                            P3RTab.MUSIC -> MusicScreen()
                            P3RTab.SOCIALS -> SocialsScreen()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MusicController.onAppForegrounded()
    }

    override fun onStop() {
        super.onStop()
        // Pause when the user leaves the app, but not for a screen rotation.
        if (!isChangingConfigurations) MusicController.onAppBackgrounded()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Rotation also destroys the activity. Only stop the music when the app is really closing.
        if (isFinishing) MusicController.release()
    }
}