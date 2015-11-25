:: only print what we want to have printed
@echo off

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
xcopy . %TMPDIR% /e /i
set log="%TMPDIR%\Render.log"
set tee="%TMPDIR%\tee"

set drive=%cd:~0,2%
set OLDPWD=%cd%
C:
cd %TMPDIR%

:: The old log was copied in, remove it.
if exist %log% del %log%


:::::::::::::::
:: Main Part ::
:::::::::::::::

::generate .pdf from .uxf (Must happen before LyX runs!)
For /R %%f in (*.uxf) do (
		%UMLET% -action=convert -format=pdf -filename="%%f"
	)


:: generate .tex
call :log Rendering with LyX
%LYX% --export pdflatex %FILENAME%.lyx > %tee% || (call :teeerror & exit /B 1)
call :tee

:: generate all the other stuff
call :log First pdflatex run
pdflatex -halt-on-error %FILENAME%.tex >> %log% || (call :logerror & exit /B 1)

:: make the glossary
call :log Generating the glossary
makeglossaries %FILENAME% > %tee% || (call :teeerror & exit /B 1)
call :tee

:: make the biliography
call :log Generating the bibliography
bibtex %FILENAME% > %tee% || (call :teeerror & exit /B 1)
call :tee

:: render the pdf (we need at least two times. And you know… just to be sure ;))
:: The output of the first runs is irrelevant
call :log Second pdflatex run
pdflatex -halt-on-error %FILENAME%.tex >> %log% || (call :logerror & exit /B 1)
call :log Third pdflatex run
pdflatex -halt-on-error %FILENAME%.tex >> %log% || (call :logerror & exit /B 1)
call :log Fourth pdflatex run
pdflatex -halt-on-error %FILENAME%.tex >> %log% || (call :logerror & exit /B 1)
call :log Last pdflatex run
pdflatex -halt-on-error %FILENAME%.tex > %tee% || (call :teeerror & exit /B 1)
call :tee

::::::::::::::::::::::
:: End of Main Part ::
::::::::::::::::::::::






:: Go back
%drive%
cd %OLDPWD%

:: copy all rendered pdfs to save them
xcopy /s /y %TMPDIR%\*.pdf .
:: save the log
copy %log% . /y

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


:: called when a normal logging command failed. Prints the whole log to stdout
:logerror
type %log%
call :errorcleanup
exit /B 1

:: called when a tee logging command failed. Stores the tee file and continues with :logerror
:teeerror
call :tee
call :logerror
exit /B 1

:: called on every fail. Restores the PWD
:: saves the log, prints a messages and exits.
:errorcleanup
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

:EOF
