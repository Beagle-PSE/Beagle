package de.uka.ipd.sdq.beagle.core.measurement;
/**
 * ATTENTION: Test coverage check turned off. Remove this comments block when implementing
 * this class!
 * 
 * <p>COVERAGE:OFF
 */

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A measurement result that may depend on the state of variables while executing the
 * measured code section. That a result is a ParameterisationDependentMeasurementResult
 * does not imply that the {@link Parameterisation} was actually recorded while measuring
 * it.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public abstract class ParameterisationDependentMeasurementResult {

	/**
	 * The {@link Parameterisation} for this measurement or {@code null}, when no
	 * measurements were made.
	 */
	private Parameterisation parameterisation;

	/**
	 * Creates a result for a code section measurement for which no parameterisation was
	 * recorded.
	 */
	public ParameterisationDependentMeasurementResult() {
	}

	/**
	 * Creates a result for a code section measurement.
	 *
	 * @param parameterisation The state of variables during the measurement. Must not be
	 *            {@code null}.
	 */
	public ParameterisationDependentMeasurementResult(final Parameterisation parameterisation) {
		Validate.notNull(parameterisation);
		this.parameterisation = parameterisation;
	}

	/**
	 * Gets the parameterisation, describing variables’ state when measuring this result.
	 *
	 * @return The parameterisation, or {@code null} if no parameterisation was recorded
	 *         when measuring this result.
	 */
	public Parameterisation getParameterisation() {
		return this.parameterisation;
	}

	@Override
	public boolean equals(final Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (object.getClass() != this.getClass()) {
			return false;
		}
		final ParameterisationDependentMeasurementResult other = (ParameterisationDependentMeasurementResult) object;
		return new EqualsBuilder().append(this.parameterisation, other.parameterisation).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(41, 63).append(this.parameterisation).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("parameterisation", this.parameterisation).toString();
	}
}
