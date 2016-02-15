package de.uka.ipd.sdq.beagle.measurement.kieker;

import de.uka.ipd.sdq.beagle.core.failurehandling.FailureHandler;
import de.uka.ipd.sdq.beagle.core.failurehandling.FailureReport;

import org.apache.commons.lang3.Validate;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Compiles source code files using Eclipse’s batch compiler. Reports failures through the
 * {@link FailureHandler Failure API}. Unless a target directory is set through
 * {@link #intoFolder(Path)}, this compiler produces no class file output.
 *
 * @author Joshua Gleitze
 */
public class EclipseCompiler {

	/**
	 * The handler of failures.
	 */
	private static final FailureHandler FAILURE_HANDLER = FailureHandler.getHandler("Measurement Source Code Compiler");

	/**
	 * The folder containing the files to compile.
	 */
	private final String sourceFilesFolder;

	/**
	 * The classpath to use.
	 */
	private final List<String> classPath = new ArrayList<>();

	/**
	 * The folder to compile into.
	 */
	// Passing "none" as target folder to the batch compiler will prevent the generation
	// of .class files.
	private String targetFolder = "none";

	/**
	 * The charset to be used by the compiler.
	 */
	private String charset;

	/**
	 * Creates a compiler that will compile all java files in the provided
	 * {@code sourceFolder}.
	 *
	 * @param sourceFolder The folder containing the files to compile. Must not be
	 *            {@code null}.
	 */
	public EclipseCompiler(final Path sourceFolder) {
		Validate.notNull(sourceFolder);
		this.sourceFilesFolder = sourceFolder.toAbsolutePath().toString();
	}

	/**
	 * Provides a classpath segment to be used by the compiler. It will be appended to
	 * existing segments.
	 *
	 * @param compiltationClassPathSegment A classpath segment the compiler should use to
	 *            compile.
	 * @return {@code this}.
	 */
	public EclipseCompiler useClassPath(final String compiltationClassPathSegment) {
		Validate.notNull(compiltationClassPathSegment);
		this.classPath.add(compiltationClassPathSegment);
		return this;
	}

	/**
	 * Set’s the compilations output folder to the provided {@code targetFolder}. The
	 * compiler will not generate output files unless a target folder is set through this
	 * method.
	 *
	 * @param compilationTargetFolder The folder into which the compiled byte code is to
	 *            be stored.
	 * @return {@code this}.
	 */
	public EclipseCompiler intoFolder(final Path compilationTargetFolder) {
		this.targetFolder = compilationTargetFolder.toAbsolutePath().toString();
		return this;
	}

	/**
	 * Makes the compiler use {@code charset} to read the source source code. It defaults
	 * to using the compiler’s default charset if no charset was set.
	 *
	 * @param ioCharset The charset to be used by this compiler.
	 * @return {@code this}.
	 */
	public EclipseCompiler useCharset(final Charset ioCharset) {
		Validate.notNull(ioCharset);
		this.charset = ioCharset.displayName();
		return this;
	}

	/**
	 * Executes the compilation.
	 */
	public void compile() {
		final StringBuilder argumentBuilder = new StringBuilder();
		argumentBuilder.append("-d \"").append(this.targetFolder).append("\" ");
		for (final String segment : this.classPath) {
			argumentBuilder.append("-classpath \"").append(segment).append("\" ");
		}
		if (this.charset != null) {
			argumentBuilder.append("-encoding ").append(this.charset).append(" ");
		}
		argumentBuilder.append("\"").append(this.sourceFilesFolder).append("\" ");

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final boolean success = BatchCompiler.compile(argumentBuilder.toString(), new PrintWriter(outputStream),
			new PrintWriter(outputStream), null);

		if (!success) {
			final FailureReport<Void> failure = new FailureReport<Void>().message("The compilation failed")
				.details("Compilation Output: %s\n\nUsed command line options: %s\n", outputStream, argumentBuilder)
				.retryWith(this::compile);
			FAILURE_HANDLER.handle(failure);
		}
	}
}
