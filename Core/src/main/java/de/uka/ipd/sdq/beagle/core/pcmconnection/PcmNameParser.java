package de.uka.ipd.sdq.beagle.core.pcmconnection;

import de.uka.ipd.sdq.beagle.core.CodeSection;
import de.uka.ipd.sdq.beagle.core.facade.SourceCodeFileProvider;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * ****************************EXTINCT****************************************************
 * ******** This parser is a draft that can only provide proper functionality if SoMoX
 * does not change its naming convention for EntityNames. It offers one method
 * {@link PcmNameParser#parse} to read out the {@link CodeSection CodeSections} in a given
 * String. Attention! This Parser can not read out as much as desired for the
 * {@link CodeSection}. For more information, read through the java-doc of the method
 * {@link parse}.
 * ***************************************************************************************
 * ******** No more usage of this class is made so far.
 *
 * @author Ansgar Spiegler
 * @author Roman Langrehr
 *
 */
public class PcmNameParser {

	/**
	 * "@position: " annotation in the entity name.
	 */
	private static final String AT_POSITION_COLON_SPACE = "@position: ";

	/**
	 * "DOTjava" annotation in the entity name.
	 */
	private static final String DOT_JAVA = ".java";

	/**
	 * " from " annotation in the entity name.
	 */
	private static final String SPACE_FROM_SPACE = " from ";

	/**
	 * " to " annotation in the entity name.
	 */
	private static final String SPACE_TO_SPACE = " to ";

	/**
	 * " at " annotation in the entity name.
	 */
	private static final String SPACE_AT_SPACE = " at ";

	/**
	 * The {@link CodeSection} that should be created.
	 */
	private CodeSection codeSection;

	/**
	 * The position of " from " in the given String.
	 */
	private int fromOffset;

	/**
	 * The position of " to " in the given String.
	 */
	private int toOffset;

	/**
	 * The position of " at " in the given String.
	 */
	private int atOffset;

	/**
	 * The file that is created by the relative path between "@position: " and ".java".
	 */
	private File file;

	/**
	 * The from-Position marking the sourceCode.
	 */
	private int fromPos;

	/**
	 * The to-Position marking the sourceCode.
	 */
	private int toPos;

	/**
	 * The at-Position marking the sourceCode.
	 */
	private int atPos;

	/**
	 * The {@link SourceCodeFileProvider} for the project under analysis.
	 */
	private SourceCodeFileProvider sourceCodeFileProvider;

	/**
	 * Creates a new name parser for a specific project to analyse.
	 *
	 * @param sourceCodeFileProvider The {@link SourceCodeFileProvider} for the project
	 *            under analysis.
	 */
	public PcmNameParser(final SourceCodeFileProvider sourceCodeFileProvider) {
		this.sourceCodeFileProvider = sourceCodeFileProvider;
	}

	/**
	 * Parsing a SoMoX-created String to get the annotated CodeSection.
	 *
	 * @param entityName SoMoX generated entityName. If not, this leads to undefined
	 *            behaviour.
	 * @return May return {@code null}, if the String contains no file-path
	 *         {@link CodeSection}. Initialized with (file, firstOffset, file,
	 *         secondOffset) OR (file, firstOffset, file, firstOffset) if there is no
	 *         second Offset available. Anyway, the first "file" and the second "file" are
	 *         the same!
	 * @throws FileNotFoundException When the file was not found at the given relative
	 *             path.
	 */
	public CodeSection parse(final String entityName) throws FileNotFoundException {

		this.reset();
		this.loadFile(entityName);
		if (this.file == null) {
			return null;
		}

		this.fromPos = entityName.indexOf(PcmNameParser.SPACE_FROM_SPACE);
		this.toPos = entityName.indexOf(PcmNameParser.SPACE_TO_SPACE);
		if (this.fromPos != -1 && this.toPos != -1) {

			this.fromOffset = Integer
				.parseInt(entityName.substring(this.fromPos + PcmNameParser.SPACE_FROM_SPACE.length(), this.toPos));
			this.toOffset = Integer.parseInt(
				entityName.substring(this.toPos + PcmNameParser.SPACE_TO_SPACE.length(), entityName.length()));

			this.codeSection = new CodeSection(this.file, this.fromOffset, this.file, this.toOffset);

		} else {
			this.atPos = entityName.indexOf(PcmNameParser.SPACE_AT_SPACE);
			if (this.atPos != -1) {
				this.atOffset = Integer.parseInt(
					entityName.substring(this.atPos + PcmNameParser.SPACE_AT_SPACE.length(), entityName.length()));

				this.codeSection = new CodeSection(this.file, this.atOffset, this.file, this.atOffset);
			}
		}

		return this.codeSection;

	}

	/**
	 * Tries to load the file, looking at the given substring between "@position: " and
	 * ".java". May set {@link file} to null if no file annotation was found. Otherwise
	 * {@line file} is set to the .java file at the given position.
	 *
	 * @param entityName The full entityName.
	 *
	 * @throws FileNotFoundException Tries to load a file at the given path. If no valid
	 *             file can be loaded at a given position, this exception is thrown.
	 */
	private void loadFile(final String entityName) throws FileNotFoundException {
		final String qualifiedName;
		final int position = entityName.indexOf(PcmNameParser.AT_POSITION_COLON_SPACE);
		final int blockPos = entityName.indexOf(PcmNameParser.DOT_JAVA);

		if (position != -1 && blockPos != -1) {
			qualifiedName = entityName.substring(position + PcmNameParser.AT_POSITION_COLON_SPACE.length(), blockPos);
			this.file = this.sourceCodeFileProvider.getSourceFile(qualifiedName);

			if (this.file == null) {
				throw new FileNotFoundException("No source file for class " + qualifiedName + " was found.");

			}

		} else {
			this.file = null;
		}
	}

	/**
	 * Reset/initialising step of all storing variables.
	 *
	 */
	private void reset() {
		this.codeSection = null;

		this.fromOffset = -1;
		this.toOffset = -1;
		this.atOffset = -1;
		this.file = null;

		this.fromPos = -1;
		this.toPos = -1;
		this.atPos = -1;
	}

}
