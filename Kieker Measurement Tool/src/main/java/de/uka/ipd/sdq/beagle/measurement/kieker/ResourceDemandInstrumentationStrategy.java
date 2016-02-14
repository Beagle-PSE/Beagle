package de.uka.ipd.sdq.beagle.measurement.kieker;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation.AbstracteEclipseAstInstrumentationStrategy;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Statement;

/**
 * Provides instrumentation statements to instrument code sections for which resource
 * demands are to be measured.
 *
 * @author Joshua Gleitze
 */
public class ResourceDemandInstrumentationStrategy extends AbstracteEclipseAstInstrumentationStrategy {

	@SuppressWarnings("unchecked")
	@Override
	protected Statement getBeforeStatement(final CodeSection codeSection) {
		final MethodInvocation startInvocation = this.factory.newMethodInvocation();
		startInvocation.setExpression(this.getName(this.getPackage(), "MeasurementCentral"));
		startInvocation.setName(this.factory.newSimpleName("startResourceDemand"));
		startInvocation.arguments().add(this.factory.newNumberLiteral("123"));
		return this.factory.newExpressionStatement(startInvocation);
	}

	@Override
	protected Statement getAfterStatement(final CodeSection codeSection) {
		final MethodInvocation endInvocation = this.factory.newMethodInvocation();
		endInvocation.setExpression(this.getName(this.getPackage(), "MeasurementCentral"));
		endInvocation.setName(this.factory.newSimpleName("stopResourceDemand"));
		return this.factory.newExpressionStatement(endInvocation);
	}

	/**
	 * Creates the name representing the remote measurement package.
	 *
	 * @return The name representing the remote measurement package.
	 */
	private Name getPackage() {
		return this.getName("de", "uka", "ipd", "sdq", "beagle", "measurement", "kieker", "remote");
	}

}
