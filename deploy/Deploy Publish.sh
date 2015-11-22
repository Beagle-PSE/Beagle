#!/bin/bash

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

echo $PWD
ls -la

PUBLISH=../publish
BASE=$PWD
mkdir -p $PUBLISH
cd $PUBLISH

REPO="https://${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git"
git clone $REPO .

echo $PWD
ls -la

if ! [ `git branch --list gh-pages `]
then
	git branch --no-track gh-pages
fi
git checkout gh-pages

git config user.name "Travis CI"
git config user.email "travis@example.org"

# Remove all content - except the git files, duh
find . -maxdepth 1 \! \( -name .git -o -name . \) -exec rm -rf {} \;

echo $PWD
ls -la

git branch

###
# Gather all assets that will be released
###

# Build the master branch to the top most folder, but all other branches to subfolders
[ $TRAVIS_BRANCH == "master" ] && pubdir="." || pubdir="branches/$TRAVIS_BRANCH"
mkdir -p "$pubdir"

# Requirements specification

cp "$BASE/doc/Requirements Specification/Requirements Specification.pdf" "$pubdir"

###

echo $PWD
ls -la

git add --all .
git commit -m "Travis build of $TRAVIS_COMMIT_RANGE"

# Push from the current repo's gh-pages branch to the remote
# repo's gh-pages branch. We redirect any output to
# /dev/null to hide any sensitive credential data that might otherwise be exposed.
git push --quiet $REPO gh-pages:gh-pages > /dev/null 2>&1

cd $OLDPWD
