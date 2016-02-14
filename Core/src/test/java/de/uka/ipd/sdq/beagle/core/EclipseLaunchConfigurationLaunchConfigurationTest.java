package de.uka.ipd.sdq.beagle.core;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.testutil.ThrowingMethod;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.junit.Test;

/**
 * Tests {@link EclipseLaunchConfigurationLaunchConfiguration} and contains all test cases
 * needed to check every method.
 *
 * @author Annika Berger
 */
public class EclipseLaunchConfigurationLaunchConfigurationTest {

	/**
	 * Test method for
	 * {@link EclipseLaunchConfigurationLaunchConfiguration#EclipseLaunchConfigurationLaunchConfiguration(ILaunchConfiguration)}
	 * .
	 */
	@Test
	public void eclipseLaunchConfigurationLaunchConfiguration() {
		final ILaunchConfiguration mockLaunchConfiguration = mock(ILaunchConfiguration.class);
		new EclipseLaunchConfigurationLaunchConfiguration(mockLaunchConfiguration);

		assertThat("launch configuration must not be null", new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				new EclipseLaunchConfigurationLaunchConfiguration(null);
			}
		}, throwsException(NullPointerException.class));
	}

	/**
	 * Test method for {@link EclipseLaunchConfigurationLaunchConfiguration#execute()} .
	 *
	 * @throws CoreException Will not happen.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void execute() throws CoreException {
		final ILaunchConfiguration mockLaunchConfiguration = mock(ILaunchConfiguration.class);
		final EclipseLaunchConfigurationLaunchConfiguration launchConfig =
			new EclipseLaunchConfigurationLaunchConfiguration(mockLaunchConfiguration);

		launchConfig.execute();
		then(mockLaunchConfiguration).should().launch(any(), any());

		given(mockLaunchConfiguration.launch(any(), any())).willThrow(CoreException.class);
		assertThat(new ThrowingMethod() {

			@Override
			public void throwException() throws Exception {
				launchConfig.execute();
			}
		}, throwsException(RuntimeException.class));
	}
}
