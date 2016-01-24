package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static de.uka.ipd.sdq.beagle.core.testutil.NullHandlingMatchers.notAcceptingNull;
import static org.hamcrest.MatcherAssert.assertThat;
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
	 * <li> Constructor does not accept {@code null} parameters
	 * 
	 * <li> Constructor works with valid input
	 */
	@Test
	public void testBlackboard() {

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			() -> new Blackboard(null, null, null, null), throwsException(NullPointerException.class));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			() -> new Blackboard(RDIA_FACTORY.getAllAsSet(), SEFF_BRANCH_FACTORY.getAllAsSet(),
				SEFF_LOOP_FACTORY.getAllAsSet(), null),
			throwsException(NullPointerException.class));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			() -> new Blackboard(RDIA_FACTORY.getAllAsSet(), SEFF_BRANCH_FACTORY.getAllAsSet(), null,
				EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet()),
			throwsException(NullPointerException.class));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			() -> new Blackboard(RDIA_FACTORY.getAllAsSet(), null, SEFF_LOOP_FACTORY.getAllAsSet(),
				EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet()),
			throwsException(NullPointerException.class));

		assertThat("Blackboard constructur must not accept any measurable seff element = null",
			() -> new Blackboard(null, SEFF_BRANCH_FACTORY.getAllAsSet(), SEFF_LOOP_FACTORY.getAllAsSet(),
				EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet()),
			throwsException(NullPointerException.class));

		new Blackboard(RDIA_FACTORY.getAllAsSet(), SEFF_BRANCH_FACTORY.getAllAsSet(), SEFF_LOOP_FACTORY.getAllAsSet(),
			EXTERNAL_CALL_PARAMETER_FACTORY.getAllAsSet());

	}

	/**
	 * Test method for {@link Blackboard#getAllRdias()}. Assert that <li> Blackboard does
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
	 * Test method for {@link Blackboard#getAllSeffBranches()}. Assert that <li>
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
	 * Test method for {@link Blackboard#getAllSeffLoops()}. Assert that <li> Blackboard
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
	 * Test method for {@link Blackboard#getAllExternalCallParameters()}. Assert that <li>
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
	 * {@link Blackboard#addToBeMeasuredRdias(de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction[])}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as or in the argument.
	 *
	 * </ul>
	 */
	@Test
	public void testAddToBeMeasuredRdias() {
		assertThat(this.emptyBlackboard::addToBeMeasuredRdias, is(notAcceptingNull(RDIA_FACTORY.getAll())));
		assertThat(this.emptyBlackboard::addToBeMeasuredRdias,
			is(notAcceptingNull(Arrays.asList(RDIA_FACTORY.getAll()))));

		final Set<ResourceDemandingInternalAction> rdiaSet = this.getEmptySetOfRdia();
		rdiaSet.add(RDIA_FACTORY.getOne());
		assertThat(
			"Blackboard should not accept RDIAs to be measured, that are not stored in its RDIA-set."
				+ "Besides an Exception should be thrown, because Measurable Seff Elements should not"
				+ "be created after Blackboard instanciation!",
			() -> this.emptyBlackboard.addToBeMeasuredRdias(rdiaSet), throwsException(IllegalArgumentException.class));
		assertThat(
			"Blackboard should not accept RDIAs to be measured, that are not stored in its RDIA-set."
				+ "Besides an Exception should be thrown, because Measurable Seff Elements should not"
				+ "be created after Blackboard instanciation!",
			() -> this.emptyBlackboard.addToBeMeasuredRdias(RDIA_FACTORY.getAll()),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addToBeMeasuredSeffBranches(java.util.Collection)} and
	 * {@link Blackboard#addToBeMeasuredSeffBranches(de.uka.ipd.sdq.beagle.core.SeffBranch[])}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as or in the argument.
	 *
	 * </ul>
	 */
	@Test
	public void testAddToBeMeasuredSeffBranches() {
		assertThat(this.emptyBlackboard::addToBeMeasuredSeffBranches,
			is(notAcceptingNull(SEFF_BRANCH_FACTORY.getAll())));
		assertThat(this.emptyBlackboard::addToBeMeasuredSeffBranches,
			is(notAcceptingNull(Arrays.asList(SEFF_BRANCH_FACTORY.getAll()))));

		final Set<SeffBranch> seffBranchSet = this.getEmptySetOfSeffBranch();
		seffBranchSet.add(SEFF_BRANCH_FACTORY.getOne());
		assertThat(
			"Blackboard should not accept SeffBranches to be measured, that are not stored in its SeffBranch-set."
				+ "Besides an Exception should be thrown, because Measurable Seff Elements should not"
				+ "be created after Blackboard instanciation!",
			() -> this.emptyBlackboard.addToBeMeasuredSeffBranches(seffBranchSet),
			throwsException(IllegalArgumentException.class));
		assertThat(
			"Blackboard should not accept SeffBranches to be measured, that are not stored in its SeffBranch-set."
				+ "Besides an Exception should be thrown, because Measurable Seff Elements should not"
				+ "be created after Blackboard instanciation!",
			() -> this.emptyBlackboard.addToBeMeasuredSeffBranches(SEFF_BRANCH_FACTORY.getAll()),
			throwsException(IllegalArgumentException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addToBeMeasuredSeffLoops(de.uka.ipd.sdq.beagle.core.SeffLoop[])}
	 * and {@link Blackboard#addToBeMeasuredSeffLoops(java.util.Collection)}. Asserts
	 * that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as or in the argument.
	 *
	 * </ul>
	 */
	@Test
	public void testAddToBeMeasuredSeffLoops() {
		assertThat(this.emptyBlackboard::addToBeMeasuredSeffLoops, is(notAcceptingNull(SEFF_LOOP_FACTORY.getAll())));
		assertThat(this.emptyBlackboard::addToBeMeasuredSeffLoops,
			is(notAcceptingNull(Arrays.asList(SEFF_LOOP_FACTORY.getAll()))));

		final Set<SeffLoop> seffLoopSet = this.getEmptySetOfSeffLoop();
		seffLoopSet.add(SEFF_LOOP_FACTORY.getOne());
		assertThat(
			"Blackboard should not accept SeffLoops to be measured, that are not stored in its SeffLoop-set."
				+ "Besides an Exception should be thrown, because Measurable Seff Elements should not"
				+ "be created after Blackboard instanciation!",
			() -> this.emptyBlackboard.addToBeMeasuredSeffLoops(seffLoopSet),
			throwsException(IllegalArgumentException.class));
		assertThat(
			"Blackboard should not accept SeffLoops to be measured, that are not stored in its SeffLoop-set."
				+ "Besides an Exception should be thrown, because Measurable Seff Elements should not"
				+ "be created after Blackboard instanciation!",
			() -> this.emptyBlackboard.addToBeMeasuredSeffLoops(SEFF_LOOP_FACTORY.getAll()),
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
	 * </ul>
	 */
	@Test
	public void testAddToBeMeasuredExternalCallParameters() {
		assertThat(this.emptyBlackboard::addToBeMeasuredExternalCallParameters,
			is(notAcceptingNull(EXTERNAL_CALL_PARAMETER_FACTORY.getAll())));
		assertThat(this.emptyBlackboard::addToBeMeasuredExternalCallParameters,
			is(notAcceptingNull(Arrays.asList(EXTERNAL_CALL_PARAMETER_FACTORY.getAll()))));

		final Set<ExternalCallParameter> externalCallParameterSet = this.getEmptySetOfExternalCallParameter();
		externalCallParameterSet.add(EXTERNAL_CALL_PARAMETER_FACTORY.getOne());
		assertThat(
			"Blackboard should not accept ExternalCallParameters to be measured,"
				+ "that are not stored in its ExternalCallParameter-set."
				+ "Besides an Exception should be thrown, because Measurable Seff Elements should not"
				+ "be created after Blackboard instanciation!",
			() -> this.emptyBlackboard.addToBeMeasuredExternalCallParameters(externalCallParameterSet),
			throwsException(IllegalArgumentException.class));
		assertThat(
			"Blackboard should not accept ExternalCallParameters to be measured,"
				+ "that are not stored in its ExternalCallParameter-set."
				+ "Besides an Exception should be thrown, because Measurable Seff Elements should not"
				+ "be created after Blackboard instanciation!",
			() -> this.emptyBlackboard.addToBeMeasuredExternalCallParameters(EXTERNAL_CALL_PARAMETER_FACTORY.getAll()),
			throwsException(IllegalArgumentException.class));

	}

	/**
	 * Test method for
	 * {@link Blackboard#getMeasurementResultsFor(ResourceDemandingInternalAction)} .
	 */
	@Test
	public void testGetMeasurementResultsForResourceDemandingInternalAction() {

		final ResourceDemandingInternalAction rdia = null;
		assertThat(
			"It must not be possible to call getMeasurementResultsFor(ResourceDemandingInternalAction) for null as parameter",
			() -> this.emptyBlackboard.getMeasurementResultsFor(rdia), throwsException(NullPointerException.class));

	}

	/**
	 * Test method for
	 * {@link Blackboard#getMeasurementResultsFor(de.uka.ipd.sdq.beagle.core.SeffBranch)}
	 * .
	 */
	@Test
	public void testGetMeasurementResultsForSeffBranch() {

		final SeffBranch seffBranch = null;
		assertThat("It must not be possible to call getMeasurementResultsFor(SeffBranch) for null as parameter",
			() -> this.emptyBlackboard.getMeasurementResultsFor(seffBranch),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#getMeasurementResultsFor(de.uka.ipd.sdq.beagle.core.SeffLoop)} .
	 */
	@Test
	public void testGetMeasurementResultsForSeffLoop() {

		final SeffLoop seffLoop = null;
		assertThat("It must not be possible to call getMeasurementResultsFor(SeffLoop) for null as parameter",
			() -> this.emptyBlackboard.getMeasurementResultsFor(seffLoop), throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#getMeasurementResultsFor(de.uka.ipd.sdq.beagle.core.ExternalCallParameter)}
	 * .
	 */
	@Test
	public void testGetMeasurementResultsForExternalCallParameter() {

		final ExternalCallParameter externalCallParameter = null;
		assertThat(
			"It must not be possible to call getMeasurementResultsFor(externalCallParameter) for null as parameter",
			() -> this.emptyBlackboard.getMeasurementResultsFor(externalCallParameter),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addMeasurementResultFor(ResourceDemandingInternalAction, de.uka.ipd.sdq.beagle.core.measurement.ResourceDemandMeasurementResult)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * </ul>
	 */
	@Test
	public void testAddMeasurementResultForResourceDemandingInternalActionResourceDemandMeasurementResult() {
		assertThat("It must not be possible to add a measurement result for null",
			() -> this.emptyBlackboard.addMeasurementResultFor(null, new ResourceDemandMeasurementResult()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> this.emptyBlackboard.addMeasurementResultFor(RDIA_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addMeasurementResultFor(SeffBranch, de.uka.ipd.sdq.beagle.core.measurement.BranchDecisionMeasurementResult)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * </ul>
	 */
	@Test
	public void testAddMeasurementResultForSeffBranchBranchDecisionMeasurementResult() {
		assertThat("It must not be possible to add a measurement result for null",
			() -> this.emptyBlackboard.addMeasurementResultFor(null, new BranchDecisionMeasurementResult()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> this.emptyBlackboard.addMeasurementResultFor(SEFF_BRANCH_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addMeasurementResultFor(SeffLoop, de.uka.ipd.sdq.beagle.core.measurement.LoopRepetitionCountMeasurementResult)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * </ul>
	 */
	@Test
	public void testAddMeasurementResultForSeffLoopLoopRepetitionCountMeasurementResult() {
		assertThat("It must not be possible to add a measurement result for null",
			() -> this.emptyBlackboard.addMeasurementResultFor(null, new LoopRepetitionCountMeasurementResult()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> this.emptyBlackboard.addMeasurementResultFor(SEFF_LOOP_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link Blackboard#addMeasurementResultFor(ExternalCallParameter, de.uka.ipd.sdq.beagle.core.measurement.ParameterChangeMeasurementResult)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * </ul>
	 */
	@Test
	public void testAddMeasurementResultForExternalCallParameterParameterChangeMeasurementResult() {
		assertThat("It must not be possible to add a measurement result for null",
			() -> this.emptyBlackboard.addMeasurementResultFor(null, new ParameterChangeMeasurementResult()),
			throwsException(NullPointerException.class));
		assertThat("It must not be possible to add null as measurement result",
			() -> this.emptyBlackboard.addMeasurementResultFor(EXTERNAL_CALL_PARAMETER_FACTORY.getOne(), null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link Blackboard#getProposedExpressionFor(MeasurableSeffElement)}
	 * . Asserts that:
	 *
	 * <ul>
	 *
	 * <li>{@code null} cannot be passed.
	 *
	 * </ul>
	 */
	@Test
	public void testGetProposedExpressionFor() {
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
	 * <li>{@code null} cannot be passed as any argument.
	 *
	 * </ul>
	 */
	@Test
	public void testAddProposedExpressionFor() {
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
	 * <li>{@code null} cannot be passed.
	 *
	 * </ul>
	 */
	@Test
	public void testGetFinalExpressionFor() {
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
	 * <li>{@code null} can not be passed to one of the arguments.
	 *
	 * </ul>
	 */
	@Test
	public void testSetFinalExpressionFor() {
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
