package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.PcmSourceStatementLinkRepository;

import java.io.File;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A factory which creates instances of {@link PcmRepositoryBlackboardFactoryAdder}.
 *
 * @author Christoph Michelbach
 * @author Ansgar Spiegler
 * @author Roman Langrehr
 */
public class PcmRepositoryBlackboardFactoryFactory {

	/**
	 * A {@link SourceCodeFileProvider} that resolves paths correctly for the AppSensor
	 * test project.
	 */
	public static final SourceCodeFileProvider APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER =
		new AppSensorTestSourceCodeFileProvider();

	/**
	 * Returns a valid {@link PcmRepositoryBlackboardFactoryAdder} object.
	 *
	 * @return A valid {@link PcmRepositoryBlackboardFactoryAdder} object.
	 */
	public PcmRepositoryBlackboardFactoryAdder getValidInstance() {
		return new PcmRepositoryBlackboardFactoryAdder(
			"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/Family.repository",
			APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER, mock(PcmSourceStatementLinkRepository.class));
	}

	/**
	 * Returns a valid instance of {@link PcmRepositoryBlackboardFactoryAdder} initialised
	 * with the AppSensor repository.
	 *
	 * @return A valid instance of {@link PcmRepositoryBlackboardFactoryAdder} initialised
	 *         with the AppSensor repository.
	 */
	public PcmRepositoryBlackboardFactoryAdder getAppSensorProjectInstance() {
		return new PcmRepositoryBlackboardFactoryAdder(
			"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/AppSensor.repository",
			APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER, mock(PcmSourceStatementLinkRepository.class));
	}

	/**
	 * A {@link SourceCodeFileProvider} that resolves paths correctly for the AppSensor
	 * test project.
	 *
	 * @author Roman Langrehr
	 */
	private static class AppSensorTestSourceCodeFileProvider implements SourceCodeFileProvider {

		/**
		 * See {@link Serializable}.
		 */
		private static final long serialVersionUID = -1066585975466213430L;

		@Override
		public File getSourceFile(final String fullyQualifiedJavaPath) {
			final URL fileUrl = PcmRepositoryBlackboardFactoryFactory.class
				.getResource("/de/uka/ipd/sdq/beagle/core/pcmconnection/Source files/"
					+ fullyQualifiedJavaPath.replace(".", "/") + ".java");
			if (fileUrl != null) {
				try {
					return new File(fileUrl.toURI());
				} catch (final URISyntaxException uriSyntaxException) {
					return null;
				}
			} else {
				return null;
			}
		}

	}
}
