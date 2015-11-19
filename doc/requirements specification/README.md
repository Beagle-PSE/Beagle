# System Requirements Specification
This folder contains the System Requirements Specification’s source files.

## Installation
The specification is written in [LyX](http://www.lyx.org/). So obviously you'll need to install LyX and LaTeX.

### Install the SDQ template

  * Download the [SDQ thesis template](https://sdqweb.ipd.kit.edu/wiki/File:Ausarbeitungs-Vorlage_SDQ_2014.zip) and install it according to your LaTeX distribution.
    * On Ubuntu: 
      * copy the unzipped folder’s _contents_ to `/usr/share/texmf/tex/latex/sdqthesis`. For example: 
       ```
       sudo cp -r Ausarbeitungs-Vorlage_SDQ_2014 /usr/share/texmf/tex/latex/sdqthesis`)
       ```
      * run `sudo texhash`
    * You may remove the files `thesis.tex` and `thesis.bib` as well as the subfolder `sections`.
    
  * Install the LyX layout file from [doc/dependencies/sqthesis.layout](../dependencies/sdqthesis.layout).
    * On Ubuntu:
      * copy the file to `~/.lyx/layouts`
      * Open LyX and run `Tools -> Reconfigure`
      * Restart LyX to use the template

### Install required packages
A (small!) subset of the required LaTeX packages are:

  * KOMAscript book (`scrbook`)
  * Default packages like `inputenc`, `fontenc`, etc.
  * Linux Libertine Font (`libertine` and `newtxmath`)
  * Adobe Source Sans Pro Font (`sourcesanspro`)
  * Bera Mono Font (`beramono`)
  * Microtype (`microtype`)
  * Enumitem (`enumitem`)
  
If you care for the full list, look it up yourself in `sdqthesis.cls`.
  
On Ubuntu, all required packages can be installed by installing the`texlive-latex-base`, `texlive-latex-recommended`, `texlive-latex-extra` and `texlive-fonts-extra` packages (you'll likely have them installed already):
```
sudo apt-get install texlive-latex-base texlive-latex-recommended texlive-latex-extra texlive-fonts-extra
```

On Windows, you’ll likely use MiKTeX, which will install automatically all required packages for you.