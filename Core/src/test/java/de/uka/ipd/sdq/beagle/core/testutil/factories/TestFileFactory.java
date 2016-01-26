package de.uka.ipd.sdq.beagle.core.testutil.factories;

import de.uka.ipd.sdq.beagle.core.CodeSection;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides methods to get one, or more, Files which can be used to create e.g. a
 * {@link CodeSection}.
 *
 * @author Annika Berger
 */
public class TestFileFactory {

	/**
	 * Gives the first File in {@link #getAll()}.
	 *
	 * @return a file.
	 */
	public File getOne() {
		return this.getAll()[0];
	}

	/**
	 * Builds and returns an array of newly initialised files.
	 *
	 * @return all files as array
	 */
	public File[] getAll() {
		final File testFile;
		try {
			testFile = new File(
				CodeSectionFactory.class.getResource("/de/uka/ipd/sdq/beagle/core/TestFile.java").toURI().getPath());
		} catch (final URISyntaxException uriSyntaxException) {
			throw new RuntimeException("Cannot find file called 'TestFile.java'");
		}
		final File pingPongClass;
		try {
			pingPongClass = new File(
				CodeSectionFactory.class.getResource("/de/uka/ipd/sdq/beagle/core/PingPong.java").toURI().getPath());
		} catch (final URISyntaxException uriSyntaxException) {
			throw new RuntimeException("Cannot find file called 'PingPong.java'");
		}
		final File[] files = {
			testFile, pingPongClass
		};
		return files;
	}

	/**
	 * Builds and returns a set of newly initialised files.
	 *
	 * @return all files as set
	 */
	public Set<File> getAllAsSet() {
		return new HashSet<>(Arrays.asList(this.getAll()));
	}

}
