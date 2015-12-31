# Prototypes
Projects that implement a certain functionality. Instead of trying to fit into the rest of the code or covering every corner case, prototypes serve as a proof of concept, often showing how to use a certain technique.

## Creating a prototype
A prototype is simply a project on its own, which lives in a subfolder of this folder. It should have its own Eclipse project and stick to the [default layout structure](https://docs.gradle.org/current/userguide/java_plugin.html#N14F8E). Any functionality that is required by a prototype but already realised in the production code should be copied from there.

## Building a prototype
Prototypes will automatically be built with the main project. They inherit a default configuration from it. However, a prototype can be configured like any gradle project. Inherited tasks can be overridden or be disabled if they do not make sense in the prototype’s context. If more complex build logic is required, a `buildSrc` subproject should be added, like its done for the main project.