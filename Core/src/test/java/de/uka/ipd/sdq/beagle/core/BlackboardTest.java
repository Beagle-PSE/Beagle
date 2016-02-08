package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.NullHandlingMatchers.notAcceptingNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.theInstance;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFitnessFunctionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExternalCallParameterFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.MeasurementResultFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ResourceDemandingInternalActionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffBranchFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffLoopFactory;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Tests for {@link Blackboard}.
 *
 * @author Joshua Gleitze
 * @author Ansgar Spiegler
 */
public class BlackboardTest {

	/**
	 * A {@link Blackboard} factory to easily obtain new blackboards from.
	 */
	private static final BlackboardFactory BLACKBOARD_FACTORY = new BlackboardFactory();

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
	 * A {@link ResourceDemandingInternalAction} factory to easily obtain new instances
	 * from.
	 */
	private static final ResourceDemandingInternalActionFactory RDIA_FACTORY =
		new ResourceDemandingInternalActionFactory();

	/**
	 * An {@link EvaluableExpression} factory to easily obtain new instances from.
	 */
	private static final EvaluableExpressionFactory EVALUABLE_EXPRESSION_FACTORY = new EvaluableExpressionFactory();

	/**
	 * An {@link EvaluableExpressionFitnessFunction} factory to easily obtain new
	 * instances from.
	 */
	private static final EvaluableExpressionFitnessFunctionFactory EVA_EX_FACTORY =
		new EvaluableExpressionFitnessFunctionFactory();

	/**
	 * A Meausurement result factory to easily obtain new instances from.
	 */
	private static final MeasurementResultFactory MEAUSUREMENT_RESULT_FACTORY = new MeasurementResultFactory();

	/**
	 * Creates an empty SeffBranch-set.
	 *
	 * @return empty {@link SeffBranch} set.
	 */
	private Set<SeffBranch> getEmptySetOfSeffBranch() {
		return new HashSet<SeffBranch>();
	}

	/**
	 * Creates an empty SeffLoop-set.
	 *
	 * @return empty {@link SeffLoop} set.
	 */
	private Set<SeffLoop> getEmptySetOfSeffLoop() {
		return new HashSet<SeffLoop>();
	}

	/**
	 * Creates an empty RDIA-set.
	 *
	 * @return empty {@link ResourceDemandingInternalAction rdia} set.
	 */
	private Set<ResourceDemandingInternalAction> getEmptySetOfRdia() {
		return new HashSet<ResourceDemandingInternalAction>();
	}

	/**
	 * Creates an empty ExternalCallParameter-set.
	 *
	 * @return empty {@link ExternalCallParameter} set.
	 */
	private Set<ExternalCallParameter> getEmptySetOfExternalCallParameter() {
		return new HashSet<ExternalCallParameter>();
	}

	/**
	 * Test method for
	 * {@link Blackboard#Blackboard(java.util.Set, java.util.Set, java.util.Set)} . Assert
	 * that:
	 *
	 * <ul>
	 *
	 * <li> Modifying the set the blackboard was created with does not modify the
	 * blackboard content
	 *
	 * <li> Constructor does neither accept {@code null} parameters nor {@code null}
	 * within a Set
	 *
	 * <li> Constructor does not accept {@code null} parameters and throws a
	 * NullPointerException
	 *
	 * <li> Proper functionality for valid input
	 *
	 * </ul>
	 *
	 */
	@Test
	public void constructor() {

		// Checking if later modification of the given sets to the Blackboard does not
		// change the Blackboard sets.
		final Set<ResourceDemandingInternalAction> rdias = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranches = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoops = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> callParameters = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();

		final Set<ResourceDemandingInternalAction> rdiasTemp = new HashSet<>(rdias);
		final Set<SeffBranch> seffBranchesTemp = new HashSet<>(seffBranches);
		final Set<SeffLoop> seffLoopsTemp = new HashSet<>(seffLoops);
		final Set<ExternalCallParameter> callParametersTemp = new HashSet<>(callParameters);

		final Blackboard blackboardTemp =
			new Blackboard(rdiasTemp, seffBranchesTemp, seffLoopsTemp, callParametersTemp, EVA_EX_FACTORY.getOne());
		rdiasTemp.remove(rdiasTemp.iterator().next());
		seffBranchesTemp.remove(seffBranchesTemp.iterator().next());
		seffLoopsTemp.remove(seffLoopsTemp.iterator().next());
		callParametersTemp.remove(callParametersTemp.iterator().next());
		assertThat("Blackboard should create a copy from the given Sets to the constructor",
			blackboardTemp.getAllRdias(), is(equalTo(rdias)));
		assertThat("Blackboard should create a copy from the given Sets to the constructor",
			blackboardTemp.getAllSeffBranches(), is(equalTo(seffBranches)));
		assertThat("Blackboard should create a copy from the given Sets to the constructor",
			blackboardTemp.getAllSeffLoops(), is(equalTo(seffLoops)));
		assertThat("Blackboard should create a copy from the given Sets to the constructor",
			blackboardTemp.getAllExternalCallParameters(), is(equalTo(callParameters)));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			(rdiaColl) -> new Blackboard(new HashSet<>(rdiaColl), SEFF_BRANCH_FACTORY.getAllAsSet(),
				SEFF_LOOP_FACTORY.getAllAsSet(), EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet(),
				EVA_EX_FACTORY.getOne()),
			is(notAcceptingNull(RDIA_FACTORY.getAllAsSet())));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			(branchColl) -> new Blackboard(RDIA_FACTORY.getAllAsSet(), new HashSet<>(branchColl),
				SEFF_LOOP_FACTORY.getAllAsSet(), EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet(),
				EVA_EX_FACTORY.getOne()),
			is(notAcceptingNull(SEFF_BRANCH_FACTORY.getAllAsSet())));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			(loopColl) -> new Blackboard(RDIA_FACTORY.getAllAsSet(), SEFF_BRANCH_FACTORY.getAllAsSet(),
				new HashSet<>(loopColl), EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet(), EVA_EX_FACTORY.getOne()),
			is(notAcceptingNull(SEFF_LOOP_FACTORY.getAllAsSet())));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			(paramColl) -> new Blackboard(RDIA_FACTORY.getAllAsSet(), SEFF_BRANCH_FACTORY.getAllAsSet(),
				SEFF_LOOP_FACTORY.getAllAsSet(), new HashSet<>(paramColl), EVA_EX_FACTORY.getOne()),
			is(notAcceptingNull(EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet())));

		new Blackboard(RDIA_FACTORY.getAllAsSet(), SEFF_BRANCH_FACTORY.getAllAsSet(), SEFF_LOOP_FACTORY.getAllAsSet(),
			EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet(), EVA_EX_FACTORY.getOne());

	}

	/**
	 * Test method for {@link Blackboard#getAllRdias()}. Assert that Blackboard does not
	 * return {@code null} for getter-method
	 *
	 * <ul>
	 *
	 * <li> The Blackboard will not return {@code null} by any getter-method.
	 *
	 * <li> Once set, SeffElements will not change.
	 *
	 * <li> Manipulating the returned set will not affect the corresponding set on the
	 * blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void getAllRdias() {
		assertThat("Blackboard should not return null for getAllRdias()", BLACKBOARD_FACTORY.getEmpty().getAllRdias(),
			is(notNullValue()));

		assertThat("Empty blackboard should not contain any Rdias", BLACKBOARD_FACTORY.getEmpty().getAllRdias(),
			is(empty()));

		final Set<ResourceDemandingInternalAction> rdias = RDIA_FACTORY.getAllAsSet();
		final Blackboard blackboard = new Blackboard(rdias, this.getEmptySetOfSeffBranch(),
			this.getEmptySetOfSeffLoop(), this.getEmptySetOfExternalCallParameter(), EVA_EX_FACTORY.getOne());
		assertThat("Rdias on the blackboard should not change!", blackboard.getAllRdias(), is(equalTo(rdias)));

		final Set<ResourceDemandingInternalAction> blackboardSet = blackboard.getAllRdias();
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!", blackboard.getAllRdias(),
			is(equalTo(rdias)));

	}

	/**
	 * Test method for {@link Blackboard#getAllSeffBranches()}. Assert that Blackboard
	 * does not return {@code null} for getter-method
	 *
	 * <ul>
	 *
	 * <li> The Blackboard will not return {@code null} by any getter-method.
	 *
	 * <li> Once set, SeffElements will not change.
	 *
	 * <li> Manipulating the returned set will not affect the corresponding set on the
	 * blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void getAllSeffBranches() {
		assertThat("Blackboard should not return null for getAllSeffBranches()",
			BLACKBOARD_FACTORY.getEmpty().getAllSeffBranches(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any Rdias", BLACKBOARD_FACTORY.getEmpty().getAllSeffBranches(),
			is(empty()));

		final Set<SeffBranch> seffBranches = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Blackboard blackboard = new Blackboard(this.getEmptySetOfRdia(), seffBranches,
			this.getEmptySetOfSeffLoop(), this.getEmptySetOfExternalCallParameter(), EVA_EX_FACTORY.getOne());
		assertThat("SeffBranches on the blackboard should not change!", blackboard.getAllSeffBranches(),
			is(equalTo(seffBranches)));

		final Set<SeffBranch> blackboardSet = blackboard.getAllSeffBranches();
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			blackboard.getAllSeffBranches(), is(equalTo(seffBranches)));

	}

	/**
	 * Test method for {@link Blackboard#getAllSeffLoops()}. Assert that Blackboard does
	 * not return {@code null} for getter-method
	 *
	 * <ul>
	 *
	 * <li> The Blackboard will not return {@code null} by any getter-method.
	 *
	 * <li> Once set, SeffElements will not change.
	 *
	 * <li> Manipulating the returned set will not affect the corresponding set on the
	 * blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void getAllSeffLoops() {
		assertThat("Blackboard should not return null for getAllSeffLoops()",
			BLACKBOARD_FACTORY.getEmpty().getAllSeffLoops(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any SeffLoops", BLACKBOARD_FACTORY.getEmpty().getAllSeffLoops(),
			is(empty()));

		final Set<SeffLoop> seffLoops = SEFF_LOOP_FACTORY.getAllAsSet();
		final Blackboard blackboard = new Blackboard(this.getEmptySetOfRdia(), this.getEmptySetOfSeffBranch(),
			seffLoops, this.getEmptySetOfExternalCallParameter(), EVA_EX_FACTORY.getOne());
		assertThat("SeffLoops on the blackboard should not change!", blackboard.getAllSeffLoops(),
			is(equalTo(seffLoops)));

		final Set<SeffLoop> blackboardSet = blackboard.getAllSeffLoops();
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			blackboard.getAllSeffLoops(), is(equalTo(seffLoops)));

	}

	/**
	 * Test method for {@link Blackboard#getAllExternalCallParameters()}. Assert that
	 * Blackboard does not return {@code null} for getter-method
	 *
	 * <ul>
	 *
	 * <li> The Blackboard will not return {@code null} by any getter-method.
	 *
	 * <li> Once set, SeffElements will not change.
	 *
	 * <li> Manipulating the returned set will not affect the corresponding set on the
	 * blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void getAllExternalCallParameters() {
		assertThat("Blackboard should not return null for getAllExternalCallParameters()",
			BLACKBOARD_FACTORY.getEmpty().getAllExternalCallParameters(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any ExternalCallParameters",
			BLACKBOARD_FACTORY.getEmpty().getAllExternalCallParameters(), is(empty()));

		final Set<ExternalCallParameter> externalCallParameters = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		final Blackboard blackboard = new Blackboard(this.getEmptySetOfRdia(), this.getEmptySetOfSeffBranch(),
			this.getEmptySetOfSeffLoop(), externalCallParameters, EVA_EX_FACTORY.getOne());
		assertThat("ExternalCallParameters on the blackboard should not change!",
			blackboard.getAllExternalCallParameters(), is(equalTo(externalCallParameters)));

		final Set<ExternalCallParameter> blackboardSet = blackboard.getAllExternalCallParameters();
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			blackboard.getAllExternalCallParameters(), is(equalTo(externalCallParameters)));
	}

	/**
	 * Test method for {@link Blackboard#getRdiasToBeMeasured()}. Assert That
	 *
	 * <ul>
	 *
	 * <li> Getter does not return {@code null}.
	 *
	 * <li> Manipulating the returned set will not affect the corresponding set on the
	 * blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void getRdiasToBeMeasured() {

		assertThat("Blackboard should not return null for getRdiasToBeMeasured()",
			BLACKBOARD_FACTORY.getEmpty().getRdiasToBeMeasured(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any RdiasToBeMeasured",
			BLACKBOARD_FACTORY.getEmpty().getRdiasToBeMeasured(), is(empty()));

		final Blackboard testedBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final Set<ResourceDemandingInternalAction> blackboardSet = testedBlackboard.getRdiasToBeMeasured();
		final Set<ResourceDemandingInternalAction> rdiasToBeMeasured =
			new HashSet<ResourceDemandingInternalAction>(blackboardSet);
		blackboardSet.remove(rdiasToBeMeasured.iterator().next());
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			testedBlackboard.getRdiasToBeMeasured(), is(equalTo(rdiasToBeMeasured)));

	}

	/**
	 * Test method for {@link Blackboard#getSeffBranchesToBeMeasured()}. Assert That
	 *
	 * <ul>
	 *
	 * <li> Getter does not return {@code null}.
	 *
	 * <li> Manipulating the returned set will not affect the corresponding set on the
	 * blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void getSeffBranchesToBeMeasured() {
		assertThat("Blackboard should not return null for getSeffBranchesToBeMeasured()",
			BLACKBOARD_FACTORY.getEmpty().getSeffBranchesToBeMeasured(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any SeffBranchesToBeMeasured",
			BLACKBOARD_FACTORY.getEmpty().getSeffBranchesToBeMeasured(), is(empty()));

		final Blackboard testedBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final Set<SeffBranch> blackboardSet = testedBlackboard.getSeffBranchesToBeMeasured();
		final Set<SeffBranch> branchesToBeMeasured = new HashSet<SeffBranch>(blackboardSet);
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			testedBlackboard.getSeffBranchesToBeMeasured(), is(equalTo(branchesToBeMeasured)));
	}

	/**
	 * Test method for {@link Blackboard#getSeffLoopsToBeMeasured()}. Assert That
	 *
	 * <ul>
	 *
	 * <li> Getter does not return {@code null}.
	 *
	 * <li> Manipulating the returned set will not affect the corresponding set on the
	 * blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void getSeffLoopsToBeMeasured() {
		assertThat("Blackboard should not return null for getSeffLoopsToBeMeasured()",
			BLACKBOARD_FACTORY.getEmpty().getSeffLoopsToBeMeasured(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any SeffLoopsToBeMeasured",
			BLACKBOARD_FACTORY.getEmpty().getSeffLoopsToBeMeasured(), is(empty()));

		final Blackboard testedBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final Set<SeffLoop> blackboardSet = testedBlackboard.getSeffLoopsToBeMeasured();
		final Set<SeffLoop> loopsToBeMeasured = new HashSet<SeffLoop>(blackboardSet);
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			testedBlackboard.getSeffLoopsToBeMeasured(), is(equalTo(loopsToBeMeasured)));
	}

	/**
	 * Test method for {@link Blackboard#getExternalCallParametersToBeMeasured()}. Assert
	 * That
	 *
	 * <ul>
	 *
	 * <li> Getter does not return {@code null}.
	 *
	 * <li> Manipulating the returned set will not affect the corresponding set on the
	 * blackboard.
	 *
	 * </ul>
	 */
	@Test
	public void getExternalCallParametersToBeMeasured() {
		assertThat("Blackboard should not return null for getExternalCallParametersToBeMeasured()",
			BLACKBOARD_FACTORY.getEmpty().getExternalCallParametersToBeMeasured(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any ExternalCallParametersToBeMeasured",
			BLACKBOARD_FACTORY.getEmpty().getExternalCallParametersToBeMeasured(), is(empty()));

		final Blackboard testedBlackboard = BLACKBOARD_FACTORY.getFull();
		final Set<ExternalCallParameter> blackboardSet = testedBlackboard.getExternalCallParametersToBeMeasured();
		final Set<ExternalCallParameter> parametersToBeMeasured = new HashSet<ExternalCallParameter>(blackboardSet);
		blackboardSet.removeAll(blackboardSet);
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			testedBlackboard.getExternalCallParametersToBeMeasured(), is(equalTo(parametersToBeMeasured)));
	}

	/**
	 * Test method for {@link Blackboard#addToBeMeasuredRdias(java.util.Collection)} and
	 * {@link Blackboard#addToBeMeasuredRdias(ResourceDemandingInternalAction[])} .
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as or in the argument.
	 *
	 * <li>Unknown MeasurableSeffElements throw an IllegalArgumentException.
	 *
	 * </ul>
	 */
	@Test
	public void addToBeMeasuredRdias() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> ecpSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		Blackboard blackboardTemp =
			new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		blackboardTemp.addToBeMeasuredRdias(rdiaSet);
		blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		assertThat(blackboardTemp::addToBeMeasuredRdias, is(notAcceptingNull(RDIA_FACTORY.getAll())));
		assertThat(blackboardTemp::addToBeMeasuredRdias, is(notAcceptingNull(Arrays.asList(RDIA_FACTORY.getAll()))));

		final Set<ResourceDemandingInternalAction> rdiaSetTwo = this.getEmptySetOfRdia();
		rdiaSetTwo.add(RDIA_FACTORY.getOne());
		assertThat("It must not be possible to add unknown elements to the Blackboard Measurement sets!",
			() -> BLACKBOARD_FACTORY.getEmpty().addToBeMeasuredRdias(rdiaSetTwo),
			throwsException(IllegalArgumentException.class));

		blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		blackboardTemp.addToBeMeasuredRdias(rdiaSet.toArray(new ResourceDemandingInternalAction[0]));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addToBeMeasuredSeffBranches(java.util.Collection)} and
	 * {@link Blackboard#addToBeMeasuredSeffBranches(SeffBranch[])} . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as or in the argument.
	 *
	 * <li>Unknown MeasurableSeffElements throw an IllegalArgumentException.
	 *
	 * </ul>
	 */
	@Test
	public void addToBeMeasuredSeffBranches() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> ecpSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		Blackboard blackboardTemp =
			new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		blackboardTemp.addToBeMeasuredSeffBranches(seffBranchSet);
		blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		assertThat(blackboardTemp::addToBeMeasuredSeffBranches, is(notAcceptingNull(SEFF_BRANCH_FACTORY.getAll())));
		assertThat(blackboardTemp::addToBeMeasuredSeffBranches,
			is(notAcceptingNull(Arrays.asList(SEFF_BRANCH_FACTORY.getAll()))));

		final Set<SeffBranch> seffBranchSetTwo = this.getEmptySetOfSeffBranch();
		seffBranchSetTwo.add(SEFF_BRANCH_FACTORY.getOne());
		assertThat("It must not be possible to add unknown elements to the Blackboard Measurement sets!",
			() -> BLACKBOARD_FACTORY.getEmpty().addToBeMeasuredSeffBranches(seffBranchSetTwo),
			throwsException(IllegalArgumentException.class));

		blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		blackboardTemp.addToBeMeasuredSeffBranches(seffBranchSet.toArray(new SeffBranch[0]));
	}

	/**
	 * Test method for {@link Blackboard#addToBeMeasuredSeffLoops(SeffLoop[])} and
	 * {@link Blackboard#addToBeMeasuredSeffLoops(java.util.Collection)}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as or in the argument.
	 *
	 * <li>Unknown MeasurableSeffElements throw an IllegalArgumentException.
	 *
	 * </ul>
	 */
	@Test
	public void addToBeMeasuredSeffLoops() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> ecpSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		Blackboard blackboardTemp =
			new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		blackboardTemp.addToBeMeasuredSeffLoops(seffLoopSet);
		blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		assertThat(blackboardTemp::addToBeMeasuredSeffLoops, is(notAcceptingNull(SEFF_LOOP_FACTORY.getAll())));
		assertThat(blackboardTemp::addToBeMeasuredSeffLoops,
			is(notAcceptingNull(Arrays.asList(SEFF_LOOP_FACTORY.getAll()))));

		final Set<SeffLoop> seffLoopSetTwo = this.getEmptySetOfSeffLoop();
		seffLoopSetTwo.add(SEFF_LOOP_FACTORY.getOne());
		assertThat("It must not be possible to add unknown elements to the Blackboard Measurement sets!",
			() -> BLACKBOARD_FACTORY.getEmpty().addToBeMeasuredSeffLoops(seffLoopSetTwo),
			throwsException(IllegalArgumentException.class));

		blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		blackboardTemp.addToBeMeasuredSeffLoops(seffLoopSet.toArray(new SeffLoop[0]));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addToBeMeasuredExternalCallParameters(ExternalCallParameter[])}
	 * and {@link Blackboard#addToBeMeasuredExternalCallParameters(java.util.Collection)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as or in the argument.
	 *
	 * <li>Unknown MeasurableSeffElements throw an IllegalArgumentException.
	 *
	 * </ul>
	 */
	@Test
	public void addToBeMeasuredExternalCallParameters() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> ecpSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		Blackboard blackboardTemp =
			new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		blackboardTemp.addToBeMeasuredExternalCallParameters(ecpSet);
		blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		assertThat(blackboardTemp::addToBeMeasuredExternalCallParameters,
			is(notAcceptingNull(EXTERNAL_CALL_PARAMETER_FACTORY.getAll())));
		assertThat(blackboardTemp::addToBeMeasuredExternalCallParameters,
			is(notAcceptingNull(Arrays.asList(EXTERNAL_CALL_PARAMETER_FACTORY.getAll()))));

		final Set<ExternalCallParameter> externalCallParameterSetTwo = this.getEmptySetOfExternalCallParameter();
		externalCallParameterSetTwo.add(EXTERNAL_CALL_PARAMETER_FACTORY.getOne());
		assertThat("It must not be possible to add unknown elements to the Blackboard Measurement sets!",
			() -> BLACKBOARD_FACTORY.getEmpty().addToBeMeasuredExternalCallParameters(externalCallParameterSetTwo),
			throwsException(IllegalArgumentException.class));

		blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet, EVA_EX_FACTORY.getOne());
		blackboardTemp.addToBeMeasuredExternalCallParameters(ecpSet.toArray(new ExternalCallParameter[0]));
	}

	/**
	 * Test method for
	 * {@link Blackboard#getMeasurementResultsFor(ResourceDemandingInternalAction)} .
	 * Assert that
	 *
	 * <ul>
	 *
	 * <li> Does not return {@code null} for valid call parameters.
	 *
	 * <li> Throws an exception for {@code null} as parameter.
	 *
	 * <li>Unknown MeasurableSeffElements throw an IllegalArgumentException.
	 *
	 * </ul>
	 */
	@Test
	public void getMeasurementResultsForResourceDemandingInternalAction() {

		assertThat(
			"Blackboard should not return null for valid input parameters in"
				+ "getMeasurementResultsFor(ResourceDemandingInternalAction)",
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getMeasurementResultsFor(
				BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllRdias().iterator().next()),
			is(notNullValue()));

		assertThat(
			"It must not be possible to call getMeasurementResultsFor(ResourceDemandingInternalAction) for null as parameter",
			() -> BLACKBOARD_FACTORY.getEmpty().getMeasurementResultsFor((ResourceDemandingInternalAction) null),
			throwsException(NullPointerException.class));

		final ResourceDemandingInternalAction anyRdia = RDIA_FACTORY.getOne();
		assertThat("It must not be possible to ask for unknown elements on the Blackboard Measurement Result sets!",
			() -> BLACKBOARD_FACTORY.getEmpty().getMeasurementResultsFor(anyRdia),
			throwsException(IllegalArgumentException.class));

		Blackboard testedBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final Iterator<ResourceDemandingInternalAction> rdias = testedBlackboard.getAllRdias().iterator();
		ResourceDemandingInternalAction rdia = rdias.next();
		final ResourceDemandMeasurementResult result = MEAUSUREMENT_RESULT_FACTORY.getRdiaResults()[0];
		testedBlackboard.addMeasurementResultFor(rdia, result);
		assertThat(testedBlackboard.getMeasurementResultsFor(rdia), contains(result));
		assertThat(testedBlackboard.getMeasurementResultsFor(rdias.next()), is(empty()));

		testedBlackboard = BLACKBOARD_FACTORY.getFull();
		rdia = testedBlackboard.getAllRdias().iterator().next();
		final Set<ResourceDemandMeasurementResult> blackboardSet = testedBlackboard.getMeasurementResultsFor(rdia);
		final Set<ResourceDemandMeasurementResult> results = new HashSet<>(blackboardSet);
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("The blackboard must return copies of its sets!", testedBlackboard.getMeasurementResultsFor(rdia),
			is(equalTo(results)));
	}

	/**
	 * Test method for {@link Blackboard#getMeasurementResultsFor(SeffBranch)} Assert that
	 *
	 * <ul>
	 *
	 * <li> Does not return {@code null} for valid call parameters.
	 *
	 * <li> Throws an exception for {@code null} as parameter.
	 *
	 * <li>Unknown MeasurableSeffElements throw an IllegalArgumentException.
	 *
	 * </ul>
	 *
	 */
	@Test
	public void getMeasurementResultsForSeffBranch() {
		assertThat(
			"Blackboard should not return null for valid input parameters in getMeasurementResultsFor(SeffBranch)",
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getMeasurementResultsFor(
				BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllSeffBranches().iterator().next()),
			is(notNullValue()));

		assertThat("It must not be possible to call getMeasurementResultsFor(SeffBranch) for null as parameter",
			() -> BLACKBOARD_FACTORY.getEmpty().getMeasurementResultsFor((SeffBranch) null),
			throwsException(NullPointerException.class));

		final SeffBranch anySeffBranch = SEFF_BRANCH_FACTORY.getOne();
		assertThat("It must not be possible to ask for unknown elements on the Blackboard Measurement Result sets!",
			() -> BLACKBOARD_FACTORY.getEmpty().getMeasurementResultsFor(anySeffBranch),
			throwsException(IllegalArgumentException.class));

		Blackboard testedBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final Iterator<SeffBranch> branches = testedBlackboard.getAllSeffBranches().iterator();
		SeffBranch seffBranch = branches.next();
		final BranchDecisionMeasurementResult result = MEAUSUREMENT_RESULT_FACTORY.getBranchResults()[0];
		testedBlackboard.addMeasurementResultFor(seffBranch, result);
		assertThat(testedBlackboard.getMeasurementResultsFor(seffBranch), contains(result));
		assertThat(testedBlackboard.getMeasurementResultsFor(branches.next()), is(empty()));

		testedBlackboard = BLACKBOARD_FACTORY.getFull();
		seffBranch = testedBlackboard.getAllSeffBranches().iterator().next();
		final Set<BranchDecisionMeasurementResult> blackboardSet =
			testedBlackboard.getMeasurementResultsFor(seffBranch);
		final Set<BranchDecisionMeasurementResult> results = new HashSet<>(blackboardSet);
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("The blackboard must return copies of its sets!",
			testedBlackboard.getMeasurementResultsFor(seffBranch), is(equalTo(results)));
	}

	/**
	 * Test method for {@link Blackboard#getMeasurementResultsFor(SeffLoop)} . Assert that
	 *
	 * <ul>
	 *
	 * <li> Does not return {@code null} for valid call parameters.
	 *
	 * <li> Throws an exception for {@code null} as parameter.
	 *
	 * <li>Unknown MeasurableSeffElements throw an IllegalArgumentException.
	 *
	 * </ul>
	 *
	 */
	@Test
	public void getMeasurementResultsForSeffLoop() {

		assertThat("Blackboard should not return null for valid input parameters in getMeasurementResultsFor(SeffLoop)",
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getMeasurementResultsFor(
				BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllSeffLoops().iterator().next()),
			is(notNullValue()));

		assertThat("It must not be possible to call getMeasurementResultsFor(SeffLoop) for null as parameter",
			() -> BLACKBOARD_FACTORY.getEmpty().getMeasurementResultsFor((SeffLoop) null),
			throwsException(NullPointerException.class));

		final SeffLoop anySeffLoop = SEFF_LOOP_FACTORY.getOne();
		assertThat("It must not be possible to ask for unknown elements on the Blackboard Measurement Result sets!",
			() -> BLACKBOARD_FACTORY.getEmpty().getMeasurementResultsFor(anySeffLoop),
			throwsException(IllegalArgumentException.class));

		Blackboard testedBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final Iterator<SeffLoop> loops = testedBlackboard.getAllSeffLoops().iterator();
		SeffLoop seffLoop = loops.next();
		final LoopRepetitionCountMeasurementResult result = MEAUSUREMENT_RESULT_FACTORY.getLoopResults()[0];
		testedBlackboard.addMeasurementResultFor(seffLoop, result);
		assertThat(testedBlackboard.getMeasurementResultsFor(seffLoop), contains(result));
		assertThat(testedBlackboard.getMeasurementResultsFor(loops.next()), is(empty()));

		testedBlackboard = BLACKBOARD_FACTORY.getFull();
		seffLoop = testedBlackboard.getAllSeffLoops().iterator().next();
		final Set<LoopRepetitionCountMeasurementResult> blackboardSet =
			testedBlackboard.getMeasurementResultsFor(seffLoop);
		final Set<LoopRepetitionCountMeasurementResult> results = new HashSet<>(blackboardSet);
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("The blackboard must return copies of its sets!",
			testedBlackboard.getMeasurementResultsFor(seffLoop), is(equalTo(results)));
	}

	/**
	 * Test method for {@link Blackboard#getMeasurementResultsFor(ExternalCallParameter)}
	 * Assert that
	 *
	 * <ul>
	 *
	 * <li> Does not return {@code null} for valid call parameters.
	 *
	 * <li> Throws an exception for {@code null} as parameter.
	 *
	 * <li>Unknown MeasurableSeffElements throw an IllegalArgumentException.
	 *
	 * </ul>
	 *
	 */
	@Test
	public void getMeasurementResultsForExternalCallParameter() {

		assertThat(
			"Blackboard should not return null for valid input parameters in getMeasurementResultsFor(ExternalCallParameter)",
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getMeasurementResultsFor(
				BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllExternalCallParameters().iterator().next()),
			is(notNullValue()));

		assertThat(
			"It must not be possible to call getMeasurementResultsFor(externalCallParameter) for null as parameter",
			() -> BLACKBOARD_FACTORY.getEmpty().getMeasurementResultsFor((ExternalCallParameter) null),
			throwsException(NullPointerException.class));

		final ExternalCallParameter anyExternalCallParameter = EXTERNAL_CALL_PARAMETER_FACTORY.getOne();
		assertThat("It must not be possible to ask for unknown elements on the Blackboard Measurement Result sets!",
			() -> BLACKBOARD_FACTORY.getEmpty().getMeasurementResultsFor(anyExternalCallParameter),
			throwsException(IllegalArgumentException.class));

		Blackboard testedBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final Iterator<ExternalCallParameter> parameters = testedBlackboard.getAllExternalCallParameters().iterator();
		ExternalCallParameter externalCallParameter = parameters.next();
		final ParameterChangeMeasurementResult result = MEAUSUREMENT_RESULT_FACTORY.getParameterResults()[0];
		testedBlackboard.addMeasurementResultFor(externalCallParameter, result);
		assertThat(testedBlackboard.getMeasurementResultsFor(externalCallParameter), contains(result));
		assertThat(testedBlackboard.getMeasurementResultsFor(parameters.next()), is(empty()));

		testedBlackboard = BLACKBOARD_FACTORY.getFull();
		externalCallParameter = testedBlackboard.getAllExternalCallParameters().iterator().next();
		final Set<ParameterChangeMeasurementResult> blackboardSet =
			testedBlackboard.getMeasurementResultsFor(externalCallParameter);
		final Set<ParameterChangeMeasurementResult> results = new HashSet<>(blackboardSet);
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("The blackboard must return copies of its sets!",
			testedBlackboard.getMeasurementResultsFor(externalCallParameter), is(equalTo(results)));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addMeasurementResultFor(ResourceDemandingInternalAction, ResourceDemandMeasurementResult)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>Accepting for valid input parameters.
	 *
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * <li>Adding measurementResult for unknown Elements throws an
	 * IllegalArgumentException
	 *
	 * </ul>
	 */
	@Test
	public void addMeasurementResultForResourceDemandingInternalActionResourceDemandMeasurementResult() {

		final Blackboard testBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final ResourceDemandMeasurementResult rdiaResult = new ResourceDemandMeasurementResult(1.7);
		testBlackboard.addMeasurementResultFor(testBlackboard.getAllRdias().iterator().next(), rdiaResult);

		assertThat("It must not be possible to add a measurement result for null",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(null, new ResourceDemandMeasurementResult(1.5)),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(RDIA_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));

		final ResourceDemandingInternalAction rdia = RDIA_FACTORY.getOne();
		assertThat("It must not be possible to add measurement results to unknown elements!",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(rdia, rdiaResult),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addMeasurementResultFor(SeffBranch, BranchDecisionMeasurementResult)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>Accepting for valid input parameters.
	 *
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * <li>Adding measurementResult for unknown Elements throws an
	 * IllegalArgumentException
	 *
	 * </ul>
	 */
	@Test
	public void addMeasurementResultForSeffBranchBranchDecisionMeasurementResult() {

		final BranchDecisionMeasurementResult branchResult = new BranchDecisionMeasurementResult(2);
		final Blackboard testBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		testBlackboard.addMeasurementResultFor(testBlackboard.getAllSeffBranches().iterator().next(), branchResult);

		assertThat("It must not be possible to add a measurement result for null",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(null, new BranchDecisionMeasurementResult(3)),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(SEFF_BRANCH_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));

		final SeffBranch seffBranch = SEFF_BRANCH_FACTORY.getOne();
		assertThat("It must not be possible to add measurement results to unknown elements!",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(seffBranch, branchResult),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addMeasurementResultFor(SeffLoop, LoopRepetitionCountMeasurementResult)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>Accepting for valid input parameters.
	 *
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * <li>Adding measurementResult for unknown Elements throws an
	 * IllegalArgumentException
	 *
	 * </ul>
	 */
	@Test
	public void addMeasurementResultForSeffLoopLoopRepetitionCountMeasurementResult() {

		final Blackboard testBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final LoopRepetitionCountMeasurementResult loopResult = new LoopRepetitionCountMeasurementResult(5);
		testBlackboard.addMeasurementResultFor(testBlackboard.getAllSeffLoops().iterator().next(), loopResult);

		assertThat(
			"It must not be possible to add a measurement result for null", () -> BLACKBOARD_FACTORY.getEmpty()
				.addMeasurementResultFor(null, new LoopRepetitionCountMeasurementResult(5)),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(SEFF_LOOP_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));

		final SeffLoop seffLoop = SEFF_LOOP_FACTORY.getOne();
		assertThat("It must not be possible to add measurement results to unknown elements!",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(seffLoop, loopResult),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addMeasurementResultFor(ExternalCallParameter, ParameterChangeMeasurementResult)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>Accepting for valid input parameters.
	 *
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * <li>Adding measurementResult for unknown Elements throws an
	 * IllegalArgumentException
	 *
	 * </ul>
	 */
	@Test
	public void addMeasurementResultForExternalCallParameterParameterChangeMeasurementResult() {

		final Blackboard testBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final ParameterChangeMeasurementResult parameterResult = new ParameterChangeMeasurementResult();
		testBlackboard.addMeasurementResultFor(testBlackboard.getAllExternalCallParameters().iterator().next(),
			parameterResult);

		assertThat("It must not be possible to add a measurement result for null",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(null, new ParameterChangeMeasurementResult()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(EXTERNAL_CALL_PARAMETER_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));

		final ExternalCallParameter ecpElement = EXTERNAL_CALL_PARAMETER_FACTORY.getOne();
		assertThat("It must not be possible to add measurement results to unknown elements!",
			() -> BLACKBOARD_FACTORY.getEmpty().addMeasurementResultFor(ecpElement, parameterResult),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for {@link Blackboard#getProposedExpressionFor(MeasurableSeffElement)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>The data written through addProposedExpressionFor is returned by this method.
	 *
	 * <li> Does not return {@code null} for valid call parameters.
	 *
	 * <li>{@code null} cannot be passed.
	 *
	 * <li>Asking for an unknown SeffElement will cause an IllegalArgumentException
	 *
	 * </ul>
	 */
	@Test
	public void getProposedExpressionFor() {
		final Blackboard testBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final Iterator<ResourceDemandingInternalAction> rdias = testBlackboard.getAllRdias().iterator();
		final ResourceDemandingInternalAction rdia = rdias.next();
		final EvaluableExpression evaEx = EVALUABLE_EXPRESSION_FACTORY.getOne();
		testBlackboard.addProposedExpressionFor(rdia, evaEx);

		assertThat("Proposed Expression should return an expression that have been added by \"addProposedExpression\"",
			testBlackboard.getProposedExpressionFor(rdia), contains(evaEx));
		assertThat(testBlackboard.getProposedExpressionFor(rdias.next()), is(empty()));

		assertThat("Proposed Expression should not return null for valid input parameters!",
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getProposedExpressionFor(
				BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllRdias().iterator().next()),
			is(notNullValue()));
		assertThat("Proposed Expression should not return null for valid input parameters!",
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getProposedExpressionFor(
				BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllSeffBranches().iterator().next()),
			is(notNullValue()));
		assertThat("Proposed Expression should not return null for valid input parameters!",
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getProposedExpressionFor(
				BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllSeffLoops().iterator().next()),
			is(notNullValue()));
		assertThat("Proposed Expression should not return null for valid input parameters!",
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getProposedExpressionFor(
				BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllExternalCallParameters().iterator().next()),
			is(notNullValue()));

		assertThat("It must not be possible get proposed expressions for null",
			() -> BLACKBOARD_FACTORY.getEmpty().getProposedExpressionFor(null),
			throwsException(NullPointerException.class));

		final ExternalCallParameter ecpElement = EXTERNAL_CALL_PARAMETER_FACTORY.getOne();
		assertThat("It must not be possible to ask for proposed Expressions of unknown elements!",
			() -> BLACKBOARD_FACTORY.getEmpty().getProposedExpressionFor(ecpElement),
			throwsException(IllegalArgumentException.class));

		final Blackboard testedBlackboard = BLACKBOARD_FACTORY.getFull();
		final MeasurableSeffElement seffElement = testedBlackboard.getAllExternalCallParameters().iterator().next();
		final Set<EvaluableExpression> blackboardSet = testedBlackboard.getProposedExpressionFor(seffElement);
		final Set<EvaluableExpression> expressions = new HashSet<>(blackboardSet);
		blackboardSet.remove(blackboardSet.iterator().next());
		assertThat("The blackboard must return copies", testedBlackboard.getProposedExpressionFor(seffElement),
			is(equalTo(expressions)));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addProposedExpressionFor(MeasurableSeffElement, EvaluableExpression)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li> No exceptions for valid inputs.
	 *
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * <li> Adding proposed expressions to unknown elements should cause an
	 * IllegalArgumentException
	 *
	 * </ul>
	 */
	@Test
	public void addProposedExpressionFor() {
		final Set<EvaluableExpression> evaEx = new HashSet<EvaluableExpression>();
		evaEx.add(EVALUABLE_EXPRESSION_FACTORY.getOne());
		BLACKBOARD_FACTORY.getWithToBeMeasuredContent().addProposedExpressionFor(
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllRdias().iterator().next(),
			EVALUABLE_EXPRESSION_FACTORY.getOne());
		BLACKBOARD_FACTORY.getWithToBeMeasuredContent().addProposedExpressionFor(
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllSeffBranches().iterator().next(),
			EVALUABLE_EXPRESSION_FACTORY.getOne());
		BLACKBOARD_FACTORY.getWithToBeMeasuredContent().addProposedExpressionFor(
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllSeffLoops().iterator().next(),
			EVALUABLE_EXPRESSION_FACTORY.getOne());
		BLACKBOARD_FACTORY.getWithToBeMeasuredContent().addProposedExpressionFor(
			BLACKBOARD_FACTORY.getWithToBeMeasuredContent().getAllExternalCallParameters().iterator().next(),
			EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat("It must not be possible to add a proposed expression for null",
			() -> BLACKBOARD_FACTORY.getEmpty().addProposedExpressionFor(null, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as a proposed expression",
			() -> BLACKBOARD_FACTORY.getEmpty().addProposedExpressionFor(SEFF_BRANCH_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));

		final ExternalCallParameter ecpElement = EXTERNAL_CALL_PARAMETER_FACTORY.getOne();
		assertThat(
			"It must not be possible to add an proposed Expression to unknown elements!", () -> BLACKBOARD_FACTORY
				.getEmpty().addProposedExpressionFor(ecpElement, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for {@link Blackboard#getFinalExpressionFor(MeasurableSeffElement)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li> An earlier call
	 *
	 * <li> Null should be returned for not yet annotated FinalExpressions
	 *
	 * <li>No exceptions for valid input.
	 *
	 * <li>{@code null} cannot be passed.
	 *
	 * <li> Throwing an {@link IllegalArgumentException} if seffElement is not on the
	 * Blackboard
	 *
	 * </ul>
	 */
	@Test
	public void getFinalExpressionFor() {
		Blackboard testBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();

		final ResourceDemandingInternalAction rdia = testBlackboard.getAllRdias().iterator().next();
		final EvaluableExpression evaEx = EVALUABLE_EXPRESSION_FACTORY.getOne();
		testBlackboard.setFinalExpressionFor(rdia, evaEx);
		assertThat("GetFinalExpressionFor should return setted expression by \"setFinalExpressionFor\"",
			testBlackboard.getFinalExpressionFor(rdia), is(theInstance(evaEx)));

		testBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		assertThat("Null should be returned for not yet annotated FinalExpressions!",
			testBlackboard.getFinalExpressionFor(testBlackboard.getAllSeffBranches().iterator().next()), nullValue());

		assertThat("It must not be possible get the final expression for null",
			() -> BLACKBOARD_FACTORY.getEmpty().getFinalExpressionFor(null),
			throwsException(NullPointerException.class));

		final SeffBranch seffBranch = SEFF_BRANCH_FACTORY.getOne();
		assertThat("It must not be possible to ask for a final Expression of unknown elements!",
			() -> BLACKBOARD_FACTORY.getEmpty().getFinalExpressionFor(seffBranch),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#setFinalExpressionFor(MeasurableSeffElement, EvaluableExpression)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li> No exceptions for valid input parameters.
	 *
	 * <li>{@code null} can not be passed to one of the arguments.
	 *
	 * <li> Throwing an {@link IllegalArgumentException} if seffElement is not on the
	 * Blackboard
	 *
	 * </ul>
	 */
	@Test
	public void setFinalExpressionFor() {
		final Blackboard testBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
		final Iterator<ResourceDemandingInternalAction> rdias = testBlackboard.getAllRdias().iterator();

		testBlackboard.setFinalExpressionFor(rdias.next(), EVALUABLE_EXPRESSION_FACTORY.getOne());
		// asserts that setting null for the final expression is possible
		testBlackboard.setFinalExpressionFor(rdias.next(), null);

		assertThat("It must not be possible to set the final expression for null",
			() -> BLACKBOARD_FACTORY.getEmpty().setFinalExpressionFor(null, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(NullPointerException.class));

		final SeffBranch seffBranch = SEFF_BRANCH_FACTORY.getOne();
		assertThat(
			"It must not be possible to ask for a final Expression of unknown elements!", () -> BLACKBOARD_FACTORY
				.getEmpty().setFinalExpressionFor(seffBranch, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for {@link Blackboard#getFitnessFunction()}.
	 */
	@Test
	public void getFitnessFunction() {
		assertThat("Blackboard should not return null for getFitnessFunction()",
			BLACKBOARD_FACTORY.getEmpty().getFitnessFunction(), is(notNullValue()));
	}

	/**
	 * Test method for {@link Blackboard#writeFor(java.lang.Class, java.io.Serializable)}.
	 * Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} can be written
	 *
	 * <li>The writer cannot be {@code null}
	 *
	 * </ul>
	 */
	@Test
	public void writeFor() {
		assertThat("It must not be possible to write on the Blackboard for null as a given parameter",
			() -> BLACKBOARD_FACTORY.getEmpty().writeFor(null, "null"), throwsException(NullPointerException.class));

		final Blackboard testBlackboard = BLACKBOARD_FACTORY.getEmpty();
		testBlackboard.writeFor(TestStorer.class, "test");
		testBlackboard.writeFor(TestStorer.class, null);
	}

	/**
	 * Test method for {@link Blackboard#readFor(java.lang.Class)}. Asserts that:
	 *
	 * <ul>
	 *
	 * <li>Initially, {@code null} is stored for every class
	 *
	 * <li>written data is returned
	 *
	 * <li>the writer cannot be {@code null}
	 *
	 * </ul>
	 */
	@Test
	public void readFor() {
		assertThat("It must not be possible to read on the Blackboard for null as a given parameter",
			() -> BLACKBOARD_FACTORY.getEmpty().readFor(null), throwsException(NullPointerException.class));

		final Blackboard testBlackboard = BLACKBOARD_FACTORY.getEmpty();

		assertThat("null must be returned if nothing was written", testBlackboard.readFor(TestStorer.class),
			is(nullValue()));

		final String testString = "test";
		testBlackboard.writeFor(TestStorer.class, testString);
		assertThat("Written data must be returned", testBlackboard.readFor(TestStorer.class),
			is(theInstance(testString)));

		testBlackboard.writeFor(TestStorer.class, null);
		assertThat("Written data must be returned", testBlackboard.readFor(TestStorer.class), is(nullValue()));
	}

	/**
	 * Test {@link BlackboardStorer}.
	 *
	 * @author Joshua Gleitze
	 */
	private final class TestStorer implements BlackboardStorer<String> {
	}

}
