package de.uka.ipd.sdq.beagle.measurement.kieker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Reads in the Eclipse AST of a source code file and can write it back to another file.
 *
 * @author Joshua Gleitze
 */
class EclipseAstBridge {

	/**
	 * The ast parser responsible to parse input files.
	 */
	private final ASTParser astParser = ASTParser.newParser(AST.JLS8);

	/**
	 * The source code file the edited AST came from.
	 */
	private final File sourceCodeFile;

	/**
	 * The content of {@link #sourceCodeFile} after {@link #getAst()} was called.
	 */
	private String sourceCode;

	/**
	 * The AST of {@link #sourceCodeFile} after {@link #getAst()} was called.
	 */
	private CompilationUnit compilationUnit;

	/**
	 * The charset to use.
	 */
	private final Charset charset;

	/**
	 * Creates a bridge to the AST of {@code sourceCodeFile}.
	 *
	 * @param sourceCodeFile A Java source code file to read the AST of. Must not be
	 *            {@code null}.
	 * @param charset The encoding to use when reading and writing the source code files.
	 *            Must not be {@code null}.
	 */
	EclipseAstBridge(final File sourceCodeFile, final Charset charset) {
		Validate.notNull(sourceCodeFile);
		Validate.notNull(charset);
		this.sourceCodeFile = sourceCodeFile;
		this.charset = charset;
	}

	/**
	 * Reads in the source code file this bridge was created for.
	 *
	 * @return The abstract syntax tree of the source code in the file this bridge was
	 *         created for. It is ready for modifications, which might be written back
	 *         through {@link #writeToFile(File)}.
	 * @throws IOException If reading the file this bridge was created for fails.
	 */
	CompilationUnit getAst() throws IOException {
		this.sourceCode = FileUtils.readFileToString(this.sourceCodeFile, this.charset);
		this.astParser.setSource(this.sourceCode.toCharArray());
		// createAST will always "at least" return a CompilationUnit
		this.compilationUnit = (CompilationUnit) this.astParser.createAST(null);
		this.compilationUnit.recordModifications();
		return this.compilationUnit;
	}

	/**
	 * Queries the fully qualified name of type defined by the source code file this
	 * bridge was created for.
	 *
	 * @return The fully qualified name of the type defined by the source code file this
	 *         bridge was created for. {@code null} if there is nothing in the file this
	 *         desription applies to.
	 */
	String getFullyQualifiedName() {
		Validate.validState(this.compilationUnit != null, "getFullyQualifiedName may only be called after getAst was!");

		// Compilation Units that don’t have exactly one type declaration at the root
		// level are not supported. The author knows of no compilable example of such a
		// compilation unit.
		final TypeDeclaration rootType = (TypeDeclaration) this.compilationUnit.types().get(0);
		if (rootType == null) {
			return null;
		}

		final PackageDeclaration cuPackage = this.compilationUnit.getPackage();
		final String packageName;
		if (cuPackage == null) {
			packageName = "";
		} else {
			packageName = cuPackage.getName() + ".";
		}

		return packageName + rootType.getName();
	}

	/**
	 * Writes the AST that was read in through {@link #getAst()} back to a file, including
	 * all modifications.
	 *
	 * @param targetFile The file to write the modified AST to.
	 * @throws IOException If an input/output error occurs while writing to
	 *             {@code targetFile}.
	 */
	void writeToFile(final File targetFile) throws IOException {
		Validate.validState(this.compilationUnit != null, "writeToFile may only be called after getAst was!");
		Validate.notNull(targetFile);

		final IDocument documentToEdit = new Document(this.sourceCode);
		final TextEdit instrumentationEdit = this.compilationUnit.rewrite(documentToEdit, null);

		try {
			instrumentationEdit.apply(documentToEdit, TextEdit.NONE);
		} catch (final MalformedTreeException | BadLocationException impossibleException) {
			// We created the document from the same source code the AST was created from.
			// It should therefore be impossible to manipulate the AST in a way that makes
			// it impossible to write the changes back.
			assert false;
		}

		FileUtils.write(targetFile, documentToEdit.get(), this.charset);
	}
}
