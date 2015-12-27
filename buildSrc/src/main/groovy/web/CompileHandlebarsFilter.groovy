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
		final Handlebars handlebars = HandlebarsFactory.getFor this.context
		
		final Template template = handlebars.compileInline this.inputString 
		return template.apply(this.context) 
	}
}