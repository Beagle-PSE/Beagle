package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.PcmSourceStatementLinkReader;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.PcmSourceStatementLinkRepository;

import java.io.File;
import java.io.Serializable;
import java.net.URISyntaxException;

/**
 * A factory which creates instances of {@link PcmRepositoryBlackboardFactoryAdder}.
 *
 * @author Christoph Michelbach
 * @author Ansgar Spiegler
 * @author Roman Langrehr
 */
public class PcmRepositoryBlackboardFactoryFactory {

	/**
	 * A {@link SourceCodeFileProvider} that resolves paths correctly for the Palladio
	 * file share project test project.
	 */
	public static final SourceCodeFileProvider PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER =
		new PalladioFileShareProjectTestSourceCodeFileProvider();

	/**
	 * Returns a valid instance of {@link PcmRepositoryBlackboardFactoryAdder} initialised
	 * with the PalladioFileShare repository.
	 *
	 * @return A valid instance of {@link PcmRepositoryBlackboardFactoryAdder} initialised
	 *         with the PalladioFileShare repository.
	 */
	public PcmRepositoryBlackboardFactoryAdder getPalladioFileShareProjectInstance() {

		final PcmSourceStatementLinkRepository pcmSourceStatementLinkRepository = new PcmSourceStatementLinkReader(
			new File("src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/PalladioFileShare/"
				+ "model/internal_architecture_model_source_statement_links.xml")).getPcmSourceLinkRepository();

		return new PcmRepositoryBlackboardFactoryAdder(
			"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/PalladioFileShare/"
				+ "model/internal_architecture_model.repository",
			PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER, pcmSourceStatementLinkRepository);
	}

	/**
	 * A {@link SourceCodeFileProvider} that resolves paths correctly for the Palladio
	 * file share test project.
	 *
	 * @author Christoph Michelbach
	 */
	private static class PalladioFileShareProjectTestSourceCodeFileProvider implements SourceCodeFileProvider {

		/**
		 * See {@link Serializable}.
		 */
		private static final long serialVersionUID = -1016535975436212430L;

		/**
		 * The base folder of Palladio file share.
		 */
		private static final File BASE_FOLDER;

		static {
			try {
				BASE_FOLDER = new File(PalladioFileShareProjectTestSourceCodeFileProvider.class
					.getResource("/PalladioFileShare/src").toURI());
			} catch (final URISyntaxException uriSyntaxException) {
				throw new RuntimeException(uriSyntaxException.getMessage());
			}
		}

		@Override
		public File getSourceFile(final String fullyQualifiedJavaPath) {
			return new File(BASE_FOLDER, fullyQualifiedJavaPath.replace(".", "/") + ".java");
		}

	}
}
