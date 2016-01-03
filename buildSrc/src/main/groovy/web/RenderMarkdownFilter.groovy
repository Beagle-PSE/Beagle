import org.pegdown.PegDownProcessor

/**
 * ReaderFilter that interprets its input as Markdown and renders it to HTML.
 *
 * @author Joshua Gleitze
 */
class RenderMarkdownFilter extends BeagleWebFilter {
	private static final PegDownProcessor PEGDOWN_PROCESSOR = new PegDownProcessor()

	public RenderMarkdownFilter(final Reader templateReader) {
		super(templateReader)
	}

	@Override
	protected String getResultString() {
		return PEGDOWN_PROCESSOR.markdownToHtml(this.inputString)
	}
}