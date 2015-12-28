import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Template

class CompileHandlebarsFilter extends BeagleWebFilter {
	// will be set from the outside after construction
	private BeagleWebContext context 

	public CompileHandlebarsFilter(final Reader templateReader) {
		super(templateReader)
	}

	@Override
	protected String getResultString() {
		return HandlebarsRenderer.getFor(this.context).render(this.inputString)
	}
}