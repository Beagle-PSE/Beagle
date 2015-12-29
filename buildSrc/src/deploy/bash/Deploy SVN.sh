#!/bin/bash

##############################################################
# SVN deployment script                                      #
# Deploys Beagle’s code an built artefacts to the SDQ’s SVN. #
# To be called from Beagle’s root folder on Travis CI        #
#                                                            #
# author: Joshua Gleitze                                     #
##############################################################

# fail the script if any command fails
set -e

if [ "$TRAVIS_REPO_SLUG" != "Beagle-PSE/Beagle" ]; then
	echo "We won't update the SVN because this is not the main repository."
	exit 0
fi

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
	echo "We won't update the SVN because this is a pull request."
	exit 0
fi

if [ "$TRAVIS_BRANCH" != "master" ]; then
	echo "We won't update the SVN because this is not the master branch."
	exit 0
fi

if [ -z ${SVN_USER:+1} ]; then
	echo "We can't update the SVN because this no SVN_USER was provided."
	exit 0
fi

if [ -z ${SVN_PASSWORD:+1} ]; then
	echo "We can't update the SVN because this no SVN_PASSWORD was provided."
	exit 0
fi

SVN_URL="https://svnserver.informatik.kit.edu/i43/svn/stud/2015WS-PSE-Gruppe5/"
GIT_URL="https://github.com/Beagle-PSE/Beagle.git"


# Set up our environment
SVN=../svn
GITSUB=$SVN/repository
GENERATEDSUB=$SVN/generated
BASE=$PWD
rm -rf $SVN
mkdir -p $SVN

cd $SVN

# Check out from SVN
svn checkout $SVN_URL $SVN --username $SVN_USER --password "$SVN_PASSWORD" --non-interactive --quiet

# Remove all content - except the svn files
find . -maxdepth 1 \! \( -name .svn -o -name . \) -exec rm -rf {} \;

# Set up the repository
git clone $GIT_URL $GITSUB --quiet
# Remove the .git folder
rm -rf $GITSUB/.git


mkdir -p $GENERATEDSUB
cp "$BASE/doc/Requirements Specification/Requirements Specification.pdf" $GENERATEDSUB

svn add . --force --non-interactive --quiet

svn commit -m "Automatic Travis update from $GIT_URL" --username $SVN_USER --password "$SVN_PASSWORD" --non-interactive --quiet

cd $BASE





