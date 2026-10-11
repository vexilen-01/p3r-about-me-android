<#
  One command to see the app: boots an Android emulator, builds + installs the debug app, launches it,
  and saves a screenshot. Works on any Windows machine with no editing: it finds the Android SDK and JDK,
  installs missing emulator pieces, and picks (or creates) a working AVD.

  Usage:  powershell -ExecutionPolicy Bypass -File scripts\run-emulator.ps1 [-Avd <name>] [-SetupOnly] [-NoScreenshot]
#>
param(
    [string]$Avd,
    [switch]$SetupOnly,
    [switch]$NoScreenshot,
    [string]$Shot = (Join-Path $env:TEMP "p3r-screen.png")
)

# Native tools (adb, sdkmanager) write progress to stderr, so errors are checked by hand.
$ErrorActionPreference = "Continue"
$ProgressPreference = "SilentlyContinue"
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

# The only tunable: which system image the emulator uses.
$ImageApi = "android-36.1"
$ImageTag = "google_apis_playstore"
$ImageAbi = if ($env:PROCESSOR_ARCHITECTURE -eq "ARM64") { "arm64-v8a" } else { "x86_64" }
$ImagePkg = "system-images;$ImageApi;$ImageTag;$ImageAbi"

$root = Split-Path $PSScriptRoot -Parent
function Say($m) { Write-Host "[run-emulator] $m" }
function Fail($m) { Write-Host "[run-emulator] ERROR: $m" -ForegroundColor Red; exit 1 }

# ---- project facts (read from the repo, not hardcoded) ----
$gradleFile = Get-Content (Join-Path $root "app\build.gradle.kts") -Raw
if ($gradleFile -notmatch 'applicationId\s*=\s*"([^"]+)"') { Fail "applicationId not found in app/build.gradle.kts" }
$pkg = $Matches[1]

# ---- locate JDK ----
$jdk = $null
$jdkCandidates = @(
    $env:JAVA_HOME,
    (Join-Path $env:ProgramFiles "Android\Android Studio\jbr"),
    (Join-Path $env:LOCALAPPDATA "Programs\Android Studio\jbr")
)
$jdkCandidates += @(Get-ChildItem (Join-Path $env:LOCALAPPDATA "P3R\jdk") -Directory -ErrorAction SilentlyContinue | ForEach-Object FullName)
foreach ($p in $jdkCandidates) {
    if ($p -and (Test-Path (Join-Path $p "bin\java.exe"))) { $jdk = $p; break }
}
if (-not $jdk) {
    $j = Get-Command java -ErrorAction SilentlyContinue
    if ($j) { $jdk = Split-Path (Split-Path $j.Source -Parent) -Parent }
}
if (-not $jdk) {
    # Fresh machine: fetch a portable JDK (nothing is installed system-wide, no admin needed).
    Say "No JDK found. Downloading JDK 21 (one-time, about 200 MB)..."
    $jdkRoot = Join-Path $env:LOCALAPPDATA "P3R\jdk"
    $jdkTmp = Join-Path $env:TEMP "p3r-jdk"
    Remove-Item $jdkTmp -Recurse -Force -ErrorAction SilentlyContinue
    New-Item -ItemType Directory -Force $jdkTmp, $jdkRoot | Out-Null
    $jdkArch = if ($env:PROCESSOR_ARCHITECTURE -eq "ARM64") { "aarch64" } else { "x64" }
    Invoke-WebRequest "https://api.adoptium.net/v3/binary/latest/21/ga/windows/$jdkArch/jdk/hotspot/normal/eclipse" -OutFile "$jdkTmp\jdk.zip" -UseBasicParsing
    if (Get-Command tar -ErrorAction SilentlyContinue) { tar -xf "$jdkTmp\jdk.zip" -C $jdkRoot } else { Expand-Archive "$jdkTmp\jdk.zip" -DestinationPath $jdkRoot -Force }
    Remove-Item $jdkTmp -Recurse -Force -ErrorAction SilentlyContinue
    $jdk = Get-ChildItem $jdkRoot -Directory | Where-Object { Test-Path (Join-Path $_.FullName "bin\java.exe") } | Select-Object -First 1 -ExpandProperty FullName
    if (-not $jdk) { Fail "JDK download failed. Install JDK 17+ manually and re-run." }
}
$env:JAVA_HOME = $jdk

# ---- locate Android SDK (or choose the standard location to create it) ----
$sdkCandidates = @($env:ANDROID_HOME, $env:ANDROID_SDK_ROOT)
$lp = Join-Path $root "local.properties"
if (Test-Path $lp) {
    $m = Select-String -Path $lp -Pattern '^sdk\.dir=(.+)$'
    if ($m) { $sdkCandidates += ($m.Matches[0].Groups[1].Value.Trim() -replace '\\\\', '\' -replace '\\:', ':') }
}
$sdkCandidates += (Join-Path $env:LOCALAPPDATA "Android\Sdk")
$sdk = $null
foreach ($p in $sdkCandidates) {
    if ($p -and (Test-Path (Join-Path $p "platform-tools\adb.exe"))) { $sdk = $p; break }
}
if (-not $sdk) {
    foreach ($p in $sdkCandidates) { if ($p -and (Test-Path $p)) { $sdk = $p; break } }
}
if (-not $sdk) { $sdk = Join-Path $env:LOCALAPPDATA "Android\Sdk" }
New-Item -ItemType Directory -Force $sdk | Out-Null
$env:ANDROID_HOME = $sdk
Say "SDK: $sdk"
Say "JDK: $jdk"

# local.properties tells Gradle where the SDK is (gitignored, per machine).
if (-not (Test-Path $lp) -or -not (Select-String -Path $lp -Pattern '^sdk\.dir=' -Quiet)) {
    $esc = ($sdk -replace '\\', '\\') -replace ':', '\:'
    Add-Content -Path $lp -Value "sdk.dir=$esc" -Encoding ASCII
    Say "Wrote sdk.dir to local.properties"
}

# ---- command-line tools (download if missing) ----
$sdkmanager = Join-Path $sdk "cmdline-tools\latest\bin\sdkmanager.bat"
$avdmanager = Join-Path $sdk "cmdline-tools\latest\bin\avdmanager.bat"
if (-not (Test-Path $sdkmanager)) {
    Say "Downloading Android command-line tools..."
    $tmp = Join-Path $env:TEMP "p3r-clt"
    Remove-Item $tmp -Recurse -Force -ErrorAction SilentlyContinue
    New-Item -ItemType Directory -Force $tmp | Out-Null
    $xml = (Invoke-WebRequest "https://dl.google.com/android/repository/repository2-3.xml" -UseBasicParsing).Content
    $zip = [regex]::Matches($xml, 'commandlinetools-win-(\d+)_latest\.zip') |
        Sort-Object { [int]$_.Groups[1].Value } | Select-Object -Last 1
    if (-not $zip) { Fail "Could not find the command-line tools download." }
    Invoke-WebRequest "https://dl.google.com/android/repository/$($zip.Value)" -OutFile "$tmp\clt.zip" -UseBasicParsing
    Expand-Archive "$tmp\clt.zip" -DestinationPath "$tmp\x" -Force
    New-Item -ItemType Directory -Force (Join-Path $sdk "cmdline-tools") | Out-Null
    Remove-Item (Join-Path $sdk "cmdline-tools\latest") -Recurse -Force -ErrorAction SilentlyContinue
    Move-Item "$tmp\x\cmdline-tools" (Join-Path $sdk "cmdline-tools\latest") -Force
}

# .bat files split arguments at ';', so every package name is quoted inside cmd /c.
function Run-Tool($bat, [string[]]$toolArgs, [string]$stdin = "") {
    $q = ($toolArgs | ForEach-Object { '"' + $_ + '"' }) -join " "
    if ($stdin) { $stdin | cmd /c "`"$bat`" $q 2>&1" } else { cmd /c "`"$bat`" $q 2>&1" }
}

# ---- required SDK pieces ----
$adb = Join-Path $sdk "platform-tools\adb.exe"
$emulatorExe = Join-Path $sdk "emulator\emulator.exe"
$imageDir = Join-Path $sdk ("system-images\$ImageApi\$ImageTag\$ImageAbi")
$need = @()
if (-not (Test-Path $adb)) { $need += "platform-tools" }
if (-not (Test-Path $emulatorExe)) { $need += "emulator" }
if (-not (Test-Path $imageDir)) { $need += $ImagePkg }
if ($need.Count) {
    Say "Installing: $($need -join ', ') (one-time download, can be several GB)..."
    Run-Tool $sdkmanager $need ("y`n" * 30) | Select-Object -Last 3 | ForEach-Object { Say $_ }
    foreach ($f in @($adb, $emulatorExe, $imageDir)) { if (-not (Test-Path $f)) { Fail "SDK install incomplete, missing: $f" } }
}

# ---- hardware acceleration (warn only; the emulator reports its own error) ----
$accel = (& $emulatorExe -accel-check 2>&1) -join " "
if ($accel -notmatch "usable|working|KVM") {
    Write-Warning ("Hardware acceleration not detected, so the emulator will be very slow or fail. Fix (one time): open PowerShell as Administrator and run " +
        "'Enable-WindowsOptionalFeature -Online -FeatureName HypervisorPlatform -All', then restart Windows. " +
        "If that errors, enable virtualization (Intel VT-x / AMD SVM) in the BIOS first.")
}

# ---- pick a healthy AVD, or create one ----
if (-not $Avd) {
    $avdHome = if ($env:ANDROID_AVD_HOME) { $env:ANDROID_AVD_HOME } else { Join-Path $env:USERPROFILE ".android\avd" }
    $healthy = @()
    foreach ($ini in Get-ChildItem $avdHome -Filter *.ini -ErrorAction SilentlyContinue) {
        # The .ini names the AVD; its folder can have a different name, so follow its path= line.
        $pm = Select-String -Path $ini.FullName -Pattern '^path=(.+)$'
        $avdDir = if ($pm) { $pm.Matches[0].Groups[1].Value.Trim() } else { Join-Path $avdHome ($ini.BaseName + ".avd") }
        $cfg = Join-Path $avdDir "config.ini"
        if (-not (Test-Path $cfg)) { continue }
        $m = Select-String -Path $cfg -Pattern '^image\.sysdir\.1=(.+)$'
        if ($m -and (Test-Path (Join-Path $sdk $m.Matches[0].Groups[1].Value.Trim()))) { $healthy += $ini.BaseName }
    }
    $Avd = $healthy | Where-Object { $_ -match "Phone|Pixel" } | Select-Object -First 1
    if (-not $Avd) { $Avd = $healthy | Select-Object -First 1 }
}
if (-not $Avd) {
    $Avd = "P3R_Emulator"
    Say "No usable emulator found. Creating '$Avd'..."
    foreach ($device in "medium_phone", "pixel_7") {
        Run-Tool $avdmanager @("create", "avd", "-n", $Avd, "-k", $ImagePkg, "-d", $device, "--force") "no" | Out-Null
        if (-not ((& $emulatorExe -list-avds) -contains $Avd)) { continue }
        break
    }
    if (-not ((& $emulatorExe -list-avds) -contains $Avd)) { Fail "Could not create an emulator (AVD)." }
}
Say "Emulator: $Avd"
if ($SetupOnly) { Say "Setup complete."; exit 0 }

# ---- boot (reuse a running emulator) ----
function Get-EmuSerial {
    $line = (& $adb devices) | Select-String "^(emulator-\d+)\s+device$" | Select-Object -First 1
    if ($line) { $line.Matches[0].Groups[1].Value } else { $null }
}
$serial = Get-EmuSerial
if (-not $serial) {
    Say "Booting emulator (first boot can take a few minutes)..."
    # Size the window to fit the screen (a tall phone otherwise opens half off-screen).
    $scale = 0.4
    try {
        Add-Type -AssemblyName System.Windows.Forms
        $workH = [System.Windows.Forms.Screen]::PrimaryScreen.WorkingArea.Height
        $scale = [math]::Round([math]::Min(1.0, [math]::Max(0.2, ($workH - 130) / 2400)), 2)
    } catch { }
    Start-Process $emulatorExe -ArgumentList "-avd", $Avd, "-no-snapshot-save", "-scale", "$scale"
    $deadline = (Get-Date).AddMinutes(8)
    do {
        Start-Sleep 3
        $serial = Get-EmuSerial
        $booted = $serial -and ((& $adb -s $serial shell getprop sys.boot_completed 2>$null) -join "").Trim() -eq "1"
    } until ($booted -or (Get-Date) -gt $deadline)
    if (-not $booted) { Fail "Emulator did not finish booting within 8 minutes." }
}
Say "Device: $serial"

# ---- build, install, launch ----
# ANDROID_SERIAL makes Gradle install only on the emulator, even if a real phone is also plugged in.
$env:ANDROID_SERIAL = $serial
Push-Location $root
try {
    & (Join-Path $root "gradlew.bat") installDebug --console=plain
    if ($LASTEXITCODE -ne 0) { Fail "Gradle build/install failed (exit $LASTEXITCODE)." }
} finally { Pop-Location }

& $adb -s $serial shell monkey -p $pkg -c android.intent.category.LAUNCHER 1 2>&1 | Out-Null
Say "Launched $pkg"

if (-not $NoScreenshot) {
    Start-Sleep 6
    # cmd handles the binary redirect; PowerShell pipes would corrupt the PNG.
    cmd /c "`"$adb`" -s $serial exec-out screencap -p > `"$Shot`""
    Say "Screenshot: $Shot"
}
