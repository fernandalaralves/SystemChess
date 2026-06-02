$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
& (Join-Path $PSScriptRoot "build.ps1")
$porta = if ($args.Length -gt 0) { $args[0] } else { "8080" }
java -cp (Join-Path $root "out\classes") br.com.systemchess.App $porta
