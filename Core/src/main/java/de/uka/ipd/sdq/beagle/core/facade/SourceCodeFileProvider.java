package de.uka.ipd.sdq.beagle.core.facade;

import java.io.File;

/**
 * Provides the source code files that shall be analysed by Beagle.
 *
 * @author Joshua Gleitze
 */
public interface SourceCodeFileProvider {

	/**
	 * Queries the source code file containing the source code of the type denoted by
	 * {@code fullyQualifiedJavaPath}.
	 *
	 * @param fullyQualifiedJavaPath The fully qualified path of a Java type.
	 * @return The source code file declaring the type denoted by
	 *         {@code fullyQualifiedJavaPath}. {@code null} if no file defining the type
	 *         can be found.
	 */
	File getSourceFile(String fullyQualifiedJavaPath);
}
