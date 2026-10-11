# AGENTS.md

This file is read by every AI agent that works in this repository, whichever IDE or model it runs in (Antigravity, Claude Code, etc.). Follow it on every session without being reminded.

There are two developers:
- **@vexilen-01** owns the repository.
- **tariqaq (Tariq)** is a collaborator with write access, and also pushes to `main`.

Either of them may have pushed since you last looked. Section 3 tells you how to check and what to report.

## Repository & Development Directives

### 1. Mandatory Git Sync, Auto-Commit & Push Rule
Whenever any changes, feature updates, bug fixes, or modifications are made to the codebase in this project:
- **Always sync first**: Before committing anything, pull the latest upstream changes to incorporate collaborator work safely:
  `git pull --rebase origin main`
- **Stage**: Immediately stage all modified files:
  `git add .`
- **Commit**: Create a meaningful conventional commit message describing the changes:
  `git commit -m "..."`
- **Push**: Immediately push and sync upstream to the remote repository:
  `git push origin main`

This rule must be followed on every development cycle without requiring explicit reminders.

Additional safety rules:
- Never force-push (`--force`) to `main`.
- If `git pull --rebase` reports conflicts, stop. Explain the conflict to the user in plain language and ask how to resolve it. Do not discard anyone's work.
- Never commit machine-specific files (`local.properties`, personal SDK paths, `*.local.*` files). They belong in `.gitignore`.

### 2. Project Context & Architecture
- **Platform**: Android Native (Kotlin / Jetpack Compose)
- **Theme**: Persona 3 Reload (P3R) HUD Aesthetic
  - Colors: Midnight Black (`#010816`), Electric Cyan (`#00E5FF`), Neon Red/Pink Ribbon (`#FF0055` to `#FF2A00`)
  - Typography: Slanted/skewed transforms (`-8°` to `-12°`), sharp high-contrast drop shadows.
- **Repository**: Public GitHub repository under `@vexilen-01`
- **Root Directory**: wherever the repo is cloned on the current machine. This differs per developer, so do not hardcode a path. The old value `C:\Users\HP\AndroidStudioProjects\P3RProfile` is only one developer's location.

### 3. Collaborator Sync Briefing (run at the start of every session and before every new task)
The first time the user asks you to add, change, fix or run anything in a session, and again before each later new task, check whether the other developer has pushed. Do this before touching any code.

1. Fetch without changing the working tree: `git fetch origin`
2. List what is new on the remote: `git log --format="%h | %an | %ar | %s" HEAD..origin/main`
3. If there are new commits, also run `git diff --stat HEAD...origin/main`, and read the diff where needed to understand it.
4. If nothing is new, say "Repo is up to date, nothing new from the other developer." in one line and continue.
5. If something is new, tell the user before starting their task. Cover:
   - **Who** pushed it (for example `tariqaq`) and when.
   - **What** changed, in plain language a beginner can follow, not just commit titles.
   - **Which files** were added, changed or removed.
   - **What it means for the user**: new scripts to know about, new dependencies, setup steps they must do on their machine, anything that could conflict with what they are about to ask.
6. Then sync: `git pull --rebase origin main`. If the user has uncommitted work, commit it first. Never throw it away.
7. Read the **Collaborator Activity Log** in section 5 and mention any entries marked `PENDING FOR YOU`.

Keep the briefing short and clear. Do not dump raw git output at the user.

### 4. Testing the App: Use an Android Emulator, Not a Physical Phone
Tariq tests with an Android Studio emulator on his laptop. The goal is that no developer has to keep a real phone plugged in with USB debugging on.

**Trigger:** whenever you are about to run, install, or test the app on a device, or the user says things like "run it", "test it", "show me the app", or "see it on my phone".

**Step A: check what is available.**
- Run `adb devices` (`adb` lives in `<Android SDK>\platform-tools`).
- Check the per-machine preference file `.agent-local` in the repo root. It is gitignored. If it does not exist, create it and add `.agent-local` to `.gitignore`.
  - If it contains `test_target=emulator`, use the emulator every time. Do not ask again, and do not use a physical phone unless the user explicitly asks for one.
  - If it contains `test_target=phone`, the user said no to the emulator. Use the phone, and do not ask again.
  - If the file has no preference, go to Step B.

**Step B: ask once.** If the only target is a physical phone, or there is no device at all, stop before installing and ask the user something like this:

> "Right now testing needs your real phone plugged in with developer options on. Tariq's setup uses an Android emulator on the laptop instead, so no phone or cable is needed. Want me to set that up for you? It is a one-time setup of roughly 3–5 GB of downloads, and after that I will run the app on the emulator by default."

- If the user says **yes**, run Step C and then write `test_target=emulator` to `.agent-local`.
- If the user says **no**, write `test_target=phone` to `.agent-local` and continue with the phone.

**Step C: set it up by running the script.** `scripts/run-emulator.ps1` does everything and has no machine-specific paths, so nobody has to edit it. It finds the Android SDK and JDK (environment variables, `local.properties`, the standard `%LOCALAPPDATA%\Android\Sdk`, Android Studio's bundled JDK), then:
- downloads the Android command-line tools if they are missing,
- installs `platform-tools`, `emulator` and the system image if they are missing (a one-time download of a few GB),
- warns if hardware virtualization is off,
- uses an existing healthy emulator (AVD), or creates one named `P3R_Emulator` if there is none,
- boots it, builds and installs the app with `gradlew installDebug`, launches the app, and saves a screenshot.

Run it like this: `powershell -ExecutionPolicy Bypass -File scripts\run-emulator.ps1`
- `-SetupOnly` prepares everything without building or launching.
- `-NoScreenshot` skips the screenshot.
- `-Avd <name>` forces a specific emulator.

Tell the user what is happening while it runs, because the first run is slow (large download, plus a first Gradle build of about 5–10 minutes). When it finishes, show the screenshot path it prints, then write `test_target=emulator` to `.agent-local`.

The only things the script cannot do for the user are installing a JDK or Android Studio (it stops and says so) and switching on virtualization in BIOS. If it prints a warning about virtualization, tell the user to enable virtualization in BIOS and turn on "Windows Hypervisor Platform" in Windows Features.

**Every run after setup:** run the same script. It reuses a running emulator and boots one only if none is up. It sets `ANDROID_SERIAL`, so Gradle installs only on the emulator even when a real phone is plugged in. Later builds are much faster than the first.

**Limits:** the script is Windows PowerShell only. On macOS or Linux, tell the user it is not supported there yet and use Android Studio's Device Manager instead.

If the script fails and you cannot fix it, tell the user exactly what failed and fall back to the phone for that run. Do not silently switch targets.

### 5. Collaborator Activity Log (handoff notes)
This section is how one developer tells the other, and their agent, what happened while they were away. Section 3 tells the agent to read it.

Rules for agents:
- Add an entry (newest first) when you leave something that the other developer should know about or act on. A commit message alone is not enough for that. Routine commits do not need a log entry.
- Mark open items `PENDING FOR YOU`. When one is done, change it to `DONE (who, date)` instead of deleting it.
- Write in plain language for a beginner.

#### 2026-10-11, tariqaq
- **Access check:** Tariq confirmed he has WRITE permission on this repo. Commit, push and sync all work.
- **Emulator setup on Tariq's laptop:** his Android SDK had been deleted. He reinstalled it, so he now tests on an Android Studio emulator (`Medium_Phone_API_36.1`). This is local to his machine. Section 4 above lets you do the same with one script run.
- **New file:** `scripts/run-emulator.ps1`. One command boots the emulator, builds, installs and launches the app, and takes a screenshot. It works on any Windows machine with no editing: it finds the SDK and JDK, installs missing emulator pieces and creates an emulator if needed.
- **AGENTS.md updated:** added the sync briefing (section 3), the emulator-first testing rule (section 4) and this log (section 5).
- **PENDING FOR YOU: setup.** Next time you want to test the app, your agent will offer to set up the emulator so you no longer need your phone connected.
- **PENDING FOR YOU: known issues found in a full project review.** None are fixed yet. Agree with Tariq on who fixes what, to avoid merge conflicts.
  1. The `p3rSkew` modifier in `ui/components/P3RShapes.kt` builds a matrix but never applies it, so text does not actually slant. The tab ribbon does slant, because it uses `AngledBannerShape`.
  2. The `AboutCard` in `ui/screens/StatusScreen.kt` renders solid pink. The 4dp accent stripe uses `matchParentSize()`, which overrides its width, so it covers the whole card. This is visible on the About Me tab.
  3. Rotating the screen restarts the music, because `MainActivity.onDestroy` calls `MusicController.release()`.
  4. Music and the background video keep playing when the app goes to the background, because nothing pauses them on `onStop`.
  5. `ui/theme/Theme.kt` and `Type.kt` are unused template leftovers (the purple scheme), and `P3RProfileTheme` is not wrapped around `setContent`.
  6. Several libraries are old (`coreKtx 1.10.1`, `activityCompose 1.8.0`, `lifecycle 2.6.1`, media3 `1.3.1`) compared with the Compose BOM `2026.02.01` and AGP `9.4.1`.
  7. The package name is still the placeholder `com.example.p3rprofile`, so the app cannot be published to the Play Store under that ID.
