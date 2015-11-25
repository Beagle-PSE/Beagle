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
find . -name '*.uxf' -exec /opt/Umlet/umlet.sh -action=convert -format=pdf -filename='{}' \;


# generate .tex
log "Rendering with LyX"
lyx -e pdflatex "$file.lyx" | tee -a $log || error

# generate all the other stuff
log "First pdflatex run"
pdflatex -halt-on-error "$file.tex" >> $log || error

# make the glossary
log "Generating the glossary"
makeglossaries "$file" | tee -a $log || error

# make the biliography
log "Generating the bibliography"
bibtex "$file" | tee -a $log || error

# render the pdf (we need at least two times. And you know… just to be sure ;))
# The output of the first runs is irrelevant
log "Second pdflatex run"
pdflatex -halt-on-error "$file.tex" >> $log || error
log "Third pdflatex run"
pdflatex -halt-on-error "$file.tex" >> $log || error
log "Fourth pdflatex run"
pdflatex -halt-on-error "$file.tex" >> $log || error
log "Last pdflatex run"
pdflatex -halt-on-error "$file.tex" | tee -a $log || error

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

