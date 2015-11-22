# Always make the publish dir, even if we won’t actually publish. Other script will not check for its existance
mkdir publish

# fail the script if any command fails
set -e

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
	exit 0
fi

# If GH_TOKEN is not set, we'll exit gracefully
if [ -z ${GH_TOKEN:+1} ]; then
	exit 0
fi

cd publish

REPO="https://${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git"
git clone $REPO .

ls -l

if ! [ `git branch --list gh-pages `]
then
	git branch --no-track gh-pages
fi
git checkout gh-pages

git config user.name "Travis CI"
git config user.email "travis@example.org"

# Remove all content 
rm -rf .[^.] .??* *

ls -l

git branch

cd $OLDPWD

git branch