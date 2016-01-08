import org.gradle.api.Project
import org.apache.commons.io.output.ByteArrayOutputStream 
import org.gradle.api.logging.LogLevel
import org.gradle.api.file.FileCollection
import static groovyx.gpars.GParsPool.withPool

/**
 * DSL extenion to renders an UMLET file. The rendered file will be called {@code $inputFile.pdf} and be created 
 * in the same folder as $inputFile.
 * 
 * @author Joshua Gleitze
 */
public class UmletRenderer {
	/**
	 * Locations to search for UMLET on Windows
	 */
	String[] UMLET_LOCATIONS = [/C:\Program Files (x86)\Umlet\\umlet.jar/,
								/C:\Program Files\Umlet\\umlet.jar/,
								/C:\weitere Programme\Umlet\\umlet.jar/]
	private Project project
	
	/** 
	 * Creates the DSL extension.
	 *
	 * @param project	The gradle project to use.
	 */
	public UmletRenderer(Project project) {
		this.project = project
	}
	
	/**
	 * Renders {@code uxfFile}.
	 *
	 * @param uxfFile	The umlet file to render.
	 */
	public void render(File uxfFile) {
		assert uxfFile ==~ /^.*\.uxf$/ : "The input file must be an uxf file!"
		
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
	
	/**
	 * Renders {@code uxfFile}. Rendering is executed in parallel by a thread pool.
	 *
	 * @param uxfFiles	The umlet files to render.
	 */
	public void render(FileCollection uxfFiles) {
		 withPool(8) {
		 	uxfFiles.eachParallel {
		 		render it
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
}