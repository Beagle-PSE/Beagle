package de.uka.ipd.sdq.beagle.core.facade;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import java.io.File;

// COVERAGE:OFF
/**
 * Provides source files from source folders of Eclipse Java Projects.
 * 
 * @author Joshua Gleitze
 */
public class JdtProjectSourceCodeFileProvider implements SourceCodeFileProvider {

	/**
	 * The java project to get the source files from.
	 */
	private final IJavaProject jdtProject;

	/**
	 * Creates a provider returning source files from the provided {@code project}’s
	 * source folders.
	 *
	 * @param project A Java project to return the source files from.
	 */
	public JdtProjectSourceCodeFileProvider(final IJavaProject project) {
		this.jdtProject = project;
	}

	@Override
	public File getSourceFile(final String fullyQualifiedJavaPath) {
		try {
			final IType type = this.jdtProject.findType(fullyQualifiedJavaPath);
			if (type != null) {
				return type.getCompilationUnit().getPath().toFile();
			}
		} catch (final JavaModelException didNotWork) {
			return null;
		}
		return null;
	}

}
