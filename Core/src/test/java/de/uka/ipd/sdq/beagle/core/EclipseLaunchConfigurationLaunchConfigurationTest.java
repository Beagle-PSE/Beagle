package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.uka.ipd.sdq.beagle.core.failurehandling.ExceptionThrowingFailureHandler.FailureException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaProject;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link EclipseLaunchConfigurationLaunchConfiguration} and contains all test cases
 * needed to check every method.
 *
 * @author Annika Berger
 */
public class EclipseLaunchConfigurationLaunchConfigurationTest {

	/**
	 * A mocked launch configuration.
	 */
	private ILaunchConfigurationWorkingCopy launchConfiguration;

	/**
	 * Populates {@link #launchConfiguration}, and {@link #workingCopy}.
	 *
	 * @throws CoreException will never be thrown
	 */
	@Before
	public void mockLaunchConfiguration() throws CoreException {
		this.launchConfiguration = mock(ILaunchConfigurationWorkingCopy.class);
		when(this.launchConfiguration.getWorkingCopy()).thenReturn(this.launchConfiguration);
	}

	/**
	 * Test method for
	 * {@link EclipseLaunchConfigurationLaunchConfiguration#EclipseLaunchConfigurationLaunchConfiguration(ILaunchConfiguration)}
	 * .
	 */
	@Test
	public void eclipseLaunchConfigurationLaunchConfiguration() {
		new EclipseLaunchConfigurationLaunchConfiguration(this.launchConfiguration, mock(IJavaProject.class));

		assertThat("launch configuration must not be null",
			() -> new EclipseLaunchConfigurationLaunchConfiguration(null, mock(IJavaProject.class)),
			throwsException(NullPointerException.class));

		assertThat("java project must not be null",
			() -> new EclipseLaunchConfigurationLaunchConfiguration(this.launchConfiguration, null),
			throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link EclipseLaunchConfigurationLaunchConfiguration#execute()} .
	 *
	 * @throws CoreException Will not happen.
	 */
	@Test
	public void execute() throws CoreException {
		final EclipseLaunchConfigurationLaunchConfiguration launchConfig =
			new EclipseLaunchConfigurationLaunchConfiguration(this.launchConfiguration, mock(IJavaProject.class));

		launchConfig.execute();
		then(this.launchConfiguration).should().launch(any(), any());

		given(this.launchConfiguration.launch(any(), any())).willThrow(new CoreException(Status.CANCEL_STATUS));
		assertThat(launchConfig::execute, throwsException(FailureException.class));
	}
}
