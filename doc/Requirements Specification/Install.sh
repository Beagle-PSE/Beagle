# Script used by Travis to install all required LaTeX and LyX assets.
# You can use this as a reference when setting up your own system.

# fail the script if any command fails
set -e

# LyX PPA for latest version
sudo add-apt-repository -y ppa:lyx-devel/release
sudo apt-get update
# Install dependency packages
sudo apt-get install -qq lyx texlive-latex-base texlive-latex-recommended texlive-latex-extra texlive-fonts-extra texlive-lang-german xindy

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
sudo cp -r $tmpdir/logos $tmpdir/sdqthesis.cls $tmpdir/title-background.pdf $tmpdir/title-background.eps $texmf

sudo texhash
	
# Export a small lyx file to assert LyX is working and to make LyX read in its config
# Reduces the noice when first calling lyx in the scripts
lyx --export-to pdflatex $tmpdir/test.tex /usr/share/lyx/examples/splash.lyx
rm -rf $tmpdir