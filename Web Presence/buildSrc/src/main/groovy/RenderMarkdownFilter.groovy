import org.pegdown.PegDownProcessor
import org.pegdown.Extensions 

/**
 * ReaderFilter that interprets its input as Markdown and renders it to HTML.
 *
 * @author Joshua Gleitze
 */
class RenderMarkdownFilter extends BeagleWebFilter {
	private static final PegDownProcessor PEGDOWN_PROCESSOR = new PegDownProcessor(
		Extensions.AUTOLINKS |
		Extensions.FENCED_CODE_BLOCKS |
		Extensions.SMARTYPANTS |
		Extensions.TABLES
	)

	public RenderMarkdownFilter(final Reader templateReader) {
		super(templateReader)
	}

	@Override
	protected String getResultString() {
		return PEGDOWN_PROCESSOR.markdownToHtml(this.inputString)
	}
}