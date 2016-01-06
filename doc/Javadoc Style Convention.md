# Javadoc Style Convention
All parts of the Javadoc are written as sentences, meaning that they begin with a capital letter and end with a period.

## Naming Convention
* Class names are written in UpperCamelCase and have a minimum length of 3 characters.
* Method names are written in lowerCamelCase and have a minimum length of 3 characters.
* Attributes are written in lowerCamelCase and have a minimum length of 3 characters.
* Package names are written in lowercase and have a minimum length of 2 characters per part.

### Use of Acronyms
1. All acronyms used in the SRS are permitted.
2. Acronyms are treated as normal words and are spelled as specified in `Naming Convention`. They are not capitalised.


## Tags
1. `@param` and `@returns` tags have to state their value ranges if they refer to primitive data types as well as stating whether they can be `null` and the meaning of them being `null` if they refer to object data types.
2. Unnecessarily stating the package when using `{@link}` or `{@see}` tags is forbidden.
