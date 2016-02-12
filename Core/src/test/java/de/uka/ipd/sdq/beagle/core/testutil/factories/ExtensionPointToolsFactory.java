package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.MeasurementResultAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser;
import de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyMeasurementResultAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.analysis.ReadOnlyProposedExpressionAnalyserBlackboardView;
import de.uka.ipd.sdq.beagle.core.measurement.MeasurementTool;

import java.util.HashSet;
import java.util.Set;

/**
 * Creates (empty) {@linkplain MeasurementTool MeasurementTools},
 * {@linkplain MeasurementResultAnalyser MeasurementResultAnalysers} and
 * {@linkplain ProposedExpressionAnalyser ProposedExpressionAnalysers} for tests.
 *
 * @author Roman Langrehr
 */
public class ExtensionPointToolsFactory {

	/**
	 * Creates a new set of {@code 2} {@linkplain MeasurementTool MeasurementTools}.
	 *
	 * @return A new set with new instances of {@link MeasurementTool}.
	 */
	public Set<MeasurementTool> createNewMeasurementToolsSet() {
		final Set<MeasurementTool> tools = new HashSet<>();
		tools.add(this.createNewMeasurementTool());
		tools.add(this.createNewMeasurementTool());
		return tools;
	}

	/**
	 * Creates a new set of {@code 2} {@linkplain MeasurementResultAnalyser
	 * MeasurementResultAnalysers}.
	 *
	 * @return A new set with new instances of {@link MeasurementResultAnalyser}.
	 */
	public Set<MeasurementResultAnalyser> createNewMeasurementResultAnalysersSet() {
		final Set<MeasurementResultAnalyser> tools = new HashSet<>();
		tools.add(this.createNewMeasurementResultAnalyser());
		tools.add(this.createNewMeasurementResultAnalyser());
		return tools;
	}

	/**
	 * Creates a new set of {@code 2} {@linkplain ProposedExpressionAnalyser
	 * ProposedExpressionAnalysers}.
	 *
	 * @return A new set with new instances of {@link ProposedExpressionAnalyser}.
	 */
	public Set<ProposedExpressionAnalyser> createNewProposedExpressionAnalyserSet() {
		final Set<ProposedExpressionAnalyser> tools = new HashSet<>();
		tools.add(this.createNewProposedExpressionAnalyser());
		tools.add(this.createNewProposedExpressionAnalyser());
		return tools;
	}

	/**
	 * Creates a new set of {@code 2} {@linkplain MeasurementTool MeasurementTools}.
	 *
	 * @return A new set with new instances of {@link MeasurementTool}.
	 */
	public Set<MeasurementTool> createNewMeasurementToolSet() {
		final Set<MeasurementTool> tools = new HashSet<MeasurementTool>();
		tools.add(this.createNewMeasurementTool());
		tools.add(this.createNewMeasurementTool());
		return tools;
	}

	/**
	 * Creates a new {@link MeasurementTool}.
	 *
	 * @return A new instance of a {@link MeasurementTool}
	 */
	public MeasurementTool createNewMeasurementTool() {
		return measurements -> {
			return null;
		};
	}

	/**
	 * Creates a new {@link MeasurementResultAnalyser}.
	 *
	 * @return A new instance of a {@link MeasurementResultAnalyser}
	 */
	public MeasurementResultAnalyser createNewMeasurementResultAnalyser() {
		return new MeasurementResultAnalyser() {

			@Override
			public void contribute(final MeasurementResultAnalyserBlackboardView blackboard) {
			}

			@Override
			public boolean canContribute(final ReadOnlyMeasurementResultAnalyserBlackboardView blackboard) {
				return false;
			}
		};
	}

	/**
	 * Creates a new {@link ProposedExpressionAnalyser}.
	 *
	 * @return A new instance of a {@link ProposedExpressionAnalyser}
	 */
	public ProposedExpressionAnalyser createNewProposedExpressionAnalyser() {
		return new ProposedExpressionAnalyser() {

			@Override
			public void contribute(final ProposedExpressionAnalyserBlackboardView blackboard) {
			}

			@Override
			public boolean canContribute(final ReadOnlyProposedExpressionAnalyserBlackboardView blackboard) {
				return false;
			}
		};
	}
}
