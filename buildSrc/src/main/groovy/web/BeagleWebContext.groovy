import org.gradle.api.Project
import org.gradle.api.Task

/**
 * The context (data object) that will be passed to all Beagle handlebars templates.
 * Each page has its own context instance. The context has properties derived from the
 * Beagle gradle project, stored in {@link #project}. This project must be set before
 * creating an instance of this class.
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
	 * Paths to artefacts available to the templates.
	 */
	Object paths = new Object() {
		/** 
		 * Path to the Requirements Specification PDF.
		 */
		String srs = relative task('copyWebArtefacts').property('srsDest')
		/**
		 * Path to the javadoc folder.
		 */
		String javadoc = relative task('copyWebArtefacts').property('javadocDest')
		/**
		 * Path to to checkstyle report html file.
		 */
		String checkstyle = relative task('copyWebArtefacts').property('checkstyleDest')
		/**
		 * Path to the test code coverage html file.
		 */
		String coverage = relative task('copyWebArtefacts').property('jacocoDest')
	}
	
	/**
	 * Returns one task on {@link #project} named {@code name}. If there are multiple such tasks,
	 * one is picked. It is neithor defined nor reliable which one will be picked.
	 *
	 * @param name	The name of the desired task.
	 * @return	A task of {@link #project} named {@code name} or {@code null} if there is no 
	 *			such task.
	 */
	private static Task task(final String name) {
		Set<Task> allTasks = project.getTasksByName name, true
		return allTasks.iterator()[0]
	}
	
	/** 
	 * Makes a given path relative to the web output folder.
	 *
	 * @param path	A path (anything accepte by Gradles Project#files).
	 * @return	A path to {@code path} relative to Beagle’s {@code web} task’s {@code dest} property.
	 */
	private static String relative(final Object path) {
		URI base = project.file(task('web').property('dest')).toURI()
		return base.relativize(project.file(path).toURI())
	}
}
