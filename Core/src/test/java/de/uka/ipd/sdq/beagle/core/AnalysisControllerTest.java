package de.uka.ipd.sdq.beagle.core;

import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExtensionPointToolsFactory;

import org.junit.Test;

import java.util.HashSet;

/**
 * Tests for {@link AnalysisController}.
 *
 * @author Roman Langrehr
 */
public class AnalysisControllerTest {

	/**
	 * A default {@link BlackboardFactory} for the tests.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

	/**
	 * A default {@link ExtensionPointToolsFactory} for the tests.
	 */
	private static final ExtensionPointToolsFactory EXTENSION_POINT_FACTORY = new ExtensionPointToolsFactory();

	/**
	 * Tests
	 * {@link AnalysisController#AnalysisController(Blackboard, java.util.Set, java.util.Set, java.util.Set)}
	 * .
	 *
	 */
	@Test
	public void constructor() {
		new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
			EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(),
			EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(),
			EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());

		// Empty input should make no problem
		new AnalysisController(BLACKBOARD_FACTORY.getEmpty(), EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(),
			EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(),
			EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
		new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(), new HashSet<>(),
			EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(),
			EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
		new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
			EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(), new HashSet<>(),
			EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
		new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
			EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(),
			EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(), new HashSet<>());

		// Null tests

	}

}
