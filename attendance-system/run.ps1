<#
Simple runner for the Attendance project.
Usage (PowerShell):
  .\run.ps1            # compile and run using default main
  .\run.ps1 -Main com.attendance.Main

This script will:
- ensure lib/ directory exists and required jars are downloaded
- compile Java sources into out/
- run the specified main class (default: com.attendance.Main)
#>

param(
    [string]$Main = 'com.attendance.Main'
)

Set-StrictMode -Version Latest

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectRoot

$lib = Join-Path $projectRoot 'lib'
if (-not (Test-Path $lib)) { New-Item -ItemType Directory -Path $lib | Out-Null }

$jars = @{
    'sqlite' = 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar'
    'commons-csv' = 'https://repo1.maven.org/maven2/org/apache/commons/commons-csv/1.10.0/commons-csv-1.10.0.jar'
}

foreach ($name in $jars.Keys) {
    $url = $jars[$name]
    $fileName = Split-Path $url -Leaf
    $dest = Join-Path $lib $fileName
    if (-not (Test-Path $dest)) {
        Write-Output "Downloading $fileName..."
        try {
            Invoke-WebRequest -Uri $url -OutFile $dest -UseBasicParsing -ErrorAction Stop
        } catch {
            Write-Error "Failed to download $url : $_"
            exit 1
        }
    }
}

$out = Join-Path $projectRoot 'out'
if (-not (Test-Path $out)) { New-Item -ItemType Directory -Path $out | Out-Null }

function Find-JdkExecutable {
    param([string]$exe)
    $cmd = Get-Command $exe -ErrorAction SilentlyContinue
    if ($cmd) { return $cmd.Source }
    $candidates = @(
        "C:\Program Files\Eclipse Adoptium\jdk-8.0.462.8-hotspot\bin\$exe",
        "C:\Program Files\Java\jdk\bin\$exe",
        "C:\Program Files\Java\jre-1.8\bin\$exe",
        "C:\Program Files\Java\jdk-8\bin\$exe"
    )
    foreach ($p in $candidates) { if (Test-Path $p) { return $p } }
    return $null
}

$javacPath = Find-JdkExecutable -exe 'javac.exe'
if (-not $javacPath) { Write-Error "javac not found on PATH and no common JDK locations found. Please install a JDK."; exit 1 }

$javaPath = Find-JdkExecutable -exe 'java.exe'
if (-not $javaPath) { Write-Error "java not found on PATH and no common JDK locations found. Please install a JDK."; exit 1 }

$sourceDir = Join-Path $projectRoot 'src\main\java'
$srcItems = Get-ChildItem -Path $sourceDir -Recurse -Filter *.java -ErrorAction SilentlyContinue
if (-not $srcItems) { Write-Error "No source files found under $sourceDir"; exit 1 }
$sources = $srcItems | ForEach-Object { $_.FullName }
$count = @($sources).Count
Write-Output "Compiling $count .java files..."
$cp = ".;lib\*"
& "$javacPath" -cp $cp -d $out $sources
if ($LASTEXITCODE -ne 0) { Write-Error "Compilation failed (javac exit code $LASTEXITCODE)"; exit $LASTEXITCODE }

Write-Output "Running $Main..."
& "$javaPath" -cp "out;lib\*" $Main
