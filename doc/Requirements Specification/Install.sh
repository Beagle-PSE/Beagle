# Script used by Travis to install all required LaTeX and LyX assets.
# You can use this as a reference when setting up your own system.

# fail the script if any command fails
set -e 

# Install dependency packages

sudo apt-get install texlive-latex-base texlive-latex-recommended texlive-latex-extra texlive-fonts-extra texlive-lang-german

# Copy the lyx layout file to the private lyx layouts folder
mkdir -p ~/.lyx/layouts
cp ../Dependencies/sdqthesis.layout ~/.lyx/layouts

# Download the sdqthesis template, unzip it, copy the important content into the local texmf, run texhash
texmf="/usr/share/texmf/tex/latex/sdqthesis"
sudo mkdir -p $texmf

sdqthesiszip="https://sdqweb.ipd.kit.edu/mediawiki-sdq-extern/images/7/76/Ausarbeitungs-Vorlage_SDQ_2014.zip"

tmpdir=`mktemp -d`
zipname="$tmpdir/sdqthesis.zip"

wget -qO- -O $zipname $sdqthesiszip && unzip $zipname -d $tmpdir
sudo cp -r $tmpdir/logos $tmpdir/sdqthesis.cls $tmpdir/title-background.pdf $tmpdir/title-background.eps $texmfhome
rm -rf $tmpdir

sudo texhash