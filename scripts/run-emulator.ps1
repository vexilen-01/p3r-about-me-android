# Boots the Medium_Phone_API_36.1 emulator, installs the debug build, launches the app, saves a screenshot.
param([string]$Avd = "Medium_Phone_API_36.1", [string]$Shot = "$env:TEMP\p3r-screen.png")
$ErrorActionPreference = "Stop"
$sdk = "C:\Users\trqboi\AppData\Local\Android\Sdk"
$adb = "$sdk\platform-tools\adb.exe"
$root = Split-Path $PSScriptRoot -Parent
$pkg = "com.example.p3rprofile"

$running = & $adb devices | Select-String "emulator-\d+\s+device"
if (-not $running) {
    Start-Process "$sdk\emulator\emulator.exe" -ArgumentList "-avd", $Avd, "-no-snapshot-save" -WindowStyle Hidden
    & $adb wait-for-device
    while ((& $adb shell getprop sys.boot_completed 2>$null).Trim() -ne "1") { Start-Sleep 3 }
}

$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
Push-Location $root
try { & .\gradlew.bat installDebug; if ($LASTEXITCODE) { throw "gradle failed" } } finally { Pop-Location }

& $adb shell am start -n "$pkg/.MainActivity"
Start-Sleep 6
cmd /c "`"$adb`" exec-out screencap -p > `"$Shot`""
Write-Host "Screenshot: $Shot"
