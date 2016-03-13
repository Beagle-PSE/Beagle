package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.BlackboardSeffElementsMatcher.equalToRegardingSeffElements;
import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.Blackboard;
import de.uka.ipd.sdq.beagle.core.ProjectInformation;
import de.uka.ipd.sdq.beagle.core.facade.BlackboardCreator;
import de.uka.ipd.sdq.beagle.core.judge.EvaluableExpressionFitnessFunction;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.PcmSourceStatementLinkReader;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.PcmSourceStatementLinkRepository;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;

import org.junit.Rule;
import org.junit.Test;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Tests for {@link PcmRepositoryBlackboardFactoryAdder}.
 *
 * @author Christoph Michelbach
 * @author Roman Langrehr
 */
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

		final PcmSourceStatementLinkRepository pcmSourceStatementLinkRepository = new PcmSourceStatementLinkReader(
			this.loadFile("/de/uka/ipd/sdq/beagle/core/pcmconnection/PalladioFileShare/"
				+ "model/internal_architecture_model_source_statement_links.xml")).getPcmSourceLinkRepository();

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder((String) null,
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(NullPointerException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder((String) null,
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(NullPointerException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder((File) null,
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(NullPointerException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(IllegalArgumentException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder(".",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(IllegalArgumentException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("..",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(IllegalArgumentException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("/",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(IllegalArgumentException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("/tmp",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(IllegalArgumentException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("\0",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(IllegalArgumentException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder("/Path which does not exist on any normal system.",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(IllegalArgumentException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder(
					"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/XML File Which Doesn't Exist.xml",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(IllegalArgumentException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new PcmRepositoryBlackboardFactoryAdder(
					"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/Incredible Art.png",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository);
			}
		}, throwsException(IllegalArgumentException.class));

		final File[] impossibleRepositoryFiles = {
			new File(""), new File("."), new File(".."), new File("/"), new File("/tmp"), new File("\0")
		};

		for (final File impossibleRepositoryFile : impossibleRepositoryFiles) {
			assertThat(new ThrowingMethod() {

				@Override
				public void throwException() throws Exception {
					new PcmRepositoryBlackboardFactoryAdder(impossibleRepositoryFile,
						PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
						pcmSourceStatementLinkRepository);
				}
			}, throwsException(IllegalArgumentException.class));
		}

		// final PcmRepositoryBlackboardFactory mockedPcmRepositoryBlackboardFactory =
		// mock(PcmRepositoryBlackboardFactory.class);

		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		new PcmRepositoryBlackboardFactoryAdder(
			this.loadFile("/de/uka/ipd/sdq/beagle/core/pcmconnection/PalladioFileShare/"
				+ "model/internal_architecture_model.repository"),
			PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
			pcmSourceStatementLinkRepository).getBlackboardForAllElements(blackboardCreator);
		blackboardCreator.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator.setProjectInformation(mock(ProjectInformation.class));
		final BlackboardCreator blackboardCreator2 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryFactory.getPalladioFileShareProjectInstance()
			.getBlackboardForAllElements(blackboardCreator2);
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
		final PcmRepositoryBlackboardFactoryAdder pcmRepositoryBlackboardFactoryAdder =
			pcmRepositoryBlackboardFactoryFactory.getPalladioFileShareProjectInstance();
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryAdder.getBlackboardForAllElements(blackboardCreator);
		blackboardCreator.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator.setProjectInformation(mock(ProjectInformation.class));
		final Blackboard result = blackboardCreator.createBlackboard();
		assertThat(result, is(notNullValue()));

		assertThat(result.getAllRdias().size(), is(not(0)));
		assertThat(result.getAllSeffBranches().size(), is(not(0)));
		assertThat(result.getAllSeffLoops().size(), is(not(0)));

		// Use a corrupted repository here.

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				final BlackboardCreator blackboardCreator = new BlackboardCreator();

				final PcmSourceStatementLinkRepository pcmSourceStatementLinkRepository =
					new PcmSourceStatementLinkReader(
						PcmRepositoryBlackboardFactoryTest.this.loadFile("/de/uka/ipd/sdq/beagle/core/pcmconnection/"
							+ "internal_architecture_model_source_statement_links.xml")).getPcmSourceLinkRepository();

				new PcmRepositoryBlackboardFactoryAdder(
					"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/"
						+ "internal_architecture_model Corrupted SeffBranch.repository",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository).getBlackboardForAllElements(blackboardCreator);
			}
		}, throwsException(RuntimeException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				final BlackboardCreator blackboardCreator = new BlackboardCreator();

				final PcmSourceStatementLinkRepository pcmSourceStatementLinkRepository =
					new PcmSourceStatementLinkReader(
						PcmRepositoryBlackboardFactoryTest.this.loadFile("/de/uka/ipd/sdq/beagle/core/pcmconnection/"
							+ "internal_architecture_model_source_statement_links.xml")).getPcmSourceLinkRepository();

				new PcmRepositoryBlackboardFactoryAdder(
					"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/"
						+ "internal_architecture_model Corrupted RDIA.repository",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository).getBlackboardForAllElements(blackboardCreator);
			}
		}, throwsException(RuntimeException.class));
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				final BlackboardCreator blackboardCreator = new BlackboardCreator();

				final PcmSourceStatementLinkRepository pcmSourceStatementLinkRepository =
					new PcmSourceStatementLinkReader(
						PcmRepositoryBlackboardFactoryTest.this.loadFile("/de/uka/ipd/sdq/beagle/core/pcmconnection/"
							+ "internal_architecture_model_source_statement_links.xml")).getPcmSourceLinkRepository();

				new PcmRepositoryBlackboardFactoryAdder(
					"src/test/resources/de/uka/ipd/sdq/beagle/core/pcmconnection/"
						+ "internal_architecture_model Corrupted ExtCallParameter.repository",
					PcmRepositoryBlackboardFactoryFactory.PALLADIO_FILE_SHARE_PROJECT_TEST_SOURCE_CODE_FILE_PROVIDER,
					pcmSourceStatementLinkRepository).getBlackboardForAllElements(blackboardCreator);
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
			pcmRepositoryBlackboardFactoryFactory.getPalladioFileShareProjectInstance();

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
		final PcmRepositoryBlackboardFactoryAdder pcmRepositoryBlackboardFactoryPalladioFileShare =
			pcmRepositoryBlackboardFactoryFactory.getPalladioFileShareProjectInstance();
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator, "");
		blackboardCreator.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator.createBlackboard(),
			is(equalToRegardingSeffElements(new BlackboardFactory().getEmpty())));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(new BlackboardCreator(),
					(String[]) null);
			}
		}, throwsException(NullPointerException.class));

		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(new BlackboardCreator(),
					(String) null);
			}
		}, throwsException(NullPointerException.class));

		final BlackboardCreator blackboardCreator1 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator1,
			"_Dr5WiOe1EeW5NafnxUciog");
		blackboardCreator1.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator1.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator1.createBlackboard(), is(not(nullValue())));

		final BlackboardCreator blackboardCreator2 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator2,
			"_Dr5WiOe1EeW5NafnxUciog");
		blackboardCreator2.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator2.setProjectInformation(mock(ProjectInformation.class));
		final BlackboardCreator blackboardCreator3 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator3,
			"_Dr5WiOe1EeW5NafnxUciog");
		blackboardCreator3.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator3.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator2.createBlackboard(),
			is(equalToRegardingSeffElements(blackboardCreator3.createBlackboard())));

		final BlackboardCreator blackboardCreator4 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator4,
			"_Dr2TM-e1EeW5NafnxUciog");
		blackboardCreator4.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator4.setProjectInformation(mock(ProjectInformation.class));
		final BlackboardCreator blackboardCreator5 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator5,
			"_FaSO4LnqEeWVlphM5rov7g");
		blackboardCreator5.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator5.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator4.createBlackboard(),
			is(not(equalToRegardingSeffElements(blackboardCreator5.createBlackboard()))));

		final BlackboardCreator blackboardCreator6 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator6,
			"_Dr2TM-e1EeW5NafnxUciog");
		blackboardCreator6.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator6.setProjectInformation(mock(ProjectInformation.class));
		final BlackboardCreator blackboardCreator7 = new BlackboardCreator();
		blackboardCreator7.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator7.setProjectInformation(mock(ProjectInformation.class));
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForAllElements(blackboardCreator7);
		assertThat(blackboardCreator6.createBlackboard(),
			is(not(equalToRegardingSeffElements(blackboardCreator7.createBlackboard()))));

		final BlackboardCreator blackboardCreator8 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator8,
			"_SomeIdWhichDosntExistA");
		blackboardCreator8.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator8.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator8.createBlackboard(),
			is(equalToRegardingSeffElements(new BlackboardFactory().getEmpty())));

		final BlackboardCreator blackboardCreator9 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator9, "_TooShortId");
		blackboardCreator9.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator9.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator9.createBlackboard(),
			is(equalToRegardingSeffElements(new BlackboardFactory().getEmpty())));

		final BlackboardCreator blackboardCreator10 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator10, "IllegalId");
		blackboardCreator10.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator10.setProjectInformation(mock(ProjectInformation.class));
		assertThat(blackboardCreator10.createBlackboard(),
			is(equalToRegardingSeffElements(new BlackboardFactory().getEmpty())));

		final BlackboardCreator blackboardCreator11 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryPalladioFileShare.getBlackboardForIds(blackboardCreator11,
			"_DjpZdue1EeW5NafnxUciog", "_DjTbMue1EeW5NafnxUciog");
		blackboardCreator11.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator11.setProjectInformation(mock(ProjectInformation.class));
		final Blackboard blackboardForIds11 = blackboardCreator11.createBlackboard();

		// Branch transitions are counted. Not branches.
		assertThat(blackboardForIds11.getAllSeffBranches().size(), is(1));
		assertThat(blackboardForIds11.getAllSeffLoops().size(), is(1));
		assertThat(blackboardForIds11.getAllRdias().size(), is(5 * 6));
		assertThat(blackboardForIds11.getAllExternalCallParameters().size(), is(2));

		final BlackboardCreator blackboardCreator12 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryFactory.getPalladioFileShareProjectInstance()
			.getBlackboardForIds(blackboardCreator12, "_DjpZcue1EeW5NafnxUciog");
		blackboardCreator12.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator12.setProjectInformation(mock(ProjectInformation.class));
		final Blackboard blackboardForIds12 = blackboardCreator12.createBlackboard();
		assertThat(blackboardForIds12.getAllSeffBranches().size(), is(0));
		assertThat(blackboardForIds12.getAllSeffLoops().size(), is(0));
		assertThat(blackboardForIds12.getAllRdias().size(), is(1 * 6));
		assertThat(blackboardForIds12.getAllExternalCallParameters().size(), is(0));

		final BlackboardCreator blackboardCreator13 = new BlackboardCreator();
		pcmRepositoryBlackboardFactoryFactory.getPalladioFileShareProjectInstance()
			.getBlackboardForIds(blackboardCreator13, "_DjjS0Oe1EeW5NafnxUciog");
		blackboardCreator13.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator13.setProjectInformation(mock(ProjectInformation.class));
		final Blackboard blackboardForIds13 = blackboardCreator13.createBlackboard();
		assertThat(blackboardForIds13.getAllSeffBranches().size(), is(0));
		assertThat(blackboardForIds13.getAllSeffLoops().size(), is(3));
		assertThat(blackboardForIds13.getAllRdias().size(), is(11 * 6));
		assertThat(blackboardForIds13.getAllExternalCallParameters().size(), is(3));

	}

	/**
	 * Test method using the Palladio file share project.
	 *
	 */
	@Test
	public void palladioFileShareProjectRepositoryTest() {
		final PcmRepositoryBlackboardFactoryAdder palladioFileShareBlackboardFactory =
			pcmRepositoryBlackboardFactoryFactory.getPalladioFileShareProjectInstance();
		final BlackboardCreator blackboardCreator = new BlackboardCreator();
		palladioFileShareBlackboardFactory.getBlackboardForAllElements(blackboardCreator);
		blackboardCreator.setFitnessFunction(mock(EvaluableExpressionFitnessFunction.class));
		blackboardCreator.setProjectInformation(mock(ProjectInformation.class));
		blackboardCreator.createBlackboard();
	}

	/**
	 * Loads a file with respect to where it's positioned in the class path.
	 *
	 * @param path The path of the file to load.
	 *
	 * @return The loaded file.
	 */
	private File loadFile(final String path) {
		try {
			return new File(PcmRepositoryBlackboardFactoryTest.class.getResource(path).toURI());
		} catch (final URISyntaxException uriSyntaxException) {
			throw new RuntimeException(uriSyntaxException.getMessage());
		}
	}
}
