package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.ExternalCallParameter;
import de.uka.ipd.sdq.beagle.core.ResourceDemandType;
import de.uka.ipd.sdq.beagle.core.ResourceDemandingInternalAction;
import de.uka.ipd.sdq.beagle.core.SeffBranch;
import de.uka.ipd.sdq.beagle.core.SeffLoop;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * *****STUB! IMPLEMENTATION DEPENDS ON SOLUTION FOR SOURCECODEDECORATOR (SOMOX)!*****
 * 
 * <p>As Beagle is going to make usage of the SourceCoreDecorator from Somox, this
 * class will replace the {@link PcmNameParser}. In contrast to the older class
 * this one does not depend on the EntityName of SEFF-Elements, but rather on
 * an adapter or interface to Somox' SourceCodeDecorator.
 * 
 * @author Ansgar Spiegler
 */
public class CodeSectionMapper {


	/**
	 * Integer with value 0.
	 */
	private final int zero = 0;
	
	/**
	 * This constructor will need an association to Somox' sourceCodeDecotor as soon
	 * as a working solution exists.
	 */
	public CodeSectionMapper() {
		
	}
	
	/**
	 * This method searches for a given ID, where its representative SEFF is situated in source code
	 * storing this information in a {@link CodeSection}.
	 *
	 * @param identity The ID of an AbstractAction
	 * @return CodeSection with all information extracted by SourceCodeDecorator
	 * @throws IllegalArgumentException Thrown when an ID is not found or does not represent a MeasurableSeff
	 */
	private CodeSection getCodeSecotionForId(final String identity) throws IllegalArgumentException {
		final int pseudoSection1 = 1;
		final int pseudoSection2 = 2;
		final File pseudoFile = new File("src/main/resources/pseudoFile.md");
		return new CodeSection(pseudoFile, pseudoSection1, pseudoFile, pseudoSection2);
			
	}
	
	/**
	 * Creates an {@link ExternalCallParameter} for a given ID. Assert that identity fits to SeffType.
	 *
	 * @param identity The identity of the ExternalCallParameter
	 * @return ExternalCallParameter element
	 */
	public ExternalCallParameter getExternalCallParameterForId(final String identity) {
		final CodeSection codeSection = this.getCodeSecotionForId(identity);
		return new ExternalCallParameter(codeSection, this.zero);
	}
	
	/**
	 * Creates a {@link SeffBranch} for a given ID. Assert that identity fits to SeffType.
	 *
	 * @param identity The identity of the SeffBranch
	 * @return SeffBranch element
	 */
	public SeffBranch getSeffBranchForId(final String identity) {
		final Set<CodeSection> sectionSet = new HashSet<CodeSection>();
		sectionSet.add(this.getCodeSecotionForId(identity));
		sectionSet.add(null);
		return new SeffBranch(sectionSet);
	}
	
	/**
	 * Creates a {@link SeffLoop} for a given ID. Assert that identity fits to SeffType.
	 *
	 * @param identity The identity of the SeffLoop
	 * @return SeffLoop element
	 */
	public SeffLoop getSeffLoopForId(final String identity) {
		final CodeSection codeSection = this.getCodeSecotionForId(identity);
		return new SeffLoop(codeSection);
	}
	
	/**
	 * Creates a {@link ResourceDemandingInternalAction} for a given ID. Assert that identity fits to SeffType.
	 * MISSING! Check if InternalAction contains a ResourceDemand !AND! Find out what kind of resourceDemand !MISSING!
	 * This will for now return a {@link ResourceDemandingInternalAction} with CPU type.
	 *
	 * @param identity The identity of the RDIA
	 * @return ResourceDemandingInternalAction element
	 */
	public ResourceDemandingInternalAction getRDIAForId(final String identity) {
		final CodeSection codeSection = this.getCodeSecotionForId(identity);
		//final CodeSection sectionTemp = this.nameParser.parse(internalAction.getEntityName());
		return new ResourceDemandingInternalAction(ResourceDemandType.RESOURCE_TYPE_CPU, codeSection);

	}
}
