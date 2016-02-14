package de.uka.ipd.sdq.beagle.core;

/**
 * Interface for configurations capable of executing the measured software.
 *
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
public interface LaunchConfiguration {

	/**
	 * Executes the launch of the measured software. Resets all settings made through the
	 * setter methods.
	 */
	void execute();

	/**
	 * Adds the given {@code classPathEntry} to the start of the Java classpath used when
	 * launching the software. This setting persists only until the next call to
	 * {@link #execute()}.
	 *
	 * @param classPathEntry A classpath entry to prepend the launched software’s Java
	 *            classpath with.
	 * @return {@code this}.
	 */
	LaunchConfiguration prependClasspath(String classPathEntry);

	/**
	 * Adds the given {@code argument} to the JVM arguments used when launching the
	 * software. This setting persists only until the next call to {@link #execute()}.
	 *
	 * @param argument An argument to pass to the launched program’s JVM.
	 * @return {@code this}.
	 */
	LaunchConfiguration appendJvmArgument(String argument);
}
