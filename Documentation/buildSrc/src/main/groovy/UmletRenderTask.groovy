import org.gradle.api.Project
import org.apache.commons.io.output.ByteArrayOutputStream 
import org.gradle.api.logging.LogLevel
import org.gradle.api.file.FileCollection
import static groovyx.gpars.GParsPool.withPool
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.DefaultTask

/**
 * Tasks that renders UMLet documents using {@code umlet} from the command line. Add paths to {@link #UMLET_LOCATIONS}
 * to add paths to search for umlet installations on windows.
 *
 * The tasks copies the {@link #renderSourceFiles} into the destination directory and and executes all commands in there.
 * The rendered PDFs will be found there among the input uxf files
 * 
 * @author Joshua Gleitze
 */
public class UmletRenderTask extends DefaultTask {
	/**
	 * Locations to search for UMLET on Windows
	 */
	String[] UMLET_LOCATIONS = [/C:\Program Files (x86)\Umlet\\umlet.jar/,
								/C:\Program Files\Umlet\\umlet.jar/,
								/C:\weitere Programme\Umlet\\umlet.jar/]
	
	/**
	 * All files to be copied to the rendered location.
	 */
	FileCollection renderSourceFiles
	/**
	 * All uxf files to be rendered
	 */
	@InputFiles
	FileCollection uxfSourceFiles
	/**
	 * The folder to copy all input files to.
	 */
	@OutputDirectory
	File dest
	
	@TaskAction
	def render() {
		assert renderSourceFiles != null : "Please specify the source files!"
		assert dest != null : "Please specify the destination folder!"
		
		dest.deleteDir()
		
		project.copy {
			from renderSourceFiles
			into dest
		}
		
		def uxfFiles = project.fileTree(dest).include('**/*.uxf')
				
		withPool(8) {
			uxfFiles.eachParallel { uxfFile ->
 				def outputRecord = new ByteArrayOutputStream()
		
				project.exec {
					executable 'java'
					args "-jar", UMLET(), "-action=convert", "-format=pdf", "-filename=$uxfFile"
		    		standardOutput = outputRecord
				}
		
				def errorStrings = ['exception', 'error']
				def outputLowerCase = outputRecord.toString().toLowerCase()
				if (errorStrings.find { outputLowerCase.contains(it) } != null) {
					throw new RuntimeException("UMLET conversion failed for $uxfFile\n\nOutput:\n$outputRecord")
				}
		 	}
		}
	}
	
	/**
	 * Returns the path to the UMLET jar on this system.
	 */
	String UMLET() {
		 if (System.getProperty('os.name').toLowerCase().split()[0] == 'windows') {
			for (String loc : UMLET_LOCATIONS) {
				if (project.file(loc).exists()) {
					return loc
				}
			}
			throw new RuntimeException("We can not find a UMLET installation on your machine. Please install it.\n"
				 + "If you’ve already installed it, please add the path to your installation folder in UmletRenderer.groovy!")
		} else {
			return "/opt/Umlet/umlet.jar"
		} 
	}
	
	/** 
	 * The rendering working files. Must contain all uxf files that shall be rendered
	 *
	 * @param renderFiles	Any path accepted by gradle’s {@code files} method.
	 */
	def from(Object... renderFiles) {
		this.renderSourceFiles = project.files(renderFiles)
		this.uxfSourceFiles = this.renderSourceFiles.asFileTree.matching { it.include '**/*.uxf'}
	}
	
	/**
	 * Sets the folder to copy all files to and render all uxf files in.
	 */
	def into(Object into) {
		this.dest = project.file(into)
	}
	
}