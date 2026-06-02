@echo off
setlocal
set "ROOT=%~dp0.."
call "%~dp0build.bat"
set "PORTA=%~1"
if "%PORTA%"=="" set "PORTA=8080"
java -cp "%ROOT%\out\classes" br.com.systemchess.App %PORTA%
