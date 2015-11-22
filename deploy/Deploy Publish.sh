# fail the script if any command fails
set -e

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
	echo "We won't deploy because we're on a pull request."
	exit 0
fi

# If GH_TOKEN is not set, we'll exit gracefully
if [ -z ${GH_TOKEN:+1} ]; then
	echo "The GH_TOKEN ENV is not set. Thus, we won't deploy to gh-pages."
	exit 0
fi

###
# Gather all assets that will be released
###

# Build the master branch to the top most folder, but all other branches to subfolders
[ $TRAVIS_BRANCH == "master"] && pubdir="publish/" || pubdir="publish/branches/$TRAVIS_BRANCH/"
mkdir -p "$pubdir"

# Requirements specification

cp doc/Requirements\ Specification/Requirements\ Specification.pdf "$pubdir"

###

REPO="https://${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git"


cd publish

ls -l

git add .
git commit -m "Travis build of $TRAVIS_COMMIT_RANGE"

# Push from the current repo's gh-pages branch to the remote
# repo's gh-pages branch. We redirect any output to
# /dev/null to hide any sensitive credential data that might otherwise be exposed.
git push --quiet $REPO gh-pages:gh-pages > /dev/null 2>&1

cd $OLDPWD
