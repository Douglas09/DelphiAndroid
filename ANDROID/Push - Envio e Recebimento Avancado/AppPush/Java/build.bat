@echo off

setlocal

if x%ANDROID% == x set ANDROID="C:\Users\Gledston Reis\Documents\Embarcadero\Studio\20.0\PlatformSDKs\android-sdk-windows"
set ANDROID_PLATFORM=%ANDROID%\platforms\android-28
set DX_LIB=%ANDROID%\build-tools\28.0.2\lib
set EMBO_DEX="C:\Program Files (x86)\Embarcadero\Studio\20.0\lib\android\debug\classes.dex"
set PROJ_DIR=%CD%
set VERBOSE=1
set JAVA="C:\Program Files\Java\jdk1.8.0_60\bin"

echo.
echo Compiling the Java service activity source files
echo.
mkdir output 2> nul
mkdir output\classes 2> nul
if x%VERBOSE% == x1 SET VERBOSE_FLAG=-verbose
%JAVA%\javac -source 1.8 -target 1.8 %VERBOSE_FLAG% -Xlint:deprecation -cp %ANDROID_PLATFORM%\android.jar;%PROJ_DIR%\android-support-v4.jar;%PROJ_DIR%\google-play-services.jar -d  output\classes src\ReceiverPush.java

echo.
echo Creating jar containing the new classes
echo.
mkdir output\jar 2> nul
if x%VERBOSE% == x1 SET VERBOSE_FLAG=v
%JAVA%\jar c%VERBOSE_FLAG%f output\jar\ReceiverPush.jar -C output\classes br

echo.
echo Converting from jar to dex...
echo.
mkdir output\dex 2> nul
if x%VERBOSE% == x1 SET VERBOSE_FLAG=--verbose
call %DX_LIB%\dx.jar --dex %VERBOSE_FLAG% --output="%PROJ_DIR%\output\dex\ReceiverPush.dex" --positions=lines %PROJ_DIR%\output\jar\ReceiverPush.jar


echo.
echo Merging dex files
echo.com.android.dx.merge.DexMerger
java -cp %DX_LIB%\dx.jar com.android.dx.merge.DexMerger %PROJ_DIR%\output\dex\classes.dex %PROJ_DIR%\output\dex\ReceiverPush.dex %EMBO_DEX%

echo Tidying up
echo.
::del output\classes\TestReceiver\BootCompletedReceiver.class
::rmdir output\classes\TestReceiver
::rmdir output\classes
::del output\dex\test_classes.dex
::del output\jar\test_classes.jar
::rmdir output\jar

echo.
echo Now we have the end result, which is output\dex\classes.dex

:Exit

pause

endlocal