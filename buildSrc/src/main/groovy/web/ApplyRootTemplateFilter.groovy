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