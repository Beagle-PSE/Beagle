package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.BlackboardSeffElementsMatcher.equalToRegardingSeffElements;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.BlackboardCreator;
import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
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
import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Tests for {@link PcmRepositoryBlackboardFactoryAdder}.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 */
@PrepareForTest(PcmRepositoryBlackboardFactoryAdder.class)
public class PcmRepositoryBlackboardFactoryTest {

	/**
	 * A factory which creates instances of {@link PcmRepositoryBlackboardFactoryAdder}.
	 */
	private static PcmRepositoryBlackboardFactoryFactory pcmRepositoryBlackboardFactoryFactory =
		new PcmRepositoryBlackboardFactoryFactory();

	/**
	 * Rule loading PowerMock (to mock static methods).
	 */
	@Rule
	public PowerMockRule loadPowerMock = new PowerMockRule();

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactoryAdder#PcmRepositoryBlackboardFactory(java.util.Set)
	 * and PcmRepositoryBlackboardFactory#PcmRepositoryBlackboardFactory(String)}. Asserts
	 * that creation is possible and {@code null} or an empty string or otherwise
	 * impossible path cannot be passed.
	 *
	 * @throws FileNotFoundException If the factory throws an exception when it’s not
	 *             expected.
	 */
	@Test
	public void pcmRepositoryBlackboardFactory() throws FileNotFoundException {
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder((String) null,
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
			}
		}, throwsException(NullPointerException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder((String) null,
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
			}
		}, throwsException(NullPointerException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder((File) null,
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
			}
		}, throwsException(NullPointerException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("",
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
			}
		}, throwsException(IllegalArgumentException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder(".",
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
			}
		}, throwsException(IllegalArgumentException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("..",
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
			}
		}, throwsException(IllegalArgumentException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("/",
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
			}
		}, throwsException(IllegalArgumentException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("/tmp",
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
			}
		}, throwsException(IllegalArgumentException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("\0",
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
			}
		}, throwsException(IllegalArgumentException.class));

		mockStatic(EmfHelper.class);

		// final EObject mocked = mock(EObject.class);
		// given(EmfHelper.loadFromXMIFile(any(), any())).willReturn(mocked);
		// assertThat(() -> pcmRepositoryBlackboardFactoryFactory.getValidInstance(),
		// throwsException(IllegalArgumentException.class));

		final File[] impossibleRepositoryFiles = {
			new File(""), new File("."), new File(".."), new File("/"), new File("/tmp"), new File("\0")
		};

		for (final File impossibleRepositoryFile : impossibleRepositoryFiles) {
			assertThat(new ThrowingMethod() {

				@Override
				public void throwException() throws Exception {
					new PcmRepositoryBlackboardFactoryAdder(impossibleRepositoryFile,
						PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER);
				}
			}, throwsException(IllegalArgumentException.class));
		}

		// final PcmRepositoryBlackboardFactory mockedPcmRepositoryBlackboardFactory =
		// mock(PcmRepositoryBlackboardFactory.class);

		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		new PcmRepositoryBlackboardFactoryAdder(
			new File("src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/Family.repository"),
			PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER)
				.getBlackboardForAllElements(blackboardCreator);
		blackboardCreator.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator.setProjectInformation(mock(ProjectInformation.class));
		final BlackboardCreator blackboardCreator2 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryFactory.getValidInstance().getBlackboardForAllElements(blackboardCreator2);
		blackboardCreator2.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator2.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator.createBlackboard(),
			is(equalToRegardingSeffElements(blackboardCreator2.createBlackboard())));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactoryAdder#getBlackboardForAllElements(BlackboardCreator)}
	 * .
	 */
	@Test
	public void getBlackboardForAllElements() {
		final PcmRepositoryBlackboardFactoryAdder pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getAppSensorProjectInstance();
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		pcmRepositoryBlackboardFactory.getBlackboardForAllElements(blackboardCreator);
		blackboardCreator.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator.setProjectInformation(mock(ProjectInformation.class));
		final Blackboard result = blackboardCreator.createBlackboard();
		assertThat(result, is(notNullValue()));

		assertThat(result.getAllRdias().size(), is(not(0)));
		assertThat(result.getAllSeffBranches().size(), is(not(0)));
		assertThat(result.getAllSeffLoops().size(), is(0));

		// Use a corrupted repository here.

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				final BlackboardCreator blackboardCreator = new BlackboardCreator();
				new PcmRepositoryBlackboardFactoryAdder(
					"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/CorruptedSeffBranchAppSensor.repository",
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER)
						.getBlackboardForAllElements(blackboardCreator);
			}
		}, throwsException(RuntimeException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				final BlackboardCreator blackboardCreator = new BlackboardCreator();
				new PcmRepositoryBlackboardFactoryAdder(
					"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/CorruptedRdiaAppSensor.repository",
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER)
						.getBlackboardForAllElements(blackboardCreator);
			}
		}, throwsException(RuntimeException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				final BlackboardCreator blackboardCreator = new BlackboardCreator();
				new PcmRepositoryBlackboardFactoryAdder(
					"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/"
						+ "CorruptedExternalCallParameterAppSensor.repository",
					PcmRepositoryBlackboardFactoryFactory.APP_SENSOR_TEST_SOURCE_CODE_FILE_PROVIDER)
						.getBlackboardForAllElements(blackboardCreator);
			}
		}, throwsException(RuntimeException.class));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactoryAdder#getBlackboardForIds(java.util.Collection)}
	 * .
	 */
	@Test
	public void getBlackboardForIdsCollectionOfString() {
		final PcmRepositoryBlackboardFactoryAdder pcmRepositoryBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getValidInstance();

		final HashSet<String> collection = new HashSet<String>();
		collection.add("");
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		pcmRepositoryBlackboardFactory.getBlackboardForIds(collection, blackboardCreator);
		blackboardCreator.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator.createBlackboard(),
			is(equalToRegardingSeffElements(new BlackboardFactory().getEmpty())));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				pcmRepositoryBlackboardFactory.getBlackboardForIds((Collection<String>) null, new BlackboardCreator());
			}
		}, throwsException(NullPointerException.class));

		final Collection<String> collectionContainingNull = new HashSet<>();
		collectionContainingNull.add("uaeua");
		collectionContainingNull.add("ueuaue");
		collectionContainingNull.add(null);
		collectionContainingNull.add("ueul");
		collectionContainingNull.add("vcwvcwv");
		collectionContainingNull.add("xlcxlc");

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				pcmRepositoryBlackboardFactory.getBlackboardForIds(collectionContainingNull, new BlackboardCreator());
			}
		}, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for
	 * {@link PcmRepositoryBlackboardFactoryAdder#getBlackboardForIds(java.lang.String[])}
	 * .
	 *
	 */
	@Test
	public void getBlackboardForIdsStringArray() {
		final PcmRepositoryBlackboardFactoryAdder pcmRepositoryBlackboardFactoryAppSensor =
			pcmRepositoryBlackboardFactoryFactory.getAppSensorProjectInstance();
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator, "");
		blackboardCreator.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator.createBlackboard(),
			is(equalToRegardingSeffElements(new BlackboardFactory().getEmpty())));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(new BlackboardCreator(), (String[]) null);
			}
		}, throwsException(NullPointerException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(new BlackboardCreator(), (String) null);
			}
		}, throwsException(NullPointerException.class));

		final BlackboardCreator blackboardCreator1 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator1, "_6f1a4LnmEeWVlphM5rov7g");
		blackboardCreator1.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator1.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator1.createBlackboard(), is(not(nullValue())));

		final BlackboardCreator blackboardCreator2 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator2, "_6f1a4LnmEeWVlphM5rov7g");
		blackboardCreator2.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator2.setProjectInformation(mock(ProjectInformation.class));
		final BlackboardCreator blackboardCreator3 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator3, "_6f1a4LnmEeWVlphM5rov7g");
		blackboardCreator3.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator3.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator2.createBlackboard(),
			is(equalToRegardingSeffElements(blackboardCreator3.createBlackboard())));

		// The first ID is from {@code AppSensor.repository}, the second one from {@code
		// Family.repositor}.
		final BlackboardCreator blackboardCreator4 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator4, "_EofuUYRwEeWnEbz-sg1tMg");
		blackboardCreator4.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator4.setProjectInformation(mock(ProjectInformation.class));
		final BlackboardCreator blackboardCreator5 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator5, "_FaSO4LnqEeWVlphM5rov7g");
		blackboardCreator5.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator5.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator4.createBlackboard(),
			is(not(equalToRegardingSeffElements(blackboardCreator5.createBlackboard()))));

		final BlackboardCreator blackboardCreator6 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator6, "_EofuUYRwEeWnEbz-sg1tMg");
		blackboardCreator6.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator6.setProjectInformation(mock(ProjectInformation.class));
		final BlackboardCreator blackboardCreator7 = new BlackboardCreator();
		blackboardCreator7.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator7.setProjectInformation(mock(ProjectInformation.class));
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForAllElements(blackboardCreator7);
		assertThat(blackboardCreator6.createBlackboard(),
			is(not(equalToRegardingSeffElements(blackboardCreator7.createBlackboard()))));

		final BlackboardCreator blackboardCreator8 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator8, "_SomeIdWhichDosntExistA");
		blackboardCreator8.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator8.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator8.createBlackboard(),
			is(equalToRegardingSeffElements(new BlackboardFactory().getEmpty())));

		final BlackboardCreator blackboardCreator9 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator9, "_TooShortId");
		blackboardCreator9.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator9.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator9.createBlackboard(),
			is(equalToRegardingSeffElements(new BlackboardFactory().getEmpty())));

		final BlackboardCreator blackboardCreator10 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator10, "IllegalId");
		blackboardCreator10.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator10.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator10.createBlackboard(),
			is(equalToRegardingSeffElements(new BlackboardFactory().getEmpty())));

		final BlackboardCreator blackboardCreator11 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAppSensor.getBlackboardForIds(blackboardCreator11, "_EnfoyoRwEeWnEbz-sg1tMg",
			"_En2OE4RwEeWnEbz-sg1tMg");
		blackboardCreator11.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator11.setProjectInformation(mock(ProjectInformation.class));
		final Blackboard blackboardForIds = blackboardCreator11.createBlackboard();

		assertThat(blackboardForIds.getAllSeffBranches().size(), is(0));
		assertThat(blackboardForIds.getAllSeffLoops().size(), is(0));
		assertThat(blackboardForIds.getAllRdias().size(), is(2));
		assertThat(blackboardForIds.getAllExternalCallParameters().size(), is(0));

		final BlackboardCreator blackboardCreator12 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryFactory.getAppSensorProjectInstance().getBlackboardForIds(blackboardCreator12,
			"_Enr2B4RwEeWnEbz-sg1tMg");
		blackboardCreator12.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator12.setProjectInformation(mock(ProjectInformation.class));
		final Blackboard blackboardForIds2 = blackboardCreator12.createBlackboard();
		assertThat(blackboardForIds2.getAllSeffBranches().size(), is(0));
		assertThat(blackboardForIds2.getAllSeffLoops().size(), is(0));
		assertThat(blackboardForIds2.getAllRdias().size(), is(1));
		assertThat(blackboardForIds2.getAllExternalCallParameters().size(), is(0));

	}

	/**
	 * Test method using the app sensor.
	 *
	 */
	@Test
	public void appSensorRepositoryTest() {
		final PcmRepositoryBlackboardFactoryAdder appSensorBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getAppSensorProjectInstance();
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		appSensorBlackboardFactory.getBlackboardForAllElements(blackboardCreator);
		blackboardCreator.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator.setProjectInformation(mock(ProjectInformation.class));
		final Blackboard appSensorBlackboard = blackboardCreator.createBlackboard();
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
