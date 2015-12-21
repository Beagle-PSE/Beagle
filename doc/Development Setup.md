# Development setup

Development is done in [Eclipse Mars for RCP and RAP Developers](http://www.eclipse.org/downloads/packages/eclipse-rcp-and-rap-developers/mars1).

## Eclipse plugins

Required Eclipse plugins are:

 * Palladio (Update Site: [https://sdqweb.ipd.kit.edu/eclipse/palladiosimulator/nightly/](https://sdqweb.ipd.kit.edu/eclipse/palladiosimulator/nightly/))
 * [JaMoPP](http://www.jamopp.org/index.php/JaMoPP)
 * [Papyrus](https://eclipse.org/papyrus/updates/index.php)
 * SoMoX based on JaMoPP, as it can be checked out (SVN) from [https://svnserver.informatik.kit.edu/i43/svn/code/SoMoX/Core/branches/SoMoXWithJaMoPP/](https://svnserver.informatik.kit.edu/i43/svn/code/SoMoX/Core/branches/SoMoXWithJaMoPP/) (Credentials: `anonymous:anonymous`)
  * import all SoMoX projects into your working space (consider using a Working Set to group them)
  * Close the projects `org.eclipse.gmt.modisco.java.edit`, `org.eclipse.gmt.modisco.omg.kdm.edit`, `org.somox.metrics.dsl.tests` and `org.somox.metrics.tests`
  * [Checkstyle] (http://eclipse-cs.sourceforge.net/#!/)
  
## Building & Dependency Management
The project is built using [Gradle](http://gradle.org/) 2.9. It can be downloaded [here](http://gradle.org/gradle-download/). Ubuntu users can use [this PPA](https://launchpad.net/~cwchien/+archive/ubuntu/gradle).

We use the default commands: `gradle build` (or simply `gradle`) to test and build, `gradle test` to only run tests. Run `gradle tasks` to see all available tasks.
  
## Documentation

### LyX
Parts of the documentation are written in LaTeX using [LyX](http://www.lyx.org/). Some documents require special classes or layout files, like the [System Requirements Specification](Requirements Specification/README.md).

#### Spell checking
You may need to set the language in `Tools -> Settings -> Language Settings -> Language` to `en` to get English spell checking.

### Markdown
Other parts are written in plain [Markdown](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet). The Eclipse [GitHub Flavored Markdown viewer plugin for Eclipse](https://marketplace.eclipse.org/content/github-flavored-markdown-viewer-plugin-eclipse) helps writing it. (_Hint:_ Don’t give the plugin your GitHub credentials. [Create a personal access token](https://github.com/settings/tokens) and change the API URL in the plugin’s settings: `https://api.github.com/?access_token=PERSONAL_ACCESS_TOKEN`)

### UML (Software Requirements Specification)

 * Install the [UMLet standalone](http://www.umlet.com/changes.htm). Extract the zip file and move it to `C:\Program Files (x86)\Umlet\Umlet.exe` (Windows) or `/opt/Umlet/` (Linux). (If you want to put it somewhere else, you have to adopt `Render.bat` or `Render.sh`.) (Required to build your project.)
 * If on Linux: Make `/opt/Umlet/umlet.sh` executable.
 * When you like to use UMLet as eclipse plugin, you can install this as well.
 * Only commit the `.uxf` files. When you want to embed them in LyX, refer to `My File.pdf`, these files will be generated during the build process. Please tick the checkbox "Keep aspect ratio" and insert a maximum width and heigth to keep the LyX preview working.
 
### Fonts (Design)
The UML Diagrams of the design phase use [Adobe Source Sans Pro](https://github.com/adobe-fonts/source-sans-pro) and [Adobe Source Code Pro](https://github.com/adobe-fonts/source-code-pro). Install them to your system as detailed. 

## Continuous Integration
Beagle uses Travis, a continuous integration service. It tests Pull Requests as well as all branches in the repository whenever changes occur.

You can enable Travis for your repository, too. This allows you to have automated tests for all your commits. It further generates all documentation documents for new commits. This is quite handy for pull requests, as you can simply post a link to the rendered file, making the reviewing process a whole lot easier.

To activate Travis:

#### Get GitHub Personal Access Token
 * Go to [your GitHub personal access token settings page](https://github.com/settings/tokens) and create a new token. Only allowing access to the `repo` scope is sufficient.
 * temporary save the token somewhere, e.g. in your clipboard
 
#### Active Travis
 * Go to [Travis](https:///travis-ci.org) and sign in
 * Activate Travis for your fork of Beagle
 * Go to the settings page for Beagle on Travis and create a new Environment Variable called `GH_TOKEN`. Paste the personal access token you created as value. _Leave the “show in log” switch OFF!_
 
Done!
