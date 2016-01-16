# Beagle’s web presence
Source files of Beagle’s web presence.

## Building
```
gradle web
```
will build the web presence into `build/web`. Note that the task copies in artefacts built previously but does not build these artefacts on its own. Run `gradle build web` to generate the web presence and generate all artefacts to be published beforehand.

## File Overview

### `src/web/handlebars`
Contains all template files. Templates are written using the [Handlebars Syntax](http://handlebarsjs.com/). Supported file types are `.html` (HTML 5), `.md` (Markdown) and `.htmd` (Markdown that will be rendered into HTML). Note that the Markdown syntax for `.htmd` files is not completely the same as GitHub Flavoured Markdown. The files are rendered using [pegdown](https://github.com/sirthias/pegdown), it may be configured as needed in [RenderMarkdownFilter.groovy](../../buildSrc/src/main/groovy/web/RenderMarkdownFilter.groovy).

##### Template Data
All data available to templates is defined in [BeagleWebContext.groovy](../../buildSrc/src/main/groovy/web/BeagleWebContext.groovy).

##### Handlebars Helpers
All helpers available to templates are defined in [BeagleHelpers.groovy](../../buildSrc/src/main/groovy/web/BeagleHelpers.groovy).

#### `src/web/handlebars/pages`
Contains the pages that will be published on Beagle’s web presence. The directory layout represents the resulting layout.

#### `src/web/handlebars/partials`
Contains the handlebars partials available to templates. All partials must have the `.part` extension, regardless of the language they are written in. 

### `src/web/css`
Css files for the web presence. Only plain CSS is supported. Usage of CSS 3 is encouraged. If used features are essential to the layout, they should have a support in Germany that’s >= 90%, as indicated by [caniuse.com](http://caniuse.com/).