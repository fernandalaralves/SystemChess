@echo off
setlocal
set "ROOT=%~dp0.."
set "OUT=%ROOT%\out"
set "CLASSES=%OUT%\test-classes"
if exist "%CLASSES%" rmdir /s /q "%CLASSES%"
mkdir "%CLASSES%"
dir /s /b "%ROOT%\src\main\java\*.java" > "%OUT%\test-sources.txt"
dir /s /b "%ROOT%\teste\java\*.java" >> "%OUT%\test-sources.txt"
javac -encoding UTF-8 -d "%CLASSES%" @"%OUT%\test-sources.txt"
java -cp "%CLASSES%" br.com.systemchess.SystemChessTest
