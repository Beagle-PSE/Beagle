import org.gradle.api.file.FileCopyDetails
import org.gradle.api.Action


public class CompileWebFilesAction implements Action<FileCopyDetails> {
	/*
	 * File extension of files that will be converted from markdown to html
	 */
	private static final String MARKDOWN_CONVERSION_EXTENSION = 'htmd'
	private static final String MARKDOWN_CONVERSION_EXTENSION_REGEX = /^(.*)\.$MARKDOWN_CONVERSION_EXTENSION$/

	private final Map<String, String> rootTemplates = [:]
	private final defaultRootTemplate = '{{{content}}}'	
	
	public void execute(final FileCopyDetails copyDetails) {
		// set up the context and helpers
		final BeagleWebContext context = new BeagleWebContext()
		
		copyDetails.filter CompileHandlebarsFilter.class, context: context
		
		if (copyDetails.sourceName ==~ MARKDOWN_CONVERSION_EXTENSION_REGEX) {
			copyDetails.filter CompileMarkdownFilter.class
			copyDetails.name = copyDetails.sourceName.replaceFirst MARKDOWN_CONVERSION_EXTENSION_REGEX, '$1.html'
		}
		
		final String rootTemplate = getRootTemplate copyDetails
		copyDetails.filter ApplyRootTemplateFilter.class, context: context, rootTemplateText: rootTemplate
	}
			
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
