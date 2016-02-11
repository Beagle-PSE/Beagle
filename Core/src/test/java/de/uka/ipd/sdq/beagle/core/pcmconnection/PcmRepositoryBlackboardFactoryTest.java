package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.BlackboardSeffElementsMatcher.equalRegardingSeffElements;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.MeasurableSeffElement;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;
import de.uka.ipd.sdq.beagle.core.evaluableexpressions.EvaluableExpression;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunctionBlackboardView;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.net4j.util.collection.Pair;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Tests for {@link PcmRepositoryBlackboardFactory}.
 * 
 * @author Christoph Michelbach
 */
public class PcmRepositoryBlackboardFactoryTest {

	/**
	 * A factory which creates instances of {@link PcmRepositoryBlackboardFactory}.
	 */
	private static PcmRepositoryBlackboardFactoryFactory pcmRepositoryBlackboardFactoryFactory =
		new PcmRepositoryBlackboardFactoryFactory();

	/**
	 * Pseudo fitness function.
	 */
	private final EvaluableExpressionFitnessFunction fitnessFunction = new EvaluableExpressionFitnessFunction() {

		/**
		 * Store between (pairs of measurable seff elements and evaluable expressions) and
		 * doubles.
		 */
		private final HashMap<Pair<MeasurableSeffElement, EvaluableExpression>, Double> store = new HashMap<>();

		@Override
		public double gradeFor(final ExternalCallParameter parameter, final EvaluableExpression expression,
			final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
			return this.determineValue(parameter, expression);
		}

		@Override
		public double gradeFor(final SeffLoop loop, final EvaluableExpression expression,
			final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
			return this.determineValue(loop, expression);
		}

		@Override
		public double gradeFor(final SeffBranch branch, final EvaluableExpression expression,
			final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
			return this.determineValue(branch, expression);
		}

		@Override
		public double gradeFor(final ResourceDemandingInternalAction rdia, final EvaluableExpression expression,
			final EvaluableExpressionFitnessFunctionBlackboardView blackboard) {
			return this.determineValue(rdia, expression);
		}

		/**
		 * Determines the fitness of a combination of a {@code MeasurableSeffElement} and
		 * an {@code EvaluableExpression}.
		 *
		 * @param element A {@code MeasurableSeffElement}.
		 * @param expression An {@code EvaluableExpression}.
		 * @return The fitness value.
		 */
		private double determineValue(final MeasurableSeffElement element, final EvaluableExpression expression) {
			final Pair<MeasurableSeffElement, EvaluableExpression> pair = new Pair<>(element, expression);
			if (this.store.containsKey(pair)) {
				return this.store.get(pair);
			} else {
				final double fitness = Math.pow(Math.random() * 10E5, 3);
				this.store.put(pair, fitness);

				return fitness;
			}
		}
	};

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#PcmRepositoryBlackboardFactory(java.util.Set)
	 * and PcmRepositoryBlackboardFactory#PcmRepositoryBlackboardFactory(String)}. Asserts
	 * that creation is possible and {@code null} or an empty string or otherwise
	 * impossible path cannot be passed.
	 */
	@Test
	public void pcmRepositoryBlackboardFactory() {
		assertThat(() -> new PcmRepositoryBlackboardFactory((String) null, this.fitnessFunction),
			throwsException(NullPointerException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory((File) null, this.fitnessFunction),
			throwsException(NullPointerException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory(".", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("..", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("/", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("/tmp", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));
		assertThat(() -> new PcmRepositoryBlackboardFactory("\0", this.fitnessFunction),
			throwsException(IllegalArgumentException.class));

		EMFHelper eMFHelper = mock(EMFHelper.class);

		given(eMFHelper.loadFromXMIFile(any(), any())).willReturn(new SomeInvalidObject());
		assertThat(() -> pcmRepositoryBlackboardFactoryFactory.getValidInstance(),
			throwsException(IllegalArgumentException.class));

		final File[] impossibleRepositoryFiles = {
			new File(""), new File("."), new File(".."), new File("/"), new File("/tmp"), new File("\0")
		};

		for (File impossibleRepositoryFile : impossibleRepositoryFiles) {
			assertThat(() -> new PcmRepositoryBlackboardFactory(impossibleRepositoryFile, this.fitnessFunction),
				throwsException(IllegalArgumentException.class));
		}

		// final PcmRepositoryBlackboardFactory mockedPcmRepositoryBlackboardFactory =
		// mock(PcmRepositoryBlackboardFactory.class);

	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForAllElements()}.
	 */
	@Test
	public void getBlackboardForAllElements() {
		final PcmRepositoryBlackboardFactory pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getAppSensorProjectInstance();
		final Blackboard result = pcmRepositoryBlackboardFactory.getBlackboardForAllElements();
		assertThat(result, is(notNullValue()));

		assertThat(result.getAllRdias().size(), is(not(0)));
		assertThat(result.getAllSeffBranches().size(), is(not(0)));
		assertThat(result.getAllSeffLoops().size(), is(not(0)));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForIds(java.util.Collection)}.
	 */
	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	@Test
	public void getBlackboardForIdsCollectionOfString() {
		final PcmRepositoryBlackboardFactory pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getValidInstance();

		final HashSet<String> collection = new HashSet<String>();
		collection.add("");

		assertThat(new Pair(pcmRepositoryBlackboardFactory.getBlackboardForIds(collection),
			new BlackboardFactory().getEmpty()), equalRegardingSeffElements());

		assertThat(() -> pcmRepositoryBlackboardFactory.getBlackboardForIds((String) null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactory#getBlackboardForIds(java.lang.String[])}.
	 * 
	 */
	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	@Test
	public void getBlackboardForIdsStringArray() {
		final PcmRepositoryBlackboardFactory pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getValidInstance();

		assertThat(new Pair(pcmRepositoryBlackboardFactory.getBlackboardForIds(""), new BlackboardFactory().getEmpty()),
			equalRegardingSeffElements());

		assertThat(() -> pcmRepositoryBlackboardFactory.getBlackboardForIds((String[]) null),
			throwsException(NullPointerException.class));

		assertThat(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"), is(not(nullValue())));

		assertThat(
			new Pair(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"),
				pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g")),
			equalRegardingSeffElements());

		assertThat(
			new Pair(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"),
				pcmRepositoryBlackboardFactory.getBlackboardForIds("_FaSO4LnqEeWVlphM5rov7g")),
			is(not(equalRegardingSeffElements())));

		assertThat(new Pair(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"),
			pcmRepositoryBlackboardFactory.getBlackboardForAllElements()), is(not(equalRegardingSeffElements())));

		assertThat(new Pair(pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g"),
			pcmRepositoryBlackboardFactory.getBlackboardForAllElements()), is(not(equalRegardingSeffElements())));

		assertThat(new Pair(pcmRepositoryBlackboardFactory.getBlackboardForIds("_SomeIdWhichDosntExistA"),
			new BlackboardFactory().getEmpty()), equalRegardingSeffElements());

		assertThat(new Pair(pcmRepositoryBlackboardFactory.getBlackboardForIds("_TooShortId"),
			new BlackboardFactory().getEmpty()), equalRegardingSeffElements());

		assertThat(new Pair(pcmRepositoryBlackboardFactory.getBlackboardForIds("IllegalId"),
			new BlackboardFactory().getEmpty()), equalRegardingSeffElements());

		final Blackboard blackboardForIds =
			pcmRepositoryBlackboardFactory.getBlackboardForIds("_6f1a4LnmEeWVlphM5rov7g", "_FaSO4LnqEeWVlphM5rov7g");

		assertThat(blackboardForIds.getAllSeffLoops().size(), is(not(0)));

		for (SeffLoop seffLoop : blackboardForIds.getAllSeffLoops()) {
			// How do i figure out whether this is correct?
			seffLoop.getLoopBody().getStartFile();
		}

		final Blackboard blackboardForIds2 = pcmRepositoryBlackboardFactoryFactory.getAppSensorProjectInstance()
			.getBlackboardForIds("_Enr2B4RwEeWnEbz-sg1tMg");
		assertThat(blackboardForIds2.getAllSeffBranches(), is(0));
		assertThat(blackboardForIds2.getAllSeffLoops(), is(0));
		assertThat(blackboardForIds2.getAllRdias(), is(1));
		assertThat(blackboardForIds2.getAllExternalCallParameters(), is(0));

	}

	@Test
	public void appSensorRepositoryTest() {
		final PcmRepositoryBlackboardFactory appSensorBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getAppSensorProjectInstance();
		Blackboard appSensorBlackboard = appSensorBlackboardFactory.getBlackboardForAllElements();
	}

	/**
	 * Non-Javadoc.
	 * 
	 * @author Christoph Michelbach
	 */
	private class SomeInvalidObject implements EObject {

		@Override
		public EList<Adapter> eAdapters() {

			return null;
		}

		@Override
		public boolean eDeliver() {

			return false;
		}

		@Override
		public void eSetDeliver(final boolean deliver) {

		}

		@Override
		public void eNotify(final Notification notification) {

		}

		@Override
		public EClass eClass() {

			return null;
		}

		@Override
		public Resource eResource() {

			return null;
		}

		@Override
		public EObject eContainer() {

			return null;
		}

		@Override
		public EStructuralFeature eContainingFeature() {

			return null;
		}

		@Override
		public EReference eContainmentFeature() {

			return null;
		}

		@Override
		public EList<EObject> eContents() {

			return null;
		}

		@Override
		public TreeIterator<EObject> eAllContents() {

			return null;
		}

		@Override
		public boolean eIsProxy() {

			return false;
		}

		@Override
		public EList<EObject> eCrossReferences() {

			return null;
		}

		@Override
		public Object eGet(final EStructuralFeature feature) {

			return null;
		}

		@Override
		public Object eGet(final EStructuralFeature feature, final boolean resolve) {

			return null;
		}

		@Override
		public void eSet(final EStructuralFeature feature, final Object newValue) {

		}

		@Override
		public boolean eIsSet(final EStructuralFeature feature) {

			return false;
		}

		@Override
		public void eUnset(final EStructuralFeature feature) {

		}

		@Override
		public Object eInvoke(final EOperation operation, final EList<?> arguments) throws InvocationTargetException {

			return null;
		}

	}
}
