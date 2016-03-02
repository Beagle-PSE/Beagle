package de.uka.ipd.sdq.beagle.core.facade;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
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
	 * Test method for {@link BlackboardCreator#createBlackboard()}.
	 */
	@Test
	public void createBlackboard() {
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		blackboardCreator.setBranches(SEFF_BRANCH_FACTORY.getAllAsSet());
		blackboardCreator.setLoops(SEFF_LOOP_FACTORY.getAllAsSet());
		blackboardCreator.setExternalCalls(EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet());
		blackboardCreator.setFitnessFunction(EVA_EX_FACTORY.getOne());
		blackboardCreator.setProjectInformation(PROJECT_INFORMATION_FACTORY.getOne());
		blackboardCreator.setPcmMappings(mock(PcmBeagleMappings.class));
		ThrowingMethod method = () -> {
			blackboardCreator.createBlackboard();
		};

		assertThat("rdias must not null to create a blackboard.", method, throwsException(IllegalStateException.class));

		final BlackboardCreator blackboardCreator1 = new BlackboardCreator();
		method = () -> {
			blackboardCreator1.setRdias(RDIA_FACTORY.getAllAsSet());

			blackboardCreator1.setLoops(SEFF_LOOP_FACTORY.getAllAsSet());
			blackboardCreator1.setExternalCalls(EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet());
			blackboardCreator1.setFitnessFunction(EVA_EX_FACTORY.getOne());
			blackboardCreator1.setProjectInformation(PROJECT_INFORMATION_FACTORY.getOne());
			blackboardCreator.setPcmMappings(mock(PcmBeagleMappings.class));
			blackboardCreator1.createBlackboard();
		};

		assertThat("branches must not null to create a blackboard.", method,
			throwsException(IllegalStateException.class));

		final BlackboardCreator blackboardCreator2 = new BlackboardCreator();
		method = () -> {
			blackboardCreator2.setRdias(RDIA_FACTORY.getAllAsSet());
			blackboardCreator2.setBranches(SEFF_BRANCH_FACTORY.getAllAsSet());

			blackboardCreator2.setExternalCalls(EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet());
			blackboardCreator2.setFitnessFunction(EVA_EX_FACTORY.getOne());
			blackboardCreator2.setProjectInformation(PROJECT_INFORMATION_FACTORY.getOne());
			blackboardCreator.setPcmMappings(mock(PcmBeagleMappings.class));
			blackboardCreator2.createBlackboard();
		};

		assertThat("loopes must not null to create a blackboard.", method,
			throwsException(IllegalStateException.class));

		final BlackboardCreator blackboardCreator3 = new BlackboardCreator();
		method = () -> {
			blackboardCreator3.setRdias(RDIA_FACTORY.getAllAsSet());
			blackboardCreator3.setBranches(SEFF_BRANCH_FACTORY.getAllAsSet());
			blackboardCreator3.setLoops(SEFF_LOOP_FACTORY.getAllAsSet());

			blackboardCreator3.setFitnessFunction(EVA_EX_FACTORY.getOne());
			blackboardCreator3.setProjectInformation(PROJECT_INFORMATION_FACTORY.getOne());
			blackboardCreator.setPcmMappings(mock(PcmBeagleMappings.class));
			blackboardCreator3.createBlackboard();
		};

		assertThat("externalCalls must not null to create a blackboard.", method,
			throwsException(IllegalStateException.class));

		final BlackboardCreator blackboardCreator4 = new BlackboardCreator();
		method = () -> {
			blackboardCreator4.setRdias(RDIA_FACTORY.getAllAsSet());
			blackboardCreator4.setBranches(SEFF_BRANCH_FACTORY.getAllAsSet());
			blackboardCreator4.setLoops(SEFF_LOOP_FACTORY.getAllAsSet());
			blackboardCreator4.setExternalCalls(EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet());

			blackboardCreator4.setProjectInformation(PROJECT_INFORMATION_FACTORY.getOne());
			blackboardCreator.setPcmMappings(mock(PcmBeagleMappings.class));
			blackboardCreator4.createBlackboard();
		};

		assertThat("fitnessFunction must not null to create a blackboard.", method,
			throwsException(IllegalStateException.class));

		final BlackboardCreator blackboardCreator5 = new BlackboardCreator();
		method = () -> {
			blackboardCreator5.setRdias(RDIA_FACTORY.getAllAsSet());
			blackboardCreator5.setBranches(SEFF_BRANCH_FACTORY.getAllAsSet());
			blackboardCreator5.setLoops(SEFF_LOOP_FACTORY.getAllAsSet());
			blackboardCreator5.setExternalCalls(EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet());
			blackboardCreator5.setFitnessFunction(EVA_EX_FACTORY.getOne());
			blackboardCreator.setPcmMappings(mock(PcmBeagleMappings.class));

			blackboardCreator5.createBlackboard();
		};

		assertThat("projectInformation must not null to create a blackboard.", method,
			throwsException(IllegalStateException.class));

		final BlackboardCreator blackboardCreator6 = new BlackboardCreator();
		method = () -> {
			blackboardCreator6.setRdias(RDIA_FACTORY.getAllAsSet());
			blackboardCreator6.setBranches(SEFF_BRANCH_FACTORY.getAllAsSet());
			blackboardCreator6.setLoops(SEFF_LOOP_FACTORY.getAllAsSet());
			blackboardCreator6.setExternalCalls(EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet());
			blackboardCreator6.setFitnessFunction(EVA_EX_FACTORY.getOne());
			blackboardCreator6.setProjectInformation(PROJECT_INFORMATION_FACTORY.getOne());

			blackboardCreator6.createBlackboard();
		};

		assertThat("pcmMappings must not null to create a blackboard.", method,
			throwsException(IllegalStateException.class));

		final BlackboardCreator blackboardCreator7 = new BlackboardCreator();
		blackboardCreator7.setRdias(RDIA_FACTORY.getAllAsSet());
		blackboardCreator7.setBranches(SEFF_BRANCH_FACTORY.getAllAsSet());
		blackboardCreator7.setLoops(SEFF_LOOP_FACTORY.getAllAsSet());
		blackboardCreator7.setExternalCalls(EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet());
		blackboardCreator7.setFitnessFunction(EVA_EX_FACTORY.getOne());
		blackboardCreator7.setProjectInformation(PROJECT_INFORMATION_FACTORY.getOne());
		blackboardCreator7.setPcmMappings(mock(PcmBeagleMappings.class));
		blackboardCreator7.createBlackboard();

	}

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
