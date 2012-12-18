@echo off
set "CP=.;classes;"
rem for /r %%i in (*.xcf) DO call :concat %%i
for /f %%a IN ('dir /b *.jar') do call :concat %%a
echo %CP%
java -cp %CP% haleakala.App %1 %*
goto :eof

:concat
set CP=%CP%%1;
goto :eof

