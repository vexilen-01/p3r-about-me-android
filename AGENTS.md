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

**Step C: one-time emulator setup (Windows).** Tell the user what you are doing as you go, because they may be new to this. Ask before starting any large download.
1. **Android Studio** must be installed. If it is not, ask the user to install it from https://developer.android.com/studio. Do not try to install it silently.
2. **Find the SDK** in this order: the `ANDROID_HOME` environment variable, `sdk.dir` in `local.properties`, `%LOCALAPPDATA%\Android\Sdk`, then Android Studio's setting (File > Settings > Languages & Frameworks > Android SDK). If the SDK folder is missing or empty, restore it with step 3. Make sure `local.properties` in the repo root contains `sdk.dir=<escaped SDK path>`. It is gitignored.
3. **Install the packages** with `sdkmanager` (in `<SDK>\cmdline-tools\latest\bin`; get the "Command-line tools" zip from the SDK Manager in Android Studio if absent). Required packages:
   - `platform-tools`
   - `emulator`
   - `platforms;android-37.0` (matches `compileSdk` in `app/build.gradle.kts`)
   - `build-tools;37.0.0` (Gradle may also fetch `36.0.0` on its own, which is normal)
   - `system-images;android-36.1;google_apis_playstore;x86_64`

   Set `JAVA_HOME` to Android Studio's bundled JDK (`<Android Studio>\jbr`) when running `sdkmanager`. On Windows, `sdkmanager.bat` splits arguments at `;`, so wrap each package name in quotes inside `cmd /c`, for example: `cmd /c ""<SDK>\cmdline-tools\latest\bin\sdkmanager.bat" "platforms;android-37.0" "emulator""`
4. **Check hardware acceleration:** `<SDK>\emulator\emulator.exe -accel-check`. It must report that WHPX, HAXM or another accelerator is usable. If not, tell the user to enable virtualization in BIOS and turn on "Windows Hypervisor Platform" in Windows Features.
5. **Create the AVD** if `emulator.exe -list-avds` shows none that is healthy (it has a real data folder under `~\.android\avd`):
   `avdmanager create avd -n Medium_Phone_API_36.1 -k "system-images;android-36.1;google_apis_playstore;x86_64" -d medium_phone`
   Alternative for a beginner: Android Studio > Device Manager > Create Device > Medium Phone > API 36.1.
6. **Launcher script:** `scripts/run-emulator.ps1` boots the AVD, waits for boot, runs `gradlew installDebug`, launches `com.example.p3rprofile/.MainActivity`, and saves a screenshot. It has **Tariq's SDK path and JDK path hardcoded**. Do not edit and commit it with your own paths, because that would break it for Tariq. Instead, copy it to `scripts/run-emulator.local.ps1` (add `*.local.ps1` to `.gitignore`), change `$sdk` and `JAVA_HOME` there, and use that copy.
7. Run the local script once to confirm the app appears on the emulator, then record `test_target=emulator` in `.agent-local`.

**Every run after setup:** run the launcher script. It reuses a running emulator and only boots one if none is up. The first Gradle build is slow (about 5–10 minutes). Later builds are much faster. If more than one device is connected, target the emulator explicitly with `adb -s emulator-5554 ...`.

If emulator setup fails and cannot be fixed, tell the user exactly what failed and fall back to the phone for that run. Do not silently switch targets.

### 5. Collaborator Activity Log (handoff notes)
This section is how one developer tells the other, and their agent, what happened while they were away. Section 3 tells the agent to read it.

Rules for agents:
- Add an entry (newest first) when you leave something that the other developer should know about or act on. A commit message alone is not enough for that. Routine commits do not need a log entry.
- Mark open items `PENDING FOR YOU`. When one is done, change it to `DONE (who, date)` instead of deleting it.
- Write in plain language for a beginner.

#### 2026-10-11, tariqaq
- **Access check:** Tariq confirmed he has WRITE permission on this repo. Commit, push and sync all work.
- **Emulator setup on Tariq's laptop:** his Android SDK had been deleted. He reinstalled it, so he now tests on an Android Studio emulator (`Medium_Phone_API_36.1`). This is local to his machine and changes nothing for you, except that section 4 above now exists so you can do the same.
- **New file:** `scripts/run-emulator.ps1` (commit `13a61a7`). It boots the emulator, installs and launches the app, and takes a screenshot. It has Tariq's paths hardcoded, so copy it to a `.local.ps1` file as described in section 4, step 6.
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
