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

REPO="https://${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git"

cd publish

git add .
git commit -m "Travis build of $TRAVIS_COMMIT_RANGE"

# Push from the current repo's gh-pages branch to the remote
# repo's gh-pages branch. We redirect any output to
# /dev/null to hide any sensitive credential data that might otherwise be exposed.
git push --quiet $REPO > /dev/null 2>&1

cd $OLDPWD
