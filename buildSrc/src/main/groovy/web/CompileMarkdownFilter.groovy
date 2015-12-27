import org.pegdown.PegDownProcessor

class CompileMarkdownFilter extends BeagleWebFilter {
	private static final PegDownProcessor PEGDOWN_PROCESSOR = new PegDownProcessor()

	public CompileMarkdownFilter(final Reader templateReader) {
		super(templateReader)
	}

	@Override
	protected String getResultString() {
		return PEGDOWN_PROCESSOR.markdownToHtml(this.inputString)
	}
}