# Development setup

Development is done in [Eclipse Mars for RCP and RAP Developers](http://www.eclipse.org/downloads/packages/eclipse-rcp-and-rap-developers/mars1).

## Eclipse plugins

Required Eclipse plugins are:

 * Palladio (Update Site: [https://sdqweb.ipd.kit.edu/eclipse/palladiosimulator/nightly/](https://sdqweb.ipd.kit.edu/eclipse/palladiosimulator/nightly/))
 * [JaMoPP](http://www.jamopp.org/index.php/JaMoPP)
 * SoMoX based on JaMoPP, as it can be checked out (SVN) from [https://svnserver.informatik.kit.edu/i43/svn/code/SoMoX/Core/branches/SoMoXWithJaMoPP/](https://svnserver.informatik.kit.edu/i43/svn/code/SoMoX/Core/branches/SoMoXWithJaMoPP/) (Credentials: `anonymous:anonymous`)
  * import all SoMoX projects into your working space (consider using a Working Set to group them)
  * Close the projects `org.eclipse.gmt.modisco.java.edit`, `org.eclipse.gmt.modisco.omg.kdm.edit`, `org.somox.metrics.dsl.tests` and `org.somox.metrics.tests`
  
## Documentation

Parts of the documentation are written in LaTeX using [LyX](http://www.lyx.org/). Some documents require special classes or layout files, like the [System Requirements Specification](requirements specification/README.md).

Other parts are written in plain [Markdown](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet). The Eclipse [GitHub Flavored Markdown viewer plugin for Eclipse](https://marketplace.eclipse.org/content/github-flavored-markdown-viewer-plugin-eclipse) helps writing it. (_Hint:_ Don’t give the plugin your GitHub credentials. [Create a personal access token](https://github.com/settings/tokens) and change the API URL in the plugin’s settings: `https://api.github.com/?access_token=PERSONAL_ACCESS_TOKEN`)