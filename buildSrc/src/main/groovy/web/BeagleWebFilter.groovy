import org.apache.tools.ant.filters.BaseFilterReader
import org.apache.commons.io.IOUtils 

/**
 * Abstract class to quickly create FilterReaders that operate on a whole file content string.
 * Implementing classes should add their logic only to {@link #getResultString()}.
 *
 * @author Joshua Gleitze
 */
abstract class BeagleWebFilter extends BaseFilterReader {
	/**
	 * The whole input file’s content as a string.
	 */
	protected final String inputString
	/**
	 * The whole input file’s conten as a reader.
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
		super.read()
	}
	
	/**
	 * Generates the filter’s result.
	 *
	 * @return	The file’s new content.
	 */
	protected abstract String getResultString()
}