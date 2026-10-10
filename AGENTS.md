# AGENTS.md

## Repository & Development Directives

### 1. Mandatory Git Auto-Commit & Sync Rule
Whenever any changes, feature updates, bug fixes, or modifications are made to the codebase in this project:
- Immediately stage all modified files: `git add .`
- Create a meaningful conventional commit message describing the changes: `git commit -m "..."`
- Immediately push and sync upstream to the remote repository: `git push`

This rule must be followed on every development cycle without requiring explicit reminders.

### 2. Project Context & Architecture
- **Platform**: Android Native (Kotlin / Jetpack Compose)
- **Theme**: Persona 3 Reload (P3R) HUD Aesthetic
  - Colors: Midnight Black (`#010816`), Electric Cyan (`#00E5FF`), Neon Red/Pink Ribbon (`#FF0055` to `#FF2A00`)
  - Typography: Slanted/skewed transforms (`-8°` to `-12°`), sharp high-contrast drop shadows.
- **Repository**: Public GitHub repository under `@vexilen-01`
- **Root Directory**: `C:\Users\HP\AndroidStudioProjects\P3RProfile`
