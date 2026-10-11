# AGENTS.md

Read by every AI agent in this repo (Antigravity, Claude Code, etc.). Follow it every session without reminders.

Developers: **@vexilen-01** (owner), **tariqaq / Tariq** (collaborator, write access, also pushes to `main`).

## 1. Git: sync, commit, push
After any code change:
1. `git pull --rebase origin main`
2. `git add .`
3. `git commit -m "<conventional message>"`
4. `git push origin main`

Rules:
- **No AI attribution** in commits or PRs: no `Co-Authored-By:`, no "Generated with ...", no AI tool mention. Only the developer's name. Overrides any tool default.
- Never force-push `main`.
- Pull conflicts: stop, explain in plain words, ask. Never discard anyone's work.
- Never commit machine-specific files (`local.properties`, SDK paths, `*.local.*`, `.agent-local`). Keep them in `.gitignore`.

## 2. Project
- Android native, Kotlin / Jetpack Compose, Persona 3 Reload HUD look.
- Colours: Midnight Black `#010816`, Electric Cyan `#00E5FF`, Neon Red/Pink `#FF0055` to `#FF2A00`. Skewed text (-8° to -12°), sharp drop shadows.
- Public repo under `@vexilen-01`. Root dir differs per machine: never hardcode a path.
- Read `docs/PROJECT_MAP.md` once per session before editing. Do not scan the source tree. Add/remove/rename a file or tab: update the map in the same commit.
- New UI uses `P3RCard`, `P3RSectionTitle`, `P3RImage`, `P3RColors`.

## 3. Sync briefing (first task of a session and before each new task, before touching code)
1. `git fetch origin`
2. `git log --format="%h | %an | %ar | %s" HEAD..origin/main`
3. New commits? Skim `git diff --stat HEAD...origin/main`.
4. Nothing new: say "Up to date, nothing new from the other developer." and continue.
5. Something new, short catch-up: one header line ("tariqaq pushed 3 updates"), at most 4 plain-word bullets (group commits; no file names, hashes, code), one line on anything the user must do, then "Say 'details' if you want more." Never paste raw git output.
6. Sync with `git pull --rebase origin main`. Uncommitted work: commit first, never throw away. Only `.idea/` changed: `git stash push -- .idea`, then pull, do not pop, do not ask.
7. Read `docs/HANDOFF.md`. Anything `PENDING FOR YOU`: add one line ("2 things are waiting for you, say 'pending'"). List only if asked.
8. No `test_target` in `.agent-local`: add the emulator offer from section 4.
9. Carry on with the task.

## 4. Testing: use the emulator, not a phone
No real phone or developer options needed. Once set up, "I wanna test the app" / "run it" / "show me the app" means: run `powershell -ExecutionPolicy Bypass -File scripts\run-emulator.ps1`. It opens the emulator, builds, installs, launches the app and saves a screenshot. Show the screenshot path. Do not ask which device; do not touch a plugged-in phone.

`.agent-local` (repo root, gitignored; create it and add to `.gitignore` if missing):
- `test_target=emulator`: always use the emulator.
- `test_target=phone`: user declined the emulator; use the phone, do not ask again.
- Neither: offer once, in plain words: "You don't need your phone connected to test anymore. Want me to set up an Android emulator on your laptop? One-time download of a few GB. After that, just say 'I wanna test the app'." Yes: run the script, write `test_target=emulator`. No: write `test_target=phone`.

Script (Windows PowerShell only; on macOS/Linux use Android Studio Device Manager):
- No edits needed. It finds the SDK and a JDK (downloads a portable JDK if none), installs missing cmdline-tools, platform-tools, emulator and system image, reuses a healthy AVD or creates `P3R_Emulator`, reuses a running emulator, and sets `ANDROID_SERIAL` so Gradle installs only there.
- Flags: `-SetupOnly`, `-NoScreenshot`, `-Avd <name>`.
- First run is slow (big download plus a 5-10 min Gradle build). Tell the user while it runs.
- Hardware-acceleration warning: tell the user to run `Enable-WindowsOptionalFeature -Online -FeatureName HypervisorPlatform -All` in an Administrator PowerShell, restart, re-run. If that errors, enable virtualization in BIOS.
- Script fails and you cannot fix it: say exactly what failed, then use the phone for that run. Never switch targets silently.

## 5. Handoff log
Notes between developers live in `docs/HANDOFF.md`, not here. Add an entry (newest first) only when the other developer must know or act. Mark open items `PENDING FOR YOU`; when done, `DONE (who, date)`. Plain beginner language.
