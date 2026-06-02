$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$out = Join-Path $root "out"
$classes = Join-Path $out "test-classes"
if (Test-Path $classes) {
    Remove-Item $classes -Recurse -Force
}
New-Item -ItemType Directory -Path $classes | Out-Null
$mainSources = Get-ChildItem -Path (Join-Path $root "src\main\java") -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
$testSources = Get-ChildItem -Path (Join-Path $root "teste\java") -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d $classes $mainSources $testSources
java -cp $classes br.com.systemchess.SystemChessTest
