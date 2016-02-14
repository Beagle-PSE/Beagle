package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.ConstantExpression;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.palladiosimulator.pcm.seff.impl.BranchActionImpl;
import org.palladiosimulator.pcm.seff.impl.ExternalCallActionImpl;
import org.palladiosimulator.pcm.seff.impl.InternalActionImpl;
import org.palladiosimulator.pcm.seff.impl.LoopActionImpl;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

/**
 * Tests {@link PcmRepositoryWriterAnnotatorEvaEx} and contains all test cases needed to
 * check all methods.
 * 
 * @author Annika Berger
 */
//@formatter:off
@PrepareForTest({PcmRepositoryWriterAnnotator.class, PcmRepositoryFileLoader.class,
	PcmRepositoryWriterAnnotatorEvaEx.class, PcmRepositoryWriter.class})
//@formatter:on
public class PcmRepositoryWriterAnnotatorEvaExTest {

	/**
	 * Rule loading PowerMock (to mock static methods).
	 */
	@Rule
	public PowerMockRule loadPowerMock = new PowerMockRule();

	/**
	 * Mocks {@link ResourceTypeMappings} to be able to run the tests.
	 * 
	 * @throws Exception if something went wrong while trying to mock the constructor of
	 *             {@link ResourceTypeMappings}.
	 *
	 */
	@Before
	public void prepare() throws Exception {
		final ResourceTypeMappings mockedMappings = PowerMockito.mock(ResourceTypeMappings.class);
		mockStatic(ResourceTypeMappings.class);
		whenNew(ResourceTypeMappings.class).withNoArguments().thenReturn(mockedMappings);
		doNothing().when(mockedMappings).initialise();
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryWriterAnnotatorEvaEx#PcmRepositoryWriterAnnotatorEvaEx()}.
	 */
	@Test
	public void constructor() {
		new PcmRepositoryWriterAnnotatorEvaEx();
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryWriterAnnotatorEvaEx#annotateEvaExFor(LoopActionImpl, EvaluableExpression)}
	 * .
	 * 
	 * <p>Asserts that {@link NullPointerException}s are thrown if one of the input
	 * parameters is {@code null}.
	 */
	@Test
	public void annotateEvaExForLoopActionImplEvaluableExpression() {
		final PcmRepositoryWriterAnnotatorEvaEx annotater = new PcmRepositoryWriterAnnotatorEvaEx();
		final LoopActionImpl loopAction = mock(LoopActionImpl.class);
		final EvaluableExpression evaEx = ConstantExpression.forValue(2.0);
		annotater.annotateEvaExFor(loopAction, evaEx);

		final LoopActionImpl nullAction = null;
		assertThat("Loop Action Impl must not be null", () -> annotater.annotateEvaExFor(nullAction, evaEx),
			throwsException(NullPointerException.class));

		assertThat("Evaluable expression must not be null", () -> annotater.annotateEvaExFor(loopAction, null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryWriterAnnotatorEvaEx#annotateEvaExFor(BranchActionImpl, EvaluableExpression)}
	 * .
	 * 
	 * <p>Asserts that {@link NullPointerException}s are thrown if one of the input
	 * parameters is {@code null} and no exception is thrown for valid calls.
	 */
	@Test
	public void annotateEvaExForBranchActionImplEvaluableExpression() {
		final PcmRepositoryWriterAnnotatorEvaEx annotater = new PcmRepositoryWriterAnnotatorEvaEx();
		final BranchActionImpl branchAction = mock(BranchActionImpl.class);
		final EvaluableExpression evaEx = ConstantExpression.forValue(2.0);
		annotater.annotateEvaExFor(branchAction, evaEx);

		final BranchActionImpl nullAction = null;
		assertThat("Branch Action Impl must not be null", () -> annotater.annotateEvaExFor(nullAction, evaEx),
			throwsException(NullPointerException.class));

		assertThat("Evaluable expression must not be null", () -> annotater.annotateEvaExFor(branchAction, null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryWriterAnnotatorEvaEx#annotateEvaExFor(InternalActionImpl, ResourceDemandType, EvaluableExpression)}
	 * .
	 * 
	 * <p>Asserts that {@link NullPointerException}s are thrown if one of the input
	 * parameters is {@code null} and no exception is thrown for valid calls.
	 */
	@Test
	public void annotateEvaExForInternalActionImplResourceDemandTypeEvaluableExpression() {
		final PcmRepositoryWriterAnnotatorEvaEx annotater = new PcmRepositoryWriterAnnotatorEvaEx();
		final InternalActionImpl internalAction = mock(InternalActionImpl.class);
		final ResourceDemandType type = ResourceDemandType.RESOURCE_TYPE_CPU;
		final EvaluableExpression evaEx = ConstantExpression.forValue(2.0);
		annotater.annotateEvaExFor(internalAction, type, evaEx);

		final InternalActionImpl nullAction = null;
		assertThat("Internal Action Impl must not be null", () -> annotater.annotateEvaExFor(nullAction, type, evaEx),
			throwsException(NullPointerException.class));

		assertThat("Evaluable expression must not be null",
			() -> annotater.annotateEvaExFor(internalAction, null, evaEx), throwsException(NullPointerException.class));

		assertThat("Evaluable expression must not be null",
			() -> annotater.annotateEvaExFor(internalAction, type, null), throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryWriterAnnotatorEvaEx#annotateEvaExFor(ExternalCallActionImpl, EvaluableExpression)}
	 * .
	 * 
	 * <p>Asserts that {@link NullPointerException}s are thrown if one of the input
	 * parameters is {@code null} and no exception is thrown for valid calls.
	 */
	@Test
	public void annotateEvaExForExternalCallActionImplEvaluableExpression() {
		final PcmRepositoryWriterAnnotatorEvaEx annotater = new PcmRepositoryWriterAnnotatorEvaEx();
		final ExternalCallActionImpl externalCallAction = mock(ExternalCallActionImpl.class);
		final EvaluableExpression evaEx = ConstantExpression.forValue(2.0);
		annotater.annotateEvaExFor(externalCallAction, evaEx);

		final ExternalCallActionImpl nullAction = null;
		assertThat("External Call Action Impl must not be null", () -> annotater.annotateEvaExFor(nullAction, evaEx),
			throwsException(NullPointerException.class));

		assertThat("Evaluable expression must not be null", () -> annotater.annotateEvaExFor(externalCallAction, null),
			throwsException(NullPointerException.class));
	}

}
