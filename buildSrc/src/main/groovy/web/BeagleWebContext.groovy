import org.gradle.api.Project
import org.gradle.api.Task

public class BeagleWebContext {
	private static Project project
	
	Map<String, String> ENV = System.getenv()
	
	String title = ''
	String content = ''
	
	Object paths = new Object() {
		String srs = relative task('copyWebArtefacts').property('srsDest')
		String javadoc = relative task('copyWebArtefacts').property('javadocDest')
		String checkstyle = relative task('copyWebArtefacts').property('checkstyleDest')
		String coverage = relative task('copyWebArtefacts').property('jacocoDest')
	}
	
	private static Task task(final String name) {
		Set<Task> allTasks = project.getTasksByName name, true
		return allTasks.iterator()[0]
	}
	
	private static String relative(final Object path) {
		URI base = project.file(task('web').property('dest')).toURI()
		return base.relativize(project.file(path).toURI())
	}
}
