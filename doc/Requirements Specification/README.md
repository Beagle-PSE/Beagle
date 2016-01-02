# System Requirements Specification
This folder contains the System Requirements Specification’s source files.

## Installation
Ubuntu users can try to run `gradle srsinstall`, which will execute all installation steps detailed below. Please note that
while the script offers great convenience, it was written to be used on a continuous integration server and might hence make settings that are not optimal for a private computer.
 
The specification is written in [LyX](http://www.lyx.org/). So obviously you'll first need to install LyX and LaTeX.

### Install the SDQ Template

  * Download the [SDQ thesis template](https://sdqweb.ipd.kit.edu/wiki/File:Ausarbeitungs-Vorlage_SDQ_2014.zip) and install it according to your LaTeX distribution.
    * On Ubuntu: 
      * copy the unzipped folder’s _contents_ to `/usr/share/texmf/tex/latex/sdqthesis`. For example: 
       ```
       sudo cp -r Ausarbeitungs-Vorlage_SDQ_2014 /usr/share/texmf/tex/latex/sdqthesis`)
       ```
      * run `sudo texhash`
      
    * On Windows (with MiKTeX):
      * copy the unzipped folder’s _contents_ to `C:\Program Files (x86)\MiKTeX 2.9\tex\latex\misc\sdqthesis`.
      * Launch "MiKTeX Settings" and hit "Refresh FNDB" 
      
    * You may remove the files `thesis.tex` and `thesis.bib` as well as the subfolder `sections`.
    
    
  * Install the LyX layout file from [doc/Dependencies/sqthesis.layout](../Dependencies/sdqthesis.layout).
      * copy the file to your personal layout folder (Ubuntu: `~/.lyx/layouts`, Windows: `C:\Users\[User]\AppData\Roaming\LyX2.1\layouts`
      * Open LyX and run `Tools -> Reconfigure`
      * Restart LyX to use the template
	  
	

### Install Required Packages
A (small!) subset of the required LaTeX packages are:

  * KOMAscript book (`scrbook`)
  * Default packages like `inputenc`, `fontenc`, etc.
  * Linux Libertine Font (`libertine` and `newtxmath`)
  * Adobe Source Sans Pro Font (`sourcesanspro`)
  * Bera Mono Font (`beramono`)
  * Microtype (`microtype`)
  * Enumitem (`enumitem`)
  * Break citations (`breakcites`)
  * xindy (`xindy`)
  
If you care for the full list, look it up yourself in `sdqthesis.cls`.

#### Ubuntu
All required packages can be installed by installing the`texlive-latex-base`, `texlive-latex-recommended`, `texlive-latex-extra`, `texlive-fonts-extra`, `xindy`, `texlive-bibtex-extra` and `texlive-lang-german` debian packages (you'll likely have most of them installed already):
```
sudo apt-get install texlive-latex-base texlive-latex-recommended texlive-latex-extra texlive-fonts-extra texlive-lang-german texlive-bibtex-extra xindy
```

#### Windows
On Windows, you’ll likely use MiKTeX, which will install automatically all required packages automatically. However, you need to install Perl: [ActivePerl](http://www.activestate.com/activeperl/downloads). Make sure the checkbox "Add Perl to PATH", which appeares during the installation process, is checked.

## Usage
Most of LyX’s usage is intuitive and should not require explanation. All documents are equiped with tons of notes and comments to make writing as easy as possible. Nevertheless, some points shall be outlined:

### Glossary
To build our glossary, we use the `glossaries` package, which is superior to all other glossary building packages. Unfortunately, it’s not supported by LyX.

#### Documentation 
Please refer to the [excellent glossaries article on WikiBooks](https://en.wikibooks.org/wiki/LaTeX/Glossary) for documentation of the `glossaries` package.

#### Definitions
All definitions of terms and abbreviations are done in the [Parts/Terms and Definitions](Parts/Terms and Definitions.lyx) file.

_Make sure to include any technical or ambiguous term in the definition file!_

#### References 
To include references to defined terms, hit `Ctrl+L` (<=> include LaTeX environment) and type the appropriate (referencing command)(https://en.wikibooks.org/wiki/LaTeX/Glossary#Using_defined_terms).

_Make sure to always reference defined terms!_

### BiBTeX

#### Add a New Bibliography Entry
All Bibliography entries are stored in the file [Requirements Specification.bib](/Requirements Specification.bib). To add an entry, you have multiple options:

 * Just edit the file.
 * Use [JabRef](http://sourceforge.net/projects/jabref/files/jabref/2.11.1/), a GUI tool for BiBTeX
 * Copy BiBTeX Code from sites like [Google Books](https://books.google.de/)
 
 
#### Cite
To cite use LyX: Insert -> Citation

## Building
Please note that the LyX preview builds will not contain the glossary. There seems to be no way to achieve that. To make a true build, run
```
gradle srs
```
in the Beagle project (“top”) directory. The SRS will be rendered to `build/doc/Requirements Specification.pdf`. It will also be built by `gradle build`.