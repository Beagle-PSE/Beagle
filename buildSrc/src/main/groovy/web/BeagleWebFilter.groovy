import org.apache.tools.ant.filters.BaseFilterReader
import org.apache.commons.io.IOUtils 

/**
 * Abstract class to quickly create FilterReaders that operate on an entire file content string.
 * Implementing classes should add their logic only to {@link #getResultString()}.
 *
 * @author Joshua Gleitze
 */
abstract class BeagleWebFilter extends BaseFilterReader {
	/**
	 * The entire input file’s content as a string.
	 */
	protected final String inputString
	/**
	 * The entire input file’s content as a reader.
	 */
	protected final Reader inputReader

	/**
	 * @see BaseFilterReader#BaseFilterReader
	 */
	public BeagleWebFilter(final Reader inputReader) {
		// call the super constructor because we have to. This does not actually make sense.
		super(inputReader)
		this.inputReader = inputReader
		this.inputString = IOUtils.toString inputReader
	}
	
	@Override
	public int read() throws IOException {
		if (!getInitialized()) {
			this.in = new StringReader(this.resultString)
			setInitialized true
		}
		super.read()a
	}
	
	/**
	 * Generates the filter’s result.
	 *
	 * @return	The file’s new content.
	 */
	protected abstract String getResultString()
}