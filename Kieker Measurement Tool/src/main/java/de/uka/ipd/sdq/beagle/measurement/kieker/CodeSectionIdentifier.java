package de.uka.ipd.sdq.beagle.measurement.kieker;

import de.uka.ipd.sdq.beagle.core.CodeSection;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * Resolves code sections to unique IDs and back. Equal code sections are assigned to the
 * same identifier, different code sections are assigned to different identifiers.
 * Identifiers obtained through {@link #getIdOf(CodeSection)} are only valid for the same
 * Identifier instance.
 *
 * <p>This class is thread safe.
 *
 * @author Joshua Gleitze
 */
public class CodeSectionIdentifier {

	/**
	 * Counter of used identifiers.
	 */
	private int idCounter;

	/**
	 * Bijection between code sections and ids.
	 */
	private final BidiMap<CodeSection, Integer> idCodeSectionBijection = new DualHashBidiMap<>();

	/**
	 * Gets the identifier that repersents the provided {@code codeSection}.
	 *
	 * @param codeSection The code section to get the id of.
	 * @return The identifer that’s unique for {@code codeSection}.
	 */
	public synchronized int getIdOf(final CodeSection codeSection) {
		return this.idCodeSectionBijection.computeIfAbsent(codeSection, (section) -> this.idCounter++);
	}

	/**
	 * Gets the code section that is identified by the provided {@code identifier}.
	 *
	 * @param identifier An identifier obtained through this instance.
	 * @return The code section that was assigned to {@code identifier} by this instance.
	 *         {@code null} if there is no such section.
	 */
	public synchronized CodeSection getSectionFor(final int identifier) {
		return this.idCodeSectionBijection.getKey(identifier);
	}
}
