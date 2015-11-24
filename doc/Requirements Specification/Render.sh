#!/bin/bash

file="Requirements Specification"

# fail the script if any command fails
set -e 

# Copy everything to a new folder that we can savely delete
tmpdir=`mktemp -d`
cp -r * "$tmpdir"
cd "$tmpdir"

# generate .pdf from .uxf (Must happen before LyX runs!)
find . -name '*.uxf' -exec /opt/Umlet/umlet.sh -action=convert -format=pdf -filename='{}' \;

# generate .tex
lyx -e pdflatex "$file.lyx"

# generate all the other stuff
pdflatex -halt-on-error "$file.tex"
# make the glossary
makeglossaries "$file"
bibtex "$file"
# render the pdf (we need at least two times. And you know… just to be sure ;))
pdflatex -halt-on-error "$file.tex"
pdflatex -halt-on-error "$file.tex"
pdflatex -halt-on-error "$file.tex"
pdflatex -halt-on-error "$file.tex"

# copy all rendered pdfs to save them
find . -name '*.pdf' -exec cp --parents -f '{}' "$OLDPWD" \;

# Go back up
cd "$OLDPWD"

# Delete tmp-render as it contains tons of files we don’t want.
rm -rf "$tmpdir"

