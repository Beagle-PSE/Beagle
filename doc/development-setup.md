# Development setup

Development is done in [Eclipse Mars for RCP and RAP Developers](http://www.eclipse.org/downloads/packages/eclipse-rcp-and-rap-developers/mars1).

## Eclipse plugins

Required Eclipse plugins are:

 * Palladio (Update Site: [https://sdqweb.ipd.kit.edu/eclipse/palladiosimulator/nightly/](https://sdqweb.ipd.kit.edu/eclipse/palladiosimulator/nightly/))
 * [JaMoPP](http://www.jamopp.org/index.php/JaMoPP)
 * SoMoX based on JaMoPP, as it can be checked out (SVN) from [https://svnserver.informatik.kit.edu/i43/svn/code/SoMoX/Core/branches/SoMoXWithJaMoPP/](https://svnserver.informatik.kit.edu/i43/svn/code/SoMoX/Core/branches/SoMoXWithJaMoPP/) (Credentials: `anonymous:anonymous`)
  * import all SoMoX projects into your working space (consider using a Working Set to group them)
  * Close the projects `org.eclipse.gmt.modisco.java.edit`, `org.eclipse.gmt.modisco.omg.kdm.edit`, `org.somox.metrics.dsl.tests` and `org.somox.metrics.tests`