import org.jsoup.Jsoup
import org.jsoup.nodes.Document.OutputSettings


/**
 * Filter Reader that formats its html input to be easy readable.
 *
 * @author Joshua Gleitze
 */
class HTMLFormatFilter extends BeagleWebFilter {	

	public HTMLFormatFilter(final Reader templateReader) {
		super(templateReader)
	}

	@Override
	protected String getResultString() {
		OutputSettings outputSettings = new OutputSettings()
			.outline(true)
			.indentAmount(4)
		return Jsoup.parse(this.inputString).outputSettings(outputSettings)
	}
}