@echo off
title maleyanga

REM --- Configuration ---
set "JAVA_HOME=C:\Program Files\Java\jdk1.7.0_80"
set "GRAILS_HOME=W:\App\grails-2.3.11\grails-2.3.11"
set "PATH=%GRAILS_HOME%\bin;%JAVA_HOME%\bin;%PATH%"

echo ===================================
echo    INICIANDO APLICACAO MALEYANGA
echo ===================================
echo.
echo JAVA_HOME: %JAVA_HOME%
echo GRAILS_HOME: %GRAILS_HOME%
echo.

REM Navigate to the project directory
cd /d "W:\mz.maleyanga"

REM Clean the project first to ensure it's built with the correct Java version
echo Limpando o projeto...
call "%GRAILS_HOME%\bin\grails.bat" clean --stacktrace

echo.

REM Run the application in development mode
echo Iniciando a aplicacao...
call "%GRAILS_HOME%\bin\grails.bat" run-app
