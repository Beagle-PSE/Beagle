package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyMeasurementResultAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.judge.FinalJudge;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExtensionPointToolsFactory;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.HashSet;
import java.util.Set;

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
		final ThrowingMethod method = () -> {
			new AnalysisController(null, EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(),
				EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(),
				EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
		};
		assertThat("Blackboard must not be null.", method, throwsException(NullPointerException.class));
		final ThrowingMethod method2 = () -> {
			new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(), null,
				EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(),
				EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
		};
		assertThat("Measurement tools set must not be null.", method2, throwsException(NullPointerException.class));
		final ThrowingMethod method3 = () -> {
			new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
				EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(), null,
				EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
		};
		assertThat("Mesasurement result analysers set must not be null.", method3,
			throwsException(NullPointerException.class));
		final ThrowingMethod method4 = () -> {
			new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
				EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(),
				EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(), null);
		};
		assertThat("Proposed expression analysers set must not be null.", method4,
			throwsException(NullPointerException.class));

		final Set<MeasurementTool> measurementToolsWithNull = EXTENSION_POINT_FACTORY.createNewMeasurementToolSet();
		measurementToolsWithNull.add(null);
		final Set<MeasurementResultAnalyser> measurementResultAnalyserWithNull =
			EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet();
		measurementResultAnalyserWithNull.add(null);
		final Set<ProposedExpressionAnalyser> proposedExpressionAnalysersWithNull =
			EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet();
		proposedExpressionAnalysersWithNull.add(null);
		final ThrowingMethod method5 = () -> {
			new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(), measurementToolsWithNull,
				EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(),
				EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
		};
		assertThat("Set must not contain null.", method5, throwsException(IllegalArgumentException.class));
		final ThrowingMethod method6 = () -> {
			new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
				EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(),
				EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(), proposedExpressionAnalysersWithNull);
		};
		assertThat("Set must not contain null.", method6, throwsException(IllegalArgumentException.class));
		final ThrowingMethod method7 = () -> {
			new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
				EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(), measurementResultAnalyserWithNull,
				EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
		};
		assertThat("Set must not contain null.", method7, throwsException(IllegalArgumentException.class));
	}

	/**
	 * Tests {@link AnalysisController#performAnalysis()}. Asserts that all tools are used
	 * in the correct order.
	 *
	 */
	@Test
	public void performAnalysis() {
		final MeasurementTool mockedMeasurementTool1 = mock(MeasurementTool.class);
		final MeasurementTool mockedMeasurementTool2 = mock(MeasurementTool.class);
		final MeasurementTool mockedMeasurementTool3 = mock(MeasurementTool.class);
		final MeasurementResultAnalyser mockedMeasurementResultAnalyser1 = mock(MeasurementResultAnalyser.class);
		final MeasurementResultAnalyser mockedMeasurementResultAnalyser2 = mock(MeasurementResultAnalyser.class);
		final MeasurementResultAnalyser mockedMeasurementResultAnalyser3 = mock(MeasurementResultAnalyser.class);
		final ProposedExpressionAnalyser mockedProposedExpressionAnalyser1 = mock(ProposedExpressionAnalyser.class);
		final ProposedExpressionAnalyser mockedProposedExpressionAnalyser2 = mock(ProposedExpressionAnalyser.class);
		final ProposedExpressionAnalyser mockedProposedExpressionAnalyser3 = mock(ProposedExpressionAnalyser.class);
		final FinalJudge mockedFinalJudge = mock(FinalJudge.class);

		final Set<MeasurementTool> oneMeasurementTool = new HashSet<>();
		oneMeasurementTool.add(mockedMeasurementTool1);
		final Set<MeasurementResultAnalyser> oneMeasurementResultAnalyser = new HashSet<>();
		oneMeasurementResultAnalyser.add(mockedMeasurementResultAnalyser1);
		final Set<ProposedExpressionAnalyser> oneProposedExpressionAnalyser = new HashSet<>();
		oneProposedExpressionAnalyser.add(mockedProposedExpressionAnalyser1);

		// Test, where no one wants to contribute.
		final Blackboard blackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		when(mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard))).thenReturn(false);
		when(mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard))).thenReturn(false);

		final AnalysisController analysisController = new AnalysisController(blackboard, oneMeasurementTool,
			oneMeasurementResultAnalyser, oneProposedExpressionAnalyser);
		analysisController.performAnalysis();
		verify(mockedMeasurementResultAnalyser1, never()).contribute(anyObject());
		verify(mockedProposedExpressionAnalyser1, never()).contribute(anyObject());
		final InOrder inOrder = inOrder(mockedFinalJudge);
		inOrder.verify(mockedFinalJudge).init(blackboard);
		inOrder.verify(mockedFinalJudge).judge(blackboard);
	}

}
