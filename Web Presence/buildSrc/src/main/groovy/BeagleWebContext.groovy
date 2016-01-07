import org.gradle.api.Project
import org.gradle.api.Task
import java.nio.file.Path

/**
 * The context (data object) that will be passed to all Beagle handlebars templates.
 * Each page has its own context instance. The context has properties derived from the
 * Beagle Web Presence gradle project, stored in {@link #project}. This project must be
 * set before creating an instance of this class.
 *
 * @author Joshua Gleitze
 */
public class BeagleWebContext {
	private static Project project
	
	
	
	/**
	 * All available System Environment Variables.
	 */
	Map<String, String> ENV = System.getenv()
	
	/**
	 * The page’s title. Usually set through {@link BeagleHelpers#setTitle(String)}.
	 */
	String title = ''
	/**
	 * The page’s content. Used when applying templates that wrap existing content, like by 
	 * {@link ApplyRootTemplateFilter}.
	 */
	String content = ''
	/**
	 * The page’s source file’s name.
	 */
	String fileName = ''
	/**
	 * The page’s source file’s path relative to the web index.
	 */
	String filePath = ''
	
	/**
	 * All Beagle subprojects that are java projects.
	 */
	Collection<Project> javaProjects = project.rootProject.javaSubprojects

	/** 
	 * Paths to artefacts available to the templates.
	 */
	Object paths = new Object() {
		/** 
		 * Path to the Requirements Specification PDF.
		 */
		String srs = relative project.copyWebArtefacts.srsDest
		/** 
		 * Path to the Design Specification PDF.
		 */
		String design = relative project.copyWebArtefacts.designDest
		/**
		 * Path to the javadoc folder.
		 */
		String javadoc = relative project.copyWebArtefacts.javadocDest
		/**
		 * Path to the checkstyle report html file.
		 */
		String checkstyle = relative project.copyWebArtefacts.checkstyleDest
		/**
		 * Path to the test code coverage html file.
		 */
		String coverage = relative project.copyWebArtefacts.jacocoDest
		/**
		 * Path to the css folder
		 */
		String css = relative project.copyWebAssets.cssdest

		/**
		 * Path to the font folders.
		 */
		Object fonts = new Object() {
			/**
			 * Font awesome, as downloaded from https://fortawesome.github.io/Font-Awesome/
			 */
			String fontAwesome = relative project.copyWebFonts.fontawesomedest
			/**
			 * Computer Modern Serif, as downloaded from http://checkmyworking.com/cm-web-fonts/
			 */
			String cmSerif = relative project.copyWebFonts.cmserifdest
			/**
			 * Computer Modern Sans, as downloaded from http://checkmyworking.com/cm-web-fonts/
			 */
			String cmSans = relative project.copyWebFonts.cmsansdest
			/**
			 * Computer Modern Typewriter, as downloaded from http://checkmyworking.com/cm-web-fonts/
			 */
			String cmTypewriter = relative project.copyWebFonts.cmtypedest
		}
	}
	
	/** 
	 * Makes a given path relative to the web output folder.
	 *
	 * @param path	A path (anything accepted by Gradle’s Project#files).
	 * @return	A path to {@code path} relative to Beagle’s {@code web} task’s {@code dest} property.
	 */
	private static String relative(final Object path) {
		Path base = project.file(project.web.dest).toPath()
		return base.relativize(project.file(path).toPath())
	}
}
