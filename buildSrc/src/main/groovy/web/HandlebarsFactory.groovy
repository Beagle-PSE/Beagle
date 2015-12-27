import com.github.jknack.handlebars.Handlebars
import  com.github.jknack.handlebars.io.FileTemplateLoader
	 	
public class HandlebarsFactory {
	/**
	 * Helper class can’t be instanciated
	 */
	 private HandlebarsFactory(){}
	 
	 private static final Map<BeagleWebContext, Handlebars> INSTANCES = [:]
	 private static File partialsFolder
	 private static String partialsExtension
	 
	 public static Handlebars getFor(final BeagleWebContext context) {
	 	if (INSTANCES.containsKey(context)) {
	 		return INSTANCES[context]
	 	}
	 	final FileTemplateLoader partialsLoader = new FileTemplateLoader(partialsFolder, '.' + partialsExtension)
	 	final Handlebars handlebars = new Handlebars(partialsLoader)
		handlebars.registerHelpers(new BeagleHelpers(context))
		
	 	INSTANCES[context] = handlebars
	 	return handlebars
	 }
}