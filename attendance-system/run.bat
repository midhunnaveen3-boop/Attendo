@echo off
REM Simple batch wrapper to run the PowerShell runner
set SCRIPT=%~dp0run.ps1
powershell -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT%" %*
