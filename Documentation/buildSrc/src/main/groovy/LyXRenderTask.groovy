import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.file.FileCollection
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
 * The tasks copies the {@link #renderSourceFiles} into {@code $project.buildDir/tmp/lyxRender/$name}
 * and executes all commands in there. It is not removed after the task ran and can be used to analyse failures.
 * 
 * @author Joshua Gleitze
 */
public class LyXRenderTask extends DefaultTask {

	/**
	 * The locations that will be searched for a LyX installation on Windows
	 */
	static final String[] LYX_LOCATIONS =  [/C:\Program Files (x86)\LyX 2.1\bin\lyx.exe/,
											/G:\Programme\LyX 2.1\bin\lyx.exe/]
	private static final OS = System.getProperty('os.name').toLowerCase().split()[0]
	private static final LYX = getLyX()
	
	/**
	 * How often the document will be rendered at least.
	 */
	static final int LATEX_RENDER_COUNT = 3

	/**
	 * Relative path to the document to be rendered. Must be contained in the document folder.
	 */
	String document	
	/**
	 * All files needed to render the document.
	 */
	@InputFiles
	FileCollection renderSourceFiles
	/**
	 * Whether to render a bibliography
	 */
	boolean bibliography
	/**
	 * Whether to render a glossary
	 */
	boolean glossary
	/**
	 * The file the rendered document will be written to.
	 */
	@OutputFile
	File dest
	/**
	 * The file the log will be written to.
	 */
	File logdest = project.file("$project.buildDir/reports/lyxRender/${getName()}.log")

	
    @TaskAction
    def render() {
    	logdest.parentFile.mkdirs()
    	logdest.write('')
    	logdest.createNewFile()
		logging.captureStandardOutput LogLevel.INFO
		
    	def tmp = project.file("$project.buildDir/tmp/lyxRender/$name")
    	tmp.deleteDir()
    	tmp.mkdirs()
    	
    	assert renderSourceFiles != null : "Please specify the document folder!"
    	assert document != null : "Please specify the relative path to the document!"
    	
    	project.copy {
    		from renderSourceFiles
    		into tmp
    	}

    	def documentfile = new File(tmp, "${document}.lyx")
    	assert documentfile.exists() : "The document must exist within the source files!"
    	def renderpwd = documentfile.parentFile
    	def documentname = documentfile.name[0..-5]
    	
    	def defaults = {
    		it.workingDir renderpwd
    		it.standardOutput = tee()
    	}
    	
    	def pdflatex = {
			defaults delegate
    		
			executable 'pdflatex'
			args '-interaction=nonstopmode', '-halt-on-error',  "${documentname}.tex"
    	}
    	
    	log "rendering with LyX"
    	project.exec {
			defaults delegate
    		
			executable LYX
			args '--export', 'pdflatex', "${documentname}.lyx"
    	}
    	
    	if (glossary) {
	    	log "pdflatex run before generating the glossary"
	    	project.exec pdflatex
	    	
	    	log "generating the glossary"
	    	project.exec {
   				defaults delegate
   				
	    		executable 'makeglossaries'
	    		args documentname
	    	}
	    }
		    
    	if (bibliography) {
	    	log "pdflatex run before generating the bibiography"
	    	project.exec pdflatex
	    	
	    	log "generating the bibliography"
	    	project.exec {
   				defaults delegate
   				
	    		executable 'bibtex'
	    		args documentname
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
	 * Sets the lyx document to be rendered. 
	 *
	 * @param relativeDocumentPath	The document that shall be rendered. A relative path to the same base as
	 *								the files specified in {@link #from}. The .lyx extension may be ommited.
	 */
	def document(String relativeDocumentPath) {
		this.document = relativeDocumentPath ==~ /^.*\.lyx$/ ? relativeDocumentPath[0..-5] : relativeDocumentPath
		dest = dest ?: project.file("$project.buildDir/docs/${getDestFileName()}")
	}
	
	/** 
	 * The rendering working files. Must contain all files referenced by the rendered document as well as the
	 * rendered document itself.
	 *
	 * @param renderFiles	Any path accepted by gradle’s {@code files} method.
	 */
	def from(Object... renderFiles) {
		this.renderSourceFiles = project.files(renderFiles)
	}
	
	/**
	 * Sets the exact file to render the document to. You’ll usually want to add the .pdf extension.
	 * If the input document is named $name.lyx, this defaults to $project.buildDir/docs/$name.pdf
	 *
	 * @param to	The file to render the document to. Anything accepted by gradle’s {@code file} method.
	 */
	def to(Object to) {
		this.dest = project.file(to)
	}
	
	/**
	 * Sets the folder to render the document to. If the input document is named $name.lyx, this 
	 * the created document will be named $name.pdf. Defaults to $project.buildDir/docs.
	  */
	def into(Object into) {
		this.dest = new File(project.file(into), getDestFileName())
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
	
	/**
	 * Get the path to the lyx executable on this system.
	 */
	private static String getLyX() {
		if (OS == 'windows') {
			for (String loc : LYX_LOCATIONS) {
				if (new File(loc).exists()) {
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
	
	/**
	 * Decorates and logs the provided message to a stream returned by {@link #tee()}
	 *
	 * @param message	The message to log.
	 */
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
	
	/**
	 * Gets an output stream suitable to log to. If the use wants to log to a file, writing to the returned
	 * stream will log both to System.out and the desired log file.
	 */
	def tee() {
		logdest == null ? System.out : new TeeOutputStream(new FileOutputStream(logdest, true), System.out)
	}
	
	private String getDestFileName() {
		assert this.document != null : "The source document must be set at this point!"
		return "${project.file(document).name}.pdf"
	}
}