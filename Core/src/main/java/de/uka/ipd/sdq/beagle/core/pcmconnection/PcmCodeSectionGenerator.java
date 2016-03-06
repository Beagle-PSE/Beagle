package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.PcmSourceStatementLink;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.PcmSourceStatementLinkRepository;
import de.uka.ipd.sdq.beagle.core.pcmsourcestatementlink.SourceCodePosition;

import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manages the creation of the {@link CodeSection} for a given ID using the
 * {@link PcmSourceStatementLinkRepository}.
 * 
 * @author Ansgar Spiegler
 */
public class PcmCodeSectionGenerator {

	/**
	 * This map simplifies and speed-ups the access to IDs.
	 */
	private Map<String, PcmSourceStatementLink> mapIdToStatementLink;

	/**
	 * The {@link PcmSourceStatementLinkRepository} the searching of IDs is based on.
	 */
	private PcmSourceStatementLinkRepository sourceStateLinkRepository;

	/**
	 * The {@link SourceCodeFileProvider} for the project under analysis.
	 */
	private SourceCodeFileProvider sourceCodeFileProvider;

	/**
	 * Adapting the given SourceStateLinkRepository to the searching of IDs.
	 *
	 * @param sourceStateLinkRepository The {@link PcmSourceStatementLinkRepository}
	 *            containing the linking of Ids to SourceCodeFiles
	 * @param sourceCodeFileProvider The {@link SourceCodeFileProvider} for the project
	 *            under analysis.
	 */
	public PcmCodeSectionGenerator(final PcmSourceStatementLinkRepository sourceStateLinkRepository,
		final SourceCodeFileProvider sourceCodeFileProvider) {
		Validate.notNull(sourceStateLinkRepository);
		this.sourceStateLinkRepository = sourceStateLinkRepository;
		this.initialiseMap();
		this.sourceCodeFileProvider = sourceCodeFileProvider;
	}

	/**
	 * Creating a private map to simplify access to the IDs.
	 *
	 */
	private void initialiseMap() {
		this.mapIdToStatementLink = new HashMap<String, PcmSourceStatementLink>();
		for (PcmSourceStatementLink statementLink : this.sourceStateLinkRepository.getLinks()) {
			final String statementLinkId = statementLink.getPcmElementId();
			Validate.notNull(statementLinkId);

			if (this.mapIdToStatementLink.containsKey(statementLinkId)
				&& !this.mapIdToStatementLink.get(statementLinkId).equals(statementLink)) {
				throw new IllegalArgumentException(
					"The given SourceStateLinkRepository contains more than one statementLink for different IDs!");
			}
			this.mapIdToStatementLink.put(statementLink.getPcmElementId(), statementLink);
		}
	}

	/**
	 * Creates a {@link CodeSection} for a given identifier.
	 *
	 * @param identifier the identifier that should represent a
	 *            BranchBehaviour_BranchTransition, a BodyBehaviour_Loop,
	 *            ExternalCallAction or InternalAction
	 * @return A {@link CodeSection} based on the given
	 *         {@link PcmSourceStatementLinkRepository}
	 * @throws FileNotFoundException if File from the given Mapping was not found
	 */
	public CodeSection getCodeSectionForID(final String identifier) throws FileNotFoundException {

		Validate.notNull(identifier);
		if (!this.mapIdToStatementLink.containsKey(identifier)) {
			throw new IllegalArgumentException(
				"The given identifier " + identifier + " was not found in the given SourceStateLinkRepository");
		}
		final PcmSourceStatementLink statementLink = this.mapIdToStatementLink.get(identifier);

		final SourceCodePosition scpFirst = statementLink.getFirstStatement();
		final SourceCodePosition scpLast = statementLink.getLastStatement();

		final File startFile = this.sourceCodeFileProvider.getSourceFile(scpFirst.getSourceCodeFile());
		final File endFile = this.sourceCodeFileProvider.getSourceFile(scpLast.getSourceCodeFile());

		if (startFile == null) {
			throw new FileNotFoundException("No source file for class " + scpFirst.getSourceCodeFile() + " was found.");
		}
		if (endFile == null) {
			throw new FileNotFoundException("No source file for class " + scpLast.getSourceCodeFile() + " was found.");
		}

		return new CodeSection(startFile, scpFirst.getCharacterIndex(), endFile, scpLast.getCharacterIndex());

	}

}
