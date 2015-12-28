import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.io.FileTemplateLoader
	 	
public class HandlebarsRenderer {
	 private static final Map<BeagleWebContext, HandlebarsRenderer> INSTANCES = [:]
	 private static File partialsFolder
	 private static String partialsExtension

	 private final BeagleWebContext context
	 private final Handlebars handlebars
	 
	 public static HandlebarsRenderer getFor(final BeagleWebContext context) {
	 	if (INSTANCES.containsKey(context)) {
	 		return INSTANCES[context]
	 	}
	 	final HandlebarsRenderer compiler = new HandlebarsRenderer(context)
	 	INSTANCES[context] = compiler
	 	return compiler
	 }
	 
	 private HandlebarsRenderer(final BeagleWebContext context) {
	 	final FileTemplateLoader partialsLoader = new FileTemplateLoader(partialsFolder, '.' + partialsExtension)
	 	this.context = context
	 	this.handlebars = new Handlebars(partialsLoader)
		this.handlebars.registerHelpers(new BeagleHelpers(context))
	 }
	 
	 public String render(String template) {
	 	return this.handlebars.compileInline(template).apply(context)
	 }
}