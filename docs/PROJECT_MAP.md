# PROJECT_MAP.md

Read this once per session before editing code. Do not scan the whole source tree.

A one-activity Android app (Kotlin, Jetpack Compose): a Persona 3 Reload styled "About Me" profile. It mirrors the owner's website. All media is streamed from `https://persona-3-reload-about-me-website-beta.vercel.app/` (needs internet). There is no database, no ViewModel and no navigation library.

Source root: `app/src/main/java/com/example/p3rprofile/`

## Files

| File | What it does |
|---|---|
| `MainActivity.kt` | Only activity. Starts music, draws background video, tab bar, and the screen for the selected tab. `onDestroy` releases music. |
| `audio/MusicController.kt` | Singleton `object` wrapping `MediaPlayer`. Holds the 5-track playlist (audio + cover URLs), play/pause/next/prev. |
| `ui/components/P3RBackgroundVideo.kt` | Looping muted ExoPlayer video behind everything, plus a dark gradient overlay. |
| `ui/components/P3RNavigationBar.kt` | `enum P3RTab` (ABOUT, STATS, PROJECTS, GAMES, MUSIC, SOCIALS) and the slanted tab bar. |
| `ui/components/P3RShapes.kt` | `AngledBannerShape` (works) and `Modifier.p3rSkew` (currently does nothing, known bug). |
| `ui/components/PentagonStatChart.kt` | Canvas pentagon stat chart. Built but **not used on any screen yet** (the website has no such chart). Kept on purpose for later use. Does not draw its labels. |
| `ui/screens/StatusScreen.kt` | ABOUT tab: `AboutMeScreen` and `AboutCard`. |
| `ui/screens/PersonalityScreen.kt` | STATS tab: pentagon chart and `PersonalityMeterBar`. |
| `ui/screens/ProjectsScreen.kt` | PROJECTS tab. Links to the website's GitHub repo. |
| `ui/screens/GameScreen.kt` | GAMES tab: `GamesScreen`, 4 game cards with Coil images. |
| `ui/screens/MusicScreen.kt` | MUSIC tab: player controls and favourite artists. Biggest file (~340 lines). |
| `ui/screens/SocialsScreen.kt` | SOCIALS tab: link cards (GitHub, Instagram, Reddit). |
| `ui/theme/P3RTheme.kt` | **Use this.** `object P3RColors` holds every colour. |
| `ui/theme/Color.kt`, `Theme.kt`, `Type.kt` | **Unused** Android Studio template leftovers (purple). Do not edit or copy from them. |

## How a tab works

`MainActivity` keeps `currentTab` in `remember`. Picking a tab in `P3RNavigationBar` swaps the screen inside `AnimatedContent` (horizontal slide). To add a tab: add it to `enum P3RTab`, then add a branch in the `when (target)` in `MainActivity.kt`, then create `ui/screens/<Name>Screen.kt`.

## Style rules for any new UI

- Colours only from `P3RColors` (background `SiteDeepBlack`, accent `ElectricCyan`, highlight `NeonRedPink`/`HotOrange`). No hardcoded hex, no purple.
- Sharp, angular shapes (`AngledBannerShape`). No rounded Material look.
- Text should look slanted (-8° to -12°) with a hard drop shadow.
- Screens are transparent so the background video shows through.

## Build and tools

- Gradle with version catalog `gradle/libs.versions.toml`. App module: `app/build.gradle.kts`.
- Image loading: Coil. Video: Media3 ExoPlayer. Audio: Android `MediaPlayer`.
- Test on the emulator with `scripts/run-emulator.ps1` (see `AGENTS.md` section 4).
- Application id is the placeholder `com.example.p3rprofile`.
