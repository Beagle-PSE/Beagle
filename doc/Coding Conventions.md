# Coding Conventions

## Beagle Project
 * The Beagle project has the gradle project structure.
 * `src\test\Code Templates.xml`, `src\test\Formatter.xml`, `src\test\Clean Up.xml` must be used.
 * Checkstyle must be used with the settings in `src\test\Checkstyle.xml`.
 * Every public class, interface, enum, method and attribute needs JavaDoc.
 * Every method must be checked by at least one JUnit Test.
 * We program defensive, this means:
 > “defensive programming” describes a way programming giving users of an API detailed error messages when they use an API wrong, for instance (but not limited to) by checking input arguments. This might lead to less bugs.
For example, a method may return useable, but false values. Defensive programming would check the input arguments and throw an exception and prevent the programmer from continuing with false values.

 * Defensive programming is verfied with JUnit test cases.
 * Getters exposing internal collections should return copy-on-write versions to prevent modification of internals and providing easy use of the returned collection. This behaviour needs to be documented.

## Prototypes
 * Prototypes will be in in our `master` branch, but as projects in the `prototypes` folder.
 * The projects must have the Gradle structure, not the eclipse structure.
 * Prototypes must use Checkstyle, and have the correct Clean Up and Formatter and Templates.
 * Prototypes don't need Unit Tests
 * Prototypes don't need to be documented as well as the normal source code.