import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.io.FileTemplateLoader
	 
/**
 * Handles rendering of handlebars template strings using the context provided. For each context,
 * an instance can be obtained by {@link #getFor(BeagleWebContext)}. The instance is guaranteed to 
 * save all data seen by handlebars over multiple calls to its {@link #render(String)} method.
 *
 * @author Joshua Gleitze
 */
public class HandlebarsRenderer {
	 private static final Map<BeagleWebContext, HandlebarsRenderer> INSTANCES = [:]
	 private static File partialsFolder
	 private static String partialsExtension

	 private final BeagleWebContext context
	 private final Handlebars handlebars
	 
	 /**
	  * Obtains an instance to render {@code context}. Multiple calls with an equal instance will
	  * lead to the same instance, such that instances of HandlebarsRenderer do not need to be saved
	  * by clients.
	  *
	  * @param context	The context that shall be used when rendering templates with the returned renderer.
	  * @return	The renderer responsible to render {@code context}.
	  */
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
	 
	 /**
	  * Renders the provided {@code template}.
	  *
	  * @param template	A handlebars template.
	  * @return	The rendered {@code template}.
	  */
	 public String render(final String template) {
	 	return this.handlebars.compileInline(template).apply(context)
	 }
}