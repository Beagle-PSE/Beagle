import org.gradle.api.Plugin
import org.gradle.api.Project
import org.apache.ivy.util.url.ApacheURLLister
import de.undercouch.gradle.tasks.download.DownloadTaskPlugin
import static groovyx.gpars.GParsPool.withPool

/**
 * Tasks downloading Eclipse Update Sites and making them available to wuff.
 *
 *	@author Joshua Gleitze
 */
public class DownloadUpdateSites implements Plugin<Project> {
	/**
	 * Stores the sites to download, mapped to the folders to put them to.
	 */
	def List<UpdateSite> sites = new ArrayList<>()
	
	/**
	 * The configured project
	 */
	def Project project
	
	/**
	 * Applies UpdateSitesConfigurationExtension to the project
	 */
	void apply(Project project) {
		project.extensions.create "updatesites", UpdateSitesConfigurationExtension, this
		this.project = project
		project.afterEvaluate {
			downloadSites()
			tellWuff()
		}
	}
	
	/**
	 * Starts the download
	 */
    def downloadSites() {
		project.apply plugin: DownloadTaskPlugin
		
		withPool(8) {
			this.sites.eachParallel { updateSite ->
				
				if (!updateSite.checksumFile.exists() || !updateSite.destinationFolder.isDirectory()) {
					download updateSite.url, updateSite.destinationFolder
					
					// unpuzzle’s dummy checksum
					updateSite.checksumFile.text = 'deadbea1'
				}
				
			}
		}
    }
    
    /**
     * Tells wuff about the sites
     */
    def tellWuff() {
    	this.sites.each { updateSite ->
    		String destinationDir = updateSite.destinationFolder.absolutePath.replace('\\', '/')
    		addToWuff("file://$destinationDir")
		}
	}
	
	/**
	 * Adds an url to wuff’s sources
	 */
	def addToWuff(String url) {
		project.wuff {
			eclipseVersion(selectedEclipseVersion) {
				sources {
					source url
				}
			}
		}
	}
    
    /**
     * Adds {@code url} to the sites to be downloaded. 
     */
    private void add(String url) {
    	// create a folder name out of the URL
		def dirName = url.replaceAll("[^a-zA-Z0-9.-]", "_")
		def wuffDir = project.wuff.wuffDir ?: System.getProperty('user.home') + '/.wuff'
		
		// imitate unpuzzle’s checksum mechanism
		def checksumFile = project.file("$wuffDir/downloaded-checksums/${dirName}.md5")
		checksumFile.parentFile.mkdirs()
		def destinationDir = project.file("$wuffDir/unpacked/$dirName")
		
		// queue the site to be downloaded
		this.sites.add new UpdateSite(url: new URL(url), destinationFolder: destinationDir, checksumFile: checksumFile)
	}
	
	/**
	 * Update Site data object
	 */
	private class UpdateSite {
		/**
		 * The site’s URL.
		 */
		private URL url
		/**
		 * The folder to download the site to.
		 */
		private File destinationFolder
		/**
		 * The file to write a dummy checksum to to mark the site as downloaded.
		 */
		private File checksumFile
	}

	/**
	 * Performs the actual, recursive download.
	 *
	 * @param url The url to be downloaded
	 * @param destination The folder to put the downloaded files in.
	 */
	private void download(URL url, File destination) {
		def lister = new ApacheURLLister()
		destination.mkdirs()
		project.download {
			src lister.listFiles(url)
			dest destination
		}
		for (folder in lister.listDirectories(url)) {
			def destPart = url.toURI().relativize(folder.toURI()).toString()
			download folder, project.file("$destination/$destPart") 
		}
	}
}

/**
 * The configuration object passed to projects.
 */
public class UpdateSitesConfigurationExtension {

	/**
	 * Reference to the plugin instance.
	 */
	private DownloadUpdateSites pluginInstance
	

	public UpdateSitesConfigurationExtension(DownloadUpdateSites pluginInstance) {
		this.pluginInstance = pluginInstance
	}
	
	/**
	 * Adds an update site to be downloaded and added to wuff.
	 */
	def from(String url) {
		pluginInstance.add url
	}
	
	/**
	 * Convenience method to add a zip to wuff
	 */
	def zip(String url) {
		pluginInstance.addToWuff url
	}
	
}
	