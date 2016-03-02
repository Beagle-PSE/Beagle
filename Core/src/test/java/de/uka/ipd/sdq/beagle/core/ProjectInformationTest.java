package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;
import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;
import de.uka.ipd.sdq.beagle.core.testutil.factories.LaunchConfigurationFactory;

import org.junit.Test;

import java.nio.charset.Charset;
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
		final ProjectInformation projectInfo = new ProjectInformation(0, mock(SourceCodeFileProvider.class), null,
			Charset.defaultCharset(), new LaunchConfigurationFactory().getAllAsSet());
		ThrowingMethod method = () -> {
			projectInfo.getBuildPath();
		};
		assertThat(method, throwsException(NullPointerException.class));

		final ProjectInformation projectInfo1 = new ProjectInformation(0, null, "", Charset.defaultCharset(),
			new LaunchConfigurationFactory().getAllAsSet());
		method = () -> {
			projectInfo1.getFileProvider();
		};
		assertThat(method, throwsException(NullPointerException.class));

		final ProjectInformation projectInfo2 = new ProjectInformation(0, mock(SourceCodeFileProvider.class), "", null,
			new LaunchConfigurationFactory().getAllAsSet());
		method = () -> {
			projectInfo2.getCharset();
		};
		assertThat(method, throwsException(NullPointerException.class));

		final ProjectInformation projectInfo3 = new ProjectInformation(0, mock(SourceCodeFileProvider.class), "",
			Charset.defaultCharset(), new LaunchConfigurationFactory().getAllAsSet());
	}

	/**
	 * Test method for {@link ProjectInformation#getBuildPath()}.
	 */
	@Test
	public void getBuildPath() {
		final String testBuildPath = "TestBuildPath";
		final ProjectInformation projectInfo = new ProjectInformation(0, mock(SourceCodeFileProvider.class),
			testBuildPath, Charset.defaultCharset(), new LaunchConfigurationFactory().getAllAsSet());
		assertThat(projectInfo.getBuildPath(), containsString(testBuildPath));
	}

	/**
	 * Test method for {@link ProjectInformation#getCharset()}.
	 */
	@Test
	public void getCharset() {
		final Charset testCharset = Charset.defaultCharset();
		final ProjectInformation projectInfo = new ProjectInformation(0, mock(SourceCodeFileProvider.class), "",
			testCharset, new LaunchConfigurationFactory().getAllAsSet());
		assertThat(projectInfo.getCharset(), is(testCharset));
	}

	/**
	 * Test method for {@link ProjectInformation#getTimeout()}.
	 */
	@Test
	public void getTimeout() {
		final int testTimeout = 10;
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
		final ProjectInformation projectInfo = new ProjectInformation(0, mock(SourceCodeFileProvider.class), "",
			Charset.defaultCharset(), new LaunchConfigurationFactory().getAllAsSet());
		assertThat(projectInfo.getFileProvider(), is(testFileProvider));
	}

	/**
	 * Test method for {@link ProjectInformation#getLaunchConfigurations()}.
	 */
	@Test
	public void getLaunchConfigurations() {
		final Set<LaunchConfiguration> testLaunchConfig = new LaunchConfigurationFactory().getAllAsSet();
		final ProjectInformation projectInfo = new ProjectInformation(0, mock(SourceCodeFileProvider.class), "",
			Charset.defaultCharset(), new LaunchConfigurationFactory().getAllAsSet());
		assertThat(projectInfo.getLaunchConfigurations(), is(testLaunchConfig));
	}

}
