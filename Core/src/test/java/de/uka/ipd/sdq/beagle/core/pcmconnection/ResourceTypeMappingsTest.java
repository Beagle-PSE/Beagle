package de.uka.ipd.sdq.beagle.core.pcmconnection;

import static de.uka.ipd.sdq.beagle.core.testutil.ExceptionThrownMatcher.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.uka.ipd.sdq.beagle.core.ResourceDemandType;

import org.junit.Test;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;

/**
 * Tests {@link ResourceTypeMappings} and contains all test cases needed to check all
 * methods.
 * 
 * @author Annika Berger
 */
public class ResourceTypeMappingsTest {

	/**
	 * Test method for {@link ResourceTypeMappings#getMappings()}.
	 * 
	 * <p>Asserts that calling the method does not throw an exception and the returned
	 * mapping is not {@code null}.
	 */
	@Test
	public void getMappings() {
		final ResourceTypeMappings mappings = ResourceTypeMappings.getMappings();
		assertThat(mappings, is(not(nullValue())));
	}

	/**
	 * Test method for {@link ResourceTypeMappings#getBeagleType(ProcessingResourceType)}.
	 * 
	 * <p>Asserts that a {@link NullPointerException} is thrown if the
	 * {@link ProcessingResourceType} is {@code null}.
	 */
	@Test
	public void getBeagleType() {
		final ResourceTypeMappings mappings = ResourceTypeMappings.getMappings();
		assertThat("Processing Resource Type must not be null.", () -> mappings.getBeagleType(null),
			throwsException(NullPointerException.class));
		
		final ProcessingResourceType processingResourceType = mock(ProcessingResourceType.class);
		given(processingResourceType.getEntityName()).willReturn("newProcessingResourceType");
		assertThat(mappings.getBeagleType(processingResourceType).getName(), is("newProcessingResourceType"));
	}

	/**
	 * Test method for {@link ResourceTypeMappings#getPcmType(ResourceDemandType)}.
	 * 
	 * <p>Asserts that a {@link NullPointerException} is thrown if the
	 * {@link ResourceDemandType} is {@code null} and no exception is thrown and the result is not {@code null} if a valid
	 * {@link ResourceDemandType} is entered. Asserts that a {@link ProcessingResourceType} is returned for CPU_NS which
	 * has the Entity name 'CPU'.
	 */
	@Test
	public void getPcmType() {
		final ResourceTypeMappings mappings = ResourceTypeMappings.getMappings();
		assertThat("Resource Demand Type must not be null.", () -> mappings.getPcmType(null),
			throwsException(NullPointerException.class));

		final ResourceDemandType newType = new ResourceDemandType("TestType", true);
		assertThat(mappings.getPcmType(newType), is(not(nullValue())));
		
		assertThat(mappings.getPcmType(ResourceDemandType.RESOURCE_TYPE_CPU_NS).getEntityName(), is("CPU"));

	}

}
