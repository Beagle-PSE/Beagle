import org.gradle.api.file.FileCopyDetails
import org.gradle.api.Action

/**
 * Main action responsible to render Beagle’s web presence’s pages.
 * It may be configured by providing root templates (templates that will be wrapped around
 * the page content) for different file types through {@link #addRootTemplateFiles}.
 *
 * @author Joshua Gleitze
 */
public class CompileWebFilesAction implements Action<FileCopyDetails> {
	/**
	 * File extension of files that will be converted from markdown to html
	 */
	private static final String MARKDOWN_CONVERSION_EXTENSION = 'htmd'
	private static final String MARKDOWN_CONVERSION_EXTENSION_REGEX = /^(.*)\.$MARKDOWN_CONVERSION_EXTENSION$/

	private final Map<String, String> rootTemplates = [:]
	private final defaultRootTemplate = '{{{content}}}'	
	
	@Override
	public void execute(final FileCopyDetails copyDetails) {
		// set up the context and helpers
		final BeagleWebContext context = new BeagleWebContext()
		
		copyDetails.filter CompileHandlebarsFilter.class, context: context
		
		if (copyDetails.sourceName ==~ MARKDOWN_CONVERSION_EXTENSION_REGEX) {
			copyDetails.filter RenderMarkdownFilter.class
			copyDetails.name = copyDetails.sourceName.replaceFirst MARKDOWN_CONVERSION_EXTENSION_REGEX, '$1.html'
		}
		
		final String rootTemplate = getRootTemplate copyDetails
		copyDetails.filter ApplyRootTemplateFilter.class, context: context, rootTemplateText: rootTemplate
		
		if (copyDetails.name ==~ /^.*\.html$/) {
			copyDetails.filter HTMLFormatFilter.class
		}
	}
	
	/**
	 * Add root templates (templates that will be wrapped around the page content) that will 
	 * be used for the file extension indicated by the map key. Consecutive calls to the method
	 * for the same file extension will override previous settings.
	 *
	 * @param templateFiles	Mappings from file extensions to the template file that will be used
	 *						to wrap files with this extension. 
	 */ 
	public void addRootTemplateFiles(final Map<String, File> templateFiles) {
		for (String fileExtension : templateFiles.keySet()) {
			 rootTemplates[fileExtension] = templateFiles[fileExtension].text
		}
	}
	
	private String getRootTemplate(final FileCopyDetails copyDetails) {
		final String inputFileExtension = copyDetails.sourceName.tokenize('.').last()
		if (rootTemplates.containsKey(inputFileExtension)) {
			return rootTemplates[inputFileExtension]
		} else {
			return defaultRootTemplate
		}
	}
}
