package de.uka.ipd.sdq.beagle.core.facade;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

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
	 * The projects we already evaluated. Used to not extract classpaths of the same
	 * project multiple times (the same projects may be referenced multiple times by
	 * different referenced projects).
	 */
	private final Set<IJavaProject> extractedProjects = new HashSet<>();

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
		return StringUtils.join(this.getClasspathEntriesFor(this.project), File.pathSeparator);
	}

	/**
	 * Extracts the classpath entries of the given {@code project}.
	 *
	 * @param projectToBeExtracted The project to extract the classpath from.
	 * @return All classpath entries found.
	 */
	private Set<String> getClasspathEntriesFor(final IJavaProject projectToBeExtracted) {
		final IClasspathEntry[] classpathEntries;
		this.extractedProjects.add(projectToBeExtracted);

		try {
			classpathEntries = projectToBeExtracted.getResolvedClasspath(true);
		} catch (final JavaModelException retrievalError) {
			final FailureReport<Set<String>> failure = new FailureReport<Set<String>>()
				.message("Could not get the classpath of the project %s", projectToBeExtracted.getProject().getName())
				.cause(retrievalError)
				.recoverable()
				.retryWith(() -> this.getClasspathEntriesFor(projectToBeExtracted));
			return FAILURE_HANDLER.handle(failure);
		}

		final IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
		// collect the entries in a set to remove duplicates that might occur when
		// resolving multiple projects.
		final Set<String> entries = new HashSet<>();

		for (final IClasspathEntry classpathEntry : classpathEntries) {
			final IPath entryPath = classpathEntry.getPath();

			switch (classpathEntry.getEntryKind()) {

				case IClasspathEntry.CPE_PROJECT:
					final IJavaProject referencedProject = JavaCore.create(workspace.getProject(entryPath.toString()));
					if (!this.extractedProjects.contains(referencedProject)) {
						entries.addAll(this.getClasspathEntriesFor(referencedProject));
					}
					break;

				default:
					// See if the root workspace can resolve the path, if yes, resolve to
					// file-system-absolute path.
					final IResource pathResource = workspace.findMember(entryPath);
					if (pathResource != null) {
						entries.add(pathResource.getRawLocation().toOSString());
					} else {
						entries.add(entryPath.toOSString());
					}
			}
		}
		return entries;
	}
}
