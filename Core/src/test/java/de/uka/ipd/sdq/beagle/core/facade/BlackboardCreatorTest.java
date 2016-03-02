package de.uka.ipd.sdq.beagle.core.facade;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.facade.BlackboardCreator;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.pcmconnection.PcmBeagleMappings;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFitnessFunctionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExternalCallParameterFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ProjectInformationFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ResourceDemandingInternalActionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffBranchFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffLoopFactory;

import org.junit.Test;

import java.util.Set;

/**
 * Tests {@link BlackboardCreator} and contains all test cases needed to check every
 * method.
 *
 * @author Michael Vogt
 */
public class BlackboardCreatorTest {

	/**
	 * A {@link ResourceDemandingInternalAction} factory to easily obtain new instances
	 * from.
	 */
	private static final ResourceDemandingInternalActionFactory RDIA_FACTORY =
		new ResourceDemandingInternalActionFactory();

	/**
	 * A {@link SeffBranch} factory to easily obtain new instances from.
	 */
	private static final SeffBranchFactory SEFF_BRANCH_FACTORY = new SeffBranchFactory();

	/**
	 * A {@link SeffLoop} factory to easily obtain new instances from.
	 */
	private static final SeffLoopFactory SEFF_LOOP_FACTORY = new SeffLoopFactory();

	/**
	 * An {@link ExternalCallParameter} factory to easily obtain new instances from.
	 */
	private static final ExternalCallParameterFactory EXTERNAL_CALL_PARAMETER_FACTORY =
		new ExternalCallParameterFactory();

	/**
	 * An {@link EvaluableExpressionFitnessFunction} factory to easily obtain new
	 * instances from.
	 */
	private static final EvaluableExpressionFitnessFunctionFactory EVA_EX_FACTORY =
		new EvaluableExpressionFitnessFunctionFactory();

	/**
	 * A project information factory to easily obtain new instances from.
	 */
	private static final ProjectInformationFactory PROJECT_INFORMATION_FACTORY = new ProjectInformationFactory();

	/**
	 * Test method for {@link BlackboardCreator#setPcmMappings()}.
	 */
	@Test
	public void setPcmMappings() {
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		final ThrowingMethod method = () -> {
			blackboardCreator.setPcmMappings(null);
		};
		assertThat(method, throwsException(NullPointerException.class));

		final PcmBeagleMappings testPcmBeagleMappings = mock(PcmBeagleMappings.class);
		blackboardCreator.setPcmMappings(testPcmBeagleMappings);
	}

	/**
	 * Test method for {@link BlackboardCreator#setRdias()}.
	 */
	@Test
	public void setRdias() {
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		final ThrowingMethod method = () -> {
			blackboardCreator.setRdias(null);
		};
		assertThat(method, throwsException(NullPointerException.class));

		final Set<ResourceDemandingInternalAction> rdias = RDIA_FACTORY.getAllAsSet();
		blackboardCreator.setRdias(rdias);
	}

	/**
	 * Test method for {@link BlackboardCreator#setBranches()}.
	 */
	@Test
	public void setBranches() {
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		final ThrowingMethod method = () -> {
			blackboardCreator.setBranches(null);
		};
		assertThat(method, throwsException(NullPointerException.class));

		final Set<SeffBranch> branches = SEFF_BRANCH_FACTORY.getAllAsSet();
		blackboardCreator.setBranches(branches);
	}

	/**
	 * Test method for {@link BlackboardCreator#setLoops()}.
	 */
	@Test
	public void setLoops() {
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		final ThrowingMethod method = () -> {
			blackboardCreator.setLoops(null);
		};
		assertThat(method, throwsException(NullPointerException.class));

		final Set<SeffLoop> loops = SEFF_LOOP_FACTORY.getAllAsSet();
		blackboardCreator.setLoops(loops);
	}

	/**
	 * Test method for {@link BlackboardCreator#setExternalCalls()}.
	 */
	@Test
	public void setExternalCalls() {
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		final ThrowingMethod method = () -> {
			blackboardCreator.setExternalCalls(null);
		};
		assertThat(method, throwsException(NullPointerException.class));

		final Set<ExternalCallParameter> externalCalls = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		blackboardCreator.setExternalCalls(externalCalls);
	}

	/**
	 * Test method for {@link BlackboardCreator#setFitnessFunction()}.
	 */
	@Test
	public void setFitnessFunction() {
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		final ThrowingMethod method = () -> {
			blackboardCreator.setFitnessFunction(null);
		};
		assertThat(method, throwsException(NullPointerException.class));

		final EvaluableExpressionFitnessFunction fitnessFunction = EVA_EX_FACTORY.getOne();
		blackboardCreator.setFitnessFunction(fitnessFunction);
	}

	/**
	 * Test method for {@link BlackboardCreator#setProjectInformation()}.
	 */
	@Test
	public void setProjectInformation() {
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		final ThrowingMethod method = () -> {
			blackboardCreator.setProjectInformation(null);
		};
		assertThat(method, throwsException(NullPointerException.class));

		final ProjectInformation projectInformation = PROJECT_INFORMATION_FACTORY.getOne();
		blackboardCreator.setProjectInformation(projectInformation);
	}

}
