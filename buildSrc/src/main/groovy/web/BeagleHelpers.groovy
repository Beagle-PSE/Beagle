/**
 * Defines all helpers available to web handlebars templates.
 *
 * @author Joshua Gleitze
 */
public class BeagleHelpers {
	private BeagleWebContext beagleContext;
	
	/**
	 * Creates an instance of helpers that can be passed to handlebars.java. Helpers may affect the 
	 * passed {@link beagleContext}, so a new instance should be used for every new BeagleWebContext.
	 *
	 * @param beagleContext	The context the helpers operate on.
	 */
	public BeagleHelpers(BeagleWebContext beagleContext) {
		this.beagleContext = beagleContext
	}
	
	/**
	 * Sets the momentary page’s title. The title may later be used in a template (e.g. an HTML frame template)
	 *
	 * @param title	The page’s title.
	 * @return	An empty string.
	 */	
	public String setTitle(final String title) {
		beagleContext.title = title
		return ''
	}
	
	/**
	 * Returns a font-awesome class name representing a file of the given format.
	 *
	 * @param formatName	The name of the file’s format.
	 * @return	A font-awesome class name of an icon representing {@code formatName}
	 */
	public String fileIconClass(final String formatName) {
		switch(formatName) {
			case 'pdf':
				return 'file-pdf-o'
			default:
				return 'file-o'
		}
	}
	
	/**
	 * Concatenates two strings.
	 *
	 * @param part1	A string.
	 * @param part2	Another string.
	 * @return	The concatenation of part1 and part2.
	 */
	public String concat(final String part1, final String part2) {
		return part1 + part2
	}
	
	/**
	 * Determines where this web presence will be published to from environment variables 
	 * available on Travis CI.
	 *
	 * @return	The URL where this web presence will be published or an empty string if
	 *			Travis CI ENVs are not available.
	 */
	public String publishUrl() {
		final String repoSlug = System.getenv('TRAVIS_REPO_SLUG')
		final String branch = System.getenv('TRAVIS_BRANCH')
		if (repoSlug == null || branch == null) {
			return ''
		}
		final String user, repo
		(user, repo) = repoSlug.tokenize('/')
		final String branchFolder = branch == 'master' ? '' : "/branches/$branch"
		return "https://${user}.github.io/${repo}${branchFolder}"
	}
}
