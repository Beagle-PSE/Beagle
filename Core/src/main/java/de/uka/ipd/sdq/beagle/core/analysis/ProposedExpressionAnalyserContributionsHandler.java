package de.uka.ipd.sdq.beagle.core.analysis;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import java.util.ArrayList;
import java.util.List;

/*
 * This class cannot reasonably be tested through unit tests. It uses an API that’s
 * offered through a static method of a final, byte-code signed class. Automatically
 * testing the class would require inadequate effort and would likely mainly test the
 * Eclipse API. This class is tested manually.
 *
 * COVERAGE:OFF
 */

/**
 * Handles the creation searching and loading of all available proposed expression
 * analyser. They are loaded through Eclipse’s extension point API. The proposed
 * expression analysers need a public zero argument constructor for the instantiation.
 *
 * @author Roman Langrehr
 */
public class ProposedExpressionAnalyserContributionsHandler {

	/**
	 * The extension point id for {@linkplain ProposedExpressionAnalyser
	 * ProposedExpressionAnalysers}.
	 */
	public static final String PROPOSED_EXPRESSION_ANALYSERS_EXTENSION_POINT_ID =
		"de.uka.ipd.sdq.beagle.proposedexpressionanalyser";

	/**
	 * The property name of the extension point for
	 * {@linkplain ProposedExpressionAnalyser} implementations.
	 */
	public static final String PROPOSED_EXPRESSION_ANALYSERS_EXTENSION_POINT_CLASS_PROPERTY_NAME =
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
