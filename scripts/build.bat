@echo off
setlocal
set "ROOT=%~dp0.."
set "OUT=%ROOT%\out"
set "CLASSES=%OUT%\classes"
if exist "%OUT%" rmdir /s /q "%OUT%"
mkdir "%CLASSES%"
dir /s /b "%ROOT%\src\main\java\*.java" > "%OUT%\sources.txt"
javac -encoding UTF-8 -d "%CLASSES%" @"%OUT%\sources.txt"
xcopy "%ROOT%\src\main\resources\*" "%CLASSES%\" /E /I /Y > nul
echo Build gerado em %CLASSES%
