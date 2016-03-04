package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;

import java.io.File;
import java.io.Serializable;

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
			APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
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
			APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
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

		/**
		 * Paths to scan for the source files.
		 */
		private static final String[] SOURCE_PATHS = {
			"../src/test/resources/AppSensor/appsensor-core/src/main/java/",
			"../src/test/resources/AppSensor/access-controllers/appsensor-access-control-reference/src/main/java/"
		};

		@Override
		public File getSourceFile(final String fullyQualifiedJavaPath) {
			int index = 0;
			File result;
			do {
				result = new File(
					SOURCE_PATHS[index] + fullyQualifiedJavaPath.replaceAll("^core.", "").replace(".", "/") + ".java");
				index++;
			} while (!result.exists() && index != SOURCE_PATHS.length);
			return result;
		}

	}
}
