# Coding Conventions

## Beagle Project
 * The Beagle project has the gradle project structure.
 * `src\test\Code Templates.xml`, `src\test\Formatter.xml`, `src\test\Clean Up.xml` must be used.
 * Checkstyle must be used with the settings in `src\test\Checkstyle.xml`.
 * Every public class, interface, enum, methode and attribute needs JavaDoc.
 * Every methode must be checked by at least one JUnit Test.
 * We program defensive, this means:
 > “defensive programming” describes a way programming giving users of an API detailed error messages when they use an API wrong, for instance (but not limited to) by checking input arguments. This might lead to less bugs.
non-defensive programming puts the method’s contract above anything. If the programmer breaks the contract by providing bad input, it’s “his fault” and the API has undefined behaviour.
For example, a method may return useable, but false values. Defensive programming would check the input arguments and throw an exception and prevent the programmer from continuing with false values.

 * The defensive programming is verfied with JUnit test cases.

## Prototypes
 * Prototypes will be in in our `master` branch, but as projects in the `prototypes` folder.
 * The projects must have the Gradle structure, not the eclipse structure.
 * Prototypes must use Checkstyle, and have the correct Clean Up and Formatter and Templates.
 * Prototypes don't need Unit Tests
 * Prototypes don't need to be documentated as good as the normal source code.