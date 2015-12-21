:: only print what we want to have printed
@echo off

:: see http://stackoverflow.com/questions/905226/mkdir-p-linux-windows
setlocal enableextensions

:: Requirements Specification render script.
:: Parameters (all optional):
::	1. the folder to copy the results to. Defaults to the working directory
::	2. the file to write the log to. Defaults to 'Render.sh' in the working directory
::	3. if "srs-only" is passed as 3. parameter, only the rendered 'Requirements Specification.pdf' will be created  

IF [%1]==[] ( SET DEST="%cd%" ) ELSE ( SET DEST=%1 )
IF [%2]==[] ( SET LOGDEST="%cd%\Render.log" ) ELSE ( SET LOGDEST=%2 )
if not exist %DEST% mkdir %DEST%

:: LyX executable. Add some logic here to search differen paths if your installation is elsewhere. 
set LYX="C:\Program Files (x86)\LyX 2.1\bin\lyx.exe"
if not exist %LYX% set LYX="G:\Programme\LyX 2.1\bin\lyx.exe"
set UMLET="C:\Program Files (x86)\Umlet\umlet.jar"
if not exist %UMLET% set UMLET="C:\Program Files\Umlet\umlet.jar"
if not exist %UMLET% set UMLET="C:\weitere Programme\Umlet\umlet.jar"
set FILENAME="Requirements Specification"

:: separator for log messages
set sep=######################################

:: Get a temporary folder name that we can savely delete later
call :GETTEMPNAME
:: Create the folder and copy all contents in it
mkdir %TMPDIR%
xcopy . %TMPDIR% /e /i /q
set log="%TMPDIR%\Render.log"
set tee="%TMPDIR%\tee"

set drive=%cd:~0,2%
set OLDPWD=%cd%
C:
cd %TMPDIR%

:: The old log was copied in, remove it.
if exist %log% del %log%
:: remove old PDFs
del /S *.pdf 1>nul


:::::::::::::::
:: Main Part ::
:::::::::::::::

::generate .pdf from .uxf (Must happen before LyX runs!)
:: For some strange reason, Umlet has no output on windws. It also fails to fail ( ;) )
:: if it encouters an error. Thus we have no way to know if it was successful and cannot
:: abort on error or even report any errors.
call :log Rendering the UML diagrams
For /R %%f in (*.uxf) do (
		%UMLET% -action=convert -format=pdf -filename="%%f" > %tee% || goto :teeerror
		call :tee
	)

:: generate .tex
call :log Rendering with LyX
%LYX% --export pdflatex %FILENAME%.lyx > %tee% || goto :teeerror
call :tee

:: generate all the other stuff
call :log First pdflatex run
pdflatex -interaction=nonstopmode -halt-on-error %FILENAME%.tex >> %log% || goto :logerror

:: make the glossary
call :log Generating the glossary
makeglossaries %FILENAME% > %tee% || goto :teeerror
call :tee

:: make the biliography
call :log Generating the bibliography
bibtex %FILENAME% > %tee% || goto :teeerror
call :tee

:: render the pdf (we need at least two times. And you know… just to be sure ;))
:: The output of the first runs is irrelevant
call :log Second pdflatex run
pdflatex -interaction=nonstopmode -halt-on-error %FILENAME%.tex >> %log% || goto :logerror
call :log Third pdflatex run
pdflatex -interaction=nonstopmode -halt-on-error %FILENAME%.tex >> %log% || goto :logerror
call :log Fourth pdflatex run
pdflatex -interaction=nonstopmode -halt-on-error %FILENAME%.tex >> %log% || goto :logerror
call :log Last pdflatex run
pdflatex -interaction=nonstopmode -halt-on-error %FILENAME%.tex > %tee% || goto :teeerror
call :tee

::::::::::::::::::::::
:: End of Main Part ::
::::::::::::::::::::::






:: Go back
%drive%
cd %OLDPWD%

IF "%3"=="srs-only" (
	xcopy /y %TMPDIR%\%FILENAME%.pdf %DEST%
) ELSE (
	:: copy all rendered pdfs to save them
	xcopy /s /y %TMPDIR%\*.pdf %DEST%
)

:: save the log
:: next line simulates 'dirname'
for %%F in (%LOGDEST%) do set LOGDIRNAME=%%~dpF
mkdir "%LOGDIRNAME%"
copy /y %log% %LOGDEST%

:: Delete the temporary folder, as we don't need the tons of files in it
rmdir %TMPDIR% /s /q
goto :EOF

:GETTEMPNAME
set TMPDIR=%TMP%\%RANDOM%
if exist "%TMPDIR%" GOTO :GETTEMPNAME 
exit /B 0

:: logs the provided argument to both stdout and the logfile
:log
echo( >> %log%
echo( >> %log%
echo( >> %log%
echo %sep% >> %log%
echo %* >> %log%
echo %sep% >> %log%
echo( >> %log%
echo( >> %log%
echo(
echo(
echo(
echo %sep%
echo %*
echo %sep%
echo(
echo(
exit /B 0

:: prints the contents of %tee% both into the logfile and on stdout (Poor man's tee)
:tee
type %tee% >> %log%
type %tee%
exit /B 0


:: called when a normal logging command failed. Prints the whole log to stdout.
:: Restores the PWD, saves the log and prints an error message
:: Jump to this section using goto, such that it can abort the script.
:logerror
type %log%
%drive%
cd %OLDPWD%
copy %log% . /y
echo(
echo(
echo(
echo(
echo An error occured (see output above).
echo You can see the files generated during rendering in %TMPDIR% (type "cd %%TMPDIR%%")
exit /B 1
exit /B 1

:: called when a tee logging command failed. Stores the tee file and continues with :logerror
:: Jump to this section using goto, such that it can abort the script.
:teeerror
call :tee
goto :logerror

:EOF
endlocal