/**
 * Filter Reader that applies an outer template to the input. The outer template must be
 * passed in through the {@link #rootTemplateText} field. The template will be applied on
 * {@link #context}, the filter’s input will be available to the template at
 * {@code context.content}.
 *
 * @author Joshua Gleitze
 */
class ApplyRootTemplateFilter extends BeagleWebFilter {	
	// these will be set from the outside after construction
	private String rootTemplateText 
	private BeagleWebContext context 

	public ApplyRootTemplateFilter(final Reader templateReader) {
		super(templateReader)
	}

	@Override
	protected String getResultString() {
		this.context.content = this.inputString
		return HandlebarsRenderer.getFor(this.context).render(rootTemplateText)
	}
}