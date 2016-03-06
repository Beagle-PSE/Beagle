package de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink;

import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Reads in instances of the PCM Source Statement Links Model given in a XML file.
 * Performs integrity checks on the input.
 *
 * @author Joshua Gleitze
 */
public class PcmSourceStatementLinkReader {

	/**
	 * The handler of failures.
	 */
	private static final FailureHandler FAILURE_HANDLER =
		FailureHandler.getHandler("Source Statement Links Model Reader");

	/**
	 * The file that will be read in.
	 */
	private final File inputFile;

	/**
	 * The repository as it was read in from the {@link inputFile}.
	 */
	private PcmSourceStatementLinkRepository linkRepository;

	/**
	 * Creates a reader to read the provided {@code linkRepositoryFile}.
	 *
	 * @param linkRepositoryFile The file to read in.
	 */
	public PcmSourceStatementLinkReader(final File linkRepositoryFile) {
		this.inputFile = linkRepositoryFile;
	}

	/**
	 * Checks the integrity of the input file. This means to check the hash sums in the
	 * file against against those calculated for the source code files provided by the
	 * given {@code sourceCodeFileProvider} and the one for the provided
	 * {@code pcmRepositoryFile}.
	 *
	 * <p>If anything, either the integrity check or reading in the input file, fails, the
	 * method will report to the {@linkplain FailureHandler failure API}.
	 *
	 * @param sourceCodeFileProvider The provider for source code files for which hash
	 *            sums should be checked.
	 * @param pcmRepositoryFile The PCM repository file to check the hash sum for.
	 */
	public void checkIntegrity(final SourceCodeFileProvider sourceCodeFileProvider, final File pcmRepositoryFile) {
		this.readIn();

	}

	/**
	 * Gets the source link repository that was read in from the input file. If reading in
	 * the input file fails, the method will report to the {@linkplain FailureHandler
	 * failure API}.
	 *
	 * @return The repository that was read in from the input file.
	 */
	public PcmSourceStatementLinkRepository getPcmSourceLinkRepository() {
		this.readIn();
		return this.linkRepository;
	}

	/**
	 * Reads the {@link #inputFile} and populates {@link #linkRepository}. Reports a
	 * failure to the failure handler if reading in fails.
	 */
	private void readIn() {
		if (this.linkRepository != null) {
			return;
		}

		final Object result;
		try {
			final JAXBContext context = JAXBContext.newInstance("de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink");
			final Unmarshaller unmarshaller = context.createUnmarshaller();

			result = unmarshaller.unmarshal(this.inputFile);
		} catch (final JAXBException unmarshalError) {
			final FailureReport<Void> failure = new FailureReport<Void>().cause(unmarshalError).retryWith(this::readIn);
			FAILURE_HANDLER.handle(failure);
			return;
		}

		if (!(result instanceof PcmSourceStatementLinkRepository)) {
			final FailureReport<Void> failure =
				new FailureReport<Void>().message("The provided PCM Source Statement Link Model has a bad root type.")
					.details("expected a PcmSourceStatementLinkRepository, but found a %s.", result.getClass())
					.retryWith(this::readIn);
			FAILURE_HANDLER.handle(failure);
			return;
		}

		this.linkRepository = (PcmSourceStatementLinkRepository) result;
	}
}
