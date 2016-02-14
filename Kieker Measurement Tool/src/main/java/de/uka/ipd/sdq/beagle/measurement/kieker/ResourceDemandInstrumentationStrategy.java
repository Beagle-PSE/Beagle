package de.uka.ipd.sdq.beagle.measurement.kieker;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation.EclipseAstInstrumentationStrategy;
import de.uka.ipd.sdq.beagle.measurement.kieker.instrumentation.EclipseStatementCreationHelper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;

/**
 * Provides instrumentation statements to instrument code sections for which resource
 * demands are to be measured.
 *
 * @author Joshua Gleitze
 */
public class ResourceDemandInstrumentationStrategy implements EclipseAstInstrumentationStrategy {

	@SuppressWarnings("unchecked")
	@Override
	public Statement instrumentStart(final CodeSection codeSection, final AST nodeFactory) {
		final EclipseStatementCreationHelper helper = new EclipseStatementCreationHelper(nodeFactory);
		final MethodInvocation startInvocation = nodeFactory.newMethodInvocation();
		startInvocation.setExpression(helper.getName("de", "uka", "ipd", "sdq", "beagle", "measurement", "kieker",
			"remote", "MeasurementCentral"));
		startInvocation.setName(nodeFactory.newSimpleName("startResourceDemand"));
		startInvocation.arguments().add(nodeFactory.newNumberLiteral("123"));
		return nodeFactory.newExpressionStatement(startInvocation);
	}

	@Override
	public Statement instrumentEnd(final CodeSection codeSection, final AST nodeFactory) {
		final EclipseStatementCreationHelper helper = new EclipseStatementCreationHelper(nodeFactory);
		final MethodInvocation endInvocation = nodeFactory.newMethodInvocation();
		endInvocation.setExpression(helper.getName("de", "uka", "ipd", "sdq", "beagle", "measurement", "kieker",
			"remote", "MeasurementCentral"));
		endInvocation.setName(nodeFactory.newSimpleName("stopResourceDemand"));
		return nodeFactory.newExpressionStatement(endInvocation);
	}

}
