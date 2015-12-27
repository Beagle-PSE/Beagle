import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Template

class ApplyRootTemplateFilter extends BeagleWebFilter {	
	// these will be set from the outside after construction
	private String rootTemplateText 
	private BeagleWebContext context 

	public ApplyRootTemplateFilter(final Reader templateReader) {
		super(templateReader)
	}

	@Override
	protected String getResultString() {
		final Handlebars handlebars = HandlebarsFactory.getFor this.context
		
		this.context.content = this.inputString
		final Template rootTemplate = handlebars.compileInline rootTemplateText
		return rootTemplate.apply(this.context)
	}
}