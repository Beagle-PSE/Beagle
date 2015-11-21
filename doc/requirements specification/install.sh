# Script used by Travis to install all required LaTeX and LyX assets.
# You can use this as a reference when setting up your own system.

# fail the script if any command fails
set -e 

# Copy the lyx layout file to the private lyx layouts folder
mkdir -p ~/.lyx/layouts
cp ../dependencies/sdqthesis.layout ~/.lyx/layouts

# Download the sdqthesis template, unzip it, copy the important content into the local texmf, run texhash
texmfhome="$(kpsewhich -var-value=TEXMFHOME)"
mkdir -p $texmfhome

sdqthesiszip="https://sdqweb.ipd.kit.edu/mediawiki-sdq-extern/images/7/76/Ausarbeitungs-Vorlage_SDQ_2014.zip"

tmpdir=`mktemp -d`
zipname="$tmpdir/sdqthesis.zip"

wget -qO- -O $zipname $sdqthesiszip && unzip $zipname -d $tmpdir
cp -r $tmpdir/logos $tmpdir/sdqthesis.cls $tmpdir/title-background.pdf $tmpdir/title-background.eps $texmfhome
rm -rf $tmpdir

texhash $texmfhome