# Handoff Log

How one developer (and their agent) tells the other what happened while they were away. `AGENTS.md` section 3 tells agents to read it.

Rules:
- Add an entry (newest first) only when the other developer must know or act. Routine commits need none.
- Mark open items `PENDING FOR YOU`. When done, change to `DONE (who, date)`. Do not delete.
- Plain language, beginner level.

## 2026-10-11 (later), tariqaq: big cleanup, all pushed
Plain summary for @vexilen-01. Bug fixes and a sturdier app; overall look unchanged.
- **Fixed:** About Me cards were solid pink; now a thin pink stripe on the left.
- **Fixed:** slanted text. Tab labels now really lean (before only the tab shape did).
- **Fixed:** music no longer restarts on rotation, and the selected tab is kept.
- **Fixed:** music and background video pause when you leave the app and resume when you return.
- **New:** music player shows album cover, progress bar, and "LOADING" / "OFFLINE" messages.
- **New:** with no internet, images show a small "!" tile and the background stays dark blue instead of empty black boxes.
- **Under the hood:** libraries updated, music uses ExoPlayer like the video, shared `P3RCard` / `P3RSectionTitle` / `P3RImage`, colours in `P3RColors`, `.idea` untracked (stops merge conflicts), real tests, README with screenshots.
- **Kept on purpose:** unused purple theme files and `PentagonStatChart` (you may use them later).
- **Good to know:** new UI should use `P3RCard`, `P3RSectionTitle`, `P3RImage`, `P3RColors`. See `docs/PROJECT_MAP.md`.
- **If git complains about `.idea` after pulling:** delete your local `.idea` folder and pull again.
- **Still open, ask Tariq first:** placeholder package name `com.example.p3rprofile`; an intro screen like the website's.

## 2026-10-11, tariqaq
- **Access check:** Tariq has WRITE permission. Commit, push and sync work.
- **Emulator:** Tariq tests on an Android Studio emulator (`Medium_Phone_API_36.1`), local to his laptop. `AGENTS.md` section 4 lets you do the same.
- **New file:** `scripts/run-emulator.ps1`. One command boots the emulator, builds, installs, launches and screenshots. Works on any Windows machine with no editing.
- **AGENTS.md updated:** sync briefing (section 3), emulator-first testing (section 4), this log.
- **PENDING FOR YOU: setup.** Next time you test, your agent offers to set up the emulator so your phone needn't be connected.
- **Known issues from the review:** 1, 2, 3, 4, 6 fixed (see entry above). Open: 5 (kept on purpose) and 7. Agree with Tariq before changing.
  1. **DONE (tariqaq, 2026-10-11)** `p3rSkew` built a matrix but never applied it.
  2. **DONE (tariqaq, 2026-10-11)** `AboutCard` accent stripe covered the whole card.
  3. **DONE (tariqaq, 2026-10-11)** Rotation restarted the music.
  4. **DONE (tariqaq, 2026-10-11)** Music and video kept playing in the background.
  5. `ui/theme/Theme.kt` and `Type.kt` are unused template leftovers; `P3RProfileTheme` is not wrapped around `setContent`.
  6. **DONE (tariqaq, 2026-10-11)** Old libraries updated.
  7. Package name is still the placeholder `com.example.p3rprofile`; blocks Play Store publishing.
