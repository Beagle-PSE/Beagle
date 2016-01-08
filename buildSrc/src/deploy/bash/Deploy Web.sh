#!/bin/bash

######################################################################
# Web page deployment script                                         #
# Deploys Beagle’s web presence to the repository’s gh-pages branch. #
# To be called from Beagle’s root folder on Travis CI                #
#                                                                    #
# author: Joshua Gleitze                                             #
######################################################################

# fail the script if any command fails
set -e
# Don't return a glob pattern if it doesn't match anything
shopt -s nullglob

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
	echo "We won't deploy because we're on a pull request."
	exit 0
fi

# If GH_TOKEN is not set, we'll exit gracefully
if [ -z ${GH_TOKEN:+1} ]; then
	echo "The GH_TOKEN ENV is not set. Thus, we won't deploy to gh-pages."
	exit 0
fi

PUBLISH=../publish
BASE=$PWD
mkdir -p $PUBLISH
cd $PUBLISH

REPO="https://${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git"

function set_git_user {
	git config user.name "Travis CI"
	git config user.email "travis@example.org"
}

if [ `git ls-remote --heads $REPO branch gh-pages | wc -l` == 1 ]
then
	git clone $REPO .
	echo "Continuing existing branch gh-pages"
	set_git_user
else
	git init
	set_git_user
	
	echo "Creating new branch gh-pages"
	cp "$BASE/deploy/Default gh-pages README.md" "README.md"
	git add .
	git commit -m "gh-pages created by Travis CI"
	git branch --no-track gh-pages
fi
git checkout gh-pages


# Remove deleted branches' directories
for branchpath in branches/*
do
	branch=$(basename $branchpath)
	git rev-parse --verify origin/$branch > /dev/null 2>&1 || (echo "Removing obsolete branch folder of $branch"; git rm -rfq branches/$branch)
done


# Deploy the master branch to the top most folder, but all other branches to subfolders
[ $TRAVIS_BRANCH == "master" ] && pubdir="." || pubdir="branches/$TRAVIS_BRANCH"
mkdir -p "$pubdir"
cd "$pubdir"

# Remove all content - except the git files and the branches folder
find . -maxdepth 1 \! \( -name .git -o -name . -o -name branches \) -exec rm -rf {} \;

# Requirements specification
cp -r "$BASE/Web Presence/build"/* .

###

git add --all .
git commit -m "Travis build of $TRAVIS_BRANCH ($TRAVIS_COMMIT_RANGE)"

# Push from the current repo's gh-pages branch to the remote
# repo's gh-pages branch. We redirect any output to
# /dev/null to hide any sensitive credential data that might otherwise be exposed.
git push --quiet $REPO gh-pages:gh-pages > /dev/null 2>&1

cd $BASE
