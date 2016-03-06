package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser;
import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.BlackboardFactory;
import de.uka.ipd.sdq.beagle.core.testutil.factories.LaunchConfigurationFactory;
import de.uka.ipd.sdq.beagle.core.timeout.Timeout;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests {@link ProjectInformation} and contains all test cases needed to check every
 * method.
 *
 * @author Michael Vogt
 */
public class ProjectInformationTest {

	/**
	 * Test method for {@link ProjectInformation#constructor()}.
	 */
	@Test
	public void constructor() {
		ThrowingMethod method = () -> {
			new ProjectInformation(mock(Timeout.class), mock(SourceCodeFileProvider.class), null,
				Charset.defaultCharset(), new LaunchConfigurationFactory().getAllAsSet());
		};
		assertThat("buildPath must not be null.", method, throwsException(NullPointerException.class));

		method = () -> {
			new ProjectInformation(mock(Timeout.class), null, "", Charset.defaultCharset(),
				new LaunchConfigurationFactory().getAllAsSet());
		};
		assertThat("fileProvider must not be null.", method, throwsException(NullPointerException.class));

		final Set<LaunchConfiguration> launchConfig = new HashSet<>();
		method = () -> {
			new ProjectInformation(mock(Timeout.class), mock(SourceCodeFileProvider.class), "",
				Charset.defaultCharset(), launchConfig);
		};
		assertThat("launchConiguration must not be empty.", method, throwsException(IllegalArgumentException.class));
		new ProjectInformation(mock(Timeout.class), mock(SourceCodeFileProvider.class), "",

			Charset.defaultCharset(), new LaunchConfigurationFactory().getAllAsSet());
	}

	/**
	 * Test method for {@link ProjectInformation#getBuildPath()}.
	 */
	@Test
	public void getBuildPath() {
		final String testBuildPath = "TestBuildPath";
		final ProjectInformation projectInfo =
			new ProjectInformation(mock(Timeout.class), mock(SourceCodeFileProvider.class), testBuildPath,
				Charset.defaultCharset(), new LaunchConfigurationFactory().getAllAsSet());
		assertThat(projectInfo.getBuildPath(), containsString(testBuildPath));
	}

	/**
	 * Test method for {@link ProjectInformation#getCharset()}.
	 */
	@Test
	public void getCharset() {
		final Charset testCharset = Charset.defaultCharset();
		final ProjectInformation projectInfo = new ProjectInformation(mock(Timeout.class),
			mock(SourceCodeFileProvider.class), "", testCharset, new LaunchConfigurationFactory().getAllAsSet());
		assertThat(projectInfo.getCharset(), is(testCharset));
	}

	/**
	 * Test method for {@link ProjectInformation#getTimeout()}.
	 */
	@Test
	public void getTimeout() {
		final Timeout testTimeout = mock(Timeout.class);
		final ProjectInformation projectInfo = new ProjectInformation(testTimeout, mock(SourceCodeFileProvider.class),
			"", Charset.defaultCharset(), new LaunchConfigurationFactory().getAllAsSet());
		assertThat(projectInfo.getTimeout(), is(testTimeout));
	}

	/**
	 * Test method for {@link ProjectInformation#getFileProvider()}.
	 */
	@Test
	public void getFileProvider() {
		final SourceCodeFileProvider testFileProvider = mock(SourceCodeFileProvider.class);
		final ProjectInformation projectInfo = new ProjectInformation(mock(Timeout.class), testFileProvider, "",
			Charset.defaultCharset(), new LaunchConfigurationFactory().getAllAsSet());
		assertThat(projectInfo.getFileProvider(), is(testFileProvider));
	}

	/**
	 * Test method for {@link ProjectInformation#getLaunchConfigurations()}.
	 */
	@Test
	public void getLaunchConfigurations() {
		final Set<LaunchConfiguration> testLaunchConfig = new LaunchConfigurationFactory().getAllAsSet();
		final ProjectInformation projectInfo = new ProjectInformation(mock(Timeout.class),
			mock(SourceCodeFileProvider.class), "", Charset.defaultCharset(), testLaunchConfig);
		assertThat(projectInfo.getLaunchConfigurations(), is(testLaunchConfig));
	}

	/**
	 * Testing some valid calls on analysisController.
	 *
	 */
	@Test
	public void setAnalysisState() {

		final BlackboardFactory blackboardFactory = new BlackboardFactory();
		final Set<MeasurementTool> measurementTools = new HashSet<MeasurementTool>();
		measurementTools.add(mock(MeasurementTool.class));
		final Set<MeasurementResultAnalyser> measurementResultAnalyser = new HashSet<MeasurementResultAnalyser>();
		measurementResultAnalyser.add(mock(MeasurementResultAnalyser.class));
		final Set<ProposedExpressionAnalyser> proposedExpressionAnalyser = new HashSet<ProposedExpressionAnalyser>();
		proposedExpressionAnalyser.add(mock(ProposedExpressionAnalyser.class));

		final AnalysisController analysisController = new AnalysisController(blackboardFactory.getEmpty(),
			measurementTools, measurementResultAnalyser, proposedExpressionAnalyser);

		analysisController.performAnalysis();

		analysisController.setAnalysisState(analysisController.getAnalysisState());
		analysisController.setAnalysisState(AnalysisState.ENDING);
		analysisController.setAnalysisState(AnalysisState.RUNNING);
		analysisController.setAnalysisState(AnalysisState.ABORTING);
		analysisController.setAnalysisState(AnalysisState.TERMINATED);

	}

}
