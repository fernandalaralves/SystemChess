$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$out = Join-Path $root "out"
$classes = Join-Path $out "classes"
if (Test-Path $out) {
    Remove-Item $out -Recurse -Force
}
New-Item -ItemType Directory -Path $classes | Out-Null
$sources = Get-ChildItem -Path (Join-Path $root "src\main\java") -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d $classes $sources
Copy-Item -Path (Join-Path $root "src\main\resources\*") -Destination $classes -Recurse -Force
Write-Host "Build gerado em $classes"
