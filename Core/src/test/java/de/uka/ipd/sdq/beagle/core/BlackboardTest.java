package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.NullHandlingMatchers.notAcceptingNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult;
import de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.CodeSectionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.EvaluableExpressionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ExternalCallParameterFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.ResourceDemandingInternalActionFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffBranchFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.SeffLoopFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests for {@link Blackboard}.
 *
 * @author Joshua Gleitze
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
	 * An {@link CodeSectionFactory} factory to easily obtain new instances from.
	 */
	private static final CodeSectionFactory CODE_SECTION_FACTORY = new CodeSectionFactory();

	/**
	 * A new, empty Blackboard will be put in here before each test. Please note that it
	 * should only be used in a test as long as it is not modified. If it was modified, a
	 * new one should be created.
	 *
	 * @see BlackboardFactory#getEmpty()
	 */
	private Blackboard emptyBlackboard;

	/**
	 * A new, filled Blackboard will be put in here before each test. Please note that it
	 * should only be used in a test as long as it is not modified. If it was modified, a
	 * new one should be created.
	 *
	 * @see BlackboardFactory#getEmpty()
	 */
	private Blackboard filledBlackboard;

	/**
	 * Puts a new, empty Blackboard into {{@link #emptyBlackboard}.
	 */
	@Before
	public void createEmptyBlackboard() {
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		this.filledBlackboard = BLACKBOARD_FACTORY.getWithToBeMeasuredContent();
	}

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
	public void testBlackboard() {

		// Checking if later modification of the given sets to the Blackboard does not
		// change the Blackboard sets.
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> ecpSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		final Blackboard blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet);
		final ResourceDemandingInternalAction rdiaToAdd =
			new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, CODE_SECTION_FACTORY.getOne());
		final SeffBranch seffBranchToAdd = new SeffBranch(CODE_SECTION_FACTORY.getAllAsSet());
		final SeffLoop seffLoopToAdd = new SeffLoop(CODE_SECTION_FACTORY.getOne());
		final ExternalCallParameter ecpToAdd = new ExternalCallParameter(CODE_SECTION_FACTORY.getOne(), 0);
		rdiaSet.add(rdiaToAdd);
		seffBranchSet.add(seffBranchToAdd);
		seffLoopSet.add(seffLoopToAdd);
		ecpSet.add(ecpToAdd);
		assertThat("Blackboard should create a copy from the given Sets to the constructor",
			blackboardTemp.getAllRdias(), not(contains(rdiaToAdd)));
		assertThat("Blackboard should create a copy from the given Sets to the constructor",
			blackboardTemp.getAllSeffBranches(), not(contains(seffBranchToAdd)));
		assertThat("Blackboard should create a copy from the given Sets to the constructor",
			blackboardTemp.getAllSeffLoops(), not(contains(seffLoopToAdd)));
		assertThat("Blackboard should create a copy from the given Sets to the constructor",
			blackboardTemp.getAllExternalCallParameters(), not(contains(ecpToAdd)));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			(rdias) -> new Blackboard(new HashSet<>(rdias), SEFF_BRANCH_FACTORY.getAllAsSet(),
				SEFF_LOOP_FACTORY.getAllAsSet(), EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet()),
			is(notAcceptingNull(RDIA_FACTORY.getAllAsSet())));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			(seffBranches) -> new Blackboard(RDIA_FACTORY.getAllAsSet(), new HashSet<>(seffBranches),
				SEFF_LOOP_FACTORY.getAllAsSet(), EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet()),
			is(notAcceptingNull(SEFF_BRANCH_FACTORY.getAllAsSet())));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			(seffLoops) -> new Blackboard(RDIA_FACTORY.getAllAsSet(), SEFF_BRANCH_FACTORY.getAllAsSet(),
				new HashSet<>(seffLoops), EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet()),
			is(notAcceptingNull(SEFF_LOOP_FACTORY.getAllAsSet())));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			(externalCallParameters) -> new Blackboard(RDIA_FACTORY.getAllAsSet(), SEFF_BRANCH_FACTORY.getAllAsSet(),
				SEFF_LOOP_FACTORY.getAllAsSet(), new HashSet<>(externalCallParameters)),
			is(notAcceptingNull(EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet())));

		new Blackboard(RDIA_FACTORY.getAllAsSet(), SEFF_BRANCH_FACTORY.getAllAsSet(), SEFF_LOOP_FACTORY.getAllAsSet(),
			EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet());

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
	public void testGetAllRdias() {
		assertThat("Blackboard should not return null for getAllRdias()", this.emptyBlackboard.getAllRdias(),
			is(notNullValue()));

		assertThat("Empty blackboard should not contain any Rdias", this.emptyBlackboard.getAllRdias(), is(empty()));

		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Blackboard blackboard = new Blackboard(rdiaSet, this.getEmptySetOfSeffBranch(),
			this.getEmptySetOfSeffLoop(), this.getEmptySetOfExternalCallParameter());
		assertThat("Rdias on the blackboard should not change!", blackboard.getAllRdias(), is(equalTo(rdiaSet)));

		final Set<ResourceDemandingInternalAction> blackboardSet = blackboard.getAllRdias();
		blackboardSet.removeAll(blackboardSet);
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!", blackboard.getAllRdias(),
			is(equalTo(rdiaSet)));

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
	public void testGetAllSeffBranches() {
		assertThat("Blackboard should not return null for getAllSeffBranches()",
			this.emptyBlackboard.getAllSeffBranches(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any Rdias", this.emptyBlackboard.getAllSeffBranches(),
			is(empty()));

		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Blackboard blackboard = new Blackboard(this.getEmptySetOfRdia(), seffBranchSet,
			this.getEmptySetOfSeffLoop(), this.getEmptySetOfExternalCallParameter());
		assertThat("SeffBranches on the blackboard should not change!", blackboard.getAllSeffBranches(),
			is(equalTo(seffBranchSet)));

		final Set<SeffBranch> blackboardSet = blackboard.getAllSeffBranches();
		blackboardSet.removeAll(blackboardSet);
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			blackboard.getAllSeffBranches(), is(equalTo(seffBranchSet)));

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
	public void testGetAllSeffLoops() {
		assertThat("Blackboard should not return null for getAllSeffLoops()", this.emptyBlackboard.getAllSeffLoops(),
			is(notNullValue()));

		assertThat("Empty blackboard should not contain any SeffLoops", this.emptyBlackboard.getAllSeffLoops(),
			is(empty()));

		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Blackboard blackboard = new Blackboard(this.getEmptySetOfRdia(), this.getEmptySetOfSeffBranch(),
			seffLoopSet, this.getEmptySetOfExternalCallParameter());
		assertThat("SeffLoops on the blackboard should not change!", blackboard.getAllSeffLoops(),
			is(equalTo(seffLoopSet)));

		final Set<SeffLoop> blackboardSet = blackboard.getAllSeffLoops();
		blackboardSet.removeAll(blackboardSet);
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			blackboard.getAllSeffLoops(), is(equalTo(seffLoopSet)));

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
	public void testGetAllExternalCallParameters() {
		assertThat("Blackboard should not return null for getAllExternalCallParameters()",
			this.emptyBlackboard.getAllExternalCallParameters(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any ExternalCallParameters",
			this.emptyBlackboard.getAllExternalCallParameters(), is(empty()));

		final Set<ExternalCallParameter> externalCallParameterSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		final Blackboard blackboard = new Blackboard(this.getEmptySetOfRdia(), this.getEmptySetOfSeffBranch(),
			this.getEmptySetOfSeffLoop(), externalCallParameterSet);
		assertThat("ExternalCallParameters on the blackboard should not change!",
			blackboard.getAllExternalCallParameters(), is(equalTo(externalCallParameterSet)));

		final Set<ExternalCallParameter> blackboardSet = blackboard.getAllExternalCallParameters();
		blackboardSet.removeAll(blackboardSet);
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			blackboard.getAllExternalCallParameters(), is(equalTo(externalCallParameterSet)));
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
	public void testGetRdiasToBeMeasured() {

		assertThat("Blackboard should not return null for getRdiasToBeMeasured()",
			this.emptyBlackboard.getRdiasToBeMeasured(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any RdiasToBeMeasured",
			this.emptyBlackboard.getRdiasToBeMeasured(), is(empty()));

		final Set<ResourceDemandingInternalAction> rdiaSet = this.filledBlackboard.getRdiasToBeMeasured();
		final Set<ResourceDemandingInternalAction> clone = new HashSet<ResourceDemandingInternalAction>();
		clone.addAll(rdiaSet);
		rdiaSet.removeAll(rdiaSet);
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			this.filledBlackboard.getRdiasToBeMeasured(), is(equalTo(clone)));

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
	public void testGetSeffBranchesToBeMeasured() {
		assertThat("Blackboard should not return null for getSeffBranchesToBeMeasured()",
			this.emptyBlackboard.getSeffBranchesToBeMeasured(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any SeffBranchesToBeMeasured",
			this.emptyBlackboard.getSeffBranchesToBeMeasured(), is(empty()));

		final Set<SeffBranch> seffBranchSet = this.filledBlackboard.getSeffBranchesToBeMeasured();
		final Set<SeffBranch> clone = new HashSet<SeffBranch>();
		clone.addAll(seffBranchSet);
		seffBranchSet.removeAll(seffBranchSet);
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			this.filledBlackboard.getSeffBranchesToBeMeasured(), is(equalTo(clone)));
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
	public void testGetSeffLoopsToBeMeasured() {
		assertThat("Blackboard should not return null for getSeffLoopsToBeMeasured()",
			this.emptyBlackboard.getSeffLoopsToBeMeasured(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any SeffLoopsToBeMeasured",
			this.emptyBlackboard.getSeffLoopsToBeMeasured(), is(empty()));

		final Set<SeffLoop> seffLoopSet = this.filledBlackboard.getSeffLoopsToBeMeasured();
		final Set<SeffLoop> clone = new HashSet<SeffLoop>();
		clone.addAll(seffLoopSet);
		seffLoopSet.removeAll(seffLoopSet);
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			this.filledBlackboard.getSeffLoopsToBeMeasured(), is(equalTo(clone)));
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
	public void testGetExternalCallParametersToBeMeasured() {
		assertThat("Blackboard should not return null for getExternalCallParametersToBeMeasured()",
			this.emptyBlackboard.getExternalCallParametersToBeMeasured(), is(notNullValue()));

		assertThat("Empty blackboard should not contain any ExternalCallParametersToBeMeasured",
			this.emptyBlackboard.getExternalCallParametersToBeMeasured(), is(empty()));

		final Set<ExternalCallParameter> externalCallParameterSet =
			this.filledBlackboard.getExternalCallParametersToBeMeasured();
		final Set<ExternalCallParameter> clone = new HashSet<ExternalCallParameter>();
		clone.addAll(externalCallParameterSet);
		externalCallParameterSet.removeAll(externalCallParameterSet);
		assertThat("Blackboard getter-methods should return copies of Sets, not their own!",
			this.filledBlackboard.getExternalCallParametersToBeMeasured(), is(equalTo(clone)));
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
	public void testAddToBeMeasuredRdias() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> ecpSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		final Blackboard blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet);
		assertThat(blackboardTemp::addToBeMeasuredRdias, is(notAcceptingNull(RDIA_FACTORY.getAll())));
		assertThat(blackboardTemp::addToBeMeasuredRdias, is(notAcceptingNull(Arrays.asList(RDIA_FACTORY.getAll()))));

		final Set<ResourceDemandingInternalAction> rdiaSetTwo = this.getEmptySetOfRdia();
		rdiaSetTwo.add(RDIA_FACTORY.getOne());
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to add unknown elements to the Blackboard Measurement sets!",
			() -> this.emptyBlackboard.addToBeMeasuredRdias(rdiaSetTwo),
			throwsException(IllegalArgumentException.class));

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
	public void testAddToBeMeasuredSeffBranches() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> ecpSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		final Blackboard blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet);
		assertThat(blackboardTemp::addToBeMeasuredSeffBranches, is(notAcceptingNull(SEFF_BRANCH_FACTORY.getAll())));
		assertThat(blackboardTemp::addToBeMeasuredSeffBranches,
			is(notAcceptingNull(Arrays.asList(SEFF_BRANCH_FACTORY.getAll()))));

		final Set<SeffBranch> seffBranchSetTwo = this.getEmptySetOfSeffBranch();
		seffBranchSetTwo.add(SEFF_BRANCH_FACTORY.getOne());
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to add unknown elements to the Blackboard Measurement sets!",
			() -> this.emptyBlackboard.addToBeMeasuredSeffBranches(seffBranchSetTwo),
			throwsException(IllegalArgumentException.class));
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
	public void testAddToBeMeasuredSeffLoops() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> ecpSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		final Blackboard blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet);
		assertThat(blackboardTemp::addToBeMeasuredSeffLoops, is(notAcceptingNull(SEFF_LOOP_FACTORY.getAll())));
		assertThat(blackboardTemp::addToBeMeasuredSeffLoops,
			is(notAcceptingNull(Arrays.asList(SEFF_LOOP_FACTORY.getAll()))));

		final Set<SeffLoop> seffLoopSetTwo = this.getEmptySetOfSeffLoop();
		seffLoopSetTwo.add(SEFF_LOOP_FACTORY.getOne());
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to add unknown elements to the Blackboard Measurement sets!",
			() -> this.emptyBlackboard.addToBeMeasuredSeffLoops(seffLoopSetTwo),
			throwsException(IllegalArgumentException.class));
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
	public void testAddToBeMeasuredExternalCallParameters() {
		final Set<ResourceDemandingInternalAction> rdiaSet = RDIA_FACTORY.getAllAsSet();
		final Set<SeffBranch> seffBranchSet = SEFF_BRANCH_FACTORY.getAllAsSet();
		final Set<SeffLoop> seffLoopSet = SEFF_LOOP_FACTORY.getAllAsSet();
		final Set<ExternalCallParameter> ecpSet = EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet();
		final Blackboard blackboardTemp = new Blackboard(rdiaSet, seffBranchSet, seffLoopSet, ecpSet);
		assertThat(blackboardTemp::addToBeMeasuredExternalCallParameters,
			is(notAcceptingNull(EXTERNAL_CALL_PARAMETER_FACTORY.getAll())));
		assertThat(blackboardTemp::addToBeMeasuredExternalCallParameters,
			is(notAcceptingNull(Arrays.asList(EXTERNAL_CALL_PARAMETER_FACTORY.getAll()))));

		final Set<ExternalCallParameter> externalCallParameterSetTwo = this.getEmptySetOfExternalCallParameter();
		externalCallParameterSetTwo.add(EXTERNAL_CALL_PARAMETER_FACTORY.getOne());
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to add unknown elements to the Blackboard Measurement sets!",
			() -> this.emptyBlackboard.addToBeMeasuredExternalCallParameters(externalCallParameterSetTwo),
			throwsException(IllegalArgumentException.class));

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
	public void testGetMeasurementResultsForResourceDemandingInternalAction() {

		assertThat(
			"Blackboard should not return null for valid input parameters in"
				+ "getMeasurementResultsFor(ResourceDemandingInternalAction)",
			this.filledBlackboard.getMeasurementResultsFor(this.filledBlackboard.getAllRdias().iterator().next()),
			is(notNullValue()));

		final ResourceDemandingInternalAction rdia = null;
		assertThat(
			"It must not be possible to call getMeasurementResultsFor(ResourceDemandingInternalAction) for null as parameter",
			() -> this.emptyBlackboard.getMeasurementResultsFor(rdia), throwsException(NullPointerException.class));

		final ResourceDemandingInternalAction rdiaTwo = RDIA_FACTORY.getOne();
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to ask for unknown elements on the Blackboard Measurement Result sets!",
			() -> this.emptyBlackboard.getMeasurementResultsFor(rdiaTwo),
			throwsException(IllegalArgumentException.class));

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
	public void testGetMeasurementResultsForSeffBranch() {

		assertThat(
			"Blackboard should not return null for valid input parameters in getMeasurementResultsFor(SeffBranch)",
			this.filledBlackboard.getMeasurementResultsFor(
				this.filledBlackboard.getAllSeffBranches().iterator().next()),
			is(notNullValue()));

		final SeffBranch seffBranch = null;
		assertThat("It must not be possible to call getMeasurementResultsFor(SeffBranch) for null as parameter",
			() -> this.emptyBlackboard.getMeasurementResultsFor(seffBranch),
			throwsException(NullPointerException.class));

		final SeffBranch seffBranchTwo = SEFF_BRANCH_FACTORY.getOne();
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to ask for unknown elements on the Blackboard Measurement Result sets!",
			() -> this.emptyBlackboard.getMeasurementResultsFor(seffBranchTwo),
			throwsException(IllegalArgumentException.class));
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
	public void testGetMeasurementResultsForSeffLoop() {

		assertThat("Blackboard should not return null for valid input parameters in getMeasurementResultsFor(SeffLoop)",
			this.filledBlackboard.getMeasurementResultsFor(this.filledBlackboard.getAllSeffLoops().iterator().next()),
			is(notNullValue()));

		final SeffLoop seffLoop = null;
		assertThat("It must not be possible to call getMeasurementResultsFor(SeffLoop) for null as parameter",
			() -> this.emptyBlackboard.getMeasurementResultsFor(seffLoop), throwsException(NullPointerException.class));

		final SeffLoop seffLoopTwo = SEFF_LOOP_FACTORY.getOne();
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to ask for unknown elements on the Blackboard Measurement Result sets!",
			() -> this.emptyBlackboard.getMeasurementResultsFor(seffLoopTwo),
			throwsException(IllegalArgumentException.class));
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
	public void testGetMeasurementResultsForExternalCallParameter() {

		assertThat(
			"Blackboard should not return null for valid input parameters in getMeasurementResultsFor(ExternalCallParameter)",
			this.filledBlackboard.getMeasurementResultsFor(
				this.filledBlackboard.getAllExternalCallParameters().iterator().next()),
			is(notNullValue()));

		final ExternalCallParameter externalCallParameter = null;
		assertThat(
			"It must not be possible to call getMeasurementResultsFor(externalCallParameter) for null as parameter",
			() -> this.emptyBlackboard.getMeasurementResultsFor(externalCallParameter),
			throwsException(NullPointerException.class));

		final ExternalCallParameter externalCallParameterTwo = EXTERNAL_CALL_PARAMETER_FACTORY.getOne();
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to ask for unknown elements on the Blackboard Measurement Result sets!",
			() -> this.emptyBlackboard.getMeasurementResultsFor(externalCallParameterTwo),
			throwsException(IllegalArgumentException.class));
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
	public void testAddMeasurementResultForResourceDemandingInternalActionResourceDemandMeasurementResult() {

		final ResourceDemandMeasurementResult rdiaResult = new ResourceDemandMeasurementResult();
		this.filledBlackboard.addMeasurementResultFor(this.filledBlackboard.getAllRdias().iterator().next(),
			rdiaResult);

		assertThat("It must not be possible to add a measurement result for null",
			() -> this.emptyBlackboard.addMeasurementResultFor(null, new ResourceDemandMeasurementResult()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> this.emptyBlackboard.addMeasurementResultFor(RDIA_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));

		final ResourceDemandingInternalAction rdia = RDIA_FACTORY.getOne();
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to add measurement results to unknown elements!",
			() -> this.emptyBlackboard.addMeasurementResultFor(rdia, rdiaResult),
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
	public void testAddMeasurementResultForSeffBranchBranchDecisionMeasurementResult() {

		final BranchDecisionMeasurementResult branchResult = new BranchDecisionMeasurementResult();
		this.filledBlackboard.addMeasurementResultFor(this.filledBlackboard.getAllSeffBranches().iterator().next(),
			branchResult);

		assertThat("It must not be possible to add a measurement result for null",
			() -> this.emptyBlackboard.addMeasurementResultFor(null, new BranchDecisionMeasurementResult()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> this.emptyBlackboard.addMeasurementResultFor(SEFF_BRANCH_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));

		final SeffBranch seffBranch = SEFF_BRANCH_FACTORY.getOne();
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to add measurement results to unknown elements!",
			() -> this.emptyBlackboard.addMeasurementResultFor(seffBranch, branchResult),
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
	public void testAddMeasurementResultForSeffLoopLoopRepetitionCountMeasurementResult() {

		final LoopRepetitionCountMeasurementResult loopResult = new LoopRepetitionCountMeasurementResult();
		this.filledBlackboard.addMeasurementResultFor(this.filledBlackboard.getAllSeffLoops().iterator().next(),
			loopResult);

		assertThat("It must not be possible to add a measurement result for null",
			() -> this.emptyBlackboard.addMeasurementResultFor(null, new LoopRepetitionCountMeasurementResult()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> this.emptyBlackboard.addMeasurementResultFor(SEFF_LOOP_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));

		final SeffLoop seffLoop = SEFF_LOOP_FACTORY.getOne();
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to add measurement results to unknown elements!",
			() -> this.emptyBlackboard.addMeasurementResultFor(seffLoop, loopResult),
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
	public void testAddMeasurementResultForExternalCallParameterParameterChangeMeasurementResult() {

		final ParameterChangeMeasurementResult parameterResult = new ParameterChangeMeasurementResult();
		this.filledBlackboard.addMeasurementResultFor(
			this.filledBlackboard.getAllExternalCallParameters().iterator().next(), parameterResult);

		assertThat("It must not be possible to add a measurement result for null",
			() -> this.emptyBlackboard.addMeasurementResultFor(null, new ParameterChangeMeasurementResult()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> this.emptyBlackboard.addMeasurementResultFor(EXTERNAL_CALL_PARAMETER_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));

		final ExternalCallParameter ecpElement = EXTERNAL_CALL_PARAMETER_FACTORY.getOne();
		this.emptyBlackboard = BLACKBOARD_FACTORY.getEmpty();
		assertThat("It must not be possible to add measurement results to unknown elements!",
			() -> this.emptyBlackboard.addMeasurementResultFor(ecpElement, parameterResult),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for {@link Blackboard#getProposedExpressionFor(MeasurableSeffElement)}
	 * . Asserts that:
	 *
	 * <ul>
	 * 
	 * <li> Does not return {@code null} for valid call parameters.
	 *
	 * <li>{@code null} cannot be passed.
	 *
	 * </ul>
	 */
	@Test
	public void testGetProposedExpressionFor() {

		assertThat("Proposed Expression should not return null for valid input parameters!",
			this.filledBlackboard.getProposedExpressionFor(RDIA_FACTORY.getOne()), is(notNullValue()));

		assertThat("It must not be possible get proposed expressions for null",
			() -> this.emptyBlackboard.getProposedExpressionFor(null), throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addProposedExpressionFor(MeasurableSeffElement, de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression)}
	 * . Asserts that:
	 *
	 * <ul>
	 * 
	 * <li> No exceptions for valid inputs.
	 *
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * </ul>
	 */
	@Test
	public void testAddProposedExpressionFor() {

		this.filledBlackboard.addProposedExpressionFor(RDIA_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat("It must not be possible to add a proposed expression for null",
			() -> this.emptyBlackboard.addProposedExpressionFor(null, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as a proposed expression",
			() -> this.emptyBlackboard.addProposedExpressionFor(SEFF_BRANCH_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link Blackboard#getFinalExpressionFor(MeasurableSeffElement)}.
	 * Asserts that:
	 *
	 * <ul>
	 * 
	 * <li>No exceptions for valid input.
	 *
	 * <li>{@code null} cannot be passed.
	 *
	 * </ul>
	 */
	@Test
	public void testGetFinalExpressionFor() {

		this.filledBlackboard.getFinalExpressionFor(SEFF_BRANCH_FACTORY.getOne());

		assertThat("It must not be possible get the final expression for null",
			() -> this.emptyBlackboard.getFinalExpressionFor(null), throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#setFinalExpressionFor(MeasurableSeffElement, de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li> No exceptions for valid input parameters.
	 *
	 * <li>{@code null} can not be passed to one of the arguments.
	 *
	 * </ul>
	 */
	@Test
	public void testSetFinalExpressionFor() {

		this.filledBlackboard.setFinalExpressionFor(SEFF_LOOP_FACTORY.getOne(), EVALUABLE_EXPRESSION_FACTORY.getOne());

		assertThat("It must not be possible to set the final expression for null",
			() -> this.emptyBlackboard.setFinalExpressionFor(null, EVALUABLE_EXPRESSION_FACTORY.getOne()),
			throwsException(NullPointerException.class));
		// asserts that setting null for the final expression is possible
		this.emptyBlackboard.setFinalExpressionFor(RDIA_FACTORY.getOne(), null);
	}

	/**
	 * Test method for {@link Blackboard#getFitnessFunction()}.
	 */
	@Test
	public void testGetFitnessFunction() {
		assertThat("Blackboard should not return null for getFitnessFunction()",
			this.emptyBlackboard.getFitnessFunction(), is(notNullValue()));
	}

	/**
	 * Test method for {@link Blackboard#writeFor(java.lang.Class, java.io.Serializable)}
	 * .
	 */
	@Test
	public void testWriteFor() {

		assertThat("It must not be possible to write on the Blackboard for null as a given parameter",
			() -> this.emptyBlackboard.writeFor(null, String.class), throwsException(NullPointerException.class));
		assertThat("It must not be possible to write on the Blackboard for null as a given parameter",
			() -> this.emptyBlackboard.writeFor(null, null), throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link Blackboard#readFor(java.lang.Class)}.
	 */
	@Test
	public void testReadFor() {
		assertThat("It must not be possible to read on the Blackboard for null as a given parameter",
			() -> this.emptyBlackboard.readFor(null), throwsException(NullPointerException.class));

	}

}
