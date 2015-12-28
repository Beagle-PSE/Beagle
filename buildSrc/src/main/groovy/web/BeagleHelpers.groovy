public class BeagleHelpers {
	private BeagleWebContext beagleContext;
	
	public BeagleHelpers(BeagleWebContext beagleContext) {
		this.beagleContext = beagleContext
	}
		
	public String setTitle(String title) {
		beagleContext.title = title
		return ''
	}
	
	public String fileIconClass(String formatName) {
		switch(formatName) {
			case 'pdf':
				return 'file-pdf-o'
			default:
				return 'file-o'
		}
	}
	
	public String concat(String part1, String part2) {
		return part1 + part2
	}
	
	public String publishUrl() {
		final String repoSlug = System.getenv('TRAVIS_REPO_SLUG')
		if (repoSlug == null) {
			return ''
		}
		final String user, repo
		(user, repo) = repoSlug.tokenize('/')
		final String branch = System.getenv('TRAVIS_BRANCH')
		final String branchFolder = branch == 'master' ? '' : "/branches/$branch"
		return "https://${user}.github.io/${repo}${branchFolder}"
	}
}
