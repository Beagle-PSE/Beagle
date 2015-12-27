import org.apache.tools.ant.filters.BaseFilterReader
import org.apache.commons.io.IOUtils 

abstract class BeagleWebFilter extends BaseFilterReader {
	protected final String inputString
	protected final Reader inputReader

	public BeagleWebFilter(final Reader inputReader) {
		// call the super constructor because we have to. This does not actually make sense.
		super(inputReader)
		this.inputReader = inputReader
		this.inputString = IOUtils.toString inputReader
	}
	
	public int read() throws IOException {
		if (!getInitialized()) {
			this.in = new StringReader(this.resultString)
			setInitialized true
		}
		super.read()
	}
	
	protected abstract String getResultString()
}