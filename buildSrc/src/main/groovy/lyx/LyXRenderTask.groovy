import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.logging.LogLevel
import org.apache.commons.io.output.TeeOutputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter


/**
 * Tasks that renders a LyX document using {@code lyx} and {@code pdflatex} from the command line.
 * These tools, all LaTeX packages used by the document, {@code bibtex} (if using {@link #bibliography})
 * and {@code makeglossaries} (if using {@link #glossary}) must be available from the command line.
 * Add paths to {@link #LYX_LOCATIONS} to add paths to search for lyx installations on windows.
 *
 * The tasks copies the {@link #documentFolder} into {@code $project.buildDir/tmp/lyxRender/$\{getName()\}}
 * and executes all commands in there. It is not removed after the task ran and can be used to analyse failures.
 * 
 * @author Joshua Gleitze
 */
public class LyXRenderTask extends DefaultTask {

	static final String[] LYX_LOCATIONS =  [/C:\Program Files (x86)\LyX 2.1\bin\lyx.exe/,
											/G:\Programme\LyX 2.1\bin\lyx.exe/]
	private static final OS = System.getProperty('os.name').toLowerCase().split()[0]
	private static final LYX = getLyX()
	
	/**
	 * How often the document will be rendered at least.
	 */
	static final int LATEX_RENDER_COUNT = 3

	String document	
	@InputDirectory
	File documentFolder
	boolean bibliography
	boolean glossary
	@OutputFile
	File dest
	File logdest = project.file("$project.buildDir/reports/lyxRender/${getName()}.log")

	
    @TaskAction
    def render() {
    	logdest.parentFile.mkdirs()
    	logdest.write('')
    	logdest.createNewFile()
		logging.captureStandardOutput LogLevel.INFO
		
    	def tmp = project.file("$project.buildDir/tmp/lyxRender/${getName()}")
    	tmp.deleteDir()
    	tmp.mkdirs()
    	
    	assert documentFolder != null : "Please specify the document folder!"
    	assert document != null : "Please specify the relative path to the document!"
    	assert documentFolder?.exists() : "The document folder must exist!"
    	assert new File(documentFolder, "${document}.lyx").exists() : "The document must exist within the document folder!"
    	
    	def defaults = {
    		it.workingDir tmp
    		it.standardOutput = tee()
    	}
    	
    	def pdflatex = {
			defaults delegate
    		
			executable 'pdflatex'
			args '-interaction=nonstopmode', '-halt-on-error',  "${document}.tex"
    	}
    	
    	project.copy {
    		from documentFolder
    		into tmp
    	}
    	
    	log "rendering with LyX"
    	project.exec {
			defaults delegate
    		
			executable LYX
			args '--export', 'pdflatex', "${document}.lyx"
    	}
    	
    	if (glossary || bibliography) {
	    	log "pdflatex run before calling auxilary tools"
	    	project.exec pdflatex
	    	
	    	if (glossary) {
		    	log "generating the glossary"
		    	project.exec {
    				defaults delegate
    				
		    		executable 'makeglossaries'
		    		args document
		    	}
		    }
		    
	    	if (bibliography) {
		    	log "generating the bibliography"
		    	project.exec {
    				defaults delegate
    				
		    		executable 'bibtex'
		    		args document
		    	}
		    }
		}
		
		LATEX_RENDER_COUNT.times {
			log "${it + 1}. pdflatex render run"
			project.exec pdflatex
		}
		
		project.copy {
			from new File(tmp, "${document}.pdf")
			rename { dest.name }
			into dest.parentFile
		}
    }
	
	/**
	 * Sets the lyx document to be rendered. The .lyx extension may be ommited.
	 *
	 * @param relativeDocumentPath	The document that shall be rendered. A path specification relative to {@link #from} is expected.
	 */
	def document(String relativeDocumentPath) {
		this.document = relativeDocumentPath ==~ /^.*\.lyx$/ ? relativeDocumentPath[0..-5] : relativeDocumentPath
		dest = dest ?: project.file("$project.buildDir/docs/${project.file(document).name}.pdf")
	}
	
	/** 
	 * The rendering working directory. It must contain all files referenced by the rendered document as well as the
	 * rendered document itself.
	 *
	 * @param documentFolder	Any path accepted by gradle’s file method to the documentFolder.
	 */
	def from(Object documentFolder) {
		this.documentFolder = project.file(documentFolder)
	}
	
	/**
	 * Sets the exact file to render the document to. You’ll usually want to add the .pdf extension.
	 * If the input document is named $name.lyx, this defaults to $project.buildDir/docs/$name.pdf
	 *
	 * @param to	The file to render the document to. Anything accepted by
	def to(Object to) {
		this.dest = project.file(to)
	}
	
	/**
	 * Sets whether the document to be rendered contains a bibliography. (Optional, {@code false} by default)
	 *
	 * @param renderBibliography	{@code true} to render the document’s bibliography using {@code bibtex}.
	 */
	def bibliography(boolean renderBibliography) {
		this.bibliography = renderBibliography
	}
	
	/**
	 * Sets whether the document to be rendered contains a glossary. (Optional, {@code false} by default)
	 *
	 * @param renderBibliography	{@code true} to render the document’s glossary using {@code makeglossaries}.
	 */
	def glossary(boolean renderGlossary) {
		this.glossary = renderGlossary
	}
	
	/**
	 * Sets where to write the render log file to. (Optional, $project.buildDir/reports/lyxRender/${getName()}.log by default)
	 *
	 * @param logdest	The file to write the log to. Provide {@code null} to not log to a file at all.
	 */
	def logdest(File logdest) {
		this.logdest = logdest
	}
	
	private static String getLyX() {
		if (OS == 'windows') {
			for (String loc : LYX_LOCATIONS) {
				if (file(loc).exists()) {
					return loc
				}
			}
			throw new RuntimeException("We can not find a LyX installation on your machine. Please install it.\n"
				 + "If you’ve already installed it, please add the path to your installation folder to LyXRenderTasks#LYX_LOCATIONS!")
		} else {
			// UNIX systems will have it added to their $PATH
			return "lyx"
		}
	}
	
	def log(String message) {
		def width = 100
		def sep = '#'
		def boundary = sep.multiply(width)
		def out = new OutputStreamWriter(tee(), "UTF-8")
		def padMessage = message.center(width - 2)
		out.write("\n\n\n$boundary\n$sep$padMessage$sep\n$boundary\n\n")
		out.flush()
		out.close()
	}
	
	def tee() {
		logdest == null ? System.out : new TeeOutputStream(new FileOutputStream(logdest, true), System.out)
	}
}