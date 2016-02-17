package de.uka.ipd.sdq.beagle.core.facade;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import java.io.File;

/**
 * Extracts the class path of an Eclipse Java project.
 *
 * @author Joshua Gleitze
 */
public class JdtProjectClasspathExtractor {

	/**
	 * Handler of failures.
	 */
	private static final FailureHandler FAILURE_HANDLER =
		FailureHandler.getHandler("Eclipse Java Project Classpath Extractor");

	/**
	 * The project to extract from.
	 */
	private final IJavaProject project;

	/**
	 * Creates the extractor for the given {@code project}.
	 *
	 * @param project The project to extract the class path from.
	 */
	public JdtProjectClasspathExtractor(final IJavaProject project) {
		this.project = project;
	}

	/**
	 * Builds a class path string separated by {@link File#pathSeparator} out of the
	 * project’s class path.
	 *
	 * @return the built classpath.
	 */
	public String getClasspath() {
		final StringBuilder pathBuilder = new StringBuilder();
		boolean first = false;
		final IClasspathEntry[] classpathEntries;

		try {
			classpathEntries = this.project.getResolvedClasspath(true);
		} catch (final JavaModelException retrievalError) {
			final FailureReport<String> failure =
				new FailureReport<String>().cause(retrievalError).retryWith(this::getClasspath);
			return FAILURE_HANDLER.handle(failure);
		}

		final IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
		for (final IClasspathEntry classpathEntry : classpathEntries) {
			if (first) {
				first = false;
			} else {
				pathBuilder.append(File.pathSeparator);
			}
			final IPath entryPath = classpathEntry.getPath();

			// See if the root workspace can resolve the path, if yes, resolve to
			// file-system-absolute path.
			final IResource pathResource = workspace.findMember(entryPath);
			if (pathResource != null) {
				pathBuilder.append(pathResource.getRawLocation().toOSString());
			} else {
				pathBuilder.append(entryPath.toOSString());
			}

		}
		return pathBuilder.toString();
	}
}
