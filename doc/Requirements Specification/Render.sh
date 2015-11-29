#!/bin/bash

file="Requirements Specification"

# fail the script if any command fails
set -e 

# Copy everything to a new folder that we can savely delete
tmpdir=`mktemp -d`
log=$tmpdir/Render.log

cp -r * "$tmpdir"
cd "$tmpdir"

# The old log was copied in, remove it.
rm -f $log

# Called when one of the commands fails. Restores the PWD, writes the full log to stdout,
# saves the log, prints a messages and exits.
function error {
	cat $log
	cd "$OLDPWD"
	cp -f $log .
	echo "

	
	
An error occured (see output above).
You can see the files generated during rendering in $tmpdir"
	exit 1
}

function log {
	echo "


######################################
$1
######################################

" | tee -a $log
}




#############
# Main Part #
#############

# generate .pdf from .uxf (Must happen before LyX runs!)
log "Rendering the UML diagrams"
# redirect stream 5 to 1 (stdout)
exec 5>&1
# the conversion's output is duplicated to 5, which will be printed on stdout. stdout is captured in umletconversion
umletconversion=$(find . -name '*.uxf' -exec /opt/Umlet/umlet.sh -action=convert -format=pdf -filename='{}' \; | tee -a $log | tee >(cat - >&5)) 
# umlet fails to fail ( ;) ) if an error occurs. We try to fix this by searching for the words 'Exception' and 'Error' in the output
# That's far from perfect but better than nothing.
echo $umletconversion | grep -Eivq '(.*Exception.*|.*Error.*)' || error


# generate .tex
log "Rendering with LyX"
lyx -e pdflatex "$file.lyx" | tee -a $log || error

# generate all the other stuff
log "First pdflatex run"
pdflatex -interaction=nonstopmode -halt-on-error "$file.tex" >> $log || error

# make the glossary
log "Generating the glossary"
makeglossaries "$file" | tee -a $log || error

# make the biliography
log "Generating the bibliography"
bibtex "$file" | tee -a $log || error

# render the pdf (we need at least two times. And you know… just to be sure ;))
# The output of the first runs is irrelevant
log "Second pdflatex run"
pdflatex -interaction=nonstopmode -halt-on-error "$file.tex" >> $log || error
log "Third pdflatex run"
pdflatex -interaction=nonstopmode -halt-on-error "$file.tex" >> $log || error
log "Fourth pdflatex run"
pdflatex -interaction=nonstopmode -halt-on-error "$file.tex" >> $log || error
log "Last pdflatex run"
pdflatex -interaction=nonstopmode -halt-on-error "$file.tex" | tee -a $log || error

####################
# End of Main Part #
####################




# copy all rendered pdfs to save them
find . -name '*.pdf' -exec cp --parents -f '{}' "$OLDPWD" \;
# save the log
cp -f $log "$OLDPWD"
# Go back up
cd "$OLDPWD"

# Delete tmp-render as it contains tons of files we don’t want.
rm -rf "$tmpdir"

