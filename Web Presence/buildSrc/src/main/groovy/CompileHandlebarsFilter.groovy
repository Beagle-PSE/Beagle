import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Template
import com.github.jknack.handlebars.HandlebarsException

/**
 * Filter reader that compiles its input as a handlebars template and applies {@link #context} on it.
 *
 * @author Joshua Gleitze
 */
class CompileHandlebarsFilter extends BeagleWebFilter {
	// will be set from the outside after construction
	private BeagleWebContext context 

	public CompileHandlebarsFilter(final Reader templateReader) {
		super(templateReader)
	}

	@Override
	protected String getResultString() {
		try {
			HandlebarsRenderer.getFor(this.context).render(this.inputString)
	 	} catch (HandlebarsException hbeException) {
	 		System.err.println ""
	 		System.err.println "Render Error:"
	 		System.err.println ""
	 		System.err.println hbeException.message
	 	}
	}
}