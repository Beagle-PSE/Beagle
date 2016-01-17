package de.uka.ipd.sdq.beagle.core.analysis;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the creation searching and loading of all available proposed
 * expression analyser. The proposed expression analysers need a public zero argument
 * constructor for the instantiation.
 *
 * @author Roman Langrehr
 */
public class ProposedExpressionAnalyserContributionsHandler {

	/**
	 * The extension point id for the proposed expression result analysers.
	 */
	private static final String PROPOSED_EXPRESSION_ANALYSERS_EXTENSION_POINT_ID =
		"de.uka.ipd.sdg.beagle.proposedexpressionanalyser";

	/**
	 * The property name of the extension point for the proposed expression analysers for
	 * the {@link ProposedExpressionAnalyser} implementations.
	 */
	private static final String PROPOSED_EXPRESSION_ANALYSERS_EXTENSION_POINT_CLASS_PROPERTY_NAME =
		"ProposedExpressionAnalyserClass";

	/**
	 * Scans the proposed expression analysers extension point for available proposed
	 * expression analysers.
	 *
	 * @return All available proposed expression analysers. Each call returns new
	 *         instances of the proposed expression analysers.
	 * @throws RuntimeException If an instance of an proposed expression analyser could
	 *             not be created for any reason, e.g. the proposed expression analyser
	 *             had no public zero argument constructor or if an
	 *             {@code ProposedExpressionAnalyserClass} provided via the extension
	 *             point was not implementing {@link ProposedExpressionAnalyser}.
	 */
	public List<ProposedExpressionAnalyser> getAvailableProposedExpressionAnalysers() {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		final List<ProposedExpressionAnalyser> proposedExpressionAnalysers = new ArrayList<>();
		final IConfigurationElement[] config =
			registry.getConfigurationElementsFor(PROPOSED_EXPRESSION_ANALYSERS_EXTENSION_POINT_ID);
		try {
			for (final IConfigurationElement element : config) {
				final Object object = element
					.createExecutableExtension(PROPOSED_EXPRESSION_ANALYSERS_EXTENSION_POINT_CLASS_PROPERTY_NAME);
				if (object instanceof ProposedExpressionAnalyser) {
					proposedExpressionAnalysers.add((ProposedExpressionAnalyser) object);
				} else {
					throw new RuntimeException(
						"A class provided via the proposed expression analyser extension point was not implementing the"
							+ "interface \"ProposedExpressionAnalyser\": " + object.getClass().getName()
							+ " in package " + object.getClass().getPackage().getName());
				}
			}
		} catch (final CoreException exception) {
			throw new RuntimeException(exception);
		}
		return proposedExpressionAnalysers;
	}
}
