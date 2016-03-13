package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyMeasurementResultAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementEvent;
import de.uka.ipd.sdq.beagle.core.measurement.order.MeasurementOrder;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExtensionPointToolsFactory;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests for {@link AnalysisController}.
 *
 * @author Roman Langrehr
 * @author Annika Berger
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
	 * A mock for {@link MeasurementTool}. Should be reseted before each use with
	 * {@code reset(mockedMeasurementTool1)}.
	 */
	private final MeasurementTool mockedMeasurementTool1 = mock(MeasurementTool.class);

	/**
	 * A mock for {@link MeasurementTool}. Should be reseted before each use with
	 * {@code reset(mockedMeasurementTool2)}.
	 */
	private final MeasurementTool mockedMeasurementTool2 = mock(MeasurementTool.class);

	/**
	 * A mock for {@link MeasurementTool}. Should be reseted before each use with
	 * {@code reset(mockedMeasurementTool3)}.
	 */
	private final MeasurementTool mockedMeasurementTool3 = mock(MeasurementTool.class);

	/**
	 * A mock for {@link MeasurementTool}. Should be reseted before each use with
	 * {@code reset(mockedMeasurementResultAnalyser1)}.
	 */
	private final MeasurementResultAnalyser mockedMeasurementResultAnalyser1 = mock(MeasurementResultAnalyser.class);

	/**
	 * A mock for {@link MeasurementTool}. Should be reseted before each use with
	 * {@code reset(mockedMeasurementResultAnalyser2)}.
	 */
	private final MeasurementResultAnalyser mockedMeasurementResultAnalyser2 = mock(MeasurementResultAnalyser.class);

	/**
	 * A mock for {@link MeasurementTool}. Should be reseted before each use with
	 * {@code reset(mockedMeasurementResultAnalyser3)}.
	 */
	private final MeasurementResultAnalyser mockedMeasurementResultAnalyser3 = mock(MeasurementResultAnalyser.class);

	/**
	 * A mock for {@link MeasurementTool}. Should be reseted before each use with
	 * {@code reset(mockedProposedExpressionAnalyser1)}.
	 */
	private final ProposedExpressionAnalyser mockedProposedExpressionAnalyser1 = mock(ProposedExpressionAnalyser.class);

	/**
	 * A mock for {@link MeasurementTool}. Should be reseted before each use with
	 * {@code reset(mockedProposedExpressionAnalyser2)}.
	 */
	private final ProposedExpressionAnalyser mockedProposedExpressionAnalyser2 = mock(ProposedExpressionAnalyser.class);

	/**
	 * A mock for {@link MeasurementTool}. Should be reseted before each use with
	 * {@code reset(mockedProposedExpressionAnalyser3)}.
	 */
	private final ProposedExpressionAnalyser mockedProposedExpressionAnalyser3 = mock(ProposedExpressionAnalyser.class);

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
		final ThrowingMethod method = new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new AnalysisController(null, EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(),
					EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(),
					EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
			}
		};
		assertThat("Blackboard must not be null.", method, throwsException(NullPointerException.class));
		final ThrowingMethod method2 = new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(), null,
					EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(),
					EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
			}
		};
		assertThat("Measurement tools set must not be null.", method2, throwsException(NullPointerException.class));
		final ThrowingMethod method3 = new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
					EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(), null,
					EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
			}
		};
		assertThat("Mesasurement result analysers set must not be null.", method3,
			throwsException(NullPointerException.class));
		final ThrowingMethod method4 = new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
					EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(),
					EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(), null);
			}
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
		final ThrowingMethod method5 = new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(), measurementToolsWithNull,
					EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(),
					EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
			}
		};
		assertThat("Set must not contain null.", method5, throwsException(IllegalArgumentException.class));
		final ThrowingMethod method6 = new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
					EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(),
					EXTENSION_POINT_FACTORY.createNewMeasurementResultAnalysersSet(),
					proposedExpressionAnalysersWithNull);
			}
		};
		assertThat("Set must not contain null.", method6, throwsException(IllegalArgumentException.class));
		final ThrowingMethod method7 = new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new AnalysisController(BLACKBOARD_FACTORY.getWithToBeMeasuredContent(),
					EXTENSION_POINT_FACTORY.createNewMeasurementToolSet(), measurementResultAnalyserWithNull,
					EXTENSION_POINT_FACTORY.createNewProposedExpressionAnalyserSet());
			}
		};
		assertThat("Set must not contain null.", method7, throwsException(IllegalArgumentException.class));

		// Immutable tests
		// Immutability is verified by calls to {@link
		final Blackboard blackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		// AnalysisController#performAnalysis()}.
		this.resetMocks();
		final Set<MeasurementTool> allMeasurementTools = new HashSet<>();
		allMeasurementTools.add(this.mockedMeasurementTool1);
		allMeasurementTools.add(this.mockedMeasurementTool2);
		allMeasurementTools.add(this.mockedMeasurementTool3);
		final Set<MeasurementResultAnalyser> allMeasurementResultAnalysers = new HashSet<>();
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser1);
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser2);
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser3);
		final Set<ProposedExpressionAnalyser> allProposedExpressionAnalysers = new HashSet<>();
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser1);
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser2);
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser3);
		final AnalysisController analysisController = new AnalysisController(blackboard, allMeasurementTools,
			allMeasurementResultAnalysers, allProposedExpressionAnalysers);
		final Set<MeasurementTool> originalMeasurementTools = new HashSet<>(allMeasurementTools);
		final Set<MeasurementResultAnalyser> originalMeasurementResultAnalysers =
			new HashSet<>(allMeasurementResultAnalysers);
		final Set<ProposedExpressionAnalyser> originalProposedExpressionAnalysers =
			new HashSet<>(allProposedExpressionAnalysers);
		allMeasurementTools.clear();
		allMeasurementResultAnalysers.clear();
		allProposedExpressionAnalysers.clear();

		analysisController.performAnalysis();
		// Assert that the measurement tools were asked.
		for (final MeasurementTool measurementTool : originalMeasurementTools) {
			verify(measurementTool, atLeastOnce()).measure((MeasurementOrder) notNull());
		}
		for (final MeasurementResultAnalyser measurementResultAnalyser : originalMeasurementResultAnalysers) {
			verify(measurementResultAnalyser, atLeastOnce())
				.canContribute(Matchers.eq(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard)));
		}
		for (final ProposedExpressionAnalyser proposedExpressionAnalyser : originalProposedExpressionAnalysers) {
			verify(proposedExpressionAnalyser, atLeastOnce())
				.canContribute(Matchers.eq(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard)));
		}
	}

	/**
	 * Tests {@link AnalysisController#performAnalysis()}. Asserts that all tools are used
	 * in the correct order.
	 *
	 */
	@Test
	public void performAnalysis() {
		final Set<MeasurementTool> oneMeasurementTool = new HashSet<>();
		oneMeasurementTool.add(this.mockedMeasurementTool1);
		final Set<MeasurementResultAnalyser> oneMeasurementResultAnalyser = new HashSet<>();
		oneMeasurementResultAnalyser.add(this.mockedMeasurementResultAnalyser1);
		final Set<ProposedExpressionAnalyser> oneProposedExpressionAnalyser = new HashSet<>();
		oneProposedExpressionAnalyser.add(this.mockedProposedExpressionAnalyser1);

		// Test, where no one wants to contribute.
		this.resetMocks();
		final Blackboard blackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		when(this.mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard))).thenReturn(false);
		when(this.mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard))).thenReturn(false);

		final AnalysisController analysisController = new AnalysisController(blackboard, oneMeasurementTool,
			oneMeasurementResultAnalyser, oneProposedExpressionAnalyser);
		analysisController.performAnalysis();
		verify(this.mockedMeasurementResultAnalyser1, never()).contribute(anyObject());
		verify(this.mockedProposedExpressionAnalyser1, never()).contribute(anyObject());

		this.resetMocks();
		final Set<MeasurementTool> allMeasurementTools = new HashSet<>();
		allMeasurementTools.add(this.mockedMeasurementTool1);
		allMeasurementTools.add(this.mockedMeasurementTool2);
		allMeasurementTools.add(this.mockedMeasurementTool3);
		final Set<MeasurementResultAnalyser> allMeasurementResultAnalysers = new HashSet<>();
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser1);
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser2);
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser3);
		final Set<ProposedExpressionAnalyser> allProposedExpressionAnalysers = new HashSet<>();
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser1);
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser2);
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser3);
		final Blackboard blackboard2 = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		when(this.mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard2))).thenReturn(false);
		when(this.mockedMeasurementResultAnalyser2
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard2))).thenReturn(false);
		when(this.mockedMeasurementResultAnalyser3
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard2))).thenReturn(false);
		when(this.mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard2))).thenReturn(false);
		when(this.mockedProposedExpressionAnalyser2
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard2))).thenReturn(false);
		when(this.mockedProposedExpressionAnalyser3
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard2))).thenReturn(false);

		final AnalysisController analysisController2 = new AnalysisController(blackboard2, allMeasurementTools,
			allMeasurementResultAnalysers, allProposedExpressionAnalysers);
		analysisController2.performAnalysis();
		verify(this.mockedMeasurementResultAnalyser1, never()).contribute(anyObject());
		verify(this.mockedMeasurementResultAnalyser2, never()).contribute(anyObject());
		verify(this.mockedMeasurementResultAnalyser3, never()).contribute(anyObject());
		verify(this.mockedProposedExpressionAnalyser1, never()).contribute(anyObject());
		verify(this.mockedProposedExpressionAnalyser2, never()).contribute(anyObject());
		verify(this.mockedProposedExpressionAnalyser3, never()).contribute(anyObject());

		// Assert that MeasurementTool#measure is not called, when the blackboard is empty
		// (-> nothing to measure).
		this.resetMocks();
		final AnalysisController analysisController3 = new AnalysisController(BLACKBOARD_FACTORY.getEmpty(),
			allMeasurementTools, allMeasurementResultAnalysers, allProposedExpressionAnalysers);
		analysisController3.performAnalysis();
		verify(this.mockedMeasurementTool1, never()).measure(anyObject());
		verify(this.mockedMeasurementTool2, never()).measure(anyObject());
		verify(this.mockedMeasurementTool3, never()).measure(anyObject());

		// Verify that everybody can contribute, if he wants.
		// Verify correct execution order
		this.resetMocks();
		final Blackboard blackboard5 = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		when(this.mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard5))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser1
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard5))).thenReturn(false);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser1)
			.contribute(new MeasurementResultAnalyserBlackboardView(blackboard5));
		when(this.mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard5))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser1
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard5)))
						.thenReturn(false);
				return null;
			}
		}).when(this.mockedProposedExpressionAnalyser1)
			.contribute(new ProposedExpressionAnalyserBlackboardView(blackboard5));

		final AnalysisController analysisController6 = new AnalysisController(blackboard5, oneMeasurementTool,
			oneMeasurementResultAnalyser, oneProposedExpressionAnalyser);
		analysisController6.performAnalysis();
		final InOrder inOrder = Mockito.inOrder(this.mockedMeasurementTool1, this.mockedMeasurementResultAnalyser1,
			this.mockedProposedExpressionAnalyser1);
		inOrder.verify(this.mockedMeasurementTool1).measure((MeasurementOrder) notNull());
		inOrder.verify(this.mockedMeasurementResultAnalyser1)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard5)));
		inOrder.verify(this.mockedProposedExpressionAnalyser1)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard5)));

		this.resetMocks();
		final Blackboard blackboard6 = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		when(this.mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard6))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser1
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard6))).thenReturn(false);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser1)
			.contribute(new MeasurementResultAnalyserBlackboardView(blackboard6));
		when(this.mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard6))).thenReturn(true);

		final AnalysisController analysisController7 = new AnalysisController(blackboard6, oneMeasurementTool,
			oneMeasurementResultAnalyser, oneProposedExpressionAnalyser);
		analysisController7.performAnalysis();
		final InOrder inOrder1 = inOrder(this.mockedMeasurementTool1, this.mockedMeasurementResultAnalyser1,
			this.mockedProposedExpressionAnalyser1);
		inOrder1.verify(this.mockedMeasurementTool1).measure((MeasurementOrder) notNull());
		inOrder1.verify(this.mockedMeasurementResultAnalyser1)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard6)));
		inOrder1.verify(this.mockedProposedExpressionAnalyser1)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard6)));

		// Test with empty blackboard
		this.resetMocks();
		final Blackboard blackboard7 = BLACKBOARD_FACTORY.getEmpty();

		final AnalysisController analysisController8 = new AnalysisController(blackboard7, allMeasurementTools,
			allMeasurementResultAnalysers, allProposedExpressionAnalysers);
		analysisController8.performAnalysis();
		for (final MeasurementTool measurementTool : allMeasurementTools) {
			verify(measurementTool, never()).measure(anyObject());
		}
	}

	/**
	 * Tests {@link AnalysisController#performAnalysis()}. Tests complex scenario.
	 *
	 */
	@Test
	public void performAnalysisScenario1() {
		this.resetMocks();
		final Set<MeasurementTool> allMeasurementTools = new HashSet<>();
		allMeasurementTools.add(this.mockedMeasurementTool1);
		allMeasurementTools.add(this.mockedMeasurementTool2);
		allMeasurementTools.add(this.mockedMeasurementTool3);
		final Set<MeasurementResultAnalyser> allMeasurementResultAnalysers = new HashSet<>();
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser1);
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser2);
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser3);
		final Set<ProposedExpressionAnalyser> allProposedExpressionAnalysers = new HashSet<>();
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser1);
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser2);
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser3);
		final Blackboard blackboard8 = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		// Measurement result analyser only can contribute in the following order: 2 1 3
		when(this.mockedMeasurementResultAnalyser2
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard8))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser2
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard8))).thenReturn(false);
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser1
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard8))).thenReturn(true);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser2)
			.contribute(new MeasurementResultAnalyserBlackboardView(blackboard8));
		when(this.mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard8))).thenReturn(false);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser1
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard8))).thenReturn(false);
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser3
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard8))).thenReturn(true);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser1)
			.contribute(new MeasurementResultAnalyserBlackboardView(blackboard8));
		when(this.mockedMeasurementResultAnalyser3
			.canContribute(eq(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard8)))).thenReturn(false);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser3
					.canContribute(eq(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard8))))
						.thenReturn(false);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser3)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard8)));

		// Proposed Expression analyser only can contribute in the following order: 3 1 2
		when(this.mockedProposedExpressionAnalyser3
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard8))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser3
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard8)))
						.thenReturn(false);
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser1
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard8))).thenReturn(true);
				return null;
			}
		}).when(this.mockedProposedExpressionAnalyser3)
			.contribute(new ProposedExpressionAnalyserBlackboardView(blackboard8));
		when(this.mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard8))).thenReturn(false);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser1
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard8)))
						.thenReturn(false);
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser2
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard8))).thenReturn(true);
				return null;
			}
		}).when(this.mockedProposedExpressionAnalyser1)
			.contribute(new ProposedExpressionAnalyserBlackboardView(blackboard8));
		when(this.mockedProposedExpressionAnalyser2
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard8))).thenReturn(false);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser2
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard8)))
						.thenReturn(false);
				return null;
			}
		}).when(this.mockedProposedExpressionAnalyser2)
			.contribute(new ProposedExpressionAnalyserBlackboardView(blackboard8));

		final AnalysisController analysisController9 = new AnalysisController(blackboard8, allMeasurementTools,
			allMeasurementResultAnalysers, allProposedExpressionAnalysers);
		analysisController9.performAnalysis();
		verify(this.mockedMeasurementTool1).measure((MeasurementOrder) notNull());
		verify(this.mockedMeasurementTool2).measure((MeasurementOrder) notNull());
		verify(this.mockedMeasurementTool3).measure((MeasurementOrder) notNull());
		final InOrder inOrder2 = inOrder(this.mockedMeasurementResultAnalyser1, this.mockedMeasurementResultAnalyser2,
			this.mockedMeasurementResultAnalyser3, this.mockedProposedExpressionAnalyser1,
			this.mockedProposedExpressionAnalyser2, this.mockedProposedExpressionAnalyser3);
		inOrder2.verify(this.mockedMeasurementResultAnalyser2)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard8)));
		inOrder2.verify(this.mockedMeasurementResultAnalyser1)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard8)));
		inOrder2.verify(this.mockedMeasurementResultAnalyser3)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard8)));
		inOrder2.verify(this.mockedProposedExpressionAnalyser3)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard8)));
		inOrder2.verify(this.mockedProposedExpressionAnalyser1)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard8)));
		inOrder2.verify(this.mockedProposedExpressionAnalyser2)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard8)));
	}

	/**
	 * Tests {@link AnalysisController#performAnalysis()}. Tests complex scenario with
	 * remeasurement.
	 *
	 */
	@Test
	public void performAnalysisScenario2() {
		final Set<MeasurementTool> oneMeasurementTool = new HashSet<>();
		oneMeasurementTool.add(this.mockedMeasurementTool1);
		final Set<MeasurementResultAnalyser> allMeasurementResultAnalysers = new HashSet<>();
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser1);
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser2);
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser3);
		final Set<ProposedExpressionAnalyser> allProposedExpressionAnalysers = new HashSet<>();
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser1);
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser2);
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser3);
		this.resetMocks();
		final Blackboard blackboard9 = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		// Measurement result analyser only can contribute in the following order: 2 1 3
		when(this.mockedMeasurementResultAnalyser2
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser2
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(false);
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser1
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(true);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser2)
			.contribute(new MeasurementResultAnalyserBlackboardView(blackboard9));
		when(this.mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(false);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser1
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(false);
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser3
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(true);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser1)
			.contribute(new MeasurementResultAnalyserBlackboardView(blackboard9));
		when(this.mockedMeasurementResultAnalyser3
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(false);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser3
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(false);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser3)
			.contribute(new MeasurementResultAnalyserBlackboardView(blackboard9));

		// Proposed Expression analyser only can contribute in the following order: 3 1 2
		when(this.mockedProposedExpressionAnalyser3
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser3
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9)))
						.thenReturn(false);
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser1
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9))).thenReturn(true);
				return null;
			}
		}).when(this.mockedProposedExpressionAnalyser3)
			.contribute(new ProposedExpressionAnalyserBlackboardView(blackboard9));
		when(this.mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9))).thenReturn(false);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser1
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9)))
						.thenReturn(false);
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser2
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9))).thenReturn(true);

				// Remeasurement
				blackboard9.addToBeMeasuredRdias(blackboard9.getAllRdias().iterator().next());
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser3
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(true);
				return null;
			}
		}).when(this.mockedProposedExpressionAnalyser1)
			.contribute(new ProposedExpressionAnalyserBlackboardView(blackboard9));
		when(this.mockedProposedExpressionAnalyser2
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9))).thenReturn(false);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser2
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9)))
						.thenReturn(false);
				return null;
			}
		}).when(this.mockedProposedExpressionAnalyser2)
			.contribute(new ProposedExpressionAnalyserBlackboardView(blackboard9));

		final AnalysisController analysisController10 = new AnalysisController(blackboard9, oneMeasurementTool,
			allMeasurementResultAnalysers, allProposedExpressionAnalysers);
		analysisController10.performAnalysis();
		final InOrder inOrder3 = inOrder(this.mockedMeasurementTool1, this.mockedMeasurementResultAnalyser1,
			this.mockedMeasurementResultAnalyser2, this.mockedMeasurementResultAnalyser3,
			this.mockedProposedExpressionAnalyser1, this.mockedProposedExpressionAnalyser2,
			this.mockedProposedExpressionAnalyser3);
		inOrder3.verify(this.mockedMeasurementTool1).measure((MeasurementOrder) notNull());
		inOrder3.verify(this.mockedMeasurementResultAnalyser2)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard9)));
		inOrder3.verify(this.mockedMeasurementResultAnalyser1)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard9)));
		inOrder3.verify(this.mockedMeasurementResultAnalyser3)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard9)));
		inOrder3.verify(this.mockedProposedExpressionAnalyser3)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard9)));
		inOrder3.verify(this.mockedProposedExpressionAnalyser1)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard9)));
		inOrder3.verify(this.mockedMeasurementTool1).measure((MeasurementOrder) notNull());
		inOrder3.verify(this.mockedMeasurementResultAnalyser3)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard9)));
		inOrder3.verify(this.mockedProposedExpressionAnalyser2)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard9)));

	}

	/**
	 * Tests {@link AnalysisController#performAnalysis()}. Tests complex scenario where
	 * each tool can contribute one time.
	 *
	 */
	@Test
	public void performAnalysisScenario3() {
		final Set<MeasurementTool> oneMeasurementTool = new HashSet<>();
		oneMeasurementTool.add(this.mockedMeasurementTool1);
		final Set<MeasurementResultAnalyser> allMeasurementResultAnalysers = new HashSet<>();
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser1);
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser2);
		allMeasurementResultAnalysers.add(this.mockedMeasurementResultAnalyser3);
		final Set<ProposedExpressionAnalyser> allProposedExpressionAnalysers = new HashSet<>();
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser1);
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser2);
		allProposedExpressionAnalysers.add(this.mockedProposedExpressionAnalyser3);
		this.resetMocks();
		final Blackboard blackboard9 = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		// Measurement result analyser only can contribute in the following order: 2 1 3
		when(this.mockedMeasurementResultAnalyser2
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser2
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(false);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser2)
			.contribute(new MeasurementResultAnalyserBlackboardView(blackboard9));
		when(this.mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser1
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(false);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser1)
			.contribute(new MeasurementResultAnalyserBlackboardView(blackboard9));
		when(this.mockedMeasurementResultAnalyser3
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedMeasurementResultAnalyser3
					.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard9))).thenReturn(false);
				return null;
			}
		}).when(this.mockedMeasurementResultAnalyser3)
			.contribute(new MeasurementResultAnalyserBlackboardView(blackboard9));

		// Proposed Expression analyser only can contribute in the following order: 3 1 2
		when(this.mockedProposedExpressionAnalyser3
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser3
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9)))
						.thenReturn(false);
				return null;
			}
		}).when(this.mockedProposedExpressionAnalyser3)
			.contribute(new ProposedExpressionAnalyserBlackboardView(blackboard9));
		when(this.mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser1
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9)))
						.thenReturn(false);
				return null;
			}
		}).when(this.mockedProposedExpressionAnalyser1)
			.contribute(new ProposedExpressionAnalyserBlackboardView(blackboard9));
		when(this.mockedProposedExpressionAnalyser2
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9))).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				when(AnalysisControllerTest.this.mockedProposedExpressionAnalyser2
					.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard9)))
						.thenReturn(false);
				return null;
			}
		}).when(this.mockedProposedExpressionAnalyser2)
			.contribute(new ProposedExpressionAnalyserBlackboardView(blackboard9));

		final AnalysisController analysisController10 = new AnalysisController(blackboard9, oneMeasurementTool,
			allMeasurementResultAnalysers, allProposedExpressionAnalysers);
		analysisController10.performAnalysis();
		verify(this.mockedMeasurementTool1).measure((MeasurementOrder) notNull());
		verify(this.mockedMeasurementResultAnalyser2)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard9)));
		verify(this.mockedMeasurementResultAnalyser1)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard9)));
		verify(this.mockedMeasurementResultAnalyser3)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard9)));
		verify(this.mockedProposedExpressionAnalyser3)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard9)));
		verify(this.mockedProposedExpressionAnalyser1)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard9)));
		verify(this.mockedMeasurementResultAnalyser3)
			.contribute(eq(new MeasurementResultAnalyserBlackboardView(blackboard9)));
		verify(this.mockedProposedExpressionAnalyser2)
			.contribute(eq(new ProposedExpressionAnalyserBlackboardView(blackboard9)));

	}

	/**
	 * Resets the following mocks.
	 *
	 * <li>
	 *
	 * <ul> {@link #mockedMeasurementTool1} </ul>
	 *
	 * <ul> {@link #mockedMeasurementTool2} </ul>
	 *
	 * <ul> {@link #mockedMeasurementTool3} </ul>
	 *
	 * <ul> {@link #mockedMeasurementResultAnalyser1} </ul>
	 *
	 * <ul> {@link #mockedMeasurementResultAnalyser2} </ul>
	 *
	 * <ul> {@link #mockedMeasurementResultAnalyser3} </ul>
	 *
	 * <ul> {@link #mockedProposedExpressionAnalyser1} </ul>
	 *
	 * <ul> {@link #mockedProposedExpressionAnalyser2} </ul>
	 *
	 * <ul> {@link #mockedProposedExpressionAnalyser3} </ul>
	 *
	 * </li>
	 *
	 */
	private void resetMocks() {
		reset(this.mockedMeasurementTool1);
		reset(this.mockedMeasurementTool2);
		reset(this.mockedMeasurementTool3);
		reset(this.mockedMeasurementResultAnalyser1);
		reset(this.mockedMeasurementResultAnalyser2);
		reset(this.mockedMeasurementResultAnalyser3);
		reset(this.mockedProposedExpressionAnalyser1);
		reset(this.mockedProposedExpressionAnalyser2);
		reset(this.mockedProposedExpressionAnalyser3);
	}

	/**
	 * Tests {@link AnalysisController#setAnalysisState(AnalysisState)} for
	 * {@link AnalysisState} {@code ABORTING}.
	 *
	 */
	@Test
	public void setAnalysisStateAbort() {
		final SleepingMeasurementTool sleepingMeasurementTool = new SleepingMeasurementTool();
		final AnalysisController analysisController = this.setUpController(sleepingMeasurementTool);

		analysisController.setAnalysisState(AnalysisState.ABORTING);
		assertThat(analysisController.getAnalysisState(), is(AnalysisState.ABORTING));
		assertThat(() -> analysisController.setAnalysisState(AnalysisState.RUNNING),
			throwsException(IllegalStateException.class));
		assertThat(analysisController.getAnalysisState(), is(AnalysisState.ABORTING));

		this.tearDownSetAnalysisTest(sleepingMeasurementTool);
	}

	/**
	 * Tests {@link AnalysisController#setAnalysisState(AnalysisState)} for
	 * {@link AnalysisState} {@code TERMINATED}.
	 *
	 */
	@Test
	public void setAnalysisStateTerminated() {
		final SleepingMeasurementTool sleepingMeasurementTool = new SleepingMeasurementTool();
		final AnalysisController analysisController = this.setUpController(sleepingMeasurementTool);

		analysisController.setAnalysisState(AnalysisState.TERMINATED);
		assertThat(analysisController.getAnalysisState(), is(AnalysisState.TERMINATED));
		assertThat(() -> analysisController.setAnalysisState(AnalysisState.RUNNING),
			throwsException(IllegalStateException.class));
		assertThat(analysisController.getAnalysisState(), is(AnalysisState.TERMINATED));

		this.tearDownSetAnalysisTest(sleepingMeasurementTool);
	}

	/**
	 * Tests {@link AnalysisController#setAnalysisState(AnalysisState)} for
	 * {@link AnalysisState} {@code ENDING}.
	 *
	 */
	@Test
	public void setAnalysisStateEnding() {
		final SleepingMeasurementTool sleepingMeasurementTool = new SleepingMeasurementTool();
		final AnalysisController analysisController = this.setUpController(sleepingMeasurementTool);

		analysisController.setAnalysisState(AnalysisState.ENDING);
		assertThat(analysisController.getAnalysisState(), is(AnalysisState.ENDING));
		analysisController.setAnalysisState(AnalysisState.RUNNING);
		assertThat(analysisController.getAnalysisState(), is(AnalysisState.RUNNING));
		analysisController.setAnalysisState(AnalysisState.ENDING);
		assertThat(analysisController.getAnalysisState(), is(AnalysisState.ENDING));
		assertThat(() -> analysisController.setAnalysisState(AnalysisState.ABORTING),
			throwsException(IllegalStateException.class));
		assertThat(analysisController.getAnalysisState(), is(AnalysisState.ENDING));

		this.tearDownSetAnalysisTest(sleepingMeasurementTool);
	}

	/**
	 * Asserts that the {@link AnalysisState} is {@code RUNNING} when the performance has
	 * been started and there was no call to
	 * {@link AnalysisController#setAnalysisState(AnalysisState)}.
	 *
	 */
	@Test
	public void analysisStateWhilePerforming() {
		final SleepingMeasurementTool sleepingMeasurementTool = new SleepingMeasurementTool();
		final AnalysisController analysisController = this.setUpController(sleepingMeasurementTool);

		assertThat(analysisController.getAnalysisState(), is(AnalysisState.RUNNING));

		this.tearDownSetAnalysisTest(sleepingMeasurementTool);
	}

	/**
	 * Asserts that {@link AnalysisController#setAnalysisState(AnalysisState)} throws a
	 * {@link NullPointerException} for input {@code null}.
	 *
	 * <p>Asserts that state is not changed.
	 *
	 */
	@Test
	public void setAnalysisStateNull() {
		final SleepingMeasurementTool sleepingMeasurementTool = new SleepingMeasurementTool();
		final AnalysisController analysisController = this.setUpController(sleepingMeasurementTool);

		assertThat(() -> analysisController.setAnalysisState(null), throwsException(NullPointerException.class));
		assertThat(analysisController.getAnalysisState(), is(AnalysisState.RUNNING));

		this.tearDownSetAnalysisTest(sleepingMeasurementTool);
	}

	/**
	 * Asserts that {@link AnalysisController#setAnalysisState(AnalysisState)} throws a
	 * {@link IllegalStateException} if called before the analysis has started.
	 *
	 * <p>Asserts that this does not change the state.
	 */
	@Test
	public void setAnalysisStateBeforeStartingMeasurement() {
		final Set<MeasurementTool> oneMeasurementTool = new HashSet<>();
		oneMeasurementTool.add(this.mockedMeasurementTool1);
		final Set<MeasurementResultAnalyser> oneMeasurementResultAnalyser = new HashSet<>();
		oneMeasurementResultAnalyser.add(this.mockedMeasurementResultAnalyser1);
		final Set<ProposedExpressionAnalyser> oneProposedExpressionAnalyser = new HashSet<>();
		oneProposedExpressionAnalyser.add(this.mockedProposedExpressionAnalyser1);

		// Test, where no one wants to contribute.
		this.resetMocks();
		final Blackboard blackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();

		when(this.mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard))).thenReturn(false);
		when(this.mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard))).thenReturn(false);

		final AnalysisController analysisController = new AnalysisController(blackboard, oneMeasurementTool,
			oneMeasurementResultAnalyser, oneProposedExpressionAnalyser);
		AnalysisState initState = analysisController.getAnalysisState();
		assertThat(() -> analysisController.setAnalysisState(AnalysisState.RUNNING),
			throwsException(IllegalStateException.class));
		assertThat(analysisController.getAnalysisState(), is(initState));

		initState = analysisController.getAnalysisState();
		assertThat(() -> analysisController.setAnalysisState(AnalysisState.TERMINATED),
			throwsException(IllegalStateException.class));
		assertThat(analysisController.getAnalysisState(), is(initState));

		initState = analysisController.getAnalysisState();
		assertThat(() -> analysisController.setAnalysisState(AnalysisState.ENDING),
			throwsException(IllegalStateException.class));
		assertThat(analysisController.getAnalysisState(), is(initState));

		initState = analysisController.getAnalysisState();
		assertThat(() -> analysisController.setAnalysisState(AnalysisState.ABORTING),
			throwsException(IllegalStateException.class));
		assertThat(analysisController.getAnalysisState(), is(initState));
	}

	/**
	 * Asserts that the {@link AnalysisState} is {@code TERMINATED} when
	 * {@link AnalysisController#performAnalysis()} is finished.
	 */
	@Test
	public void analysisStateTerminatedAtEnd() {
		final Set<MeasurementTool> oneMeasurementTool = new HashSet<>();
		oneMeasurementTool.add(this.mockedMeasurementTool1);
		final Set<MeasurementResultAnalyser> oneMeasurementResultAnalyser = new HashSet<>();
		oneMeasurementResultAnalyser.add(this.mockedMeasurementResultAnalyser1);
		final Set<ProposedExpressionAnalyser> oneProposedExpressionAnalyser = new HashSet<>();
		oneProposedExpressionAnalyser.add(this.mockedProposedExpressionAnalyser1);

		// Test, where no one wants to contribute.
		this.resetMocks();
		final Blackboard blackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();

		when(this.mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard))).thenReturn(false);
		when(this.mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard))).thenReturn(false);

		final AnalysisController analysisController = new AnalysisController(blackboard, oneMeasurementTool,
			oneMeasurementResultAnalyser, oneProposedExpressionAnalyser);
		analysisController.performAnalysis();
		assertThat(analysisController.getAnalysisState(), is(AnalysisState.TERMINATED));
	}

	/**
	 * Inizialises an {@link AnalysisController} with a endless running
	 * {@link MeasurementTool}, used to test the
	 * {@link AnalysisController#setAnalysisState(AnalysisState)}.
	 *
	 * @param sleepingMeasurementTool measurementTool which is running.
	 *
	 * @return the Analysis Controller (as specified above)
	 */
	private AnalysisController setUpController(final SleepingMeasurementTool sleepingMeasurementTool) {
		final Set<MeasurementTool> oneMeasurementTool = new HashSet<>();
		oneMeasurementTool.add(this.mockedMeasurementTool1);
		final Set<MeasurementResultAnalyser> oneMeasurementResultAnalyser = new HashSet<>();
		oneMeasurementResultAnalyser.add(this.mockedMeasurementResultAnalyser1);
		final Set<ProposedExpressionAnalyser> oneProposedExpressionAnalyser = new HashSet<>();
		oneProposedExpressionAnalyser.add(this.mockedProposedExpressionAnalyser1);

		this.resetMocks();
		final Blackboard blackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();

		when(this.mockedMeasurementTool1.measure(anyObject())).then(sleepingMeasurementTool);
		when(this.mockedMeasurementResultAnalyser1
			.canContribute(new ReadOnlyMeasurementResultAnalyserBlackboardView(blackboard))).thenReturn(false);
		when(this.mockedProposedExpressionAnalyser1
			.canContribute(new ReadOnlyProposedExpressionAnalyserBlackboardView(blackboard))).thenReturn(false);

		final AnalysisController analysisController = new AnalysisController(blackboard, oneMeasurementTool,
			oneMeasurementResultAnalyser, oneProposedExpressionAnalyser);
		new Thread(analysisController::performAnalysis).start();
		synchronized (sleepingMeasurementTool) {
			while (!sleepingMeasurementTool.called) {
				try {
					sleepingMeasurementTool.wait();
				} catch (final InterruptedException interrupted) {
					// try again
				}
			}
		}
		return analysisController;
	}

	/**
	 * Stops the running {@link MeasurementTool} to stop the Analysis and the test.
	 *
	 * @param sleepingMeasurementTool which needs to be stopped.
	 */
	private void tearDownSetAnalysisTest(final SleepingMeasurementTool sleepingMeasurementTool) {
		synchronized (sleepingMeasurementTool) {
			sleepingMeasurementTool.sleep = false;
			sleepingMeasurementTool.notifyAll();
		}
	}

	/**
	 * An {@link Answer} that goes to sleep until it’s waking up. Useful to assure that
	 * tests happen <em>during</em> the analysis.
	 *
	 * @author Joshua Gleitze
	 */
	private class SleepingMeasurementTool implements Answer<List<MeasurementEvent>> {

		/**
		 * Whether we should sleep.
		 */
		private boolean sleep = true;

		/**
		 * Will be set to {@code true} once this answer was called.
		 */
		private boolean called;

		@Override
		public List<MeasurementEvent> answer(final InvocationOnMock invocation) throws Throwable {
			synchronized (this) {
				this.called = true;
				this.notifyAll();
			}
			synchronized (this) {
				while (this.sleep) {
					try {
						this.wait();
					} catch (final InterruptedException interrupt) {
						// Try again.
					}
				}
			}
			return new ArrayList<>();
		}

	}
}
